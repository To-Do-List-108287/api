package com._108287.api.UnitTest;

import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.assertThat;

class ResponseTaskDTOTest {

  @Test
  void testFromTaskEntity() {
    Task task = new Task(1L, "sub", "title", "description", "category", LocalDate.now(), LocalDate.now(), LocalDate.now(), TaskCompletionStatus.TO_DO, TaskPriority.HIGH);

    ResponseTaskDTO responseTaskDTO = ResponseTaskDTO.fromTaskEntity(task);

    assertThat(responseTaskDTO)
      .extracting(
        ResponseTaskDTO::getId,
        ResponseTaskDTO::getTitle,
        ResponseTaskDTO::getDescription,
        ResponseTaskDTO::getCreationDate,
        ResponseTaskDTO::getLastUpdated,
        ResponseTaskDTO::getDeadline,
        ResponseTaskDTO::getCompletionStatus,
        ResponseTaskDTO::getPriority
      )
      .containsExactly(1L, "title", "description", LocalDate.now(), LocalDate.now(), LocalDate.now(),
        TaskCompletionStatus.TO_DO, TaskPriority.HIGH);
  }
}
