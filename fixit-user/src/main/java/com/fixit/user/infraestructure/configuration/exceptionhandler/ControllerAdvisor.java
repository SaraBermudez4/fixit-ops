package com.fixit.user.infraestructure.configuration.exceptionhandler;

import com.fixit.user.domain.exceptions.*;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.ExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerAdvisor {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handlerInvalidCredentialsException(
            InvalidCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value()));
    }
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<Map<String, Object>> handleExternalServiceException(ExternalServiceException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("error", "External Service Communication Error");

        return new ResponseEntity<>(body, HttpStatus.BAD_GATEWAY);
    }
    @ExceptionHandler(TechnicianAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handlerTechnicianAlreadyExistsException(
            TechnicianAlreadyExistsException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.CONFLICT.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.CONFLICT.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handlerArgumentInvalidException(
            MethodArgumentNotValidException exception) {
        FieldError firstFieldError = exception.getFieldErrors().get(0);
        return ResponseEntity.badRequest().body(new ExceptionResponse(firstFieldError.getDefaultMessage(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(TechnicianBusyException.class)
    public ResponseEntity<ExceptionResponse> handleTechnicianBusyException(TechnicianBusyException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(TechnicianSameCategoryException.class)
    public ResponseEntity<ExceptionResponse> handleTechnicianSameCategoryException(TechnicianSameCategoryException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.BAD_REQUEST.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(TechnicianNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleTechnicianNotFoundException(TechnicianNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(
                exception.getMessage(), HttpStatus.NOT_FOUND.getReasonPhrase(), LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value()));
    }
}
