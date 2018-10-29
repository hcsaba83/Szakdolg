package com.szakdolg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.szakdolg.domain.Ticket;
import com.szakdolg.repository.TicketRepository;

@Service
public class TicketService {
	

	private TicketRepository ticketRepo;
	
	private String thatname;
	
	

	@Autowired
	public void setTicketRepo(TicketRepository ticketRepo) {
		this.ticketRepo = ticketRepo;
	}

	public List<Ticket> getTickets() {
		return ticketRepo.findAllByOrderByStartdateAsc();
	}

	public Object getSpecificTicket(Long id) {
		return ticketRepo.findById(id);
	}
	
	public List<Ticket> getTicketsByName() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		thatname = auth.getName();
		System.out.println("getTicketsByName meghívódott.");
		System.out.println(thatname);
		return ticketRepo.findAllByClient(thatname);
	}
	
	public Boolean idExists(Long id) {
		return ticketRepo.exists(id);
	}
	

}
