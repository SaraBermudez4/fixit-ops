package com.fixit.notification.application.usecase;

import static com.fixit.notification.domain.util.constants.NotificationConstants.INVALID_PHONE_NUMBER_MESSAGE;
import com.fixit.notification.application.port.in.ISmsNotificationServicePort;
import com.fixit.notification.application.port.out.ITwilioSmsNotificationPort;
import com.fixit.notification.domain.exceptions.InvalidPhoneNumberException;
import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
public class SmsNotificationUseCase implements ISmsNotificationServicePort {

    private final ITwilioSmsNotificationPort twilioSmsNotificationPort;

    @Override
    public SmsNotificationResponse sendSms(SmsNotification smsNotification) {
        log.info("[USECASE][SMS] Request to send SMS to {}", smsNotification.toPhoneNumber());

        SmsNotificationResponse response = twilioSmsNotificationPort.sendSms(smsNotification);

        log.info("[USECASE][SMS-SUCCESS] SMS sent successfully to {}", smsNotification.toPhoneNumber());
        return response;
    }

}