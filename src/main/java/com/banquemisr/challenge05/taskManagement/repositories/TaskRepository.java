package com.banquemisr.challenge05.taskManagement.repositories;

import com.banquemisr.challenge05.taskManagement.entities.TaskEntity;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long>, JpaSpecificationExecutor<TaskEntity> {

  Page<TaskEntity> findByUserId(Long userId, Specification<TaskEntity> specification, Pageable page);

  Optional<TaskEntity> findByTitle(String title);

  Optional<TaskEntity> findByTitleAndIdNot(String title, Long id);

  @Modifying
  @Transactional
  @Query("UPDATE TaskEntity t SET t.user = NULL WHERE t.user.id = :user")
  void unAssignTask(Long user);

}
