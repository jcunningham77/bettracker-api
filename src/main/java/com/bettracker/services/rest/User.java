package com.bettracker.services.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User
{
  private long id;
  private String email;
  private String name;
  private List<Bet> bets;
  
  public List<Bet> getBets()
  {
    return this.bets;
  }
  
  public void setBets(List<Bet> bets)
  {
    this.bets = bets;
  }
  
  public User() {}
  
  public User(long id)
  {
    this.id = id;
  }
  
  public User(String email, String name)
  {
    this.email = email;
    this.name = name;
  }
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long value)
  {
    this.id = value;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String value)
  {
    this.email = value;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String value)
  {
    this.name = value;
  }
  
  public String toString()
  {
    return "This user is id: " + getId() + " email: " + getEmail() + " name:" + getName() + ". ";
  }
}
