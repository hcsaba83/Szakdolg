package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>  {
	
	List<Ticket> findAllByOrderByStartdateAsc();

	Ticket findById(Long id);

}
