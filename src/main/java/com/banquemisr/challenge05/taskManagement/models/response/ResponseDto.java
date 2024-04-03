package com.banquemisr.challenge05.taskManagement.models.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder
public class ResponseDto<T> {
  private String message;
  @Builder.Default
  private boolean success = true;
  private T data;
}
