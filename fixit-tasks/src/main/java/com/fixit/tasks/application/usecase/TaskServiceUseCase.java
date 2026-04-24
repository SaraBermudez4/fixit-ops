package com.fixit.tasks.application.usecase;



import com.fixit.tasks.application.port.in.ITaskServicePort;
import com.fixit.tasks.application.port.out.ITaskPersistencePort;
import com.fixit.tasks.application.port.out.ITechnicianFeignClientPort;
import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.enums.TechnicianStatus;
import com.fixit.tasks.domain.model.MasterWithUrgentCount;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.domain.service.AssignmentStrategy;
import com.fixit.tasks.domain.service.TaskDomainService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TaskServiceUseCase implements ITaskServicePort {

    private final ITaskPersistencePort taskPersistencePort;
    private final TaskDomainService taskDomainService;
    private final AssignmentStrategy assignmentStrategy;
    private final ITechnicianFeignClientPort technicianFeignClientPort;

    @Override
    public Task createTask(Task task) {
        Task newTask = Task.createNewPending(task.getName(), task.getDescription(), task.getPriority());
        return (newTask.isUrgent()) ? handleUrgentCreation(newTask) : handleStandardCreation(newTask);
    }

    private Task handleUrgentCreation(Task task) {
        List<Technician> availableMasters = technicianFeignClientPort.findByCategory(TechnicianCategory.MASTER).stream()
                .filter(m -> m.getStatus() != TechnicianStatus.NOT_AVAILABLE)
                .toList();

        if (availableMasters.isEmpty()) {
            return taskPersistencePort.save(task);
        }

        return assignTaskToMaster(task);
    }

    private Task handleStandardCreation(Task task) {
        List<Technician> allTechs = technicianFeignClientPort.findAll();
        return assignmentStrategy.findTechnicianByHierarchy(allTechs, task)
                .map(selected -> executeAssignment(task, selected, task.getPriority().getPoints()))
                .orElseGet(() -> taskPersistencePort.save(task));
    }
    private Task assignTaskToMaster(Task task) {
        List<Technician> masters = technicianFeignClientPort.findByCategory(TechnicianCategory.MASTER).stream()
                .filter(m -> m.getStatus() != TechnicianStatus.NOT_AVAILABLE)
                .toList();

        if (masters.isEmpty()) {
            return taskPersistencePort.save(task);
        }

        List<MasterWithUrgentCount> mastersWithCount = masters.stream()
                .map(m -> new MasterWithUrgentCount(m, taskPersistencePort.countUrgentTasksByTechnicianId(m.getUser().getId())))
                .toList();

        Technician selected = taskDomainService.selectBestMaster(mastersWithCount);
        return executeAssignment(task, selected, 0);
    }
    @Override
    public List<Task> findByTechnicianId(Long technicianId) {
        return taskPersistencePort.findByTechnicianId(technicianId);
    }

    private Task executeAssignment(Task task, Technician technician, int points) {
        Technician updatedTech = assignmentStrategy.updateTechnicianState(technician, points);
        technicianFeignClientPort.updateTechnician(updatedTech);

        Task assignedTask = task.toBuilder()
                .technicianId(technician.getUser().getId())
                .status(TaskStatus.ASSIGNED)
                .build();

        return taskPersistencePort.save(assignedTask);
    }
}