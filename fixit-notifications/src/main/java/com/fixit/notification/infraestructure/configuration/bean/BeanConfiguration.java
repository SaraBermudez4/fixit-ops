package com.fixit.notification.infraestructure.configuration.bean;

import com.fixit.notification.application.port.in.ISmsNotificationServicePort;
import com.fixit.notification.application.port.out.ITwilioSmsNotificationPort;
import com.fixit.notification.application.usecase.SmsNotificationUseCase;
import com.fixit.notification.infraestructure.adapters.driven.jpa.adapter.TwilioJpaAdapter;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.handler.TaskNotificationEventHandler;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.listener.TaskNotificationListener;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.mapper.INotificationEventMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.validation.Validator;



@Configuration
public class BeanConfiguration {

    @Bean
    public ITwilioSmsNotificationPort twilioSmsNotificationPort(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.phone-number}") String fromPhoneNumber
    ) {
        return new TwilioJpaAdapter(accountSid, authToken, fromPhoneNumber);
    }

    @Bean
    public ISmsNotificationServicePort smsNotificationServicePort(
            ITwilioSmsNotificationPort twilioSmsNotificationPort
    ) {
        return new SmsNotificationUseCase(twilioSmsNotificationPort);
    }

    @Bean
    public TaskNotificationEventHandler taskNotificationEventHandler(
            Validator validator,
            ISmsNotificationServicePort smsNotificationServicePort,
            INotificationEventMapper mapper
    ) {
        return new TaskNotificationEventHandler(validator, smsNotificationServicePort, mapper);
    }

    @Bean
    public TaskNotificationListener taskNotificationListener(
            TaskNotificationEventHandler handler
    ) {
        return new TaskNotificationListener(handler);
    }
}