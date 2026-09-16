package com.example.javatodo.dao;

import com.example.javatodo.db.DatabaseConnection;
import com.example.javatodo.model.Work;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcWorkDao implements WorkDao {

    public JdbcWorkDao() {
        ensureColumns();
    }

    private void ensureColumns() {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.createStatement()) {
            stmt.execute("ALTER TABLE works ADD COLUMN IF NOT EXISTS assignee VARCHAR(100)");
            stmt.execute("ALTER TABLE works ADD COLUMN IF NOT EXISTS category VARCHAR(100)");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Work> findAll() {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(
                     "SELECT id, title, description, assignee, category, due_date, created_at, status FROM works ORDER BY id DESC")) {
            List<Work> works = new ArrayList<>();
            while (rs.next()) {
                works.add(fromRow(rs));
            }
            return works;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(String title, String description, String assignee, String category, LocalDate dueDate) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement(
                     "INSERT INTO works (title, description, assignee, category, status, due_date) VALUES (?, ?, ?, ?, 'NEW', ?)")) {
            stmt.setString(1, title);
            stmt.setString(2, description);
            stmt.setString(3, assignee);
            stmt.setString(4, category);
            stmt.setObject(5, dueDate);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long id) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("DELETE FROM works WHERE id = ?")) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateStatus(long id, String status) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("UPDATE works SET status = ? WHERE id = ?")) {
            stmt.setString(1, status);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateCategory(long id, String category) {
        try (var conn = DatabaseConnection.getConnection();
             var stmt = conn.prepareStatement("UPDATE works SET category = ? WHERE id = ?")) {
            stmt.setString(1, category);
            stmt.setLong(2, id);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void replaceAll(List<Work> works) {
        try (var conn = DatabaseConnection.getConnection()) {
            conn.createStatement().execute("DELETE FROM works");
            try (var stmt = conn.prepareStatement(
                    "INSERT INTO works (id, title, description, assignee, category, status, due_date, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                for (Work work : works) {
                    stmt.setLong(1, work.id());
                    stmt.setString(2, work.title());
                    stmt.setString(3, work.description());
                    stmt.setString(4, work.assignee());
                    stmt.setString(5, work.category());
                    stmt.setString(6, work.status());
                    stmt.setObject(7, work.dueDate());
                    stmt.setObject(8, work.createdAt());
                    stmt.addBatch();
                }
                stmt.executeBatch();
            }
            conn.createStatement().execute(
                    "SELECT setval(pg_get_serial_sequence('works', 'id'), COALESCE((SELECT MAX(id) FROM works), 1))");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Work fromRow(ResultSet rs) throws Exception {
        return new Work(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getString("assignee"),
                rs.getString("category"),
                rs.getObject("due_date", LocalDate.class),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getString("status")
        );
    }
}
