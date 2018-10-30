package com.szakdolg.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>  {
	
	List<Ticket> findAllByOrderByStartdateAsc();
	
	
	@Query(value="SELECT t FROM Ticket t where client_email= (select email from User where email= :email)")
	List<Ticket> findAllByClient(@Param("email") String email);
	
	@Query(value="SELECT t FROM Ticket t where worker= :worker")
	List<Ticket> findAllByWorker(@Param("worker") String worker);

	Ticket findById(Long id);

}
