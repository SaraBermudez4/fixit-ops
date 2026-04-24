package com.fixit.notification.infraestructure.adapters.driving.rest.controller;

import com.fixit.notification.application.port.in.ISmsNotificationServicePort;
import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;
import com.fixit.notification.infraestructure.adapters.driving.rest.dto.request.SmsNotificationRequestDto;
import com.fixit.notification.infraestructure.adapters.driving.rest.dto.response.SmsNotificationResponseDto;
import com.fixit.notification.infraestructure.adapters.driving.rest.mapper.INotificationRestMapper;
import com.fixit.notification.infraestructure.adapters.driving.rest.util.RestConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
@Tag(name = RestConstants.SMS_TAG_NAME, description = RestConstants.SMS_TAG_DESCRIPTION)
public class NotificationController {

    private final ISmsNotificationServicePort smsNotificationServicePort;
    private final INotificationRestMapper notificationRestMapper;

    @PostMapping("/sms")
    @Operation(
            summary = "Send SMS notification",
            description = "Sends an SMS notification using Twilio."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "SMS sent successfully",
                    content = @Content(schema = @Schema(implementation = SmsNotificationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "502", description = "Error communicating with Twilio", content = @Content)
    })
    public ResponseEntity<SmsNotificationResponseDto> sendSms(
            @RequestBody(
                    description = "Information required to send an SMS",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SmsNotificationRequestDto.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody SmsNotificationRequestDto requestDto
    ) {
        log.info("[CONTROLLER][SMS] Incoming request to send SMS to {}", requestDto.toPhoneNumber());

        SmsNotification smsNotification = notificationRestMapper.toModel(requestDto);

        SmsNotificationResponse response = smsNotificationServicePort.sendSms(smsNotification);

        log.info("[CONTROLLER][SMS-SUCCESS] SMS sent successfully to {}", requestDto.toPhoneNumber());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(notificationRestMapper.toResponseDto(response));
    }
}