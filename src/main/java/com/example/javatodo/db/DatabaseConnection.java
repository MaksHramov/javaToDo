package com.example.javatodo.db;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        try (InputStream in = DatabaseConnection.class.getResourceAsStream("/application.properties")) {
            Properties props = new Properties();
            props.load(in);
            URL = props.getProperty("db.url");
            USER = props.getProperty("db.user");
            PASSWORD = props.getProperty("db.password");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
