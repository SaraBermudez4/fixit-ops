package com.fixit.user.infraestructure.adapters.driving.rest.dto.response.auth;

import lombok.Builder;

@Builder
public record AuthResponseDto(
        String accessToken,
        String tokenType,
        Long accessExpirationTime
) {
}