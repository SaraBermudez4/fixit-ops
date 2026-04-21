package com.fixit.tasks.domain.exceptions;

public class TaskCannotBeDeletedException extends RuntimeException {
    public TaskCannotBeDeletedException(String message) {
        super(message);
    }
}
