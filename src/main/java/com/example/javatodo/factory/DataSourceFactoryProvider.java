package com.example.javatodo.factory;

import com.example.javatodo.config.AppConfig;
import com.example.javatodo.dao.WorkDao;

public final class DataSourceFactoryProvider {

    private static WorkDao workDao;

    private DataSourceFactoryProvider() {
    }

    public static WorkDao getWorkDao() {
        if (workDao == null) {
            workDao = createFactory().createWorkDao();
        }
        return workDao;
    }

    private static DataSourceFactory createFactory() {
        String type = AppConfig.get("datasource.type");
        return switch (type) {
            case "sync" -> new SyncDataSourceFactory();
            case "inmemory" -> new InMemoryDataSourceFactory();
            case "file" -> new FileDataSourceFactory();
            case "jdbc" -> new JdbcDataSourceFactory();
            default -> throw new IllegalArgumentException("Unknown datasource.type: " + type);
        };
    }
}
