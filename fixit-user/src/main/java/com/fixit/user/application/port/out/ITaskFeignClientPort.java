package com.fixit.user.application.port.out;

import com.fixit.user.domain.model.Task;

import java.util.List;

public interface ITaskFeignClientPort {

    List<Task> findByTechnicianId (Long technicianId);
}
