package com.banquemisr.challenge05.taskManagement.controller;


import com.banquemisr.challenge05.taskManagement.contracts.IUserService;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.models.user.LoginRequestDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {
  private final IUserService userService;


  @PostMapping("/login")
  public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto loginRequestDTO) throws BadRequestException {
    log.debug("API ---> (/api/auth/login) has been called.");
    log.debug("Method Location: {}", this.getClass().getName() + ".login()");
    log.debug("Request body: {}", loginRequestDTO);
    return ResponseEntity.ok(userService.loginUser(loginRequestDTO));
  }
}
