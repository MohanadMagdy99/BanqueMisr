package com.banquemisr.challenge05.taskManagement.utils;

import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.models.user.UserFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UserFilterSpecifications {
  public Specification<UserEntity> byCriteria(UserFilter criteria) {
    return (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (criteria.getName() != null) {
        predicates.add(builder.like(root.get("name"), criteria.getName()));
      }
      if (criteria.getEmail() != null) {
        predicates.add(builder.like(root.get("email"), criteria.getEmail()));
      }
      if (criteria.getRole() != null) {
        predicates.add(builder.equal(root.get("role"), criteria.getRole()));
      }
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }
}
