package com.banquemisr.challenge05.taskManagement.models.user;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequestDto {
  @NotBlank(message = "Username is required")
  @Size(min = 4, message = "Username must contain at least 4 characters")
  private String username;
  @Size(min = 8, message = "Password must contain at least 8 characters")
  private String password;
}
