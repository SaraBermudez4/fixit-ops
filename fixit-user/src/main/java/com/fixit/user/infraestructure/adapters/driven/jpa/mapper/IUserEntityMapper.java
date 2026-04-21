package com.fixit.user.infraestructure.adapters.driven.jpa.mapper;

import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.User;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.TechnicianEntity;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IUserEntityMapper {

    UserEntity toUserEntity(User user);

    User toUserDomain(UserEntity entity);

    @Mapping(target = "user", source = "user")
    TechnicianEntity toTechEntity(Technician technician);

    @Mapping(target = "user", source = "user")
    Technician toTechDomain(TechnicianEntity entity);
}