package com.fixit.user.application.port.in;

import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.TechnicianUpdate;
import com.fixit.user.domain.model.TechnicianWorkload;

import java.util.List;

public interface ITechnicianServicePort {
    Technician createTechnician(Technician technicianInfo);
    List<Technician> getAll();
    Technician getTechnicianById(Long id);
    List<Technician> getTechniciansByCategory (TechnicianCategory category);
    Technician updateTechnician(Long id, TechnicianUpdate technician);
    TechnicianWorkload getTechnicianWorkload(Long id);
    Technician updateTechnicianCategory (Long id, TechnicianCategory newCategory);
    void delete(Long id);
}
