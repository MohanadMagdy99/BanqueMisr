package com.banquemisr.challenge05.taskManagement.models.user;

import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import lombok.Value;

@Value
public class UserFilter {
  String email;
  UserRole role;
  String username;
  String name;
}
