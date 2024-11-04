package com._108287.api.service;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import java.util.Optional;

public interface TaskService {

  Optional<ResponseTaskDTO> createTask(CreateRequestTaskDTO createRequestTaskDTO, String sub);
  Optional<Long> deleteTask(Long id, String sub);

}
