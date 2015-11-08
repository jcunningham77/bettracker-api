package com.bettracker.services.exception;

public abstract class APIException
  extends RuntimeException
{
  private final String logMsg;
  
  public APIException(String msg, String logMsg)
  {
    super(msg);
    
    this.logMsg = logMsg;
  }
  
  public APIException(String msg, String logMsg, Throwable cause)
  {
    super(msg, cause);
    this.logMsg = logMsg;
  }
  
  public String getLogMsg()
  {
    return this.logMsg;
  }
}
