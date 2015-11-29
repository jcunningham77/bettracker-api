package com.bettracker.services.exception;

public class InvalidRequestBodyException  extends APIException {
		  public InvalidRequestBodyException(String message, String logMessage)
		  {
		    super(message, logMessage);
		  }
}

