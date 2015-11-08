package com.bettracker.services.model;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public abstract interface UserDAO
  extends CrudRepository<User, Long>
{
  public abstract User findByEmail(String paramString);
}
