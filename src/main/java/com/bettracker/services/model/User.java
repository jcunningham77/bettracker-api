package com.bettracker.services.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="user")
public class User
{
  @Id
  @GeneratedValue(strategy=GenerationType.AUTO)
  private long user_id;
  @OneToMany(mappedBy="user")
  @OrderBy("bet_id")
  private Set<Bet> bets = new HashSet();
  @NotNull
  private String email;
  @NotNull
  private String name;
  
  public Set<Bet> getBets()
  {
    return this.bets;
  }
  
  public void setBets(Set<Bet> bets)
  {
    this.bets = bets;
  }
  
  public User() {}
  
  public User(long user_id)
  {
    this.user_id = user_id;
  }
  
  public long getUser_id()
  {
    return this.user_id;
  }
  
  public void setUser_id(long user_id)
  {
    this.user_id = user_id;
  }
  
  public User(String email, String name)
  {
    this.email = email;
    this.name = name;
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
    return "This user is id: " + getUser_id() + " email: " + getEmail() + " name:" + getName() + ". ";
  }
}
