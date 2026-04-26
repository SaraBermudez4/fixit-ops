package com.fixit.tasks.infraestructure.configuration.bean;


import com.fixit.tasks.application.port.in.ITaskServicePort;
import com.fixit.tasks.application.port.out.INotificationEventPort;
import com.fixit.tasks.application.port.out.ITaskPersistencePort;
import com.fixit.tasks.application.port.out.ITechnicianFeignClientPort;
import com.fixit.tasks.application.usecase.TaskServiceUseCase;
import com.fixit.tasks.domain.service.AssignmentStrategy;
import com.fixit.tasks.domain.service.TaskDomainService;
import com.fixit.tasks.infraestructure.adapters.driven.feign.TechnicianFeignAdapter;
import com.fixit.tasks.infraestructure.adapters.driven.feign.clients.ITechnicianFeignClient;
import com.fixit.tasks.infraestructure.adapters.driven.feign.mapper.ITechnicianFeignMapper;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.adapter.TaskJpaAdapter;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.mapper.ITaskEntityMapper;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.repository.ITaskRepository;
import com.fixit.tasks.infraestructure.adapters.driven.rabbit.adapter.NotificationEventAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

    @Bean
    public ITaskPersistencePort taskPersistencePort(
            ITaskRepository taskRepository,
            ITaskEntityMapper taskEntityMapper
    ) {
        return new TaskJpaAdapter(taskRepository, taskEntityMapper);
    }
    @Bean
    public AssignmentStrategy assignmentStrategy() {
        return new AssignmentStrategy();
    }

    @Bean
    public TaskDomainService taskDomainService() {
        return new TaskDomainService();
    }


    @Bean
    public ITaskServicePort taskServicePort(
            ITaskPersistencePort taskPersistencePort,
            TaskDomainService taskDomainService,
            AssignmentStrategy assignmentStrategy,
            ITechnicianFeignClientPort technicianFeignClientPort,
            INotificationEventPort notificationEventPort
    ) {
        return new TaskServiceUseCase(taskPersistencePort, taskDomainService, assignmentStrategy, technicianFeignClientPort, notificationEventPort);
    }

    @Bean
    public ITechnicianFeignClientPort technicianFeignClientPort( ITechnicianFeignClient technicianFeignClient,
     ITechnicianFeignMapper feignMapper) {
        return new TechnicianFeignAdapter(technicianFeignClient, feignMapper);
    }

    @Bean
    public INotificationEventPort notificationEventPort(RabbitTemplate rabbitTemplate) {
        return new NotificationEventAdapter(rabbitTemplate);
    }
}