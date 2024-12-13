package com._108287.api.controller;

import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.dto.UpdateRequestTaskDTO;
import com._108287.api.entities.MyUserDetails;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Create a new task.", description = "This endpoint allows the user to create a new task. The authenticated user is associated with the task.")
  @ApiResponse(responseCode = "201", description = "Task successfully created and return respective created record.", content = {
    @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTaskDTO.class))})
  @ApiResponse(responseCode = "400", description = "Bad Request due to invalid task details.", content = {@Content(mediaType = "application/json")})
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
  @Operation(summary = "Delete a user's existing task.", description = "This endpoint allows the user to delete one of his tasks. The authenticated user has to be associated with the task is trying to delete.")
  @ApiResponse(responseCode = "204", description = "Successfully deleted task.", content = @Content(schema = @Schema(implementation = Void.class)))
  @ApiResponse(responseCode = "404", description = "Task for the given id and authenticated user not found.", content = {@Content(mediaType = "application/json")})
  public ResponseEntity<Void> deleteTask(
    @AuthenticationPrincipal MyUserDetails userDetails,
    @PathVariable Long id
  ) {
    return taskService.deleteTask(id, userDetails.getUsername())
      .<ResponseEntity<Void>>map(longId -> ResponseEntity.noContent().build())
      .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}")
  @Operation(summary = "Update a user's existing task.", description = "This endpoint allows the user to update one of his tasks. The authenticated user has to be associated with the task is trying to update.")
  @ApiResponse(responseCode = "200", description = "Successfully updated task and return respective updated record.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ResponseTaskDTO.class)))
  @ApiResponse(responseCode = "404", description = "Task for the given id and user not found.", content = {@Content(mediaType = "application/json")})
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
  @Operation(summary = "Retrieve all tasks related to an authenticated user, optionally filtered by category and/or completion status, and sorted as specified.", description = "This endpoint allows to get all tasks related to an authenticated user. The tasks can be optionally filtered by category or task completion status. By default, the tasks are retrieved in descending order of creation date (1st criteria) and descending order of last update time (2nd criteria). Optionally, this sorting order can also be changed.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of tasks by specified sorting order and filtering options.", content = {
    @Content(mediaType = "application/json",
      array = @ArraySchema(schema = @Schema(implementation = ResponseTaskDTO.class)))
  })
  @ApiResponse(responseCode = "400", description = "Bad request. This may occur due to various reasons, including but not limited to: specified task sorting field does not exist.", content = {@Content(mediaType = "application/json")})
  public ResponseEntity<List<ResponseTaskDTO>> getTasks(
    @AuthenticationPrincipal MyUserDetails userDetails,
    Sort sort,
    @RequestParam(required = false) String category,
    @RequestParam(required = false) TaskCompletionStatus completionStatus
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
        category,
        completionStatus
      )
    );
  }

  @GetMapping("/categories")
  @Operation(summary = "Retrieve all task categories related to an authenticated user's tasks.", description = "This endpoint allows to get all task categories related to an authenticated user.")
  @ApiResponse(responseCode = "200", description = "Successfully retrieved list of task categories of an authenticated user.", content = {
    @Content(mediaType = "application/json",
      array = @ArraySchema(schema = @Schema(implementation = String.class)))
  })
  public ResponseEntity<List<String>> getCategories(
    @AuthenticationPrincipal MyUserDetails userDetails
  ) {
    return ResponseEntity.ok(taskService.getCategoriesBySub(userDetails.getUsername()));
  }


}
