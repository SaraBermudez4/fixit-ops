package com.fixit.notification.domain.exceptions;

public class SmsNotificationException extends RuntimeException {
    public SmsNotificationException(String message) {
        super(message);
    }
}