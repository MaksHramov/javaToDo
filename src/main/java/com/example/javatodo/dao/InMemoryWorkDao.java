package com.example.javatodo.dao;

import com.example.javatodo.model.Work;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InMemoryWorkDao implements WorkDao {

    private static final List<Work> WORKS = new ArrayList<>();
    private static long nextId = 1;

    @Override
    public synchronized List<Work> findAll() {
        return WORKS.stream()
                .sorted(Comparator.comparingLong(Work::id).reversed())
                .toList();
    }

    @Override
    public synchronized void save(String title, String description, LocalDate dueDate) {
        WORKS.add(new Work(
                nextId++,
                title,
                description,
                dueDate,
                LocalDateTime.now(),
                "NEW"
        ));
    }

    @Override
    public synchronized void delete(long id) {
        WORKS.removeIf(work -> work.id() == id);
    }

    @Override
    public synchronized void updateStatus(long id, String status) {
        for (int i = 0; i < WORKS.size(); i++) {
            Work work = WORKS.get(i);
            if (work.id() == id) {
                WORKS.set(i, new Work(work.id(), work.title(), work.description(), work.dueDate(), work.createdAt(), status));
                return;
            }
        }
    }

    public synchronized void replaceAll(List<Work> works) {
        WORKS.clear();
        WORKS.addAll(works);
        nextId = works.stream().mapToLong(Work::id).max().orElse(0) + 1;
    }
}
