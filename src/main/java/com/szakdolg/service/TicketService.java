package com.szakdolg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.szakdolg.domain.Ticket;
import com.szakdolg.domain.User;
import com.szakdolg.repository.TicketRepository;

@Service
public class TicketService {
	

	private TicketRepository ticketRepo;
	
	private String thatname;
	
	

	@Autowired
	public void setTicketRepo(TicketRepository ticketRepo) {
		this.ticketRepo = ticketRepo;
	}

	public List<Ticket> getAllTickets() {
		return ticketRepo.findAllByOrderByStartdateAsc();
	}

	public Ticket getSpecificTicket(Long id) {
		return ticketRepo.findById(id);
	}
	
	public List<Ticket> getTicketsByClient() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		System.out.println("getTicketsByClient meghívódott.");
		System.out.println(thatname);
		return ticketRepo.findAllByClient(thatname);
	}
	
	public List<Ticket> getTicketsByWorker() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		System.out.println("getTicketsByWorker meghívódott.");
		return ticketRepo.findAllByWorker(thatname);
	}
	
	public List<Ticket> getTicketsByWorkerByStatus(String status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		System.out.println("getTicketsByWorkerbySTATUS meghívódott.");
		return ticketRepo.findAllByWorkerbyStatus(thatname, status);
	}
	
	
	public List<Ticket> getTicketsNoWorker() {
		System.out.println("getTicketsNoWorker meghívódott.");
		return ticketRepo.findAllNoWorker();
	}
	
	public List<Ticket> getTicketsByStatus(String status) {
		System.out.println("getTicketsByStatus meghívódott.");
		return ticketRepo.findAllByStatus(status);
	}
	
	public Boolean idExists(Long id) {
		return ticketRepo.exists(id);
	}
	

}