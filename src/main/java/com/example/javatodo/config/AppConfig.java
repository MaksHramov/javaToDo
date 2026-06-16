package com.example.javatodo.config;

import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final Properties PROPERTIES = load();

    private AppConfig() {
    }

    private static Properties load() {
        try (InputStream in = AppConfig.class.getResourceAsStream("/application.properties")) {
            Properties properties = new Properties();
            properties.load(in);
            return properties;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
