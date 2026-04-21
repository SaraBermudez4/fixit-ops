package com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician;

import lombok.Builder;

import java.util.List;

@Builder
public record TechnicianWorkloadResponse(
        String status,
        Integer availablePoints,
        List<TaskSummaryResponse> assignedTasks
) {}