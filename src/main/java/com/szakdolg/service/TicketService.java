package com.szakdolg.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.szakdolg.entity.Ticket;
import com.szakdolg.repository.TicketRepository;

@Service
public class TicketService {
	
	private UserService userService;
	private TicketRepository ticketRepository;
	private String thatname;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	public void setTicketRepo(TicketRepository ticketRepository) {
		this.ticketRepository = ticketRepository;
	}
	@Autowired
	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	
	public List<Ticket> getAllTickets() {
		//log.debug("getAllTickets");
		return ticketRepository.findAllByOrderByStartdateDesc();
	}

	public Ticket getSpecificTicket(Long id) {
		//log.debug("getSpecificTicket: " + id);
		return ticketRepository.findById(id);
	}
	
	public List<Ticket> getTicketsByClient() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		//log.debug("getTicketsByClient: " + thatname);
		return ticketRepository.findAllByClient(thatname);
	}
	
	public List<Ticket> getTicketsByWorker() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		//log.debug("getTicketsByWorker: " + thatname);
		return ticketRepository.findAllByWorker(thatname);
	}
	
	public List<Ticket> getTicketsByWorkerByStatus(String status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		//log.debug("getTicketsByWorkerbySTATUS: " + thatname + " és " + status);
		return ticketRepository.findAllByWorkerbyStatus(thatname, status);
	}
	
	public List<Ticket> getTicketsByClientByStatus(String status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		log.debug("getTicketsByWorkerbySTATUS: " + thatname + " és " + status);
		return ticketRepository.findAllByClientByStatus(thatname, status);
	}
	
	//gazdátlan
	public List<Ticket> getTicketsNoWorker() {
		return ticketRepository.findAllNoWorker();
	}
	//gazdátlan (no worker - opened) limit 5
	public List<Ticket> getTicketsNoWorkerLimit() {
		//log.debug("getTicketsNoWorker");
		return ticketRepository.findAllNoWorkerLimit(new PageRequest(0,5));
	}

	
	//státusz szerint
	public List<Ticket> getTicketsByStatus(String status) {
		//log.debug("getTicketsByStatus: " + status);
		return ticketRepository.findAllByStatus(status);
	}
	
	//inprogress szerint limit 5
	public List<Ticket> getTicketsByInProgressLimit(String status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		return ticketRepository.findAllByInProgressLimit(status, new PageRequest(0,5));
	}
	
	//by status by worker limit 5
	public List<Ticket> getTicketsByStatusByWorkerLimit(String status) {
		return ticketRepository.findAllByWorkerbyStatusLimit(thatname, status, new PageRequest(0,5));
	}
	
	//by status by user limit 5
	public Object getTicketByStatusByClientLimit(String status) {
		return ticketRepository.findAllByClientByStatusLimit(thatname, status, new PageRequest(0,5));
	}
	
	//by status by worker COUNT
	public int getTicketsByStatusByWorkerCount(String status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		return ticketRepository.findAllByWorkerbyStatusCount(thatname, status);
	}
	//by status by client COUNT
		public int getTicketsByStatusByClientCount(String status) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			thatname = auth.getName();
			return ticketRepository.findAllByClientbyStatusCount(thatname, status);
		}
	//no worker COUNT
		public int getTicketsByNoWorkerCount() {
			return ticketRepository.findAllNoWorkerCount();
		}
	// by worker COUNT
		public int getTicketsByWorkerCount() {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			thatname = auth.getName();
			return ticketRepository.findAllByWorkerCount(thatname);
		}
	// by client COUNT
				public int getTicketsByClientCount() {
					Authentication auth = SecurityContextHolder.getContext().getAuthentication();
					thatname = auth.getName();
					return ticketRepository.findAllByClientCount(thatname);
				}
		
		
	//
	public Boolean idExists(Long id) {
		return ticketRepository.exists(id);
	}

	public void createTicket(Ticket ticket) {
		String sec_user = SecurityContextHolder.getContext().getAuthentication().getName();
		ticket.setStartdate(new Date());
		ticket.setStatus("opened");
		ticket.setClient(userService.findByEmail(sec_user));
		ticketRepository.save(ticket);
		log.info("Új hibajegy elkészítve.");
		log.debug("User: " + ticket.getClient().getName() + " | Email: " + sec_user);
		log.debug("Task: " + ticket.getTask());
	}
	
	public void solveTicket(Long id, String solution) throws Exception {
		if (idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy.");
		log.info("Ticket Módosítás. Ticket ID: " +id);
		Ticket jegy = getSpecificTicket(id);
		jegy.setSolution(solution);
		jegy.setEnddate(new Date());
		jegy.setStatus("closed");
		ticketRepository.save(jegy);
		
	}
	
	public void addWorkerByAdmin(Long id, String worker) throws Exception {
		if (idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy.");
		Ticket jegy = getSpecificTicket(id);
		jegy.setWorker(userService.findByEmail(worker));
		jegy.setStatus("inprogress");
		ticketRepository.save(jegy);
		log.info("Megoldó Módosítás. Ticket ID: " +id+" Megoldó: " +worker);
		
	}
	
	public void ticketAccept(Long id) throws Exception {
		if (idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet elvállalni.");
		Ticket jegy = getSpecificTicket(id);

		jegy.setWorker(userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
		jegy.setStatus("inprogress");
		
		ticketRepository.save(jegy);
		log.info("Elvállalt Hibajegy ID: " +id+ " : " + jegy.getWorker().getName());
		
	}
	
	public void deleteTicket(Long id) {
		ticketRepository.delete(id);
	}
	
	
	//EXCEPTION
		@ExceptionHandler(Exception.class)
		public String exceptionHandler(HttpServletRequest rA, Exception ex, Model model) {
			if (ex.getClass().getName().endsWith("TypeMismatchException") == true) {
				model.addAttribute("errmessage", "Rossz formátumú azonosító. Kérem, szám formátumban adja meg a hibajegy azonosítóját!");
			}
			else {model.addAttribute("errmessage", ex.getMessage());}
			return "exceptionHandler";
		}



		
		
	

}