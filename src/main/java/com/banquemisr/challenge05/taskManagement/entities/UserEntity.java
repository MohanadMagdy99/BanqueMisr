package com.banquemisr.challenge05.taskManagement.entities;


import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "users")
public class UserEntity extends BaseEntity {
  @Column(nullable = false, unique = true)
  @NotBlank(message = "Username is required")
  @Size(min = 4, message = "Username must contain at least 4 characters")
  private String username;
  @NotBlank(message = "name is required")
  private String name;
  @Email(message = "invalid email")
  private String email;
  @Column(nullable = false)
  @Size(min = 8, message = "Password must contain at least 8 characters")
  private String password;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private UserRole role;
}
