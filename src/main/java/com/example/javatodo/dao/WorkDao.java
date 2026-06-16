package com.example.javatodo.dao;

import com.example.javatodo.model.Work;

import java.time.LocalDate;
import java.util.List;

public interface WorkDao {

    List<Work> findAll();

    void save(String title, String description, LocalDate dueDate);

    void delete(long id);

    void updateStatus(long id, String status);
}
