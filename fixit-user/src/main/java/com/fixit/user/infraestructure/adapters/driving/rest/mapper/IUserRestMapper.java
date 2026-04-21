package com.fixit.user.infraestructure.adapters.driving.rest.mapper;

import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;
import com.fixit.user.domain.model.User;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.TechnicianUpdatePutRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.UserRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianWorkloadResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserRestMapper {

    @Mapping(target = "id", ignore = true)
    User toUser(UserRequest request);

    @Mapping(target = "user.dni", source = "dni")
    @Mapping(target = "user.name", source = "name")
    @Mapping(target = "user.lastName", source = "lastName")
    @Mapping(target = "user.email", source = "email")
    @Mapping(target = "user.password", source = "password")
    @Mapping(target = "user.phoneNumber", source = "phoneNumber")
    @Mapping(target = "user.role", expression = "java(com.fixit.user.domain.enums.Role.TECHNICIAN)")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "taskCount", ignore = true)
    @Mapping(target = "currentPoints", ignore = true)
    Technician toTechnician(UserRequest request);


    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "dni", source = "user.dni")
    @Mapping(target = "name", source = "user.name")
    @Mapping(target = "lastName", source = "user.lastName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "taskCount", source = "taskCount")
    @Mapping(target = "currentPoints", source = "currentPoints")
    @Mapping(target = "availablePoints", expression = "java(technician.getAvailablePoints())")
    TechnicianResponse toTechnicianResponse(Technician technician);
    default List<TechnicianResponse> toTechnicianResponseList(List<Technician> technicians) {
        return technicians.stream()
                .map(this::toTechnicianResponse)
                .toList();
    }
    @Mapping(target = "status", source = "technician.status")
    @Mapping(target = "availablePoints", source = "availablePoints")
    @Mapping(target = "assignedTasks", source = "activeAssignments")
    TechnicianWorkloadResponse toWorkloadResponse(TechnicianWorkload workload);

    TechnicianUpdate toTechnicianUpdate(TechnicianUpdatePutRequest request);

}