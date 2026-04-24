package com.fixit.notification.infraestructure.adapters.driven.jpa.adapter;

import com.fixit.notification.application.port.out.ITwilioSmsNotificationPort;
import com.fixit.notification.domain.exceptions.SmsNotificationException;
import com.fixit.notification.domain.model.SmsNotification;
import com.fixit.notification.domain.model.SmsNotificationResponse;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

import static com.fixit.notification.domain.util.constants.NotificationConstants.*;

@Slf4j
public class TwilioJpaAdapter implements ITwilioSmsNotificationPort {

    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;

    public TwilioJpaAdapter(String accountSid, String authToken, String fromPhoneNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhoneNumber = fromPhoneNumber;
    }

    @Override
    public SmsNotificationResponse sendSms(SmsNotification smsNotification) {
        try {
            log.info("[TWILIO][SMS] Sending SMS to {}", smsNotification.toPhoneNumber());

            Twilio.init(accountSid, authToken);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(smsNotification.toPhoneNumber()),
                    new PhoneNumber(fromPhoneNumber),
                    smsNotification.message()
            ).create();

            log.info("[TWILIO][SMS-SUCCESS] SMS sent successfully. sid={}, to={}",
                    twilioMessage.getSid(), smsNotification.toPhoneNumber());

            return SmsNotificationResponse.builder()
                    .sid(twilioMessage.getSid())
                    .status(twilioMessage.getStatus().toString())
                    .toPhoneNumber(smsNotification.toPhoneNumber())
                    .message(smsNotification.message())
                    .sentAt(LocalDateTime.now())
                    .build();

        } catch (ApiException ex) {
            log.error("[TWILIO][SMS-ERROR] Twilio API error while sending SMS to {}", smsNotification.toPhoneNumber(), ex);

            if (ex.getStatusCode() != null && ex.getStatusCode() == 400) {
                throw new SmsNotificationException(TWILIO_SMS_BAD_REQUEST);
            }

            if (ex.getStatusCode() != null && ex.getStatusCode() == 401) {
                throw new SmsNotificationException(TWILIO_SMS_UNAUTHORIZED);
            }

            throw new SmsNotificationException(TWILIO_SMS_GENERIC_ERROR);

        } catch (Exception ex) {
            log.error("[TWILIO][SMS-ERROR] Unexpected error while sending SMS to {}", smsNotification.toPhoneNumber(), ex);
            throw new SmsNotificationException(TWILIO_SMS_GENERIC_ERROR);
        }
    }

}
