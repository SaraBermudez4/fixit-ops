package com.fixit.user.domain.exceptions;

public class UserRoleConflictException extends Exception {
    public UserRoleConflictException(String message) {
        super(message);
    }
}
