module com.example.javatodo {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.javatodo to javafx.fxml;
    exports com.example.javatodo;
}