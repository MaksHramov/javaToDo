package com.example.javatodo.controller;

import com.example.javatodo.service.WorkService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddTaskController {

    @FXML
    private TextField titleField;

    @FXML
    private TextArea descriptionField;

    @FXML
    private DatePicker dueDateField;

    private WorkService workService;
    private Runnable onSaved;
    private Stage dialogStage;

    public void setWorkService(WorkService workService) {
        this.workService = workService;
    }

    public void setOnSaved(Runnable onSaved) {
        this.onSaved = onSaved;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    private void onSaveClick() {
        String title = titleField.getText().trim();
        String description = descriptionField.getText().trim();

        if (title.isEmpty() || description.isEmpty() || dueDateField.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Заполните все поля");
            alert.setHeaderText(null);
            alert.setContentText("Название, описание и срок обязательны.");
            alert.showAndWait();
            return;
        }

        workService.createWork(title, description, dueDateField.getValue());
        onSaved.run();
        dialogStage.close();
    }

    @FXML
    private void onCancelClick() {
        dialogStage.close();
    }
}
