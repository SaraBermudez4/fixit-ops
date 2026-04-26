package com.fixit.notification.infraestructure.adapters.driving.rabbit.handler;

import com.fixit.notification.application.port.in.ISmsNotificationServicePort;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.event.TaskNotificationEventDto;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.mapper.INotificationEventMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class TaskNotificationEventHandler {

    private final Validator validator;
    private final ISmsNotificationServicePort smsNotificationServicePort;
    private  final INotificationEventMapper notificationEventMapper;

    public void handle(TaskNotificationEventDto dto) {
        log.info("[HANDLER] Processing event: {} for phone: {}", dto.getEventType(), dto.getPhoneNumber());

        Set<ConstraintViolation<TaskNotificationEventDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            log.error("[HANDLER] Validation failed for event: {}", dto.getEventType());
            throw new ConstraintViolationException(violations);
        }

        smsNotificationServicePort.sendSms(notificationEventMapper.toModel(dto));

        log.info("[HANDLER] Event {} processed successfully for phone: {}", dto.getEventType(), dto.getPhoneNumber());
    }
}