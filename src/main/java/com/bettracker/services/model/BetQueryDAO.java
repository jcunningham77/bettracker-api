package com.bettracker.services.model;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.PagingAndSortingRepository;
@Transactional
public interface BetQueryDAO extends PagingAndSortingRepository<Bet, Long> {
	List<Bet> findByUser(User user);

}
