package com.example.javatodo.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record Work(
        long id,
        String title,
        String description,
        LocalDate dueDate,
        LocalDateTime createdAt
) {
}
