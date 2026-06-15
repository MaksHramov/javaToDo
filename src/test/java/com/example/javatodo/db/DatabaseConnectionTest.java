package com.example.javatodo.db;

import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DatabaseConnectionTest {

    @Test
    void connectsToDatabase() throws Exception {
        try (Connection connection = DatabaseConnection.getConnection()) {
            assertTrue(connection.isValid(2));
        }
    }
}
