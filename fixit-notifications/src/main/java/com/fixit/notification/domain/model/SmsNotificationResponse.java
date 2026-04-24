package com.fixit.notification.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SmsNotificationResponse(
        String sid,
        String status,
        String toPhoneNumber,
        String message,
        LocalDateTime sentAt
) {
}