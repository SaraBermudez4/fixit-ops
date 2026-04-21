package com.fixit.tasks.infraestructure.adapters.driving.rest.dto.request;

import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.infraestructure.adapters.driving.rest.util.RestConstants;
import com.fixit.tasks.infraestructure.adapters.driving.rest.validation.ValidEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
@Builder
public record TaskRequest(
        @NotBlank(message = RestConstants.TASK_NAME_REQUIRED)
        String name,

        String description,

        @NotBlank(message = RestConstants.PRIORITY_REQUIRED)
        @ValidEnum(
                enumClass = TaskPriority.class,
                message = RestConstants.PRIORITY_VALID
        )
        String priority
) {}