package com.fixit.tasks.domain.exceptions;

public class TechnicianCannotBeDeletedException extends RuntimeException {
    public TechnicianCannotBeDeletedException(String message) {
        super(message);
    }
}
