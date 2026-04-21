package com.fixit.tasks.infraestructure.adapters.driving.rest.controller;

import com.fixit.tasks.application.port.in.ITaskServicePort;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.request.TaskRequest;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.AutoAssignResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.DeleteResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response.TaskResponse;
import com.fixit.tasks.infraestructure.adapters.driving.rest.mapper.ITaskRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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



}