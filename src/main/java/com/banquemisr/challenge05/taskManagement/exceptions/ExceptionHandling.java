package com.banquemisr.challenge05.taskManagement.exceptions;

import com.banquemisr.challenge05.taskManagement.models.response.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> handleNotFoundException(RuntimeException runtimeException) {
    var errorResponse = ResponseDto.builder()
        .success(false)
        .message(runtimeException.getMessage())
        .build();
    if (runtimeException instanceof NotFoundException)
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(errorResponse);
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(errorResponse);
  }
}
