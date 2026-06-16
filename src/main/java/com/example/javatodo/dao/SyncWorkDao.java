package com.example.javatodo.dao;

import com.example.javatodo.config.AppConfig;
import com.example.javatodo.model.Work;

import java.time.LocalDate;
import java.util.List;

public class SyncWorkDao implements WorkDao {

    private final JdbcWorkDao jdbcDao = new JdbcWorkDao();
    private final InMemoryWorkDao memoryDao = new InMemoryWorkDao();
    private final FileWorkDao fileDao = new FileWorkDao(AppConfig.get("datasource.file.path"));
    private boolean initialized;

    private void init() {
        if (initialized) {
            return;
        }
        List<Work> fromJdbc = jdbcDao.findAll();
        List<Work> fromFile = fileDao.findAll();
        if (fromJdbc.isEmpty() && !fromFile.isEmpty()) {
            jdbcDao.replaceAll(fromFile);
        }
        syncFromJdbc();
        initialized = true;
    }

    private void syncFromJdbc() {
        List<Work> works = jdbcDao.findAll();
        memoryDao.replaceAll(works);
        fileDao.replaceAll(works);
    }

    @Override
    public List<Work> findAll() {
        init();
        return jdbcDao.findAll();
    }

    @Override
    public void save(String title, String description, LocalDate dueDate) {
        init();
        jdbcDao.save(title, description, dueDate);
        syncFromJdbc();
    }

    @Override
    public void delete(long id) {
        init();
        jdbcDao.delete(id);
        syncFromJdbc();
    }

    @Override
    public void updateStatus(long id, String status) {
        init();
        jdbcDao.updateStatus(id, status);
        syncFromJdbc();
    }
}
