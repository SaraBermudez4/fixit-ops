package com.fixit.tasks.infraestructure.adapters.driving.rest.controller;

import com.fixit.tasks.application.port.in.ITaskServicePort;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.request.TaskRequest;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.AutoAssignResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.DeleteResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.TaskResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.mapper.ITaskRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
@RequiredArgsConstructor
@Tag(name = "Task Management", description = "Endpoints for creating and assigning maintenance tasks")
public class TaskController {

        private final ITaskServicePort taskServicePort;
        private final ITaskRestMapper taskRestMapper;

        @PostMapping
        @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
        @Operation(summary = "Create a new task with automatic assignment", description = "Creates a new task and automatically assigns it to an available technician. The system selects the best technician based on skill and availability.")
        public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(taskRestMapper.toResponse(
                                taskServicePort.createTask(taskRestMapper.toDomain(request))));
        }

        @GetMapping("/technician/{technicianId}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
        @Operation(summary = "List tasks assigned to a technician", description = "Gets the list of tasks assigned to a specific technician by their ID")
        public ResponseEntity<List<TaskResponse>> getTasksByTechnicianId(
                @Parameter(description = "Technician ID", required = true) @PathVariable Long technicianId) {
                return ResponseEntity.ok(
                        taskRestMapper.toResponseList(taskServicePort.getTasksByTechnicianId(technicianId)));
        }



        @GetMapping
        @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
        public ResponseEntity<List<TaskResponse>> getAllTasks() {
                return ResponseEntity.ok(
                        taskRestMapper.toResponseList(taskServicePort.getAll()));
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
        public ResponseEntity<TaskResponse> getTaskById(
                @Parameter(description = "Task ID", required = true) @PathVariable Long id) {
                return ResponseEntity.ok(
                        taskRestMapper.toResponse(taskServicePort.getById(id)));
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<DeleteResponse> deleteTask(
                @Parameter(description = "Task ID", required = true) @PathVariable Long id) {
                taskServicePort.delete(id);
                return ResponseEntity.ok(DeleteResponse.createDeleteResponse(id));
        }

        @PostMapping("/{id}/assign-urgent")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<TaskResponse> assignUrgentTask(
                @Parameter(description = "Task ID", required = true) @PathVariable Long id) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(taskRestMapper.toResponse(taskServicePort.assignUrgentTask(id)));
        }

        @PostMapping("/auto-assign/urgent")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<AutoAssignResponse> autoAssignUrgentTasks() {
                return ResponseEntity.ok(
                        taskRestMapper.toAutoAssignResponse(taskServicePort.autoAssignAllUrgentTasks())
                );
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
        public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id,
                                                       @Valid @RequestBody TaskRequest request) {
                return ResponseEntity.ok(
                        taskRestMapper.toResponse(
                                taskServicePort.updateTask(id, taskRestMapper.toDomain(request))
                        )
                );
        }

        @PostMapping("/process-waiting")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<String> processWaitingTasks() {
                taskServicePort.processWaitingTasks();
                return ResponseEntity.ok("Waiting tasks processed successfully");
        }

        @PatchMapping("/{id}/start")
        @PreAuthorize("hasRole('TECHNICIAN')")
        public ResponseEntity<String> startTask(@PathVariable Long id) {
                taskServicePort.startTask(id);
                return ResponseEntity.ok("Task started successfully");
        }

        @PatchMapping("/{id}/complete")
        @PreAuthorize("hasRole('TECHNICIAN')")
        public ResponseEntity<String> completeTask(@PathVariable Long id) {
                taskServicePort.completeTask(id);
                return ResponseEntity.ok("Task completed successfully");
        }


}