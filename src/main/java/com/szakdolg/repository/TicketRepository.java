package com.szakdolg.repository;

import java.util.List;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.szakdolg.entity.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>  {
	
	List<Ticket> findAllByOrderByStartdateAsc();
	
	
	@Query(value="SELECT t FROM Ticket t where client_email= (select email from User where email= :email)")
	List<Ticket> findAllByClient(@Param("email") String email);
	
	@Query(value="SELECT t FROM Ticket t where worker_email= (select email from User where email= :email)")
	List<Ticket> findAllByWorker(@Param("email") String worker);
	
	@Query(value="SELECT t FROM Ticket t where worker_email= (select email from User where email= :email) AND status=:status")
	List<Ticket> findAllByWorkerbyStatus(@Param("email") String worker, @Param("status") String status);
	
	@Query(value="SELECT t FROM Ticket t where worker_email = null")
	List<Ticket> findAllNoWorker();
	
	@Query(value="SELECT t FROM Ticket t where status = :status")
	List<Ticket> findAllByStatus(@Param("status") String status);

	Ticket findById(Long id);

}
