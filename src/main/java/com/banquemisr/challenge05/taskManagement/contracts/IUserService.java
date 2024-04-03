package com.banquemisr.challenge05.taskManagement.contracts;

import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.models.response.LoginResponse;
import com.banquemisr.challenge05.taskManagement.models.user.LoginRequestDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
  Page<UserDto> getAllUsers(UserFilter userFilter, Pageable page);

  LoginResponse loginUser(LoginRequestDto loginRequestDTO) throws BadRequestException;

  UserDto signUp(UserDto user) throws BadRequestException;

  void createAdminUser(UserDto user) throws BadRequestException;


  UserEntity getCurrentUser() throws NotFoundException;

  UserDto getUserById(Long id) throws NotFoundException;

  UserDto updateUser(Long id, UserDto userDto) throws BadRequestException, NotFoundException;

  void deleteUser(Long id) throws NotFoundException;

}
