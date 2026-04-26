    package com.fixit.tasks.infraestructure.adapters.driven.rabbit.adapter;


    import com.fixit.tasks.application.port.out.INotificationEventPort;
    import com.fixit.tasks.domain.model.TaskNotificationEvent;
    import com.fixit.tasks.infraestructure.adapters.driven.rabbit.mapper.TaskNotificationEventMapper;
    import com.fixit.tasks.infraestructure.adapters.driven.rabbit.config.RabbitMQConfig;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;

    @Slf4j
    @RequiredArgsConstructor
    public class NotificationEventAdapter implements INotificationEventPort {

        private final RabbitTemplate rabbitTemplate;

        @Override
        public void sendTaskAssignedNotification(TaskNotificationEvent event) {
            var dto = TaskNotificationEventMapper.toDto(event, "TASK_ASSIGNED");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_ASSIGNED,
                    dto
            );
            log.info("Event TASK_ASSIGNED published for phone: {}", event.getPhoneNumber());
        }

        @Override
        public void sendTaskCompletedNotification(TaskNotificationEvent event) {
            var dto = TaskNotificationEventMapper.toDto(event, "TASK_COMPLETED");
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    RabbitMQConfig.ROUTING_KEY_COMPLETED,
                    dto
            );
            log.info("Event TASK_COMPLETED published for phone: {}", event.getPhoneNumber());
        }
    }