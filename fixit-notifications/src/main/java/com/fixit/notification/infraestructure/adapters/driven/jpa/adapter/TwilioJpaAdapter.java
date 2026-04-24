package com.fixit.notification.infraestructure.adapters.driven.jpa.adapter;

import com.fixit.notification.application.port.out.ITwilioSmsNotificationPort;
import com.fixit.notifications.application.port.out.ITaskPersistencePort;
import com.fixit.notifications.domain.model.Task;
import com.fixit.notifications.infraestructure.adapters.driven.jpa.mapper.ITaskEntityMapper;
import com.fixit.notifications.infraestructure.adapters.driven.jpa.repository.ITaskRepository;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TwilioJpaAdapter implements ITwilioSmsNotificationPort {

    private final String accountSid;
    private final String authToken;
    private final String fromPhoneNumber;

    public TwilioSmsAdapter(String accountSid, String authToken, String fromPhoneNumber) {
        this.accountSid = accountSid;
        this.authToken = authToken;
        this.fromPhoneNumber = fromPhoneNumber;
    }

    @Override
    public void sendSms(String toPhoneNumber, String message) {
        try {
            log.info("[TWILIO][SMS] Sending SMS to {}", toPhoneNumber);

            Twilio.init(accountSid, authToken);

            Message twilioMessage = Message.creator(
                    new PhoneNumber(toPhoneNumber),
                    new PhoneNumber(fromPhoneNumber),
                    message
            ).create();

            log.info("[TWILIO][SMS-SUCCESS] SMS sent successfully. sid={}, to={}",
                    twilioMessage.getSid(), toPhoneNumber);

        } catch (ApiException ex) {
            log.error("[TWILIO][SMS-ERROR] Twilio API error while sending SMS to {}", toPhoneNumber, ex);

            if (ex.getStatusCode() != null && ex.getStatusCode() == 400) {
                throw new SmsNotificationException(TWILIO_SMS_INVALID_PHONE);
            }

            if (ex.getStatusCode() != null && ex.getStatusCode() == 401) {
                throw new SmsNotificationException(TWILIO_SMS_UNAUTHORIZED);
            }

            throw new SmsNotificationException(TWILIO_SMS_GENERIC_ERROR);

        } catch (Exception ex) {
            log.error("[TWILIO][SMS-ERROR] Unexpected error while sending SMS to {}", toPhoneNumber, ex);
            throw new SmsNotificationException(TWILIO_SMS_GENERIC_ERROR);
        }
    }
}