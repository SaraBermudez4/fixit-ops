package com.fixit.tasks.infraestructure.adapters.driving.rest.mapper;

import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.domain.model.AutoAssignSummary;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.request.TaskRequest;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.AutoAssignResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.TaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ITaskRestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "technicianId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "closedAt", ignore = true)
    Task toDomain(TaskRequest request);

    TaskResponse toResponse(Task task);

}