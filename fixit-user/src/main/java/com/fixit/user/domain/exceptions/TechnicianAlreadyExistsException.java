package com.fixit.user.domain.exceptions;

public class TechnicianAlreadyExistsException extends RuntimeException {
    public TechnicianAlreadyExistsException(String message) {
        super(message);
    }
}