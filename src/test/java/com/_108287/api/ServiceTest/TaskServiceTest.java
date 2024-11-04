package com._108287.api.ServiceTest;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import com._108287.api.repository.TaskRepository;
import com._108287.api.service.TaskService;
import com._108287.api.service.impl.ITaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

  @Mock
  TaskRepository taskRepository;

  @InjectMocks
  ITaskService taskService;

  CreateRequestTaskDTO createRequestTaskDTO = new CreateRequestTaskDTO(
    "title", "description", LocalDate.of(2024,12,12), TaskPriority.HIGH
  );
  Task task = new Task(1L, "sub", "title", "description", LocalDate.of(2024,12,12),
    TaskPriority.HIGH);


  @Test
  void whenCreateTaskSuccess_ReturnNonEmptyOptionalWithDefaultValues(){
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Optional<ResponseTaskDTO> createdTaskDTO = taskService.createTask(createRequestTaskDTO, "sub");

    assertThat(createdTaskDTO)
      .isNotEmpty()
      .get()
      .isInstanceOf(ResponseTaskDTO.class)
      .extracting(ResponseTaskDTO::getId, ResponseTaskDTO::getTitle, ResponseTaskDTO::getDescription,
        ResponseTaskDTO::getCreationDate, ResponseTaskDTO::getLastUpdated, ResponseTaskDTO::getDeadline,
        ResponseTaskDTO::getCompletionStatus, ResponseTaskDTO::getPriority)
      .containsExactly(1L, "title", "description", LocalDate.now(),
        LocalDate.now(), LocalDate.of(2024,12,12),
        TaskCompletionStatus.TO_DO, TaskPriority.HIGH);
  }

  @Test
  void whenDeleteTaskSuccess_ReturnNonEmptyOptionalWithId(){
    when(taskRepository.findByIdAndSub(1L, "sub")).thenReturn(Optional.of(task));

    Optional<Long> deletedTaskId = taskService.deleteTask(1L, "sub");

    assertThat(deletedTaskId)
      .isNotEmpty()
      .hasValue(1L);

    verify(taskRepository, times(1)).findByIdAndSub(1L, "sub");
    verify(taskRepository, times(1)).delete(task);
  }

  @Test
  void whenDeleteTaskFail_ReturnEmptyOptional(){
    when(taskRepository.findByIdAndSub(1L, "sub")).thenReturn(Optional.empty());

    Optional<Long> deletedTaskId = taskService.deleteTask(1L, "sub");

    assertThat(deletedTaskId)
      .isEmpty();

    verify(taskRepository, times(1)).findByIdAndSub(1L, "sub");
    verify(taskRepository, never()).delete(task);
  }

}
