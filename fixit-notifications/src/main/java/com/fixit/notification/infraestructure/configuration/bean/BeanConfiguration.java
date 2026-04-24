package com.fixit.notification.infraestructure.configuration.bean;

import com.fixit.notification.application.port.in.ISmsNotificationServicePort;
import com.fixit.notification.application.port.out.ITwilioSmsNotificationPort;
import com.fixit.notification.application.usecase.SmsNotificationUseCase;
import com.fixit.notification.infraestructure.adapters.driven.twilio.TwilioSmsAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ITwilioSmsNotificationPort twilioSmsNotificationPort(
            @Value("${twilio.account-sid}") String accountSid,
            @Value("${twilio.auth-token}") String authToken,
            @Value("${twilio.phone-number}") String fromPhoneNumber
    ) {
        return new TwilioSmsAdapter(accountSid, authToken, fromPhoneNumber);
    }

    @Bean
    public ISmsNotificationServicePort smsNotificationServicePort(
            ITwilioSmsNotificationPort twilioSmsNotificationPort
    ) {
        return new SmsNotificationUseCase(twilioSmsNotificationPort);
    }
}