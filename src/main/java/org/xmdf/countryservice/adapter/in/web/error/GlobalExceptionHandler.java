package org.xmdf.countryservice.adapter.in.web.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.xmdf.countryservice.adapter.in.web.paging.InvalidPageRequestException;
import org.xmdf.countryservice.application.exception.RefreshAlreadyRunningException;
import org.xmdf.countryservice.application.exception.ResourceNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(RefreshAlreadyRunningException.class)
  public ProblemDetail handleRefreshAlreadyRunning(RefreshAlreadyRunningException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, e.getMessage());
  }

  @ExceptionHandler(ResourceNotFoundException.class)
  public ProblemDetail handleResourceNotFound(ResourceNotFoundException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
  }

  @ExceptionHandler(InvalidPageRequestException.class)
  public ProblemDetail handleInvalidPageRequest(InvalidPageRequestException e) {
    return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
  }
}
