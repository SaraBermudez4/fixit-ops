package com.fixit.user.application.port.out;

import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.domain.model.Technician;
import com.fixit.user.domain.model.User;

import java.util.List;

public interface ITechnicianPersistencePort {
    Technician saveTechnician(Technician technician);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);

    User findByEmail(String email);

    List<Technician> findAllTechnicians();

    List<Technician> findTechniciansByCategory(TechnicianCategory category);
    Technician findById(Long id);
    Technician updateTechnician (Technician technician);
    void deleteById(Long id);
}