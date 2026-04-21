package com.fixit.user.infraestructure.adapters.driving.rest.dto.response.technician;

import com.fixit.user.domain.enums.Role;
import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.enums.TechnicianStatus;
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