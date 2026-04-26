package com.fixit.notification.infraestructure.adapters.driving.rabbit.listener;

import com.fixit.notification.infraestructure.adapters.driving.rabbit.event.TaskNotificationEventDto;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.handler.TaskNotificationEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskNotificationListener {

    private final TaskNotificationEventHandler handler;

    @RabbitListener(queues = "${rabbit.queues.task-assigned}")
    public void onTaskAssigned(TaskNotificationEventDto dto) {
        log.info("[LISTENER] Received TASK_ASSIGNED event");
        handler.handle(dto);
    }

    @RabbitListener(queues = "${rabbit.queues.task-completed}")
    public void onTaskCompleted(TaskNotificationEventDto dto) {
        log.info("[LISTENER] Received TASK_COMPLETED event");
        handler.handle(dto);
    }
}