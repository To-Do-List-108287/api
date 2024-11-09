package com._108287.api.dto;

import com._108287.api.entities.Task;
import com._108287.api.entities.TaskPriority;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@AllArgsConstructor
@Getter
public class CreateRequestTaskDTO {
  @NotNull private String title;
  @NotNull private String description;
  @NotNull private String category;
  @NotNull private LocalDate deadline;
  @NotNull private TaskPriority priority;

  public Task toTaskEntity(String sub) {
    return new Task(
      sub,
      this.title,
      this.description,
      this.category,
      this.deadline,
      this.priority
    );
  }
}
