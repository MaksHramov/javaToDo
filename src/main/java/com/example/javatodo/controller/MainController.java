package com.example.javatodo.controller;

import com.example.javatodo.factory.DataSourceFactoryProvider;
import com.example.javatodo.model.Work;
import com.example.javatodo.model.WorkStatus;
import com.example.javatodo.service.WorkService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    private FlowPane tasksContainer;
    @FXML
    private TextField assigneeFilter;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private DatePicker dueFilter;
    @FXML
    private ComboBox<String> sortBox;

    private final WorkService workService = new WorkService(DataSourceFactoryProvider.getWorkDao());

    @FXML
    public void initialize() {
        categoryFilter.getItems().setAll("Все");
        categoryFilter.getItems().addAll(WorkService.CATEGORIES);
        categoryFilter.getSelectionModel().selectFirst();
        sortBox.getItems().setAll("По id", "По сроку", "По исполнителю");
        sortBox.getSelectionModel().selectFirst();
        loadTasks();
    }

    @FXML
    private void onAddTaskClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/javatodo/add-task-dialog.fxml"));
            Parent root = loader.load();
            AddTaskController controller = loader.getController();
            controller.setWorkService(workService);
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

    @FXML
    private void onSearchClick() {
        loadTasks();
    }

    private void loadTasks() {
        tasksContainer.getChildren().clear();
        String category = categoryFilter.getValue();
        if ("Все".equals(category)) {
            category = null;
        }
        String sort = switch (sortBox.getValue() == null ? "" : sortBox.getValue()) {
            case "По сроку" -> "due";
            case "По исполнителю" -> "assignee";
            default -> "id";
        };
        for (Work work : workService.list(assigneeFilter.getText(), category, dueFilter.getValue(), sort)) {
            tasksContainer.getChildren().add(card(work));
        }
    }

    private VBox card(Work work) {
        VBox card = new VBox(5);
        card.getStyleClass().add("task-card");
        card.getChildren().add(new Label(work.title()));
        card.getChildren().add(new Label(work.description() == null ? "" : work.description()));
        card.getChildren().add(new Label("Исполнитель: " + nullToDash(work.assignee())));
        if (work.dueDate() != null) {
            String due = "Срок: " + work.dueDate();
            if (work.isOverdue()) {
                due += " (просрочено)";
            }
            card.getChildren().add(new Label(due));
        }

        ComboBox<String> categoryBox = new ComboBox<>(FXCollections.observableArrayList(WorkService.CATEGORIES));
        categoryBox.setValue(work.category() == null ? WorkService.CATEGORIES.get(0) : work.category());
        categoryBox.setOnAction(e -> {
            workService.updateCategory(work.id(), categoryBox.getValue());
            loadTasks();
        });
        card.getChildren().add(new Label("Категория:"));
        card.getChildren().add(categoryBox);

        ComboBox<String> statusBox = new ComboBox<>(FXCollections.observableArrayList(
                "Новая", "В работе", "На проверке", "На доработке", "Завершено"
        ));
        statusBox.setValue(WorkStatus.fromCode(work.status()).label());
        statusBox.setOnAction(e -> {
            String code = WorkStatus.fromLabel(statusBox.getValue()).code();
            workService.updateStatus(work.id(), code);
            if ("DONE".equals(code)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Задача выполнена");
                alert.setHeaderText(null);
                alert.setContentText("Цель «" + work.title() + "» отмечена как выполненная.");
                alert.showAndWait();
            }
            loadTasks();
        });
        card.getChildren().add(new Label("Статус:"));
        card.getChildren().add(statusBox);

        Button deleteButton = new Button("Удалить");
        deleteButton.setOnAction(e -> {
            workService.deleteWork(work.id());
            loadTasks();
        });
        card.getChildren().add(deleteButton);
        return card;
    }

    private static String nullToDash(String value) {
        return value == null || value.isBlank() ? "—" : value;
    }
}
