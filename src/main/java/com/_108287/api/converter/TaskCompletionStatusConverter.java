package com._108287.api.converter;

import com._108287.api.entities.TaskCompletionStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class TaskCompletionStatusConverter implements Converter<String, TaskCompletionStatus> {

  @Override
  public TaskCompletionStatus convert(String source) {
    // Convert the string to the appropriate enum value
    return switch (source.toLowerCase()) {
      case "to_do" -> TaskCompletionStatus.TO_DO;
      case "in_progress" -> TaskCompletionStatus.IN_PROGRESS;
      case "done" -> TaskCompletionStatus.DONE;
      default -> throw new IllegalArgumentException("Unknown status: " + source);
    };
  }

}
