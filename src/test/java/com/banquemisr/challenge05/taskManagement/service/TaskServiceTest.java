package com.banquemisr.challenge05.taskManagement.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.banquemisr.challenge05.taskManagement.entities.TaskEntity;
import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.mappers.TaskMapper;
import com.banquemisr.challenge05.taskManagement.mappers.UserMapper;
import com.banquemisr.challenge05.taskManagement.models.enums.PriorityStatus;
import com.banquemisr.challenge05.taskManagement.models.task.TaskDto;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import com.banquemisr.challenge05.taskManagement.repositories.TaskRepository;
import com.banquemisr.challenge05.taskManagement.service.email.EmailService;
import com.banquemisr.challenge05.taskManagement.service.task.TaskService;
import com.banquemisr.challenge05.taskManagement.service.user.UserService;
import javax.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

  @Mock
  private TaskRepository taskRepository;
  @Mock
  private TaskMapper taskMapper;

  @Mock
  private UserService userService;

  @Mock
  private EmailService emailService;

  @Mock
  private UserMapper userMapper;

  @InjectMocks
  private TaskService taskService;

  @Test
  void getTaskById_Found() {
    Long taskId = 1L;
    TaskEntity mockEntity = new TaskEntity();
    TaskDto mockDto = new TaskDto();
    when(taskRepository.findById(taskId)).thenReturn(Optional.of(mockEntity));
    when(taskMapper.map(mockEntity)).thenReturn(mockDto);

    TaskDto result = taskService.getTaskById(taskId);

    assertNotNull(result);
    verify(taskRepository).findById(taskId);
    verify(taskMapper).map(mockEntity);
  }

  @Test
  void getTaskById_NotFound() {
    Long taskId = 1L;
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.getTaskById(taskId));
  }

  @Test
  void createTask_Success() throws MessagingException {
    TaskDto taskDto = new TaskDto();
    UserDto userDto = UserDto.builder().id(1L).build();
    taskDto.setUser(userDto);
    taskDto.setPriority(PriorityStatus.HIGH);
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setPriority(PriorityStatus.HIGH);
    UserEntity userEntity = new UserEntity();
    userEntity.setId(1L);
    taskEntity.setUser(userEntity);
    when(taskRepository.findByTitle(anyString())).thenReturn(Optional.empty());
    when(userService.checkAndGetUserEntityById(anyLong())).thenReturn(userEntity);
    when(taskMapper.map(taskDto)).thenReturn(taskEntity);
    when(taskMapper.map(taskEntity)).thenReturn(taskDto);
    when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);

    TaskDto result = taskService.createTask(taskDto);

    assertNotNull(result);
    verify(emailService, times(1)).sendEmail(any(), any(), any());
  }

  @Test
  void createTask_DuplicateTitle() {
    TaskDto taskDto = new TaskDto();
    taskDto.setTitle("Existing Title");
    when(taskRepository.findByTitle(taskDto.getTitle())).thenReturn(Optional.of(new TaskEntity()));

    assertThrows(BadRequestException.class, () -> taskService.createTask(taskDto));
  }

  @Test
  void updateTask_Success() {
    Long taskId = 1L;
    TaskDto taskDto = new TaskDto();
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setId(taskId);

    when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
    when(taskRepository.save(any(TaskEntity.class))).thenReturn(taskEntity);
    when(taskMapper.map(any(TaskEntity.class))).thenReturn(taskDto);

    assertDoesNotThrow(() -> taskService.updateTask(taskId, taskDto));
  }

  @Test
  void updateTask_NotFound() {
    Long taskId = 1L;
    TaskDto taskDto = new TaskDto();

    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.updateTask(taskId, taskDto));
  }

  @Test
  void deleteById_Success() {
    Long taskId = 1L;
    TaskEntity taskEntity = new TaskEntity();
    taskEntity.setId(taskId);

    when(taskRepository.findById(taskId)).thenReturn(Optional.of(taskEntity));
    doNothing().when(taskRepository).deleteById(taskId);

    assertDoesNotThrow(() -> taskService.deleteById(taskId));
    verify(taskRepository, times(1)).deleteById(taskId);
  }

  @Test
  void deleteById_NotFound() {
    Long taskId = 1L;
    when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> taskService.deleteById(taskId));
  }


}
