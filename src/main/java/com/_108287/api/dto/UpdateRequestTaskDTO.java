package com._108287.api.dto;

import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UpdateRequestTaskDTO {
  private String title;
  private String description;
  private String category;
  private LocalDate deadline;
  private TaskCompletionStatus completionStatus;
  private TaskPriority priority;
}
