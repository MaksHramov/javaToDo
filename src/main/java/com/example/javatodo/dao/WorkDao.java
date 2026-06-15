package com.example.javatodo.dao;

import com.example.javatodo.db.DatabaseConnection;
import com.example.javatodo.model.Work;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WorkDao {

    public static List<Work> findAll() {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(
                     "SELECT id, title, description, due_date, created_at, status FROM works ORDER BY id DESC")) {
            List<Work> works = new ArrayList<>();
            while (rs.next()) {
                works.add(fromRow(rs));
            }
            return works;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(String title, String description, LocalDate dueDate) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(
                     "INSERT INTO works (title, description, status, due_date) VALUES (?, ?, 'NEW', ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setObject(3, dueDate);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(long id) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM works WHERE id = ?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateStatus(long id, String status) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("UPDATE works SET status = ? WHERE id = ?")) {
            stmt.setString(1, status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Work fromRow(ResultSet rs) throws Exception {
        return new Work(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getObject("due_date", LocalDate.class),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("status")
        );
    }
}
