package com.banquemisr.challenge05.taskManagement.models.response;


import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
  String token;
  UserDto userDto;
}
