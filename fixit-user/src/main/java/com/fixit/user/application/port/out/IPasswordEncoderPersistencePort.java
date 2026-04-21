package com.fixit.user.application.port.out;

public interface IPasswordEncoderPersistencePort {

    String encodePassword(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);

}
