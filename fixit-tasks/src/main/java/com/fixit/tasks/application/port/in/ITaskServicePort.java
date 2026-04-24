package com.fixit.tasks.application.port.in;

import com.fixit.tasks.domain.model.AutoAssignSummary;
import com.fixit.tasks.domain.model.Task;

import java.util.List;


public interface ITaskServicePort{
    List<Task> getAll();
    Task getById(Long id);
    void delete(Long id);
    Task createTask(Task task);
    List<Task> getTasksByTechnicianId (Long technicianId);
    Task assignUrgentTask(Long taskId);
    Task updateTask(Long id, Task task);
    AutoAssignSummary autoAssignAllUrgentTasks();
    void processWaitingTasks();
    void startTask(Long taskId);
    void completeTask(Long taskId);
}