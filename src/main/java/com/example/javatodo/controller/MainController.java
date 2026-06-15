package com.example.javatodo.controller;

import com.example.javatodo.dao.WorkDao;
import com.example.javatodo.model.Work;
import com.example.javatodo.model.WorkStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private FlowPane tasksContainer;

    @FXML
    public void initialize() {
        loadTasks();
    }

    @FXML
    private void onAddTaskClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javatodo/add-task-dialog.fxml"));
            Parent root = loader.load();

            AddTaskController controller = loader.getController();
            controller.setOnSaved(this::loadTasks);

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Новая задача");
            dialog.setScene(new Scene(root));
            controller.setDialogStage(dialog);
            dialog.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadTasks() {
        tasksContainer.getChildren().clear();
        for (Work work : WorkDao.findAll()) {
            VBox card = new VBox(5,
                    new Label(work.title()),
                    new Label(work.description() == null ? "" : work.description())
            );
            if (work.dueDate() != null) {
                card.getChildren().add(new Label("Срок: " + work.dueDate()));
            }
            if (work.createdAt() != null) {
                card.getChildren().add(new Label("Создано: " + work.createdAt()));
            }

            ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList(
                    "Новая", "В работе", "На проверке", "На доработке", "Завершено"
            ));
            statusBox.setValue(WorkStatus.fromCode(work.status()).label());
            statusBox.setOnAction(e -> WorkDao.updateStatus(
                    work.id(),
                    WorkStatus.fromLabel(statusBox.getValue()).code()
            ));
            card.getChildren().add(new Label("Статус:"));
            card.getChildren().add(statusBox);

            Button deleteButton = new Button("Удалить");
            deleteButton.setOnAction(e -> {
                WorkDao.delete(work.id());
                loadTasks();
            });
            card.getChildren().add(deleteButton);
            tasksContainer.getChildren().add(card);
        }
    }
}
