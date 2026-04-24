package com.fixit.notification.domain.model;

public record SmsNotification(
        String toPhoneNumber,
        String message
) {
}