package com._108287.api.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum TaskPriority {
  @JsonProperty("low") LOW,
  @JsonProperty("medium") MEDIUM,
  @JsonProperty("high") HIGH;
}
