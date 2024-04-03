package com.banquemisr.challenge05.taskManagement.service.task;

import com.banquemisr.challenge05.taskManagement.contracts.ITaskService;
import com.banquemisr.challenge05.taskManagement.entities.TaskEntity;
import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.mappers.TaskMapper;
import com.banquemisr.challenge05.taskManagement.mappers.UserMapper;
import com.banquemisr.challenge05.taskManagement.models.enums.PriorityStatus;
import com.banquemisr.challenge05.taskManagement.models.task.TaskDto;
import com.banquemisr.challenge05.taskManagement.models.task.TaskFilter;
import com.banquemisr.challenge05.taskManagement.repositories.TaskRepository;
import com.banquemisr.challenge05.taskManagement.service.email.EmailService;
import com.banquemisr.challenge05.taskManagement.service.user.UserService;
import com.banquemisr.challenge05.taskManagement.utils.TaskFilterSpecifications;
import java.util.Objects;
import javax.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {
  private final TaskRepository taskRepository;
  private final UserService userService;
  private final TaskMapper taskMapper;
  private final UserMapper userMapper;
  private final EmailService emailService;
  private final TaskFilterSpecifications taskFilterSpecifications;

  @Override
  public Page<TaskDto> getAllTasks(TaskFilter taskFilter, Pageable pageable) {
    Specification<TaskEntity> spec = taskFilterSpecifications.byCriteria(taskFilter);
    return taskRepository.findAll(spec, pageable).map(taskMapper::map);
  }

  @Override
  public TaskDto getTaskById(Long id) throws NotFoundException {
    return taskMapper.map(taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found")));
  }

  @Override
  public Page<TaskDto> getMyTasks(TaskFilter taskFilter, Pageable pageable) {
    var user = userService.getCurrentUser();
    Specification<TaskEntity> combinedSpecs = Specification.where(taskFilterSpecifications.hasUserId(user.getId()));
    return taskRepository.findAll(combinedSpecs, pageable).map(taskMapper::map);
  }

  @Override
  public TaskDto createTask(TaskDto task) throws NotFoundException, BadRequestException, MessagingException {
    UserEntity user = null;
    if (task.getUser() != null)
      user = userService.checkAndGetUserEntityById(task.getUser().getId());
    task.setUser(userMapper.map(user));

    // check if the task already exists
    if (taskRepository.findByTitle(task.getTitle()).isPresent()) {
      throw new BadRequestException("Title already exists");
    }

    var savedTask = taskMapper.map(task);
    savedTask.setUser(user);
    sendEmailForTask(savedTask);
    return taskMapper.map(taskRepository.save(savedTask));
  }

  private void sendEmailForTask(TaskEntity savedTask) throws MessagingException {
    if (PriorityStatus.HIGH.equals(savedTask.getPriority())
        || PriorityStatus.CRITICAL.equals(savedTask.getPriority()) && (savedTask.getUser() != null)) {
      emailService.sendEmail(savedTask.getUser().getEmail(), savedTask.getUser().getName(), savedTask.getTitle());
    }
  }

  @Override
  public TaskDto updateTask(Long id, TaskDto task) throws NotFoundException, BadRequestException {
    // get the task by id
    var taskToUpdate = taskRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Task not found"));

    // get the current user
    var user = userService.getCurrentUser();

    // check if the current user is the assigned user to the task
    if (taskToUpdate.getUser() != null && !Objects.equals(taskToUpdate.getUser().getId(), user.getId())) {
      throw new NotFoundException("Task not found");
    }

    // check if the task title already exists except for the current task
    if (taskRepository.findByTitleAndIdNot(task.getTitle(), id).isPresent()) {
      throw new BadRequestException("Task already exists");
    }

    // update the task
    taskToUpdate.setTitle(task.getTitle());
    if (task.getDescription() != null && task.getDescription().isEmpty())
      taskToUpdate.setDescription(task.getDescription());
    if (task.getPriority() != null)
      taskToUpdate.setPriority(task.getPriority());
    if (task.getStatus() != null)
      taskToUpdate.setStatus(task.getStatus());

    // save the task
    return taskMapper.map(taskRepository.save(taskToUpdate));
  }

  @Override
  public void deleteById(Long id) throws NotFoundException, BadRequestException {
    var task = taskRepository.findById(id).orElseThrow(() -> new NotFoundException("Task not found"));

    // get the current user
    var user = userService.getCurrentUser();

    // check if the current user is the assigned user of the task
    if (task.getUser() != null && !Objects.equals(task.getUser().getId(), user.getId())) {
      throw new NotFoundException("You are not assigned to this task");
    }

    // delete the task
    taskRepository.deleteById(id);
  }

  @Override
  public void assignTaskToUser(Long userId, Long taskId) throws MessagingException {
    var user = userService.checkAndGetUserEntityById(userId);
    var task = taskRepository.findById(taskId)
        .orElseThrow(() -> new NotFoundException("Task not found"));
    task.setUser(user);
    var taskEntity = taskRepository.save(task);
    sendEmailForTask(taskEntity);
  }
}
