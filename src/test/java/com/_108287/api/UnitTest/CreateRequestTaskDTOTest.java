package com._108287.api.UnitTest;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.entities.Task;
import com._108287.api.entities.TaskPriority;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class CreateRequestTaskDTOTest {

  @Test
  void testToTaskEntity() {
    CreateRequestTaskDTO createRequestTaskDTO = new CreateRequestTaskDTO("title", "description", "category", LocalDate.now(), TaskPriority.HIGH);

    Task task = createRequestTaskDTO.toTaskEntity("sub");

    assertThat(task)
      .extracting(
        Task::getSub,
        Task::getTitle,
        Task::getDescription,
        Task::getDeadline,
        Task::getPriority
      )
      .containsExactly("sub", "title", "description", LocalDate.now(), TaskPriority.HIGH);
  }

}
