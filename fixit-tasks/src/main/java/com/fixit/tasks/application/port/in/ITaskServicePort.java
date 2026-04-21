package com.fixit.tasks.application.port.in;

import com.fixit.tasks.domain.model.AutoAssignSummary;
import com.fixit.tasks.domain.model.Task;

import java.util.List;


public interface ITaskServicePort{

    Task createTask(Task task);


}