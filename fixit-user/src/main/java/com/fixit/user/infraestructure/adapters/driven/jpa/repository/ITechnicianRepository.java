package com.fixit.user.infraestructure.adapters.driven.jpa.repository;

import com.fixit.user.domain.enums.TechnicianCategory;
import com.fixit.user.infraestructure.adapters.driven.jpa.entity.TechnicianEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITechnicianRepository extends JpaRepository<TechnicianEntity, Long> {
    List<TechnicianEntity> findByCategory(TechnicianCategory category);

}