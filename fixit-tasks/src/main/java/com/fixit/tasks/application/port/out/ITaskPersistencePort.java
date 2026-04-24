package com.fixit.tasks.application.port.out;

import com.fixit.tasks.domain.model.Task;

import java.util.List;

public interface ITaskPersistencePort {

    Task save(Task task);
    long countUrgentTasksByTechnicianId(Long technicianId);
    List<Task> findByTechnicianId(Long technicianId);

}