package com.fixit.tasks.application.port.out;

import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskPersistencePort {
    List<Task> findByStatus(TaskStatus status);
    Task save(Task task);
    long countUrgentTasksByTechnicianId(Long technicianId);
    List<Task> findByTechnicianId (Long technicianId);
    Optional<Task> findById(Long id);
    List<Task> findAll();
    void deleteById(Long id);
}