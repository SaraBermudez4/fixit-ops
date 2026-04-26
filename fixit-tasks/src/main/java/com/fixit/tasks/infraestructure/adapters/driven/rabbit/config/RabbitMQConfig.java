package com.fixit.tasks.infraestructure.adapters.driven.rabbit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fixit.tasks.infraestructure.adapters.driven.rabbit.dto.TaskNotificationEventDto;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE             = "fixit.tasks.exchange";
    public static final String ROUTING_KEY_ASSIGNED  = "task.notification.assigned";
    public static final String ROUTING_KEY_COMPLETED = "task.notification.completed";
    public static final String QUEUE_ASSIGNED        = "fixit.notifications.task.assigned";
    public static final String QUEUE_COMPLETED       = "fixit.notifications.task.completed";

    @Bean
    public TopicExchange taskExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue queueAssigned() {
        return QueueBuilder.durable(QUEUE_ASSIGNED).build();
    }

    @Bean
    public Queue queueCompleted() {
        return QueueBuilder.durable(QUEUE_COMPLETED).build();
    }

    @Bean
    public Binding bindingAssigned(Queue queueAssigned, TopicExchange taskExchange) {
        return BindingBuilder.bind(queueAssigned)
                .to(taskExchange)
                .with(ROUTING_KEY_ASSIGNED);
    }

    @Bean
    public Binding bindingCompleted(Queue queueCompleted, TopicExchange taskExchange) {
        return BindingBuilder.bind(queueCompleted)
                .to(taskExchange)
                .with(ROUTING_KEY_COMPLETED);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();

        classMapper.setDefaultType(TaskNotificationEventDto.class);
        classMapper.setIdClassMapping(Map.of(
                "taskNotification", TaskNotificationEventDto.class
        ));

        classMapper.setTrustedPackages("*");

        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());

        template.setBeforePublishPostProcessors(message -> {
            message.getMessageProperties()
                    .getHeaders()
                    .put("__TypeId__", "taskNotification");
            return message;
        });

        return template;
    }
}