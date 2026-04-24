package com.fixit.notification.application.port.in;

import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;

public interface ISmsNotificationServicePort {
    SmsNotificationResponse sendSms(SmsNotification smsNotification);
}