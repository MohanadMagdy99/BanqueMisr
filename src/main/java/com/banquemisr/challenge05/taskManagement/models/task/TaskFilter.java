package com.banquemisr.challenge05.taskManagement.models.task;

import com.banquemisr.challenge05.taskManagement.models.enums.PriorityStatus;
import com.banquemisr.challenge05.taskManagement.models.enums.TaskStatus;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import java.time.OffsetDateTime;
import lombok.Value;

@Value
public class TaskFilter {
  String title;
  String description;
  PriorityStatus priority;
  TaskStatus status;
  UserDto user;
  OffsetDateTime dueDate;
}
