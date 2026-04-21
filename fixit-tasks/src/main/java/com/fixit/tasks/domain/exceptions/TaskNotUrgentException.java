package com.fixit.tasks.domain.exceptions;

public class TaskNotUrgentException extends RuntimeException {
    public TaskNotUrgentException(String message) {
        super(message);
    }
}
