package com.fixit.tasks.application.port.out;

import com.fixit.tasks.domain.model.TaskNotificationEvent;

public interface INotificationEventPort {
    void sendTaskAssignedNotification(TaskNotificationEvent event);
    void sendTaskCompletedNotification(TaskNotificationEvent event);
}