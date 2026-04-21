package com.fixit.tasks.domain.exceptions;

public class TaskMustBeAssignedToStartException extends RuntimeException{
    public TaskMustBeAssignedToStartException(String message) {
        super(message);
    }
}
