package com.fixit.notification.infraestructure.adapters.driving.rest.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Value;

@Value
public class TaskNotificationEventDto {

    @NotBlank(message = "eventType is required")
    String eventType;

    @NotBlank(message = "phoneNumber is required")
    @Pattern(regexp = "^\\+[1-9]\\d{7,14}$", message = "phoneNumber format is invalid")
    String phoneNumber;

    @NotBlank(message = "message is required")
    String message;
}