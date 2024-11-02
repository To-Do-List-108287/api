package com._108287.api.controller;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.entities.MyUserDetails;
import com._108287.api.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<ResponseTaskDTO> createTask(
    @AuthenticationPrincipal MyUserDetails userDetails,
    @RequestBody CreateRequestTaskDTO createRequestTaskDTO
  ) {
    // getUsername() is same as getSub() in MyUserDetails
    return taskService
      .createTask(createRequestTaskDTO, userDetails.getUsername())
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.badRequest().build());
  }

}
