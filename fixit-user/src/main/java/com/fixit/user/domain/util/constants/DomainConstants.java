package com.fixit.user.domain.util.constants;

public final class DomainConstants {
    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TECHNICIAN_BUSY_MESSAGE = "Technician with ID %s is currently busy and cannot be assigned new tasks.";
    public static final String TECHNICIAN_EMAIL_ALREADY_EXISTS_MESSAGE = "Technician with email %s already exists.";
    public static final String TECHNICIAN_DNI_ALREADY_EXISTS_MESSAGE = "Technician with DNI %s already exists.";
    public static final String TECHNICIAN_NOT_FOUND_MESSAGE = "Technician with id %d was not found.";
    public static final String TECHNICIAN_CANNOT_BE_DELETED_MESSAGE = "Technician with id %d cannot be deleted because its status is %s.";
    public static final String TECHNICIAN_SAME_CATEGORY_MESSAGE = "Technician with ID %d already has category %s.";
    public static final String FEIGN_INVALID_REQUEST = "Invalid request data sent to Task Service.";
    public static final String FEIGN_NOT_FOUND = "Resource not found in Task Service.";
    public static final String FEIGN_TASK_CONFLICT = "Conflict in Task Service operation.";
    public static final String FEIGN_GENERIC_ERROR = "Generic error in Task Microservice communication.";
}