package com.banquemisr.challenge05.taskManagement.service.user;


import com.banquemisr.challenge05.taskManagement.contracts.IUserService;
import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.mappers.UserMapper;
import com.banquemisr.challenge05.taskManagement.models.response.LoginResponse;
import com.banquemisr.challenge05.taskManagement.models.user.LoginRequestDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserFilter;
import com.banquemisr.challenge05.taskManagement.repositories.TaskRepository;
import com.banquemisr.challenge05.taskManagement.repositories.UserRepository;
import com.banquemisr.challenge05.taskManagement.security.JWTUtilityService;
import com.banquemisr.challenge05.taskManagement.security.UserPrincipal;
import com.banquemisr.challenge05.taskManagement.utils.UserFilterSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService implements IUserService {

  private final PasswordEncoder passwordEncoder;
  private final JWTUtilityService jwtUtilityService;
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserFilterSpecifications userFilterSpecifications;
  private final TaskRepository taskRepository;

  @Override
  public Page<UserDto> getAllUsers(UserFilter userFilter, Pageable page) {
    Specification<UserEntity> spec = userFilterSpecifications.byCriteria(userFilter);
    return userRepository.findAll(spec, page).map(userMapper::map);
  }

  @Override
  public LoginResponse loginUser(LoginRequestDto loginRequestDTO) throws BadRequestException {
    UserEntity user = userRepository.findFirstByUsername(loginRequestDTO.getUsername())
        .orElseThrow(() -> new BadRequestException("User does not exist"));

    String jwt = jwtUtilityService.generateJWTToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());

    log.debug("Login User JWT: {}", jwt);

    UserDto userDTO = userMapper.map(user);


    return LoginResponse.builder()
        .userDto(userDTO)
        .token(jwt)
        .build();
  }

  @Override
  public UserDto signUp(UserDto user) throws BadRequestException {
    if (userRepository.findFirstByUsername(user.getUsername()).isPresent()) {
      throw new BadRequestException("Username already exists");
    }

    if (StringUtils.isBlank(user.getPassword())) {
      throw new BadRequestException("Password is required");
    }

    if (user.getRole() == null) {
      throw new BadRequestException("Role is required");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    var userEntity = userMapper.map(user);

    var savedUser = userRepository.save(userEntity);

    return userMapper.map(savedUser);
  }

  @Override
  public void createAdminUser(UserDto user) throws BadRequestException {
    if (userRepository.findByUsernameAndRole(user.getUsername(), user.getRole()).isPresent()) {
      return;
    }
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    var userEntity = userMapper.map(user);
    userRepository.save(userEntity);
  }

  @Override
  public UserEntity getCurrentUser() throws NotFoundException {
    UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

    return userRepository.findFirstByUsername(userPrincipal.getUsername())
        .orElseThrow(() -> new NotFoundException("User not found"));
  }

  @Override
  public UserDto getUserById(Long id) throws NotFoundException {
    return userMapper.map(checkAndGetUserEntityById(id));
  }

  public UserEntity checkAndGetUserEntityById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
  }

  @Override
  public UserDto updateUser(Long id, UserDto userDto) throws BadRequestException, NotFoundException {
    var existingUser = checkAndGetUserEntityById(id);

    if (StringUtils.isBlank(userDto.getUsername())) {
      throw new BadRequestException("Username is required");
    }

    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new BadRequestException("Username already exists");
    }
    if (!StringUtils.isBlank(userDto.getEmail())) {
      if (userRepository.findByEmail(userDto.getEmail()).isPresent()) {
        throw new BadRequestException("Email already exists");
      }
      existingUser.setEmail(userDto.getEmail());
    }
    existingUser.setUsername(userDto.getUsername());

    if (!StringUtils.isBlank(userDto.getPassword())) {
      existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    userRepository.save(existingUser);

    return userMapper.map(existingUser);
  }

  @Override
  public void deleteUser(Long id) {
    userRepository.findById(id).orElseThrow(() -> new NotFoundException("User Not Found"));
    taskRepository.unAssignTask(id);
    userRepository.deleteById(id);
  }

}
