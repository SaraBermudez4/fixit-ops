package com.fixit.user.domain.util.constants;

public class AuthConstants {
    public static final String INVALID_CREDENTIALS_EXCEPTION_MESSAGE = "Invalid email or password";
    public static final int PASSWORD_MIN_LENGTH = 8;
    public static final int PASSWORD_MAX_LENGTH = 100;
    public static final int NAME_MAX_LENGTH = 50;
    public static final int LASTNAME_MAX_LENGTH = 50;
    public static final int DOCUMENT_MAX_LENGTH = 50;
    public static final int PHONE_MAX_LENGTH = 16;
    public static final int EMAIL_MAX_LENGTH = 200;
    public static final int PHONE_EXACT_LENGTH = 13;

    public static final String FIRST_NAME_REQUIRED_EXCEPTION_MESSAGE = "First name is required";
    public static final String NAME_LENGTH_EXCEPTION_MESSAGE = "Name cannot exceed " + NAME_MAX_LENGTH + " characters";

    public static final String FIRST_LASTNAME_REQUIRED_EXCEPTION_MESSAGE = "First last name is required";
    public static final String LASTNAME_LENGTH_EXCEPTION_MESSAGE = "Last name cannot exceed " + LASTNAME_MAX_LENGTH + " characters";

    public static final String DOCUMENT_NUMBER_REQUIRED_EXCEPTION_MESSAGE = "Document number is required";
    public static final String DOCUMENT_NUMBER_LENGTH_EXCEPTION_MESSAGE = "Document number cannot exceed " + DOCUMENT_MAX_LENGTH + " characters";
    public static final String DOCUMENT_NUMBER_ONLY_NUMERIC_EXCEPTION_MESSAGE = "Document number must contain only numbers";
    public static final String DOCUMENT_TYPE_REQUIRED_EXCEPTION_MESSAGE = "Document type is required";
    public static final String DOCUMENT_FORMAT_REGEX = "^[0-9]+$";

    public static final String PHONE_REQUIRED_EXCEPTION_MESSAGE = "Phone number is required";
    public static final String PHONE_LENGTH_MAX_EXCEPTION_MESSAGE = "Phone number cannot exceed " + PHONE_MAX_LENGTH + " characters";
    public static final String PHONE_INVALID_EXCEPTION_MESSAGE = "Invalid phone number, it must have " + PHONE_EXACT_LENGTH + " digits";
    public static final String PHONE_REGEX_EXCEPTION_MESSAGE = "Phone number can only contain numbers and the + symbol";
    public static final String PHONE_FORMAT_REGEX = "^[0-9+]+$";

    public static final String BIRTHDATE_REQUIRED_EXCEPTION_MESSAGE = "Birth date is required";

    public static final String EMAIL_REQUIRED_EXCEPTION_MESSAGE = "Email is required";
    public static final String EMAIL_INVALID_EXCEPTION_MESSAGE = "Invalid email format";
    public static final String EMAIL_LENGTH_EXCEPTION_MESSAGE = "Email cannot exceed " + EMAIL_MAX_LENGTH + " characters";
    public static final String EMAIL_FORMAT_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";

    public static final String PASSWORD_REQUIRED_EXCEPTION_MESSAGE = "Password is required";
    public static final String PASSWORD_LENGTH_EXCEPTION_MESSAGE = "Password must be between " + PASSWORD_MIN_LENGTH + " and " + PASSWORD_MAX_LENGTH + " characters long";

    public static final String USER_ALREADY_INACTIVE_EXCEPTION_MESSAGE = "User is already inactive";
    public static final String EMAIL_ADDRESS_ALREADY_REGISTERED_EXCEPTION_MESSAGE = "Email address is already registered";
    public static final String DOCUMENT_ALREADY_REGISTERED_EXCEPTION_MESSAGE = "Document is already registered";

    public static final String ADDRESS_REQUIRED = "Address information is required";
    public static final String USER_NOT_FOUND_EXCEPTION_MESSAGE = "User not found ";

    public static final String INVALID_DOCUMENT_TYPE = "Invalid document type";
}
