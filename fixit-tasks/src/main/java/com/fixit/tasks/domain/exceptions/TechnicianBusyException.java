package com.fixit.tasks.domain.exceptions;

public class TechnicianBusyException extends RuntimeException{
    public TechnicianBusyException(String message) {
        super(message);
    }
}
