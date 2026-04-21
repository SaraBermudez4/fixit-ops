package com.fixit.tasks.infraestructure.adapters.driven.feign.configuration;


import com.fixit.tasks.domain.exceptions.ExternalServiceException;
import com.fixit.tasks.domain.exceptions.UserRoleConflictException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

import static com.fixit.tasks.domain.util.constants.DomainConstants.*;


@Slf4j
public class CustomFeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        log.error("[FEIGN-ERROR] Method: {} | Status: {}", methodKey, response.status());

        return switch (response.status()) {
            case 400 -> new ExternalServiceException(FEIGN_INVALID_REQUEST);
            case 404 -> new ExternalServiceException(FEIGN_NOT_FOUND);
            case 409 -> new UserRoleConflictException(FEIGN_ROLE_CONFLICT);
            default -> new ExternalServiceException(FEIGN_GENERIC_ERROR);
        };
    }
}