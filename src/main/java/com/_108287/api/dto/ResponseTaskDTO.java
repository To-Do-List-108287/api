package com._108287.api.dto;

import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ResponseTaskDTO {
  @Valid @NotNull private Long id;
  @Valid @NotNull private String title;
  @Valid @NotNull private String description;
  @Valid @NotNull private LocalDateTime creationDate;
  @Valid @NotNull private LocalDateTime lastUpdated;
  @Valid @NotNull private LocalDateTime deadline;
  @Valid @NotNull private TaskCompletionStatus completionStatus;
  @Valid @NotNull private TaskPriority priority;

  public static ResponseTaskDTO fromTaskEntity(Task task) {
    return new ResponseTaskDTO(
      task.getId(),
      task.getTitle(),
      task.getDescription(),
      task.getCreationDate(),
      task.getLastUpdated(),
      task.getDeadline(),
      task.getCompletionStatus(),
      task.getPriority()
    );
  }

}
