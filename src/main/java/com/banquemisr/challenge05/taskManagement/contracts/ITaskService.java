package com.banquemisr.challenge05.taskManagement.contracts;

import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.models.task.TaskDto;
import com.banquemisr.challenge05.taskManagement.models.task.TaskFilter;
import javax.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITaskService {

  Page<TaskDto> getAllTasks(TaskFilter taskFilter, Pageable pageable);

  TaskDto getTaskById(Long id) throws NotFoundException;

  Page<TaskDto> getMyTasks(TaskFilter taskFilter, Pageable pageable);

  TaskDto createTask(TaskDto task) throws NotFoundException, BadRequestException, MessagingException;


  TaskDto updateTask(Long id, TaskDto task) throws NotFoundException, BadRequestException;

  void deleteById(Long id) throws NotFoundException, BadRequestException;

  void assignTaskToUser(Long userId, Long taskId) throws MessagingException;
}
