package com.example.javatodo.service;

import com.example.javatodo.dao.WorkDao;
import com.example.javatodo.model.Work;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class WorkService {

    public static final List<String> CATEGORIES = List.of("Работа", "Учёба", "Личное");

    private final WorkDao workDao;

    public WorkService(WorkDao workDao) {
        this.workDao = workDao;
    }

    public List<Work> list(String assignee, String category, LocalDate dueDate, String sortBy) {
        var stream = workDao.findAll().stream();
        if (assignee != null && !assignee.isBlank()) {
            String q = assignee.trim().toLowerCase();
            stream = stream.filter(w -> w.assignee() != null && w.assignee().toLowerCase().contains(q));
        }
        if (category != null && !category.isBlank()) {
            stream = stream.filter(w -> category.equals(w.category()));
        }
        if (dueDate != null) {
            stream = stream.filter(w -> dueDate.equals(w.dueDate()));
        }
        Comparator<Work> order = switch (sortBy == null ? "" : sortBy) {
            case "due" -> Comparator.comparing(Work::dueDate, Comparator.nullsLast(Comparator.naturalOrder()));
            case "assignee" -> Comparator.comparing(Work::assignee, Comparator.nullsLast(String::compareToIgnoreCase));
            default -> Comparator.comparingLong(Work::id).reversed();
        };
        return stream.sorted(order).toList();
    }

    public void createWork(String title, String description, String assignee, String category, LocalDate dueDate) {
        workDao.save(title, description, assignee, category, dueDate);
    }

    public void deleteWork(long id) {
        workDao.delete(id);
    }

    public void updateStatus(long id, String status) {
        workDao.updateStatus(id, status);
    }

    public void updateCategory(long id, String category) {
        workDao.updateCategory(id, category);
    }
}
