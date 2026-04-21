package com.fixit.user.domain.exceptions;

public class TechnicianBusyException extends RuntimeException {
    public TechnicianBusyException(String message) {
        super(message);
    }
}
