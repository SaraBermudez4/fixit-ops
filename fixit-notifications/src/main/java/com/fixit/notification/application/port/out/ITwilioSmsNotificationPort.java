package com.fixit.notification.application.port.out;

import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;

public interface ITwilioSmsNotificationPort {
    SmsNotificationResponse sendSms(SmsNotification smsNotification);

    void sendSms(String toPhoneNumber, String message);
}