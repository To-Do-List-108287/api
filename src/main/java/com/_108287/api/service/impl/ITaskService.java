package com._108287.api.service.impl;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.entities.Task;
import com._108287.api.repository.TaskRepository;
import com._108287.api.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ITaskService implements TaskService {
  private TaskRepository taskRepository;

  @Override
  public Optional<ResponseTaskDTO> createTask(CreateRequestTaskDTO createRequestTaskDTO, String sub) {
    return Optional.of(ResponseTaskDTO.fromTaskEntity(
      taskRepository.save(createRequestTaskDTO.toTaskEntity(sub))
    ));
  }

  @Override
  public Optional<Task> getTaskById(Long id) {
    return taskRepository.findById(id);
  }

}
