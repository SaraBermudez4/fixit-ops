package com.fixit.tasks.domain.model;

import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.domain.service.TaskMessageBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
@Value
@Builder(toBuilder = true)
public class TaskNotificationEvent {
    String phoneNumber;
    String message;

    public static TaskNotificationEvent forAssignment(Task task, Technician technician) {
        return TaskNotificationEvent.builder()
                .phoneNumber(technician.getUser().getPhoneNumber())
                .message(TaskMessageBuilder.buildAssignedMessage(task, technician))
                .build();
    }

    public static TaskNotificationEvent forCompletion(Task task, Technician technician) {
        return TaskNotificationEvent.builder()
                .phoneNumber(technician.getUser().getPhoneNumber())
                .message(TaskMessageBuilder.buildCompletedMessage(task, technician))
                .build();
    }
}