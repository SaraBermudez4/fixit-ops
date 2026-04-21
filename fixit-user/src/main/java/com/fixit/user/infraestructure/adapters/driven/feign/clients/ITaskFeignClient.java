package com.fixit.user.infraestructure.adapters.driven.feign.clients;

import com.fixit.user.infraestructure.adapters.driven.feign.configuration.CustomFeignErrorDecoder;
import com.fixit.user.infraestructure.adapters.driven.feign.dto.response.TaskFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "fixit-task",
        url = "${external.services.fixit-tasks-service.url}/api/v1/tasks",
        configuration = CustomFeignErrorDecoder.class
)
public interface ITaskFeignClient {

    @GetMapping("/technician/{technicianId}")
    List<TaskFeignResponse> findByTechnicianId(@PathVariable("technicianId") Long technicianId);

}