package com._108287.api.specifications;

import com._108287.api.entities.Task;
import com._108287.api.entities.TaskCompletionStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {

  private TaskSpecification() { }

  public static Specification<Task> hasSub(String sub) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (sub == null) {
        throw new IllegalArgumentException("sub cannot be null");
      }
      return cb.equal(root.get("sub"), sub);
    };
  }

  public static Specification<Task> hasCategory(String category) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (category == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("category"), category);
    };
  }

  public static Specification<Task> hasCompletionStatus(TaskCompletionStatus completionStatus) {
    return (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
      if (completionStatus == null) {
        return cb.conjunction();
      }
      return cb.equal(root.get("completionStatus"), completionStatus);
    };
  }

}
