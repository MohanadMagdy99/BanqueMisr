package com.banquemisr.challenge05.taskManagement.models.user;


import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class UserDto {
  Long id;
  String username;
  String password;
  String email;
  UserRole role;
  String name;
}
