package com.fixit.tasks.domain.service;

import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.exceptions.*;
import com.fixit.tasks.domain.model.MasterWithUrgentCount;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.domain.util.constants.DomainConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
public class TaskDomainService {

    public void validateTaskCanBeDeleted(Task task) {
        log.debug("Validating deletion eligibility for task ID: {} with status: {}", task.getId(), task.getStatus());
        if (TaskStatus.IN_PROGRESS.equals(task.getStatus()) || TaskStatus.COMPLETED.equals(task.getStatus())) {
            log.error("Deletion failed: Task ID {} is in an immutable state ({})", task.getId(), task.getStatus());
            throw new TaskCannotBeDeletedException(
                    String.format(DomainConstants.TASK_CANNOT_BE_DELETED_MESSAGE, task.getId(), task.getStatus()));
        }
    }

    public Task validateTaskExist(Optional<Task> task, Long id) {
        if (task.isEmpty()) {
            log.error("Task validation failed: Task with ID {} not found", id);
            throw new TaskNotFoundException(String.format(DomainConstants.TASK_NOT_FOUND_MESSAGE, id));
        }
        return task.get();
    }

    public void validateTaskUrgent(Task task) {
        log.debug("Validating urgency and assignment status for task ID: {}", task.getId());
        if (!task.isUrgent()) {
            log.warn("Validation failed: Task ID {} is not marked as URGENT", task.getId());
            throw new TaskNotUrgentException(DomainConstants.TASK_NOT_URGENT_MESSAGE);
        }

        if (task.getStatus().equals(TaskStatus.ASSIGNED)) {
            log.warn("Validation failed: Task ID {} is already ASSIGNED", task.getId());
            throw new TaskNotUrgentException(DomainConstants.TASK_NOT_ASSIGNED_MESSAGE);
        }
    }

    public List<Task> getPendingUrgentTasks(List<Task> tasks) {
        log.debug("Filtering list for pending urgent tasks");
        return tasks.stream()
                .filter(Task::isUrgent)
                .filter(task -> TaskStatus.PENDING.equals(task.getStatus()))
                .toList();
    }

    public Technician selectBestMaster(List<MasterWithUrgentCount> mastersWithCount) {
        if (mastersWithCount.isEmpty()) {
            log.error("Master selection failed: No Master technicians available in the provided list");
            throw new NoMasterTechniciansAvailableException(DomainConstants.NO_MASTER_TECHNICIANS_AVAILABLE_MESSAGE);
        }

        long minCount = mastersWithCount.stream()
                .mapToLong(MasterWithUrgentCount::urgentCount)
                .min()
                .orElse(0L);

        List<Technician> candidates = mastersWithCount.stream()
                .filter(m -> m.urgentCount() == minCount)
                .map(MasterWithUrgentCount::master)
                .toList();

        Technician selected = candidates.get(ThreadLocalRandom.current().nextInt(candidates.size()));
        log.info("Master selection successful: Technician ID {} chosen (Urgent task load: {})",
                selected.getUser().getId(), minCount);

        return selected;
    }

    public void validatePriorityTask(Task existingTask, Task updatedTask) {
        if (existingTask.getPriority().equals(updatedTask.getPriority())) {
            log.warn("Update redundant: Task ID {} already has {} priority", existingTask.getId(), existingTask.getPriority());
            throw new TaskAlreadyHasPriorityException(DomainConstants.TASK_ALREADY_HAS_PRIORITY, updatedTask.getId());
        }
    }

    public void validateStatusAssigned(Task task) {
        if (!task.getStatus().equals(TaskStatus.ASSIGNED)) {
            log.error("Status validation failed: Task ID {} must be ASSIGNED to start, but is {}", task.getId(), task.getStatus());
            throw new TaskMustBeAssignedToStartException(DomainConstants.TASK_MUST_BE_ASSIGNED_TO_START);
        }
    }

    public void validateStatusProgress(Task task) {
        if (!task.getStatus().equals(TaskStatus.IN_PROGRESS)) {
            log.error("Status validation failed: Task ID {} must be IN_PROGRESS to complete, but is {}", task.getId(), task.getStatus());
            throw new TaskMustBeProgressToStartException(DomainConstants.TASK_MUST_BE_IN_PROGRESS);
        }
    }
}