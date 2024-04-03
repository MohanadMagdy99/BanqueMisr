package com.banquemisr.challenge05.taskManagement.controller;


import com.banquemisr.challenge05.taskManagement.contracts.IUserService;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.models.response.ResponseDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserFilter;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
  private final IUserService userService;

  @GetMapping("/")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> getAllUsers(UserFilter userFilter,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size,
                                       @RequestParam(value = "sort", required = false) String sort) {
    log.info("Request URI --> (/api/user) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".getAllUsers()");
    PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id"));
    return ResponseEntity.ok(userService.getAllUsers(userFilter, pageRequest).getContent());
  }

  @PostMapping("/signUp")
  public ResponseEntity<?> signUp(@Valid @RequestBody UserDto user) throws BadRequestException {
    log.info("Request URI --> (/api/user) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".createUser()");
    log.info("Request body: {}", user);
    var userDTO = userService.signUp(user);
    var response = ResponseDto.builder()
        .message("Registration Has Been Completed")
        .data(userDTO)
        .build();
    return ResponseEntity.ok(response);
  }


  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserDto user)
      throws BadRequestException, NotFoundException {
    log.info("Request URI --> (/api/user) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".updateUser()");
    log.info("Request body: {}", user);
    var response = ResponseDto.builder()
        .message("User has been updated successfully")
        .data(userService.updateUser(id, user))
        .build();
    return ResponseEntity.ok(response);
  }


  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> deleteUser(@PathVariable Long id) throws BadRequestException {
    log.info("Request URI --> (/api/user/{}) has been called.", id);
    log.info("Method: {}", this.getClass().getName() + ".deleteUser()");
    userService.deleteUser(id);
    var respone = ResponseDto.builder()
        .message("User has been deleted successfully")
        .build();
    return ResponseEntity.ok(respone);
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<?> getUserById(@PathVariable Long id) throws BadRequestException {
    log.info("Request URI --> (/api/user/{}) has been called.", id);
    log.info("Method: {}", this.getClass().getName() + ".getUserById()");
    return ResponseEntity.ok(userService.getUserById(id));
  }


  @GetMapping("/profile")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> getUserInfo() throws NotFoundException {
    log.info("Request URI --> (/api/user/profile) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".getUserInfo()");
    return ResponseEntity.ok(userService.getCurrentUser());
  }


  @PutMapping("/profile")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> updateProfile(@Valid @RequestBody UserDto user) throws BadRequestException {
    log.info("Request URI --> (/api/user/profile) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".updateProfile()");
    log.info("Request body: {}", user);
    var currentUser = userService.getCurrentUser();
    user.setRole(null); // to prevent the user from changing his role
    var response = ResponseDto.builder()
        .message("User has been updated successfully")
        .data(userService.updateUser(currentUser.getId(), user))
        .build();
    return ResponseEntity.ok(
        response
    );
  }

}
