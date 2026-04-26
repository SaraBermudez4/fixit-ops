package com.fixit.notification.infraestructure.adapters.driving.rabbit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fixit.notification.infraestructure.adapters.driving.rabbit.event.TaskNotificationEventDto;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

import static com.fixit.notification.infraestructure.adapters.driving.rabbit.util.EventConstants.QUEUE_ASSIGNED;
import static com.fixit.notification.infraestructure.adapters.driving.rabbit.util.EventConstants.QUEUE_COMPLETED;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue queueAssigned() {
        return QueueBuilder.durable(QUEUE_ASSIGNED).build();
    }

    @Bean
    public Queue queueCompleted() {
        return QueueBuilder.durable(QUEUE_COMPLETED).build();
    }

    @Bean
    public Jackson2JsonMessageConverter consumerMessageConverter() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(mapper);

        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(Map.of(
                "taskNotification", TaskNotificationEventDto.class
        ));
        classMapper.setTrustedPackages("*");
        converter.setClassMapper(classMapper);

        return converter;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(consumerMessageConverter());
        return factory;
    }
}