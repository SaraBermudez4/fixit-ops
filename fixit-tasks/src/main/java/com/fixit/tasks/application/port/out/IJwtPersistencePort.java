package com.fixit.tasks.application.port.out;

public interface IJwtPersistencePort {

    String getUsernameFromToken(String token);
    String getRoleFromToken(String token);
}