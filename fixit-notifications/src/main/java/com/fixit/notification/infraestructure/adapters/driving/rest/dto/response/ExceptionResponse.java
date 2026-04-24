package com.fixit.notification.infraestructure.adapters.driving.rest.dto.response;

import java.time.LocalDateTime;

public class ExceptionResponse {

    private final String message;
    private final Integer code;
    private final String status;
    private final LocalDateTime timestamp;

    public ExceptionResponse(String message, String status, LocalDateTime timestamp, Integer code) {
        this.message = message;
        this.code = code;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}