package com._108287.api.service.impl;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.dto.UpdateRequestTaskDTO;
import com._108287.api.repository.TaskRepository;
import com._108287.api.service.TaskService;
import com._108287.api.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ITaskService implements TaskService {
  private final TaskRepository taskRepository;
  Logger logger = LoggerFactory.getLogger(ITaskService.class);

  @Autowired
  public ITaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
  }

  @Override
  public Optional<ResponseTaskDTO> createTask(CreateRequestTaskDTO createRequestTaskDTO, String sub) {
    return Optional.of(ResponseTaskDTO.fromTaskEntity(
      taskRepository.save(createRequestTaskDTO.toTaskEntity(sub))
    ));
  }

  @Override
  public Optional<Long> deleteTask(Long id, String sub) {
    return taskRepository.findByIdAndSub(id, sub)
      .map(task -> {
        taskRepository.delete(task);
        return task.getId();
      });
  }

  @Override
  public Optional<ResponseTaskDTO> updateTask(Long id, UpdateRequestTaskDTO updateRequestTaskDTO, String sub){
    String[] nullPropertyNames = Utils.getNullPropertyNames(updateRequestTaskDTO);
    if (nullPropertyNames.length == 5) {
      // no property to update in the request because all properties are null
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No property to update");
    }
    return taskRepository.findByIdAndSub(id, sub).map(task -> {
      // update non null properties
      BeanUtils.copyProperties(updateRequestTaskDTO, task, nullPropertyNames);
      task.setLastUpdated(LocalDate.now());
      return ResponseTaskDTO.fromTaskEntity(taskRepository.save(task));
    });
  }


}
