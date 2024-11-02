package com._108287.api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tasks")
@Getter
@NoArgsConstructor
public class Task {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Column(nullable = false)
  private String sub;

  @NotNull
  @Column(nullable = false)
  private String title;

  @NotNull
  @Column(nullable = false)
  private String description;

  @NotNull
  @Column(nullable = false)
  private LocalDate creationDate = LocalDate.now();

  @NotNull
  @Column(nullable = false)
  private LocalDate lastUpdated = LocalDate.now();

  @NotNull
  @Column(nullable = false)
  private LocalDate deadline;

  @Enumerated(EnumType.STRING)
  @NotNull
  @Column(nullable = false)
  private TaskCompletionStatus completionStatus = TaskCompletionStatus.TO_DO;

  @Enumerated(EnumType.STRING)
  @NotNull
  @Column(nullable = false)
  private TaskPriority priority;

  public Task(String sub, String title, String description, LocalDate deadline, TaskPriority priority) {
    this.sub = sub;
    this.title = title;
    this.description = description;
    this.deadline = deadline;
    this.priority = priority;
  }
}
