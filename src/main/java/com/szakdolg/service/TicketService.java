package com.szakdolg.service;


import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.szakdolg.domain.Ticket;
import com.szakdolg.repository.TicketRepository;

@Service
public class TicketService {
	
	private TicketRepository ticketRepo;
	
	@Autowired
	public void setTicketRepo(TicketRepository ticketRepo) {
		this.ticketRepo = ticketRepo;
	}
	
	public List<Ticket> getTickets() {
		return ticketRepo.findAllByOrderByDateAsc();
	}

	public Object getSpecificTicket(Long id) {
		return ticketRepo.findById(id);
	}
	

}
