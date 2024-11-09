package com._108287.api.dto;

import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class ResponseTaskDTO {
  private Long id;
  private String title;
  private String description;
  private String category;
  private LocalDate creationDate;
  private LocalDate lastUpdated;
  private LocalDate deadline;
  private TaskCompletionStatus completionStatus;
  private TaskPriority priority;

  public static ResponseTaskDTO fromTaskEntity(Task task) {
    return new ResponseTaskDTO(
      task.getId(),
      task.getTitle(),
      task.getDescription(),
      task.getCategory(),
      task.getCreationDate(),
      task.getLastUpdated(),
      task.getDeadline(),
      task.getCompletionStatus(),
      task.getPriority()
    );
  }

}
