package com.fixit.tasks.domain.service;

import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.enums.TechnicianStatus;
import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.domain.model.Technician;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
public class AssignmentStrategy {

    public Optional<Technician> findTechnicianByHierarchy(List<Technician> technicians, Task task) {
        log.info("Searching for technician by hierarchy for task: '{}' (Priority Points: {})", task.getName(), task.getPriority().getPoints());

        List<TechnicianCategory> hierarchy = List.of(
                TechnicianCategory.JUNIOR,
                TechnicianCategory.SEMI_SENIOR,
                TechnicianCategory.SENIOR);

        for (TechnicianCategory category : hierarchy) {
            Optional<Technician> selected = findInCategory(technicians, category, task.getPriority().getPoints());
            if (selected.isPresent()) {
                log.info("Suitable technician found in category: {} (Technician ID: {})", category, selected.get().getUser().getId());
                return selected;
            }
        }

        log.warn("No suitable technician found across JUNIOR, SEMI_SENIOR, or SENIOR categories for task: {}", task.getName());
        return Optional.empty();
    }

    private Optional<Technician> findInCategory(List<Technician> techs, TechnicianCategory category, int taskPoints) {
        return techs.stream()
                .filter(t -> t.getCategory() == category)
                .filter(t -> t.getStatus() != TechnicianStatus.NOT_AVAILABLE)
                .filter(t -> (t.getCurrentPoints() + taskPoints) <= t.getCategory().getMaxPoints())
                .sorted(Comparator.comparing(Technician::getStatus).reversed()
                        .thenComparing(Technician::getCurrentPoints, Comparator.reverseOrder()))
                .findFirst();
    }

    public Technician updateTechnicianState(Technician technician, int pointsToAdd) {
        log.info("Updating state for technician ID: {}. Adding {} points", technician.getUser().getId(), pointsToAdd);

        int newPoints = technician.getCurrentPoints() + pointsToAdd;
        int newTaskCount = technician.getTaskCount() + 1;

        TechnicianStatus nextStatus = determineNextStatus(technician, newPoints, newTaskCount);
        log.debug("Technician ID: {} new state - Points: {}, Tasks: {}, Status: {}", technician.getUser().getId(), newPoints, newTaskCount, nextStatus);

        return buildUpdatedTechnician(technician, newPoints, newTaskCount, nextStatus);
    }

    private TechnicianStatus determineNextStatus(Technician technician, int points, int taskCount) {
        if (technician.getCategory() == TechnicianCategory.MASTER) {
            return calculateMasterStatus(taskCount);
        }
        return calculateStandardStatus(technician.getCategory(), points);
    }

    private TechnicianStatus calculateMasterStatus(int taskCount) {
        return (taskCount >= 3) ? TechnicianStatus.NOT_AVAILABLE : TechnicianStatus.BUSY;
    }

    private TechnicianStatus calculateStandardStatus(TechnicianCategory category, int points) {
        return (points >= category.getMaxPoints()) ? TechnicianStatus.NOT_AVAILABLE : TechnicianStatus.BUSY;
    }

    private Technician buildUpdatedTechnician(Technician technician, int points, int tasks, TechnicianStatus status) {
        return technician.toBuilder()
                .currentPoints(points)
                .taskCount(tasks)
                .status(status)
                .build();
    }

    public int calculateRecalculatedPoints(Technician technician, int oldPoints, int newPoints) {
        int result = technician.getCurrentPoints() - oldPoints + newPoints;
        log.debug("Recalculating points for tech ID: {}. Old: {}, New: {}, Result: {}", technician.getUser().getId(), oldPoints, newPoints, result);
        return result;
    }

    public boolean isOverloaded(Technician technician, int recalculatedPoints) {
        boolean overloaded = recalculatedPoints > technician.getCategory().getMaxPoints();
        if (overloaded) {
            log.warn("Overload detected for technician ID: {} (Points: {}, Max: {})", technician.getUser().getId(), recalculatedPoints, technician.getCategory().getMaxPoints());
        }
        return overloaded;
    }

    public Technician updateTechnicianPoints(Technician technician, int newPoints) {
        log.info("Manually updating points for technician ID: {} to {}", technician.getUser().getId(), newPoints);

        TechnicianStatus nextStatus = (newPoints >= technician.getCategory().getMaxPoints())
                ? TechnicianStatus.NOT_AVAILABLE
                : TechnicianStatus.BUSY;

        return technician.toBuilder()
                .currentPoints(newPoints)
                .status(nextStatus)
                .build();
    }

    public Technician releaseTechnicianLoad(Technician technician, int pointsToSubtract) {
        log.info("Releasing load for technician ID: {}. Subtracting {} points", technician.getUser().getId(), pointsToSubtract);

        int actualPointsToSubtract = (technician.getCategory() == TechnicianCategory.MASTER) ? 0 : pointsToSubtract;

        int newPoints = Math.max(0, technician.getCurrentPoints() - actualPointsToSubtract);
        int newTaskCount = Math.max(0, technician.getTaskCount() - 1);

        TechnicianStatus nextStatus = determineStatusAfterRelease(technician, newPoints, newTaskCount);
        log.info("Technician ID: {} released. New Status: {}", technician.getUser().getId(), nextStatus);

        return technician.toBuilder()
                .currentPoints(newPoints)
                .taskCount(newTaskCount)
                .status(nextStatus)
                .build();
    }

    private TechnicianStatus determineStatusAfterRelease(Technician technician, int points, int taskCount) {
        if (taskCount == 0) {
            return TechnicianStatus.AVAILABLE;
        }

        if (technician.getCategory() == TechnicianCategory.MASTER) {
            return (taskCount >= 3) ? TechnicianStatus.NOT_AVAILABLE : TechnicianStatus.BUSY;
        }

        return (points >= technician.getCategory().getMaxPoints())
                ? TechnicianStatus.NOT_AVAILABLE
                : TechnicianStatus.BUSY;
    }
}