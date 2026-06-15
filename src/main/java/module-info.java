module com.example.javatodo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;

    opens com.example.javatodo to javafx.fxml;
    opens com.example.javatodo.controller to javafx.fxml;
    exports com.example.javatodo;
}
