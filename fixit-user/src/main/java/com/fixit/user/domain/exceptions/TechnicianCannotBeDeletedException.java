package com.fixit.user.domain.exceptions;

public class TechnicianCannotBeDeletedException extends RuntimeException {
    public TechnicianCannotBeDeletedException(String message) {
        super(message);
    }
}
