package com.fixit.tasks.infraestructure.adapters.driven.rabbit.mapper;

import com.fixit.tasks.domain.model.TaskNotificationEvent;
import com.fixit.tasks.infraestructure.adapters.driven.rabbit.dto.TaskNotificationEventDto;

public class TaskNotificationEventMapper {

    private TaskNotificationEventMapper() {}

    public static TaskNotificationEventDto toDto(TaskNotificationEvent event, String eventType) {
        return TaskNotificationEventDto.builder()
                .eventType(eventType)
                .phoneNumber(event.getPhoneNumber())
                .message(event.getMessage())
                .build();
    }
}