package com.fixit.tasks.domain.exceptions;

public class TaskMustBeProgressToStartException extends  RuntimeException{
    public TaskMustBeProgressToStartException(String message) {
        super(message);
    }
}
