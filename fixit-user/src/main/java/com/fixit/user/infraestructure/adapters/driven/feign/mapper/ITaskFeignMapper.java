package com.fixit.user.infraestructure.adapters.driven.feign.mapper;


import com.fixit.user.domain.model.Task;
import com.fixit.user.infraestructure.adapters.driven.feign.dto.response.TaskFeignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ITaskFeignMapper {

    @Mapping(target = "priority", expression = "java(com.fixit.user.domain.enums.TaskPriority.valueOf(response.getPriority().toUpperCase()))")
    @Mapping(target = "status", expression = "java(com.fixit.user.domain.enums.TaskStatus.valueOf(response.getStatus().toUpperCase()))")
    Task toDomain(TaskFeignResponse response);

    List<Task> toDomainList(List<TaskFeignResponse> taskFeignResponses);
}