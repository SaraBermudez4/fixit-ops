package com.fixit.tasks.domain.exceptions;

public class TechnicianSameCategoryException extends RuntimeException{
    public TechnicianSameCategoryException(String message) {
        super(message);
    }
}
