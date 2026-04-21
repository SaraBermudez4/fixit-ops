package com.fixit.tasks.application.port.out;

import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.model.Technician;

import java.util.List;

public interface ITechnicianFeignClientPort {
    List<Technician> findByCategory(TechnicianCategory category);
    List<Technician> findAll();
    Technician updateTechnician(Technician technician);
}
