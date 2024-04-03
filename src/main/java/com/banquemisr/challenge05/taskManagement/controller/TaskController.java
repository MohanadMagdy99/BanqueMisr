package com.banquemisr.challenge05.taskManagement.controller;


import com.banquemisr.challenge05.taskManagement.contracts.ITaskService;
import com.banquemisr.challenge05.taskManagement.exceptions.BadRequestException;
import com.banquemisr.challenge05.taskManagement.exceptions.NotFoundException;
import com.banquemisr.challenge05.taskManagement.models.response.ResponseDto;
import com.banquemisr.challenge05.taskManagement.models.task.TaskDto;
import com.banquemisr.challenge05.taskManagement.models.task.TaskFilter;
import javax.mail.MessagingException;
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
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

  private final ITaskService taskService;

  @GetMapping("/")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> getAllTasks(TaskFilter taskFilter,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size,
                                       @RequestParam(value = "sort", required = false) String sort) {
    log.info("Request URI --> (/api/tasks) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".getAllTasks()");
    if (sort == null)
      sort = "id";
    PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sort));
    return ResponseEntity.ok(taskService.getAllTasks(taskFilter, pageRequest).getContent());
  }


  @GetMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> getTaskById(@PathVariable Long id) throws NotFoundException {
    log.info("Request URI --> (/api/tasks/{}) has been called.", id);
    log.info("Method: {}", this.getClass().getName() + ".getTaskById()");
    return ResponseEntity.ok(taskService.getTaskById(id));
  }


  @GetMapping("/my-tasks")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> getMyTasks(TaskFilter taskFilter,
                                      @RequestParam("page") int page,
                                      @RequestParam("size") int size,
                                      @RequestParam(value = "sort", required = false) String sort) {
    log.info("Request URI --> (/api/tasks/my-tasks) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".getMyTasks()");
    if (sort == null)
      sort = "id";
    PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by(sort));
    return ResponseEntity.ok(taskService.getMyTasks(taskFilter, pageRequest).getContent());
  }


  @PostMapping("/")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<?> createTask(@RequestBody TaskDto task) throws NotFoundException, BadRequestException, MessagingException {
    log.info("Request URI --> (/api/tasks/) has been called.");
    log.info("Method: {}", this.getClass().getName() + ".createTask()");
    log.info("Request body: {}", task);
    var taskDto = taskService.createTask(task);
    return ResponseEntity.ok(
        ResponseDto.builder()
            .message("Task has been created successfully")
            .data(taskDto)
            .build()
    );
  }


  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('MANAGER')")
  public ResponseEntity<?> updateTask(@PathVariable Long id, @RequestBody TaskDto task) throws NotFoundException, BadRequestException {
    log.info("Request URI --> (/api/tasks/{}) has been called.", id);
    log.info("Method: {}", this.getClass().getName() + ".updateTask()");
    log.info("Request body: {}", task);
    return ResponseEntity.ok(
        ResponseDto.builder()
            .message("Task has been updated successfully")
            .data(taskService.updateTask(id, task))
            .build()
    );
  }


  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('EMPLOYEE')")
  public ResponseEntity<?> deleteTaskById(@PathVariable Long id) throws NotFoundException, BadRequestException {
    log.info("Request URI --> (/api/tasks/{}) has been called.", id);
    log.info("Method: {}", this.getClass().getName() + ".deleteTaskById()");
    taskService.deleteById(id);
    return ResponseEntity.ok(
        ResponseDto.builder()
            .message("Task has been deleted successfully")
            .build()
    );
  }

  @PutMapping("/{userId}/{taskId}")
  public ResponseEntity<?> assignTaskToUser(@PathVariable Long userId,
                                            @PathVariable Long taskId) throws MessagingException {
    log.info("Request URI --> (/api/tasks/{}/{}) has been called.", userId, taskId);
    log.info("Method: {}", this.getClass().getName() + ".assignTaskToUser()");
    taskService.assignTaskToUser(userId, taskId);
    return ResponseEntity.ok(
        ResponseDto.builder()
            .message("Task has been assigned successfully")
            .build()
    );
  }

}
