package com.example.javatodo.factory;

import com.example.javatodo.dao.SyncWorkDao;
import com.example.javatodo.dao.WorkDao;

public class SyncDataSourceFactory implements DataSourceFactory {

    @Override
    public WorkDao createWorkDao() {
        return new SyncWorkDao();
    }
}
