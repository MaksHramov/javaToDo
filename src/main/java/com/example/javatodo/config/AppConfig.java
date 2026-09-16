package com.example.javatodo.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class AppConfig {

    private static final Properties PROPERTIES = load();

    private AppConfig() {
    }

    private static Properties load() {
        Properties properties = new Properties();
        Path local = Path.of("application.properties");
        try {
            if (Files.exists(local)) {
                try (var in = Files.newInputStream(local)) {
                    properties.load(in);
                    return properties;
                }
            }
            try (InputStream in = AppConfig.class.getResourceAsStream("/application.properties")) {
                if (in == null) {
                    throw new IllegalStateException(
                            "Нет application.properties. Скопируйте application.properties.example");
                }
                properties.load(in);
                return properties;
            }
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
