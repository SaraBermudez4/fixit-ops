package com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician;

import lombok.Builder;

@Builder
public record TaskSummaryResponse(
        Long id,
        String name,
        String priority,
        String status
) {}
