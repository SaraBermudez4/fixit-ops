package com.fixit.tasks.infraestructure.adapters.driven.rabbit.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TaskNotificationEventDto {
    String eventType;
    String phoneNumber;
    String message;
}