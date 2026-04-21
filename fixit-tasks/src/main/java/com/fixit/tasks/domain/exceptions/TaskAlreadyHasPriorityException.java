package com.fixit.tasks.domain.exceptions;

public class TaskAlreadyHasPriorityException extends RuntimeException{
    public TaskAlreadyHasPriorityException(String message, Long id) {
        super(message);
    }
}
