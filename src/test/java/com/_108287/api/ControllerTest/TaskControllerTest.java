package com._108287.api.ControllerTest;

import com._108287.api.WithMockMyUser;
import com._108287.api.controller.TaskController;
import com._108287.api.dto.CreateRequestTaskDTO;
import com._108287.api.dto.ResponseTaskDTO;
import com._108287.api.entities.TaskCompletionStatus;
import com._108287.api.entities.TaskPriority;
import com._108287.api.service.JwtService;
import com._108287.api.service.TaskService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.Optional;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc(addFilters = false)
class TaskControllerTest {
  static final Logger logger = getLogger(lookup().lookupClass());

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  JwtService jwtService;

  @MockBean
  TaskService taskService;

  CreateRequestTaskDTO createRequestTaskDTO = new CreateRequestTaskDTO(
    "title", "description", LocalDate.of(2024,12,12), TaskPriority.HIGH
  );
  ResponseTaskDTO responseTaskDTO = new ResponseTaskDTO(
    1L, "title", "description", LocalDate.of(2024,12,12),
    LocalDate.of(2024,11,11), LocalDate.of(2024,10,10),
    TaskCompletionStatus.TO_DO, TaskPriority.HIGH
  );

  @BeforeEach
  void setUp() {
    RestAssuredMockMvc.mockMvc(mockMvc);
  }

  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskWithInvalidTaskTitle_Return400(){
    // teste de despiste para ver se o spring está a tratar da validação
    // null should be caught by the Spring Validation
    CreateRequestTaskDTO invalidCreateRequestTaskDTO = new CreateRequestTaskDTO(
      null, "description", LocalDate.of(2024,12,12), TaskPriority.HIGH
    );

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(invalidCreateRequestTaskDTO)
      .when()
        .post("api/tasks")
      .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    verify(taskService, never()).createTask(any(CreateRequestTaskDTO.class), anyString());
  }

  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskWithInvalidTaskDescription_Return400(){
    // teste de despiste para ver se o spring está a tratar da validação
    // null should be caught by the Spring Validation
    CreateRequestTaskDTO invalidCreateRequestTaskDTO = new CreateRequestTaskDTO(
      "title", null, LocalDate.of(2024,12,12), TaskPriority.HIGH
    );

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(invalidCreateRequestTaskDTO)
      .when()
        .post("api/tasks")
      .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    verify(taskService, never()).createTask(any(CreateRequestTaskDTO.class), anyString());
  }

  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskWithInvalidDeadline_Return400(){
    // teste de despiste para ver se o spring está a tratar da validação
    // null should be caught by the Spring Validation
    CreateRequestTaskDTO invalidCreateRequestTaskDTO = new CreateRequestTaskDTO(
      "title", "description", null, TaskPriority.HIGH
    );

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(invalidCreateRequestTaskDTO)
      .when()
        .post("api/tasks")
      .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    verify(taskService, never()).createTask(any(CreateRequestTaskDTO.class), anyString());
  }

  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskWithInvalidPriority_Return400(){
    // teste de despiste para ver se o spring está a tratar da validação
    // null should be caught by the Spring Validation
    CreateRequestTaskDTO invalidCreateRequestTaskDTO = new CreateRequestTaskDTO(
      "title", "description", LocalDate.of(2024,12,12), null
    );

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(invalidCreateRequestTaskDTO)
      .when()
        .post("api/tasks")
      .then()
        .statusCode(HttpStatus.SC_BAD_REQUEST);

    verify(taskService, never()).createTask(any(CreateRequestTaskDTO.class), anyString());
  }


  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskSuccess_ReturnCreatedTask() {
    when(taskService.createTask(any(CreateRequestTaskDTO.class), anyString())).thenReturn(Optional.of(responseTaskDTO));

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(createRequestTaskDTO).
      when().
        post("api/tasks").
      then().
        statusCode(HttpStatus.SC_CREATED)
        .body("id", is(1))
        .body("title", is("title"))
        .body("description", is("description"))
        .body("creationDate", is("2024-12-12"))
        .body("lastUpdated", is("2024-11-11"))
        .body("deadline", is("2024-10-10"))
        .body("completionStatus", is("to_do"))
        .body("priority", is("high"))
        ;

    verify(taskService, times(1)).createTask(any(CreateRequestTaskDTO.class), anyString());
  }

  @Test
  @WithMockMyUser(username = "sub")
  void whenCreateTaskFailure_ReturnBadRequest() {
    when(taskService.createTask(any(CreateRequestTaskDTO.class), anyString())).thenReturn(Optional.empty());

    RestAssuredMockMvc.
      given().
        mockMvc(mockMvc).
        contentType(ContentType.JSON).
        body(createRequestTaskDTO).
      when().
        post("api/tasks").
      then().
        statusCode(HttpStatus.SC_BAD_REQUEST)
    ;

    verify(taskService, times(1)).createTask(any(CreateRequestTaskDTO.class), anyString());
  }


}
