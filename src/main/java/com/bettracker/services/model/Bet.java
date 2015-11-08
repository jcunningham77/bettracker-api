package com.bettracker.services.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="bet")
public class Bet
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private long bet_id;
  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;
  @NotNull
  private Integer wager;
  private Integer result;
  @NotNull
  private String homeTeam;
  @NotNull
  private String awayTeam;
  
  public User getUser()
  {
    return this.user;
  }
  
  public void setUser(User user)
  {
    this.user = user;
  }
  
  public Bet() {}
  
  public Bet(long bet_id)
  {
    this.bet_id = bet_id;
  }
  
  public Bet(Integer wager, Integer result, String homeTeam, String awayTeam)
  {
    this.wager = wager;
    this.result = result;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }
  
  public Bet(User user, Integer wager, Integer result, String homeTeam, String awayTeam)
  {
    this.user = user;
    this.wager = wager;
    this.result = result;
    this.homeTeam = homeTeam;
    this.awayTeam = awayTeam;
  }
  
  public long getBet_id()
  {
    return this.bet_id;
  }
  
  public void setBet_id(long bet_id)
  {
    this.bet_id = bet_id;
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
