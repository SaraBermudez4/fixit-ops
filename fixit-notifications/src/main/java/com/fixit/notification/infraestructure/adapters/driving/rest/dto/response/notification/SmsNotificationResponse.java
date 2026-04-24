package com.fixit.notification.infraestructure.adapters.driving.rest.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record SmsNotificationResponseDto(
        String sid,
        String status,
        String toPhoneNumber,
        String message,
        LocalDateTime sentAt
) {
}