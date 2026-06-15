package com.example.javatodo.model;

public enum WorkStatus {
    NEW("NEW", "Новая"),
    IN_PROGRESS("IN_PROGRESS", "В работе"),
    ON_REVIEW("ON_REVIEW", "На проверке"),
    REWORK("REWORK", "На доработке"),
    DONE("DONE", "Завершено");

    private final String code;
    private final String label;

    WorkStatus(String code, String label) {
        this.code = code;
        this.label = label;
    }

    public String code() {
        return code;
    }

    public String label() {
        return label;
    }

    public static WorkStatus fromCode(String code) {
        for (WorkStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return NEW;
    }

    public static WorkStatus fromLabel(String label) {
        for (WorkStatus status : values()) {
            if (status.label.equals(label)) {
                return status;
            }
        }
        return NEW;
    }
}
