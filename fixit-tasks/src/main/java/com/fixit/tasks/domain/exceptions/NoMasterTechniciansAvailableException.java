package com.fixit.tasks.domain.exceptions;

public class NoMasterTechniciansAvailableException extends RuntimeException {
    public NoMasterTechniciansAvailableException(String message) {
        super(message);
    }
}
