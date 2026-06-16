package com.example.javatodo.service;

import com.example.javatodo.dao.WorkDao;
import com.example.javatodo.model.Work;

import java.time.LocalDate;
import java.util.List;

public class WorkService {

    private final WorkDao workDao;

    public WorkService(WorkDao workDao) {
        this.workDao = workDao;
    }

    public List<Work> getAllWorks() {
        return workDao.findAll();
    }

    public void createWork(String title, String description, LocalDate dueDate) {
        workDao.save(title, description, dueDate);
    }

    public void deleteWork(long id) {
        workDao.delete(id);
    }

    public void updateStatus(long id, String status) {
        workDao.updateStatus(id, status);
    }
}
