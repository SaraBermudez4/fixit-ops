package com.fixit.user.infraestructure.adapters.driving.rest.controller;

import com.fixit.user.application.port.in.ITechnicianServicePort;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.TechnicianUpdatePutRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.request.UserRequest;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician.TechnicianWorkloadResponse;
import com.fixit.user.infraestructure.adapters.driving.rest.mapper.IUserRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/technicians")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User Management", description = "Endpoints for registering and managing users")
@SecurityRequirement(name = "bearerAuth")
public class TechnicianController {

    private final ITechnicianServicePort userServicePort;
    private final IUserRestMapper userRestMapper;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(
            summary = "Create technician",
            description = "Creates a new technician in the system. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Technician created successfully",
                    content = @Content(schema = @Schema(implementation = TechnicianResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "409", description = "Technician already exists", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<TechnicianResponse> createTechnician(
            @RequestBody(
                    description = "Technician data required for registration",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserRequest.class))
            )
            @Valid @org.springframework.web.bind.annotation.RequestBody UserRequest request) {

        log.info("[CONTROLLER][CREATE] Incoming request to create technician with email: {}", request.email());

        Technician technicianDomain = userRestMapper.toTechnician(request);
        log.debug("[CONTROLLER][CREATE] Request mapped successfully to Technician domain for email: {}", request.email());

        Technician savedTechnician = userServicePort.createTechnician(technicianDomain);
        log.info("[CONTROLLER][CREATE-SUCCESS] Technician created successfully with ID: {}", savedTechnician.getUser().getId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userRestMapper.toTechnicianResponse(savedTechnician));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get all technicians",
            description = "Retrieves the complete list of technicians registered in the system. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technicians retrieved successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<List<TechnicianResponse>> getAllTechnicians() {
        log.info("[CONTROLLER][QUERY] Incoming request to fetch all technicians");

        List<TechnicianResponse> response = userServicePort.getAll()
                .stream()
                .map(userRestMapper::toTechnicianResponse)
                .toList();

        log.info("[CONTROLLER][QUERY-SUCCESS] Returning {} technicians", response.size());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get technician by ID",
            description = "Retrieves the information of a technician by its unique identifier. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technician retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TechnicianResponse.class))),
            @ApiResponse(responseCode = "404", description = "Technician not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<TechnicianResponse> getTechnicianById(
            @Parameter(description = "Unique identifier of the technician", example = "1", required = true)
            @PathVariable Long id) {

        log.info("[CONTROLLER][QUERY] Incoming request to fetch technician by ID: {}", id);

        Technician technician = userServicePort.getTechnicianById(id);
        log.info("[CONTROLLER][QUERY-SUCCESS] Technician found for ID: {}", id);

        return ResponseEntity.ok(userRestMapper.toTechnicianResponse(technician));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Get technicians by category",
            description = "Retrieves all technicians that belong to a specific category. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technicians retrieved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid technician category", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<List<TechnicianResponse>> getTechniciansByCategory(
            @Parameter(description = "Technician category used as filter", example = "MASTER", required = true)
            @PathVariable TechnicianCategory category) {

        log.info("[CONTROLLER][QUERY] Incoming request to fetch technicians by category: {}", category);

        List<TechnicianResponse> response = userServicePort.getTechniciansByCategory(category)
                .stream()
                .map(userRestMapper::toTechnicianResponse)
                .toList();

        log.info("[CONTROLLER][QUERY-SUCCESS] Returning {} technicians for category {}", response.size(), category);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
    @Operation(
            summary = "Update technician operational data",
            description = "Updates the operational information of a technician such as category, status, task count or current points. Accessible by ADMIN and TECHNICIAN roles."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technician updated successfully",
                    content = @Content(schema = @Schema(implementation = TechnicianResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Technician not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<TechnicianResponse> updateTechnician(
            @Parameter(description = "Unique identifier of the technician", example = "1", required = true)
            @PathVariable Long id,
            @RequestBody(
                    description = "Technician operational data to update",
                    required = true,
                    content = @Content(schema = @Schema(implementation = TechnicianUpdatePutRequest.class))
            )
            @org.springframework.web.bind.annotation.RequestBody TechnicianUpdatePutRequest request) {

        log.info("[CONTROLLER][UPDATE] Incoming request to update technician with ID: {}", id);

        TechnicianUpdate technician = userRestMapper.toTechnicianUpdate(request);
        log.debug("[CONTROLLER][UPDATE] Request mapped successfully to TechnicianUpdate for technician ID: {}", id);

        Technician updated = userServicePort.updateTechnician(id, technician);
        log.info("[CONTROLLER][UPDATE-SUCCESS] Technician updated successfully with ID: {}", id);

        return ResponseEntity.ok(userRestMapper.toTechnicianResponse(updated));
    }

    @GetMapping("/{id}/workload")
    @PreAuthorize("hasRole('ADMIN') or hasRole('TECHNICIAN')")
    @Operation(
            summary = "Get technician workload",
            description = "Retrieves workload and capacity information for a technician. Accessible by ADMIN and TECHNICIAN roles."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Workload retrieved successfully",
                    content = @Content(schema = @Schema(implementation = TechnicianWorkloadResponse.class))),
            @ApiResponse(responseCode = "404", description = "Technician not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<TechnicianWorkloadResponse> getTechnicianWorkload(
            @Parameter(description = "Unique identifier of the technician", example = "1", required = true)
            @PathVariable Long id) {

        log.info("[CONTROLLER][WORKLOAD] Incoming request to fetch workload for technician ID: {}", id);

        TechnicianWorkload workload = userServicePort.getTechnicianWorkload(id);
        log.info("[CONTROLLER][WORKLOAD-SUCCESS] Workload retrieved successfully for technician ID: {}", id);

        return ResponseEntity.ok(userRestMapper.toWorkloadResponse(workload));
    }

    @PutMapping("/{id}/category")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Change technician category",
            description = "Updates the category of an existing technician. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Technician category updated successfully",
                    content = @Content(schema = @Schema(implementation = TechnicianResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category or invalid state transition", content = @Content),
            @ApiResponse(responseCode = "404", description = "Technician not found", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "409", description = "Technician cannot change to the requested category", content = @Content)
    })
    public ResponseEntity<TechnicianResponse> updateTechnicianCategory(
            @Parameter(description = "Unique identifier of the technician", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "New category assigned to the technician", example = "MASTER", required = true)
            @Valid @RequestParam TechnicianCategory newTechnicianCategory) {

        log.info("[CONTROLLER][CATEGORY-CHANGE] Incoming request to change category to {} for technician ID: {}",
                newTechnicianCategory, id);

        Technician updated = userServicePort.updateTechnicianCategory(id, newTechnicianCategory);
        log.info("[CONTROLLER][CATEGORY-SUCCESS] Category updated successfully for technician ID: {}", id);

        return ResponseEntity.ok(
                userRestMapper.toTechnicianResponse(updated)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "Delete technician",
            description = "Deletes a technician from the system if business rules allow it. Only accessible by users with ADMIN role."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Technician deleted successfully", content = @Content),
            @ApiResponse(responseCode = "404", description = "Technician not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Technician cannot be deleted due to current state", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content)
    })
    public ResponseEntity<Void> deleteTechnician(
            @Parameter(description = "Unique identifier of the technician", example = "1", required = true)
            @PathVariable Long id) {

        log.warn("[CONTROLLER][DELETE] Incoming request to delete technician ID: {}", id);

        userServicePort.delete(id);
        log.info("[CONTROLLER][DELETE-SUCCESS] Technician deleted successfully with ID: {}", id);

        return ResponseEntity.noContent().build();
    }
}