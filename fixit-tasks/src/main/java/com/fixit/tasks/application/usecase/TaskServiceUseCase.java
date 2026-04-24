package com.fixit.tasks.application.usecase;

import com.fixit.tasks.application.port.in.ITaskServicePort;
import com.fixit.tasks.application.port.out.ITaskPersistencePort;
import com.fixit.tasks.application.port.out.ITechnicianFeignClientPort;
import com.fixit.tasks.domain.enums.TaskPriority;
import com.fixit.tasks.domain.enums.TaskStatus;
import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.enums.TechnicianStatus;
import com.fixit.tasks.domain.model.AutoAssignSummary;
import com.fixit.tasks.domain.model.MasterWithUrgentCount;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.domain.service.AssignmentStrategy;
import com.fixit.tasks.domain.service.TaskDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class TaskServiceUseCase implements ITaskServicePort {

    private final ITaskPersistencePort taskPersistencePort;
    private final TaskDomainService taskDomainService;
    private final AssignmentStrategy assignmentStrategy;
    private final ITechnicianFeignClientPort technicianFeignClientPort;

    @Override
    public List<Task> getAll() {
        log.info("Fetching all tasks from the system");
        return taskPersistencePort.findAll();
    }

    @Override
    public Task getById(Long id) {
        log.info("Searching for task with ID: {}", id);
        return taskDomainService.validateTaskExist(taskPersistencePort.findById(id), id);
    }

    @Override
    public void delete(Long id) {
        log.info("Initiating deletion process for task ID: {}", id);
        Task task = getById(id);
        taskDomainService.validateTaskCanBeDeleted(task);

        if (task.getTechnicianId() != null) {
            log.info("Releasing technician ID: {} prior to task deletion", task.getTechnicianId());
            updateTechnicianOnTaskDeletion(task);
        }

        taskPersistencePort.deleteById(id);
        log.info("Task ID: {} successfully deleted", id);
    }

    @Override
    public Task createTask(Task task) {
        log.info("Creating new task: '{}' with priority: {}", task.getName(), task.getPriority());
        Task newTask = Task.createNewPending(task.getName(), task.getDescription(), task.getPriority());
        return (newTask.isUrgent()) ? handleUrgentCreation(newTask) : handleStandardCreation(newTask);
    }

    @Override
    public List<Task> getTasksByTechnicianId(Long technicianId) {
        log.info("Retrieving tasks assigned to technician ID: {}", technicianId);
        return taskPersistencePort.findByTechnicianId(technicianId);
    }

    @Override
    public Task updateTask(Long id, Task updatedTask) {
        log.info("Updating task ID: {}", id);
        Task existingTask = getById(id);
        taskDomainService.validatePriorityTask(existingTask, updatedTask);
        Task taskToSave = mapBasicInfo(existingTask, updatedTask);

        if (existingTask.getTechnicianId() == null) {
            return handleUnassignedTaskUpdate(taskToSave);
        }

        Technician technician = technicianFeignClientPort.findById(existingTask.getTechnicianId());

        if (technician == null) {
            log.warn("Previously assigned technician not found for task ID: {}", id);
            return saveAsUnassigned(taskToSave);
        }

        return processTaskWithTechnician(taskToSave, existingTask, technician);
    }

    @Override
    public void startTask(Long taskId) {
        log.info("Setting status to IN_PROGRESS for task ID: {}", taskId);
        Task task = getById(taskId);
        taskDomainService.validateStatusAssigned(task);

        Task startedTask = task.start();
        taskPersistencePort.save(startedTask);
    }

    @Override
    public void completeTask(Long taskId) {
        log.info("Completing task ID: {}", taskId);
        Task task = getById(taskId);
        taskDomainService.validateStatusProgress(task);

        Technician technician = technicianFeignClientPort.findById(task.getTechnicianId());
        Technician updatedTechnician = assignmentStrategy.releaseTechnicianLoad(
                technician,
                task.getPriority().getPoints()
        );

        Task completedTask = task.complete();

        technicianFeignClientPort.updateTechnician(updatedTechnician);
        taskPersistencePort.save(completedTask);
        log.info("Task ID: {} completed and technician ID: {} load released", taskId, technician.getUser().getId());
    }

    @Override
    public AutoAssignSummary autoAssignAllUrgentTasks() {
        log.info("Starting automatic assignment for all pending urgent tasks");
        List<Task> pendingUrgent = taskDomainService.getPendingUrgentTasks(taskPersistencePort.findAll());

        if (pendingUrgent.isEmpty()) {
            log.info("No pending urgent tasks found for assignment");
            return AutoAssignSummary.buildEmptySummary();
        }

        pendingUrgent.forEach(this::assignTaskToMaster);

        List<Task> remaining = taskDomainService.getPendingUrgentTasks(taskPersistencePort.findAll());
        long assignedCount = pendingUrgent.size() - remaining.size();

        log.info("Auto-assignment summary: {} tasks assigned, {} tasks remaining pending", assignedCount, remaining.size());
        return AutoAssignSummary.buildFinalSummary(assignedCount, remaining.size());
    }

    @Override
    public void processWaitingTasks() {
        log.info("Processing waiting tasks queue");
        List<Task> waitingTasks = taskPersistencePort.findByStatus(TaskStatus.PENDING);

        if (waitingTasks == null || waitingTasks.isEmpty()) {
            log.info("No waiting tasks to process");
            return;
        }

        List<Technician> technicians = technicianFeignClientPort.findAll();

        for (Task task : waitingTasks) {
            assignmentStrategy.findTechnicianByHierarchy(technicians, task)
                    .ifPresent(selected -> {
                        log.info("Assigning pending task '{}' to technician ID: {}", task.getName(), selected.getUser().getId());
                        executeAssignment(task, selected, task.getPriority().getPoints());
                    });
        }
    }

    @Override
    public Task assignUrgentTask(Long taskId) {
        log.info("Requesting manual assignment to MASTER for urgent task ID: {}", taskId);
        Task task = getById(taskId);
        taskDomainService.validateTaskUrgent(task);
        return assignTaskToMaster(task);
    }

    private Task prepareForUnassignment(Task task) {
        return task.toBuilder()
                .technicianId(null)
                .status(TaskStatus.PENDING)
                .build();
    }

    private Task saveAsUnassigned(Task task) {
        log.info("Saving task ID: {} as UNASSIGNED", task.getId());
        return taskPersistencePort.save(prepareForUnassignment(task));
    }

    private Task handleUnassignedTaskUpdate(Task task) {
        if (task.getPriority() == TaskPriority.URGENT) {
            log.info("Task ID: {} priority upgraded to URGENT, searching for Master technician", task.getId());
            return assignTaskToMaster(prepareForUnassignment(task));
        }
        return taskPersistencePort.save(task);
    }

    private Task mapBasicInfo(Task existing, Task updated) {
        return existing.toBuilder()
                .name(updated.getName())
                .description(updated.getDescription())
                .priority(updated.getPriority())
                .build();
    }

    private Task processTaskWithTechnician(Task taskToSave, Task existingTask, Technician technician) {
        TaskPriority oldPriority = existingTask.getPriority();
        TaskPriority newPriority = taskToSave.getPriority();

        if (newPriority == TaskPriority.URGENT || technician.getCategory() == TechnicianCategory.MASTER) {
            log.info("Reassignment required due to URGENT priority or Master category technician");
            releaseAndSaveTechnician(technician, oldPriority.getPoints());
            return assignTaskToMaster(prepareForUnassignment(taskToSave));
        }

        int newTotalPoints = assignmentStrategy.calculateRecalculatedPoints(technician, oldPriority.getPoints(), newPriority.getPoints());

        if (assignmentStrategy.isOverloaded(technician, newTotalPoints)) {
            log.warn("Technician ID: {} would be overloaded. Unassigning task ID: {}", technician.getUser().getId(), taskToSave.getId());
            releaseAndSaveTechnician(technician, oldPriority.getPoints());
            return saveAsUnassigned(taskToSave);
        }

        Technician updatedTech = assignmentStrategy.updateTechnicianPoints(technician, newTotalPoints);
        technicianFeignClientPort.updateTechnician(updatedTech);

        return taskPersistencePort.save(taskToSave);
    }

    private void releaseAndSaveTechnician(Technician technician, int points) {
        Technician released = assignmentStrategy.releaseTechnicianLoad(technician, points);
        technicianFeignClientPort.updateTechnician(released);
    }

    private void updateTechnicianOnTaskDeletion(Task task) {
        Technician technician = technicianFeignClientPort.findById(task.getTechnicianId());
        Technician updatedTechnician = assignmentStrategy.releaseTechnicianLoad(technician, task.getPriority().getPoints());
        technicianFeignClientPort.updateTechnician(updatedTechnician);
    }

    private Task handleUrgentCreation(Task task) {
        List<Technician> availableMasters = technicianFeignClientPort.findByCategory(TechnicianCategory.MASTER).stream()
                .filter(m -> m.getStatus() != TechnicianStatus.NOT_AVAILABLE)
                .toList();

        if (availableMasters.isEmpty()) {
            log.warn("No available Master technicians for urgent task '{}'", task.getName());
            return taskPersistencePort.save(task);
        }

        return assignTaskToMaster(task);
    }

    private Task handleStandardCreation(Task task) {
        List<Technician> allTechs = technicianFeignClientPort.findAll();
        return assignmentStrategy.findTechnicianByHierarchy(allTechs, task)
                .map(selected -> {
                    log.info("Technician ID: {} selected for new standard task", selected.getUser().getId());
                    return executeAssignment(task, selected, task.getPriority().getPoints());
                })
                .orElseGet(() -> {
                    log.info("No technician available for task '{}', task remains PENDING", task.getName());
                    return taskPersistencePort.save(task);
                });
    }

    private Task assignTaskToMaster(Task task) {
        List<Technician> masters = technicianFeignClientPort.findByCategory(TechnicianCategory.MASTER).stream()
                .filter(m -> m.getStatus() != TechnicianStatus.NOT_AVAILABLE)
                .toList();

        if (masters.isEmpty()) {
            log.warn("Failed to assign urgent task ID: {} due to lack of available Master technicians", task.getId());
            return taskPersistencePort.save(task);
        }

        List<MasterWithUrgentCount> mastersWithCount = masters.stream()
                .map(m -> new MasterWithUrgentCount(m, taskPersistencePort.countUrgentTasksByTechnicianId(m.getUser().getId())))
                .toList();

        Technician selected = taskDomainService.selectBestMaster(mastersWithCount);
        log.info("Selected Master technician ID: {} for task ID: {}", selected.getUser().getId(), task.getId());
        return executeAssignment(task, selected, 0);
    }

    private Task executeAssignment(Task task, Technician technician, int points) {
        Technician updatedTech = assignmentStrategy.updateTechnicianState(technician, points);
        technicianFeignClientPort.updateTechnician(updatedTech);

        Task assignedTask = task.toBuilder()
                .technicianId(technician.getUser().getId())
                .status(TaskStatus.ASSIGNED)
                .build();

        log.info("Task ID: {} successfully assigned to technician ID: {}", task.getId(), technician.getUser().getId());
        return taskPersistencePort.save(assignedTask);
    }
}