package com.example.javatodo.db;

import com.example.javatodo.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(
                AppConfig.get("db.url"),
                AppConfig.get("db.user"),
                AppConfig.get("db.password")
        );
    }
}
