package com.fixit.tasks.domain.service;

import com.fixit.tasks.domain.model.Task;
import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.domain.model.User;

public class TaskMessageBuilder {

    public static String buildAssignedMessage(Task task, Technician technician) {
        User user = technician.getUser();
        return String.format(
                "Hola %s %s, se le informa que la tarea \"%s\" (ID: %d) le ha sido asignada exitosamente.%n" +
                        "Prioridad: %s.%n" +
                        "Tareas actuales a su cargo: %d | Puntos actuales: %d / %d.",
                user.getName(),
                user.getLastName(),
                task.getName(),
                task.getId(),
                task.getPriority().name(),
                technician.getTaskCount(),
                technician.getCurrentPoints(),
                technician.getCategory().getMaxPoints()
        );
    }

    public static String buildCompletedMessage(Task task, Technician technician) {
        User user = technician.getUser();
        return String.format(
                "Hola %s %s, la tarea \"%s\" (ID: %d) ha sido completada exitosamente.%n" +
                        "Tareas restantes a su cargo: %d | Puntos actuales: %d / %d.",
                user.getName(),
                user.getLastName(),
                task.getName(),
                task.getId(),
                technician.getTaskCount(),
                technician.getCurrentPoints(),
                technician.getCategory().getMaxPoints()
        );
    }
}