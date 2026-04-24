package com.fixit.tasks.domain.util.constants;

public final class DomainConstants {
    private DomainConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String TASK_MUST_BE_IN_PROGRESS =  "Task must be IN_PROGRESS to complete";
    public static final String TASK_MUST_BE_ASSIGNED_TO_START = "Task must be ASSIGNED to start";
    public static final String TASK_ALREADY_HAS_PRIORITY = "Task with id already has a priority assigned.";
    public static final String TASK_NOT_FOUND_MESSAGE = "Task with id %d was not found.";
    public static final String TASK_CANNOT_BE_DELETED_MESSAGE = "Task with id %d cannot be deleted because its status is %s.";
    public static final String TASK_NOT_URGENT_MESSAGE = "Task must have URGENT priority to be assigned to Master technicians.";
    public static final String TASK_NOT_ASSIGNED_MESSAGE = "Only tasks with ASSIGNED status can be reassigned to Master technicians.";
    public static final String NO_MASTER_TECHNICIANS_AVAILABLE_MESSAGE = "No Master technicians available to assign urgent tasks.";
    public static final String FEIGN_INVALID_REQUEST = "Invalid request data sent to User Service.";
    public static final String FEIGN_NOT_FOUND = "User or Role not found in User Service.";
    public static final String FEIGN_ROLE_CONFLICT = "User already holds a role in this tournament.";
    public static final String FEIGN_GENERIC_ERROR = "Generic error in User Microservice communication.";

}