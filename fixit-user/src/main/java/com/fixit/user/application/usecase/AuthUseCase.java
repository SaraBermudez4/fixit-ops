package com.fixit.user.application.usecase;

import com.fixit.user.application.port.in.IAuthServicePort;
import com.fixit.user.application.port.out.IJwtPersistencePort;
import com.fixit.user.application.port.out.IPasswordEncoderPersistencePort;
import com.fixit.user.application.port.out.ITechnicianPersistencePort;
import com.fixit.user.domain.exceptions.InvalidCredentialsException;
import com.fixit.user.domain.model.AuthResponseModel;
import com.fixit.user.domain.model.AuthenticationModel;
import com.fixit.user.domain.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.fixit.user.domain.util.constants.AuthConstants.INVALID_CREDENTIALS_EXCEPTION_MESSAGE;

@Slf4j
@RequiredArgsConstructor
public class AuthUseCase implements IAuthServicePort {


    private final ITechnicianPersistencePort userPersistencePort;
    private final IPasswordEncoderPersistencePort passwordEncoder;
    private final IJwtPersistencePort jwtPort;

    @Override
    public AuthResponseModel authenticate(AuthenticationModel auth) {
        log.info("[USE-CASE] Authenticating user: {}", auth.email());

        User user = userPersistencePort.findByEmail(auth.email());
        if (user == null) {
            log.warn("[USE-CASE] User not found: {}", auth.email());
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION_MESSAGE);
        }

        log.info("[USE-CASE] User found for email: {}", auth.email());

        if (!passwordEncoder.matches(auth.password(), user.getPassword())) {
            log.warn("[USE-CASE] Invalid password for user: {}", auth.email());
            throw new InvalidCredentialsException(INVALID_CREDENTIALS_EXCEPTION_MESSAGE);
        }

        log.info("[USE-CASE] Credentials validated successfully for userId={}, role={}", user.getId(), user.getRole());

        String accessToken = jwtPort.generateAccessToken(user.getEmail(), user.getId(), user.getRole().name());

        log.info("[USE-CASE] User authenticated successfully: {}", auth.email());

        return new AuthResponseModel(
                accessToken,
                "Bearer",
                jwtPort.getAccessExpirationTime()
        );
    }
}