package com.fixit.tasks.application.port.out;

public interface IJwtPersistencePort {
    String generateAccessToken(String email, Long userId, String role);

    String getUsernameFromToken(String token);
    Long getUserIdFromToken(String token);
    String getRoleFromToken(String token);

    boolean isTokenValid(String token, String username);

    Long getAccessExpirationTime();
}