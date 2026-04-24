package com.fixit.tasks.infraestructure.adapters.driven.feign.mapper;


import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.infraestructure.adapters.driven.feign.dto.request.TechnicianFeignRequest;
import com.fixit.tasks.infraestructure.adapters.driven.feign.dto.response.TechnicianFeignResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ITechnicianFeignMapper {

    @Mapping(target = "user.id", source = "id")
    @Mapping(target = "user.dni", source = "dni")
    @Mapping(target = "user.name", source = "name")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "status", source = "status")
    Technician toDomain(TechnicianFeignResponse response);

    @Mapping(target = "category", expression = "java(technician.getCategory())")
    @Mapping(target = "status", expression = "java(technician.getStatus())")
    @Mapping(target = "taskCount", source = "taskCount")
    @Mapping(target = "currentPoints", source = "currentPoints")
    TechnicianFeignRequest toFeignRequest(Technician technician);

}