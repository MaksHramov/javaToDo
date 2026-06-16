package com.example.javatodo.factory;

import com.example.javatodo.dao.InMemoryWorkDao;
import com.example.javatodo.dao.WorkDao;

public class InMemoryDataSourceFactory implements DataSourceFactory {

    @Override
    public WorkDao createWorkDao() {
        return new InMemoryWorkDao();
    }
}
