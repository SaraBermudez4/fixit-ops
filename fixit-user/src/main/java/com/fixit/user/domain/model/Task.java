package com.fixit.user.domain.model;

import com.fixit.user.domain.enums.TaskPriority;
import com.fixit.user.domain.enums.TaskStatus;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder(toBuilder = true)
public class Task {
    Long id;
    String name;
    String description;
    TaskPriority priority;
    TaskStatus status;
    Long technicianId;
    LocalDateTime createdAt;
    LocalDateTime closedAt;
}