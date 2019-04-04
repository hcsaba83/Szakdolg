package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.szakdolg.entity.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket, Long>  {
	
	List<Ticket> findAllByOrderByStartdateDesc();
	
	
	@Query(value="SELECT t FROM Ticket t where client_email= (select email from User where email= :email)")
	List<Ticket> findAllByClient(@Param("email") String email);
	
	@Query(value="SELECT t FROM Ticket t where worker_email= (select email from User where email= :email)")
	List<Ticket> findAllByWorker(@Param("email") String worker);
	
	@Query(value="SELECT t FROM Ticket t where worker_email= (select email from User where email= :email) AND status=:status")
	List<Ticket> findAllByWorkerbyStatus(@Param("email") String worker, @Param("status") String status);
	
	@Query(value="SELECT t FROM Ticket t where client_email= (select email from User where email= :email) AND status=:status")
	List<Ticket> findAllByClientByStatus(@Param("email") String user, @Param("status") String status);
	
	@Query(value="SELECT t FROM Ticket t where worker_email = null ORDER BY t.startdate DESC")
	List<Ticket> findAllNoWorker();

	
	@Query(value="SELECT t FROM Ticket t where status = :status")
	List<Ticket> findAllByStatus(@Param("status") String status);
	
	
	// LIMIT LEKÉRÉSEK
	//by status
	@Query(value="SELECT t FROM Ticket t where status = :status")
	List<Ticket> findAllByInProgressLimit(@Param("status") String status, Pageable pageable);
	//by worker by status
	@Query(value="SELECT t FROM Ticket t where worker_email= (select email from User where email= :email) AND status=:status")
	List<Ticket> findAllByWorkerbyStatusLimit(@Param("email") String worker, @Param("status") String status, Pageable pageable);
	//by client by status
	@Query(value="SELECT t FROM Ticket t where client_email= (select email from User where email= :email) AND status=:status")
	List<Ticket> findAllByClientByStatusLimit(@Param("email") String client, @Param("status") String status, Pageable pageable);
	//by no worker
	@Query(value="SELECT t FROM Ticket t where worker_email = null ORDER BY t.startdate DESC")
	List<Ticket> findAllNoWorkerLimit(Pageable pageable);
	//by status
	@Query(value="SELECT t FROM Ticket t where status = :status")
	List<Ticket> findAllByStatusLimit(@Param("status") String status, Pageable pageable);
	
	
	//COUNT LEKÉRÉSEK
	//by worker
	@Query(value="SELECT COUNT(id) FROM Ticket t where worker_email= (select email from User where email= :email)")
	int findAllByWorkerCount(@Param("email") String worker);
	//by client
	@Query(value="SELECT COUNT(id) FROM Ticket t where client_email= (select email from User where email= :email)")
	int findAllByClientCount(@Param("email") String worker);
	//by worker by status
	@Query(value="SELECT COUNT(id) FROM Ticket t where worker_email= (select email from User where email= :email) AND status=:status")
	int findAllByWorkerbyStatusCount(@Param("email") String worker, @Param("status") String status);
	//by client by status
	@Query(value="SELECT COUNT(id) FROM Ticket t where client_email= (select email from User where email= :email) AND status=:status")
	int findAllByClientbyStatusCount(@Param("email") String client, @Param("status") String status);
	//by no worker - opened
	@Query(value="SELECT COUNT(id) FROM Ticket t where worker_email = null")
	int findAllNoWorkerCount();
	//by status
	@Query(value="SELECT COUNT(id) FROM Ticket t where status = :status")
	int findAllByStatusCount(@Param("status") String status);
	@Query(value="SELECT COUNT(id) FROM Ticket")
	int findAllCount();

	Ticket findById(Long id);


}
