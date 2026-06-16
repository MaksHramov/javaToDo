package com.example.javatodo.factory;

import com.example.javatodo.dao.WorkDao;

public interface DataSourceFactory {

    WorkDao createWorkDao();
}
