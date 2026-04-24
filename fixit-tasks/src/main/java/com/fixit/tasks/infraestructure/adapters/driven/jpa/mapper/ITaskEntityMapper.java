package com.fixit.tasks.infraestructure.adapters.driven.jpa.mapper;

import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITaskEntityMapper {

    TaskEntity toEntity(Task task);

    Task toDomain(TaskEntity taskEntity);

}