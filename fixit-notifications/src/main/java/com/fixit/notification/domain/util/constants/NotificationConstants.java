package com.fixit.notification.domain.util.constants;

public final class NotificationConstants {

    private NotificationConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String INVALID_PHONE_NUMBER_MESSAGE =
            "Invalid phone number. It must be in E.164 format, for example +573001112233.";

    public static final String TWILIO_SMS_GENERIC_ERROR =
            "Error sending SMS notification.";

    public static final String TWILIO_SMS_UNAUTHORIZED =
            "Twilio authentication failed.";

    public static final String TWILIO_SMS_BAD_REQUEST =
            "Invalid SMS request for Twilio.";

    public static final String SMS_MESSAGE_REQUIRED =
            "Message is required.";

    public static final String SMS_TO_REQUIRED =
            "Destination phone number is required.";

    public static final String SMS_MESSAGE_SIZE =
            "Message must be between 1 and 1600 characters.";

    public static final String SMS_TO_INVALID =
            "Phone number must be in E.164 format.";
}