package com.szakdolg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szakdolg.entity.Ticket;
import com.szakdolg.service.TicketService;

@RestController
public class ApiController {
	
	private TicketService ticketService;
	
	@Autowired
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@RequestMapping("/tickets_rest")
	public List<Ticket> tickets_rest() {
		return ticketService.getAllTickets();
	}

}
