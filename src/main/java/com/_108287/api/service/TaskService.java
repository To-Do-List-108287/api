package com._108287.api.service;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.dto.UpdateRequestTaskDTO;

import java.util.Optional;

public interface TaskService {

  Optional<ResponseTaskDTO> createTask(CreateRequestTaskDTO createRequestTaskDTO, String sub);
  Optional<Long> deleteTask(Long id, String sub);

  Optional<ResponseTaskDTO> updateTask(Long id, UpdateRequestTaskDTO updateRequestTaskDTO, String sub);

}
