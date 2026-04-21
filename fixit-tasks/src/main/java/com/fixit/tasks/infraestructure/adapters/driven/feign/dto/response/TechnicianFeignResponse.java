package com.fixit.tasks.infraestructure.adapters.driven.feign.dto.response;


import lombok.Data;

@Data
public class TechnicianFeignResponse {
    private Long id;
    private String dni;
    private String name;
    private String lastName;
    private String email;
    private String category;
    private String status;
    private Integer taskCount;
    private Integer currentPoints;
}