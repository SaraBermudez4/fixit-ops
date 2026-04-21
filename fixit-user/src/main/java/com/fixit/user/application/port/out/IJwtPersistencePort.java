package com.fixit.user.application.port.out;

public interface IJwtPersistencePort {

    String generateAccessToken(String email, Long userId, String role);

    String getUsernameFromToken(String token);

    boolean isTokenValid(String token, String username);

    Long getAccessExpirationTime();
}