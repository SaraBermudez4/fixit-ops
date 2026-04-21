package com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response;

import lombok.Builder;

@Builder
public record TaskSummaryResponse(
        Long id,
        String name,
        String priority,
        String status
) {}
