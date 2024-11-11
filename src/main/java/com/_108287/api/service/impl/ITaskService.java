package com._108287.api.service.impl;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.dto.UpdateRequestTaskDTO;
import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.repository.TaskRepository;
import com._108287.api.service.TaskService;
import com._108287.api.specifications.TaskSpecification;
import com._108287.api.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.Specification.where;

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
    if (nullPropertyNames.length == updateRequestTaskDTO.getClass().getDeclaredFields().length) {
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

  @Override
  public boolean taskSortFieldsExist(Sort sort) {
    Set<String> entityFieldNames = Arrays.stream(Task.class.getDeclaredFields())
      .map(Field::getName)
      .collect(Collectors.toSet());

    for (Sort.Order order : sort) {
      if (!entityFieldNames.contains(order.getProperty())) {
        return false;
      }
    }

    return true;
  }

  @Override
  public List<ResponseTaskDTO> getTasksBySubSortedAndFiltered(
    String sub,
    Sort sort,
    String category,
    TaskCompletionStatus completionStatus
  ) {
    return taskRepository.findAll(
      where(TaskSpecification.hasSub(sub)
          .and(TaskSpecification.hasCategory(category))
          .and(TaskSpecification.hasCompletionStatus(completionStatus))),
      sort
      ).stream()
      .map(ResponseTaskDTO::fromTaskEntity)
      .toList();
  }

  @Override
  public List<String> getCategoriesBySub(String sub) {
    return taskRepository.findDistinctCategoriesBySub(sub);
  }


}
