package com.banquemisr.challenge05.taskManagement.repositories;


import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {

  Optional<UserEntity> findFirstByUsername(String username);

  Optional<UserEntity> findByUsernameAndRole(String username, UserRole userRole);

  List<UserEntity> findByRole(UserRole role);

  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

}
