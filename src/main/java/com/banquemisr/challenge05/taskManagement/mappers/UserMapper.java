package com.banquemisr.challenge05.taskManagement.mappers;


import com.banquemisr.challenge05.taskManagement.entities.UserEntity;
import com.banquemisr.challenge05.taskManagement.models.user.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR, componentModel = "spring")
public interface UserMapper {


  @Mapping(source = ".", target = ".")
  UserEntity map(UserDto userDto);

  @Mapping(source = ".", target = ".")
  UserDto map(UserEntity user);
}
