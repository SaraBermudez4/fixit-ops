package com.fixit.notification.infraestructure.configuration.exceptionhandler;

import com.fixit.notification.domain.exceptions.InvalidPhoneNumberException;
import com.fixit.notification.domain.exceptions.SmsNotificationException;
import com.fixit.notification.infraestructure.adapters.driving.rest.dto.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(InvalidPhoneNumberException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidPhoneNumberException(
            InvalidPhoneNumberException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        exception.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }

    @ExceptionHandler(SmsNotificationException.class)
    public ResponseEntity<ExceptionResponse> handleSmsNotificationException(
            SmsNotificationException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(
                new ExceptionResponse(
                        exception.getMessage(),
                        HttpStatus.BAD_GATEWAY.getReasonPhrase(),
                        LocalDateTime.now(),
                        HttpStatus.BAD_GATEWAY.value()
                )
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception
    ) {
        FieldError firstFieldError = exception.getFieldErrors().get(0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponse(
                        firstFieldError.getDefaultMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        LocalDateTime.now(),
                        HttpStatus.BAD_REQUEST.value()
                )
        );
    }
}