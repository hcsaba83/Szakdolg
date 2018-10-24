package com.szakdolg.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.szakdolg.service.TicketService;

@Controller
public class HomeController {
	
	TicketService ticketService;
	
	@Autowired
	public void setStoryService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@RequestMapping("/")
	public String stories(Model model) {
		model.addAttribute("pageTitle", "Hibabejelentő oldal");
		model.addAttribute("tickets", ticketService.getTickets());
		return "tickets";
	}
	
	//@Secured("ROLE_USER")
	@RequestMapping("/tickets")
	public String tickets(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTickets());
		return "tickets";
	}
	
	//@Secured("ROLE_ADMIN")
	@RequestMapping("/tickets/{id}")
	public String searchForStory(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítószámú hibajegy.");
		model.addAttribute("pageTitle", "Ticket részletei");
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticket";
	}
	
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(HttpServletRequest rA, Exception ex, Model model) {
		model.addAttribute("errmessage", ex.getMessage());
		return "exceptionHandler";
	}
	
	
	

	

}
