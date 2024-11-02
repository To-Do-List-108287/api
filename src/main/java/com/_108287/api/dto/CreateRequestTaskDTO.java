package com._108287.api.dto;

import com._108287.api.entities.Task;
import com._108287.api.entities.TaskPriority;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class CreateRequestTaskDTO {
  @Valid @NotNull private String title;
  @Valid @NotNull private String description;
  @Valid @NotNull private LocalDate deadline;
  @Valid @NotNull private TaskPriority priority;

  public Task toTaskEntity(String sub) {
    return new Task(
      sub,
      this.title,
      this.description,
      this.deadline,
      this.priority
    );
  }
}
