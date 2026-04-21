package com.fixit.tasks.infraestructure.adapters.driven.feign;

import com.fixit.tasks.application.port.out.ITechnicianFeignClientPort;
import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.domain.model.Technician;
import com.fixit.tasks.infraestructure.adapters.driven.feign.clients.ITechnicianFeignClient;
import com.fixit.tasks.infraestructure.adapters.driven.feign.mapper.ITechnicianFeignMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TechnicianFeignAdapter implements ITechnicianFeignClientPort {

    private final ITechnicianFeignClient technicianFeignClient;
    private final ITechnicianFeignMapper feignMapper;

    @Override
    public List<Technician> findByCategory(TechnicianCategory category) {
        return technicianFeignClient.getTechniciansByCategory(category).stream()
                .map(feignMapper::toDomain)
                .toList();
    }

    @Override
    public List<Technician> findAll() {
        return technicianFeignClient.getAllTechnicians().stream()
                .map(feignMapper::toDomain)
                .toList();
    }

    @Override
    public Technician updateTechnician(Technician technician) {
        return feignMapper.toDomain(
                technicianFeignClient.updateTechnician(
                        technician.getUser().getId(),
                        feignMapper.toFeignRequest(technician)
                )
        );
    }
}