package com.example.javatodo.factory;

import com.example.javatodo.config.AppConfig;
import com.example.javatodo.dao.FileWorkDao;
import com.example.javatodo.dao.WorkDao;

public class FileDataSourceFactory implements DataSourceFactory {

    @Override
    public WorkDao createWorkDao() {
        return new FileWorkDao(AppConfig.get("datasource.file.path"));
    }
}
