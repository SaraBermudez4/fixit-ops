package com.fixit.user.infraestructure.adapters.driven.feign;

import com.fixit.user.application.port.out.ITaskFeignClientPort;
import com.fixit.user.domain.model.Task;
import com.fixit.user.infraestructure.adapters.driven.feign.clients.ITaskFeignClient;
import com.fixit.user.infraestructure.adapters.driven.feign.mapper.ITaskFeignMapper;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TaskFeignAdapter implements ITaskFeignClientPort {

    private final ITaskFeignClient technicianFeignClient;
    private final ITaskFeignMapper feignMapper;

    @Override
    public List<Task> findByTechnicianId(Long technicianId) {

        return  feignMapper.toDomainList(technicianFeignClient.findByTechnicianId(technicianId));
    }
}