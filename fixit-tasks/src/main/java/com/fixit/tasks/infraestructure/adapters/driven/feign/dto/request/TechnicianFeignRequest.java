package com.fixit.tasks.infraestructure.adapters.driven.feign.dto.request;


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
public class TechnicianFeignRequest {

    private TechnicianCategory category;
    private TechnicianStatus status;
    private Integer taskCount;
    private Integer currentPoints;

}