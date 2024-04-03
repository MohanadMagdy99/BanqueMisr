package com.banquemisr.challenge05.taskManagement.entities;


import com.banquemisr.challenge05.taskManagement.models.enums.PriorityStatus;
import com.banquemisr.challenge05.taskManagement.models.enums.TaskStatus;
import java.time.OffsetDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "task")
public class TaskEntity extends BaseEntity {

  @Column(nullable = false)
  @NotBlank(message = "Task title cannot be blank")
  private String title;
  @Column(nullable = false)
  private String description;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PriorityStatus priority;
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TaskStatus status;
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;
  @Column(name = "due_date")
  private OffsetDateTime dueDate;

}
