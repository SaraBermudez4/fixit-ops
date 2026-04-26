package com.fixit.notification.infraestructure.adapters.driving.rabbit.util;

public final class EventConstants {

    private EventConstants() {
        throw new IllegalStateException("Utility class");
    }


    public static final String QUEUE_ASSIGNED  = "fixit.notifications.task.assigned";
    public static final String QUEUE_COMPLETED = "fixit.notifications.task.completed";

    public static final String EVENT_ASSIGNED  = "TASK_ASSIGNED";
    public static final String EVENT_COMPLETED = "TASK_COMPLETED";

    public static final String EVENT_TYPE_REQUIRED     = "eventType is required";
    public static final String PHONE_REQUIRED          = "phoneNumber is required";
    public static final String PHONE_INVALID_FORMAT    = "phoneNumber format is invalid";
    public static final String MESSAGE_REQUIRED        = "message is required";
    public static final String PHONE_REGEX             = "^\\+[1-9]\\d{7,14}$";


  }