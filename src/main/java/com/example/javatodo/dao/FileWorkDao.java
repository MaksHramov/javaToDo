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
import java.util.function.UnaryOperator;

public class FileWorkDao implements WorkDao {

    private final Path file;
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

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
    public synchronized void save(String title, String description, String assignee, String category, LocalDate dueDate) {
        List<Work> works = loadAll();
        long nextId = works.stream().mapToLong(Work::id).max().orElse(0) + 1;
        works.add(new Work(nextId, title, description, assignee, category, dueDate, LocalDateTime.now(), "NEW"));
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
        update(id, work -> work.withStatus(status));
    }

    @Override
    public synchronized void updateCategory(long id, String category) {
        update(id, work -> work.withCategory(category));
    }

    public synchronized void replaceAll(List<Work> works) {
        saveAll(new ArrayList<>(works));
    }

    private void update(long id, UnaryOperator<Work> change) {
        List<Work> works = loadAll();
        for (int i = 0; i < works.size(); i++) {
            if (works.get(i).id() == id) {
                works.set(i, change.apply(works.get(i)));
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
}
