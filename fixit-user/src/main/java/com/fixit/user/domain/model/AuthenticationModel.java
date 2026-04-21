package com.fixit.user.domain.model;

public record AuthenticationModel(
        String email,
        String password
) {
}