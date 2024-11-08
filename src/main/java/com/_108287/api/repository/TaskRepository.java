package com._108287.api.repository;

import com._108287.api.entities.Task;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
  Optional<Task> findByIdAndSub(Long id, String sub);
  List<Task> findBySub(String sub, Sort sort);
}
