package com.fixit.user.infraestructure.configuration.bean;

import com.fixit.user.application.port.in.IAuthServicePort;
import com.fixit.user.application.port.in.ITechnicianServicePort;
import com.fixit.user.application.port.out.*;
import com.fixit.user.application.usecase.AuthUseCase;
import com.fixit.user.application.usecase.TechnicianUseCase;
import com.fixit.user.domain.service.TechnicianDomainService;
import com.fixit.user.infraestructure.adapters.driven.feign.TaskFeignAdapter;
import com.fixit.user.infraestructure.adapters.driven.feign.clients.ITaskFeignClient;
import com.fixit.user.infraestructure.adapters.driven.feign.mapper.ITaskFeignMapper;
import com.fixit.user.infraestructure.adapters.driven.security.adapter.PasswordEncoderAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {



    @Bean
    public TechnicianDomainService technicianDomainService() {
        return new TechnicianDomainService();
    }





    @Bean
    public ITechnicianServicePort technicianServicePort(
            ITechnicianPersistencePort userPersistencePort,
            IPasswordEncoderPersistencePort passwordEncoderPort,
            ITaskFeignClientPort taskFeignClientPort

    ) {
        return new TechnicianUseCase(
                userPersistencePort,
                passwordEncoderPort,
                technicianDomainService(),
                taskFeignClientPort
        );
    }


    @Bean
    public IPasswordEncoderPersistencePort passwordEncoderPersistencePort(PasswordEncoder passwordEncoder) {
        return new PasswordEncoderAdapter(passwordEncoder);
    }

    @Bean
    public IAuthServicePort authServicePort(
            ITechnicianPersistencePort userPersistencePort,
            IPasswordEncoderPersistencePort passwordEncoderPersistencePort,
            IJwtPersistencePort jwtPersistencePort) {
        return new AuthUseCase(
                userPersistencePort,
                passwordEncoderPersistencePort,
                jwtPersistencePort);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(ITechnicianPersistencePort userPersistencePort) {
        return username -> {
            var user = userPersistencePort.findByEmail(username);
            if (user == null) {
                throw new UsernameNotFoundException("Usuario no encontrado: " + username);
            }

            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getEmail())
                    .password(user.getPassword())
                    .authorities("ROLE_" + user.getRole().name())
                    .build();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider(ITechnicianPersistencePort userPersistencePort) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService(userPersistencePort));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public ITaskFeignClientPort taskFeignClientPort(ITaskFeignClient taskFeignClient,
                                                      ITaskFeignMapper feignMapper) {
        return new TaskFeignAdapter(taskFeignClient, feignMapper);
    }
}