package com.fixit.user.infraestructure.adapters.driving.rest.mapper;

import com.fixit.user.domain.model.AuthResponseModel;
import com.fixit.user.domain.model.AuthenticationModel;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.auth.AuthRequestDto;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.auth.AuthResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IAuthDtoMapper {
    AuthenticationModel toModel(AuthRequestDto authRequestDto);

    AuthResponseDto toAuthResponseDto(AuthResponseModel authResponseModel);
}