package com.example.javatodo.controller;

import com.example.javatodo.service.WorkService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
    private TextField assigneeField;
    @FXML
    private ComboBox<String> categoryBox;
    @FXML
    private DatePicker dueDateField;

    private WorkService workService;
    private Runnable onSaved;
    private Stage dialogStage;

    @FXML
    public void initialize() {
        categoryBox.setItems(FXCollections.observableArrayList(WorkService.CATEGORIES));
        categoryBox.getSelectionModel().selectFirst();
    }

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
        String assignee = assigneeField.getText().trim();
        if (title.isEmpty() || description.isEmpty() || assignee.isEmpty()
                || categoryBox.getValue() == null || dueDateField.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Заполните все поля");
            alert.setHeaderText(null);
            alert.setContentText("Название, описание, исполнитель, категория и срок обязательны.");
            alert.showAndWait();
            return;
        }
        workService.createWork(title, description, assignee, categoryBox.getValue(), dueDateField.getValue());
        onSaved.run();
        dialogStage.close();
    }

    @FXML
    private void onCancelClick() {
        dialogStage.close();
    }
}
