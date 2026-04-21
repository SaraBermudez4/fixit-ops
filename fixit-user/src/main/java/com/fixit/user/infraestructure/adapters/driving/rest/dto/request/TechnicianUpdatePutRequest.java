package com.fixit.user.infraestructure.adapters.driving.rest.dto.request;

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
public class TechnicianUpdatePutRequest {
    private TechnicianCategory category;
    private TechnicianStatus status;
    private Integer taskCount;
    private Integer currentPoints;
}
