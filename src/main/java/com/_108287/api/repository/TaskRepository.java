package com._108287.api.repository;

import com._108287.api.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
  Optional<Task> findByIdAndSub(Long id, String sub);

  @Query("SELECT DISTINCT t.category FROM Task t WHERE t.sub = :sub")
  List<String> findDistinctCategoriesBySub(String sub);
}
