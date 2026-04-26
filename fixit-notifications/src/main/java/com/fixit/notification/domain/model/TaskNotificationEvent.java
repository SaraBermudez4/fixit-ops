package com.fixit.notification.domain.model;

import com.fixit.notification.domain.enums.TaskPriority;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TaskNotificationEvent {
    private String eventType;
    private Long taskId;
    private String taskName;
    private String taskDescription;
    private TaskPriority taskPriority;
    private Long technicianId;
    private String technicianName;
    private String technicianLastName;
    private String technicianPhone;
    private Integer taskCount;
    private Integer currentPoints;
    private Integer maxPoints;
    private LocalDateTime occurredAt;
}