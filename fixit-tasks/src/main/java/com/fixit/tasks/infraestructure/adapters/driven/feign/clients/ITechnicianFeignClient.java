package com.fixit.tasks.infraestructure.adapters.driven.feign.clients;

import com.fixit.tasks.domain.enums.TechnicianCategory;
import com.fixit.tasks.infraestructure.adapters.driven.feign.configuration.CustomFeignErrorDecoder;
import com.fixit.tasks.infraestructure.adapters.driven.feign.dto.request.TechnicianFeignRequest;
import com.fixit.tasks.infraestructure.adapters.driven.feign.dto.response.TechnicianFeignResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
        name = "fixit-user",
        url = "${external.services.fixit-user-service.url}/api/v1/technicians",
        configuration = CustomFeignErrorDecoder.class
)
public interface ITechnicianFeignClient {

    @GetMapping
    List<TechnicianFeignResponse> getAllTechnicians();

    @GetMapping("/category/{category}")
    List<TechnicianFeignResponse> getTechniciansByCategory(@PathVariable("category") TechnicianCategory category);

    @PutMapping("/{id}")
    TechnicianFeignResponse updateTechnician(
            @PathVariable("id") Long id,
            @RequestBody TechnicianFeignRequest technicianRequest
    );

    @GetMapping("/{id}")
    TechnicianFeignResponse getTechnicianById(@PathVariable Long id);
}