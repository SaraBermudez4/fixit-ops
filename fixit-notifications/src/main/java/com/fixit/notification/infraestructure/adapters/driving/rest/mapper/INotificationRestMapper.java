package com.fixit.notification.infraestructure.adapters.driving.rest.mapper;

import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;
import com.fixit.notification.infraestructure.adapters.driving.rest.dto.request.SmsNotificationRequestDto;
import com.fixit.notification.infraestructure.adapters.driving.rest.dto.response.SmsNotificationResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface INotificationRestMapper {

    SmsNotification toModel(SmsNotificationRequestDto requestDto);

    SmsNotificationResponseDto toResponseDto(SmsNotificationResponse response);
}