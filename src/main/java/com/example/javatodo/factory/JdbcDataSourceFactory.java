package com.example.javatodo.factory;

import com.example.javatodo.dao.JdbcWorkDao;
import com.example.javatodo.dao.WorkDao;

public class JdbcDataSourceFactory implements DataSourceFactory {

    @Override
    public WorkDao createWorkDao() {
        return new JdbcWorkDao();
    }
}
