package com.fixit.tasks.application.port.out;

import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskPersistencePort {

    Task save(Task task);
    long countUrgentTasksByTechnicianId(Long technicianId);

}