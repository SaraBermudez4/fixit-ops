package com.fixit.user.infraestructure.adapters.driving.rest.util;

public class RestConstants {

    private RestConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DNI_REQUIRED = "DNI is required";
    public static final String DNI_SIZE = "DNI must be at most 20 characters";
    public static final String NAME_REQUIRED = "Name is required";
    public static final String NAME_SIZE = "Name must be at most 100 characters";
    public static final String CATEGORY_REQUIRED = "Category is required";
    public static final String CATEGORY_VALID = "Category must be JUNIOR, SEMI_SENIOR, SENIOR, or MASTER";

    public static final String LAST_NAME_REQUIRED = "Last name is required";
    public static final String LAST_NAME_SIZE = "Last name must be at most 100 characters";
    public static final String EMAIL_REQUIRED = "Email is required";
    public static final String EMAIL_VALID = "Invalid email format";
    public static final String PASSWORD_REQUIRED = "Password is required";
    public static final String PASSWORD_SIZE = "Password must be between 8 and 100 characters long";
    public static final String PHONE_NUMBER_REQUIRED = "Phone number is required";
}