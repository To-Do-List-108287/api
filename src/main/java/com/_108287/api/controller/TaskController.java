package com._108287.api.controller;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.dto.UpdateRequestTaskDTO;
import com._108287.api.entities.MyUserDetails;
import com._108287.api.service.TaskService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/tasks")
@AllArgsConstructor
public class TaskController {
  private TaskService taskService;

  @PostMapping
  public ResponseEntity<ResponseTaskDTO> createTask(
    @AuthenticationPrincipal MyUserDetails userDetails,
    @Valid @RequestBody CreateRequestTaskDTO createRequestTaskDTO
  ) {
    // getUsername() is same as getSub() in MyUserDetails
    return  taskService.createTask(createRequestTaskDTO, userDetails.getUsername())
      .map(responseTaskDTO -> ResponseEntity.status(HttpStatus.CREATED).body(responseTaskDTO))
      .orElse(ResponseEntity.badRequest().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(
    @AuthenticationPrincipal MyUserDetails userDetails,
    @PathVariable Long id
  ) {
    return taskService.deleteTask(id, userDetails.getUsername())
      .<ResponseEntity<Void>>map(longId -> ResponseEntity.noContent().build())
      .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseTaskDTO> updateTask(
    @AuthenticationPrincipal MyUserDetails userDetails,
    @PathVariable Long id,
    @Valid @RequestBody UpdateRequestTaskDTO updateRequestTaskDTO
  ) {

    return taskService.updateTask(id, updateRequestTaskDTO, userDetails.getUsername())
      .map(ResponseEntity::ok)
      .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public ResponseEntity<List<ResponseTaskDTO>> getTasks(
    @AuthenticationPrincipal MyUserDetails userDetails,
    Sort sort,
    @RequestParam(required = false) String category
  ) {
    if (sort.isUnsorted()) {
      // sort not passed in api call (sort object is never null)
      sort = Sort.by(
        Sort.Order.desc("creationDate"),
        Sort.Order.desc("lastUpdated")
      );
    } else if (!taskService.taskSortFieldsExist(sort)) {
      return ResponseEntity.badRequest().build();
    }
    return ResponseEntity.ok(
      taskService.getTasksBySubSortedAndFiltered(
        userDetails.getUsername(),
        sort,
        category
      )
    );
  }

  @GetMapping("/categories")
  public ResponseEntity<List<String>> getCategories(
    @AuthenticationPrincipal MyUserDetails userDetails
  ) {
    return ResponseEntity.ok(taskService.getCategoriesBySub(userDetails.getUsername()));
  }


}
