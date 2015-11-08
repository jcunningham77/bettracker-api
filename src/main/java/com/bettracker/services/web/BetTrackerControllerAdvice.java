package com.bettracker.services.web;

import com.bettracker.services.exception.ResourceNotFoundException;
import com.bettracker.services.rest.ErrorMessage;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(assignableTypes={BetTrackerController.class})
public class BetTrackerControllerAdvice
{
  private static Logger logger = Logger.getLogger(BetTrackerController.class);
  
  @ExceptionHandler({ResourceNotFoundException.class})
  public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex)
  {
    ErrorMessage errorMessage = new ErrorMessage();
    errorMessage.setMessage(ex.getMessage());
    logger.error(ex.getLogMsg());
    logger.info(ex.getLogMsg());
    return new ResponseEntity(errorMessage, HttpStatus.NOT_FOUND);
  }
}
