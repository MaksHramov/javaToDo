package com.example.javatodo.dao;

import com.example.javatodo.model.Work;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FileWorkDao implements WorkDao {

    private final Path file;
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public FileWorkDao(String filePath) {
        this.file = Path.of(filePath);
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            if (!Files.exists(file)) {
                saveAll(new ArrayList<>());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized List<Work> findAll() {
        return loadAll().stream()
                .sorted(Comparator.comparingLong(Work::id).reversed())
                .toList();
    }

    @Override
    public synchronized void save(String title, String description, LocalDate dueDate) {
        List<Work> works = loadAll();
        long nextId = works.stream().mapToLong(Work::id).max().orElse(0) + 1;
        works.add(new Work(nextId, title, description, dueDate, LocalDateTime.now(), "NEW"));
        saveAll(works);
    }

    @Override
    public synchronized void delete(long id) {
        List<Work> works = loadAll();
        works.removeIf(work -> work.id() == id);
        saveAll(works);
    }

    @Override
    public synchronized void updateStatus(long id, String status) {
        List<Work> works = loadAll();
        for (int i = 0; i < works.size(); i++) {
            Work work = works.get(i);
            if (work.id() == id) {
                works.set(i, new Work(work.id(), work.title(), work.description(), work.dueDate(), work.createdAt(), status));
                saveAll(works);
                return;
            }
        }
    }

    private List<Work> loadAll() {
        try {
            if (!Files.exists(file) || Files.size(file) == 0) {
                return new ArrayList<>();
            }
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Work.class);
            return new ArrayList<>(mapper.readValue(file.toFile(), type));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void saveAll(List<Work> works) {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file.toFile(), works);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void replaceAll(List<Work> works) {
        saveAll(new ArrayList<>(works));
    }
}
