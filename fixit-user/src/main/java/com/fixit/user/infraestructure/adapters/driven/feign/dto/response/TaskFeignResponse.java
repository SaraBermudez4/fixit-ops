package com.fixit.user.infraestructure.adapters.driven.feign.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
