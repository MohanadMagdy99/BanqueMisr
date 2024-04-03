package com.banquemisr.challenge05.taskManagement.controller;

import com.banquemisr.challenge05.taskManagement.contracts.IUserService;
import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostConstructInit {

  private final IUserService userService;
  @Value("${adminUserName}")
  private String defaultAdminUsername;
  @Value("${adminPassword}")
  private String defaultAdminPassword;

  @PostConstruct
  public void instantiateAdminUser() {
    try {
      var admin = UserDto.builder()
          .username(defaultAdminUsername)
          .password(defaultAdminPassword)
          .name("Banque Misr Admin")
          .email("admin@banquemisr.com")
          .role(UserRole.ADMIN)
          .build();
      userService.createAdminUser(admin);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
