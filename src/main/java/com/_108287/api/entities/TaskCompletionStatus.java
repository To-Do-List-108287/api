package com._108287.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskCompletionStatus {
  @JsonProperty("to_do") TO_DO,
  @JsonProperty("in_progress") IN_PROGRESS,
  @JsonProperty("done") DONE;
}
