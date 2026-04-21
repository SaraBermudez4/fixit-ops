package com.fixit.tasks.domain.enums;

public enum TechnicianStatus {
    AVAILABLE,
    BUSY,
    NOT_AVAILABLE;

    public boolean isAvailable() {
        return this == AVAILABLE;
    }
}