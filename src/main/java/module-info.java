module com.example.javatodo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;

    opens com.example.javatodo to javafx.fxml;
    opens com.example.javatodo.controller to javafx.fxml;
    opens com.example.javatodo.model to com.fasterxml.jackson.databind;
    exports com.example.javatodo;
}
