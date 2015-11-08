package com.bettracker.services.model;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;

@Transactional
public abstract interface BetDAO
  extends CrudRepository<Bet, Long>
{}
