package com.fixit.tasks.infraestructure.adapters.driven.jpa.repository;

import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.infraestructure.adapters.driven.jpa.entity.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITaskRepository extends JpaRepository<TaskEntity, Long> {

    long countByTechnicianIdAndPriority(Long technicianId, TaskPriority priority);
    List<TaskEntity> findByStatus(TaskStatus status);
    List<TaskEntity> findByTechnicianId(Long technicianId);
}