package com.banquemisr.challenge05.taskManagement.utils;

import com.banquemisr.challenge05.taskManagement.entities.TaskEntity;
import com.banquemisr.challenge05.taskManagement.models.task.TaskFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class TaskFilterSpecifications {
  public Specification<TaskEntity> byCriteria(TaskFilter criteria) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (criteria.getDescription() != null) {
        predicates.add(builder.like(root.get("description"), criteria.getDescription()));
      }
      if (criteria.getPriority() != null) {
        predicates.add(builder.equal(root.get("priority"), criteria.getPriority()));
      }
      if (criteria.getTitle() != null) {
        predicates.add(builder.like(root.get("title"), criteria.getTitle()));
      }
      if (criteria.getStatus() != null) {
        predicates.add(builder.equal(root.get("status"), criteria.getStatus()));
      }
      if (criteria.getDueDate() != null) {
        predicates.add(builder.equal(root.get("due_date"), criteria.getDueDate()));
      }
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }


  public Specification<TaskEntity> hasUserId(Long userId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("user").get("id"), userId);
  }
}
