package com.banquemisr.challenge05.taskManagement.mappers;


import com.banquemisr.challenge05.taskManagement.entities.TaskEntity;
import com.banquemisr.challenge05.taskManagement.models.task.TaskDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface TaskMapper {
  @Mapping(source = ".", target = ".")
  TaskDto map(TaskEntity task);

  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "lastModifiedAt", ignore = true)
  @Mapping(source = ".", target = ".")
  TaskEntity map(TaskDto taskDto);
}
