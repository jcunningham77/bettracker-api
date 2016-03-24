package com.bettracker.services.rest;

import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class Bet
{
  private long id;
  private Integer wager;
  private Integer result;
  private String homeTeam;
  private String awayTeam;
  private User user;
  
  public User getUser()
  {
    return this.user;
  }
  
  public void setUser(User user)
  {
    this.user = user;
  }
  
  public Bet() {}
  
  public Bet(Integer wager, Integer result, String homeTeam, String awayTeam)
  {
    this.wager = wager;
    this.result = result;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }
  
  public long getId()
  {
    return this.id;
  }
  
  public void setId(long id)
  {
    this.id = id;
  }
  
  public Integer getWager()
  {
    return this.wager;
  }
  
  public void setWager(Integer wager)
  {
    this.wager = wager;
  }
  
  public Integer getResult()
  {
    return this.result;
  }
  
  public void setResult(Integer result)
  {
    this.result = result;
  }
  
  public String getHomeTeam()
  {
    return this.homeTeam;
  }
  
  public void setHomeTeam(String homeTeam)
  {
    this.homeTeam = homeTeam;
  }
  
  public String getAwayTeam()
  {
    return this.awayTeam;
  }
  
  public void setAwayTeam(String awayTeam)
  {
    this.awayTeam = awayTeam;
  }
}
