package com.fixit.user.infraestructure.adapters.driven.feign.dto.response;

import com.fixit.user.domain.enums.TaskPriority;
import com.fixit.user.domain.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TaskFeignResponse {
    Long id;
    String name;
    String description;
    String priority;
    String status;
    Long technicianId;
    LocalDateTime createdAt;
    LocalDateTime closedAt;

}
