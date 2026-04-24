package com.fixit.tasks.infraestructure.adapters.driven.jpa.adapter;

import com.fixit.tasks.application.port.out.ITaskPersistencePort;
import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.mapper.ITaskEntityMapper;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.repository.ITaskRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TaskJpaAdapter implements ITaskPersistencePort {

    private final ITaskRepository taskRepository;
    private final ITaskEntityMapper taskEntityMapper;

    @Override
    public Task save(Task task) {
        return taskEntityMapper.toDomain(
                taskRepository.save(taskEntityMapper.toEntity(task))
        );
    }

    @Override
    public long countUrgentTasksByTechnicianId(Long technicianId) {
        return taskRepository.countByTechnicianIdAndPriority(technicianId, TaskPriority.URGENT);
    }

    @Override
    public List<Task> findByTechnicianId(Long technicianId) {
        return taskEntityMapper.toDomainList(taskRepository.findByTechnicianId(technicianId));
    }
}