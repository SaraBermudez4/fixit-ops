package com.fixit.notification.infraestructure.adapters.driving.rest.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import static com.fixit.notification.domain.util.constants.NotificationConstants.SMS_MESSAGE_REQUIRED;
import static com.fixit.notification.domain.util.constants.NotificationConstants.SMS_MESSAGE_SIZE;
import static com.fixit.notification.domain.util.constants.NotificationConstants.SMS_TO_INVALID;
import static com.fixit.notification.domain.util.constants.NotificationConstants.SMS_TO_REQUIRED;

public record SmsNotificationRequestDto(

        @NotBlank(message = SMS_TO_REQUIRED)
        @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = SMS_TO_INVALID)
        String toPhoneNumber,

        @NotBlank(message = SMS_MESSAGE_REQUIRED)
        @Size(min = 1, max = 1600, message = SMS_MESSAGE_SIZE)
        String message
) {
}