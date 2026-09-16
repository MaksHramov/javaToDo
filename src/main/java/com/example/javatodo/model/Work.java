package com.example.javatodo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Work(
        long id,
        String title,
        String description,
        String assignee,
        String category,
        LocalDate dueDate,
        LocalDateTime createdAt,
        String status
) {
    public Work withStatus(String newStatus) {
        return new Work(id, title, description, assignee, category, dueDate, createdAt, newStatus);
    }

    public Work withCategory(String newCategory) {
        return new Work(id, title, description, assignee, newCategory, dueDate, createdAt, status);
    }

    public boolean isOverdue() {
        return dueDate != null
                && dueDate.isBefore(LocalDate.now())
                && !"DONE".equals(status);
    }
}
