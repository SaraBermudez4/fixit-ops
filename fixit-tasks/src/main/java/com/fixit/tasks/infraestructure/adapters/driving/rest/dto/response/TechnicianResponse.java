package com.fixit.tasks.infraestructure.adapters.driving.rest.dto.response;

import com.fixit.tasks.domain.enums.Role;
import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.enums.TechnicianStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TechnicianResponse {
    private Long id;
    private String dni;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Role role;

    private TechnicianCategory category;
    private TechnicianStatus status;
    private Integer taskCount;
    private Integer currentPoints;
    private Integer availablePoints;
}