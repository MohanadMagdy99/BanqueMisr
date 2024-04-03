package com.banquemisr.challenge05.taskManagement.service;


import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.mappers.UserMapper;
import com.banquemisr.challenge05.taskManagement.models.enums.UserRole;
import com.banquemisr.challenge05.taskManagement.models.response.LoginResponse;
import com.banquemisr.challenge05.taskManagement.models.user.LoginRequestDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import com.banquemisr.challenge05.taskManagement.repositories.UserRepository;
import com.banquemisr.challenge05.taskManagement.security.JWTUtilityService;
import com.banquemisr.challenge05.taskManagement.security.UserPrincipal;
import com.banquemisr.challenge05.taskManagement.service.user.UserService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserMapper userMapper;

  @Mock
  private JWTUtilityService jwtUtilityService;

  @InjectMocks
  private UserService userService;


  @Test
  void loginUser_Success() {
    String username = "user";
    String password = "password";
    LoginRequestDto loginRequestDTO = LoginRequestDto.builder().username(username).password(password).build();
    UserEntity userEntity = new UserEntity();

    when(userRepository.findFirstByUsername(username)).thenReturn(Optional.of(userEntity));
    when(jwtUtilityService.generateJWTToken(username, password)).thenReturn("jwtToken");

    LoginResponse response = userService.loginUser(loginRequestDTO);

    assertNotNull(response);
    assertEquals("jwtToken", response.getToken());
  }

  @Test
  void loginUser_UserDoesNotExist() {
    String username = "nonExistingUser";
    LoginRequestDto loginRequestDTO = LoginRequestDto.builder().username(username).password("password").build();

    when(userRepository.findFirstByUsername(username)).thenReturn(Optional.empty());

    assertThrows(BadRequestException.class, () -> userService.loginUser(loginRequestDTO));
  }


  @Test
  void signUp_Success() {
    UserDto newUser = UserDto
        .builder()
        .username("newUser")
        .password("super$ecur#")
        .email("mail@mail.com")
        .role(UserRole.EMPLOYEE)
        .build();

    when(userRepository.findFirstByUsername(newUser.getUsername())).thenReturn(Optional.empty());
    when(passwordEncoder.encode(newUser.getPassword())).thenReturn("encodedPassword");
    when(userRepository.save(any(UserEntity.class))).thenReturn(new UserEntity());

    assertDoesNotThrow(() -> userService.signUp(newUser));
  }

  @Test
  void signUp_UsernameAlreadyExists() {
    UserDto newUser = UserDto.builder().username("existingUser").build();
    newUser.setUsername("existingUser");

    when(userRepository.findFirstByUsername(newUser.getUsername())).thenReturn(Optional.of(new UserEntity()));

    assertThrows(BadRequestException.class, () -> userService.signUp(newUser));
  }
  @Test
  void getCurrentUser_Success() {
    String username = "currentUser";
    UserEntity userEntity = UserEntity
        .builder().username(username).password("password").build();
    UserPrincipal userPrincipal = new UserPrincipal(userEntity,List.of());
    userEntity.setUsername(username);

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal,
        null, userPrincipal.getAuthorities()));
    when(userRepository.findFirstByUsername(username)).thenReturn(Optional.of(userEntity));

    assertDoesNotThrow(() -> userService.getCurrentUser());
  }

  @Test
  void getCurrentUser_NotFound() {
    String username = "missingUser";
    UserEntity userEntity = UserEntity
        .builder().username(username).password("password").build();
    UserPrincipal userPrincipal = new UserPrincipal(userEntity, List.of());

    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities()));
    when(userRepository.findFirstByUsername(username)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> userService.getCurrentUser());
  }


}
