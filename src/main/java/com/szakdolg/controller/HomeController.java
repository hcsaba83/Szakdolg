package com.szakdolg.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.szakdolg.domain.Ticket;
import com.szakdolg.domain.User;
import com.szakdolg.repository.TicketRepository;
import com.szakdolg.repository.UserRepository;
import com.szakdolg.service.TicketService;
import com.szakdolg.service.UserDetailsImpl;

@Controller
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TicketService ticketService;
	private UserDetailsImpl userDetails;
	TicketRepository ticketRepository;
	UserRepository userRepository;

	

	public HomeController(TicketRepository ticketRepository, UserRepository userRepository) {
		super();
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
	}

	@Autowired
	public TicketRepository getTicketRepository() {
		return ticketRepository;
	}

	@Autowired
	public UserDetailsImpl getUserDetailsImpl() {
		return userDetails;
	}

	@Autowired
	public void setStoryService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@RequestMapping("/")
	public String stories() {
		return "index";
	}
	
	//@Secured("ROLE_USER")
	@RequestMapping("/tickets")
	public String tickets(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTickets());
		return "tickets";
	}
	
	@RequestMapping("/usertickets")
	public String userTickets(Model model)  throws Exception  {
		if (ticketService.getTicketsByClient() == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "USER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByClient());
		return "tickets";
	}
	
	
	@RequestMapping("/workertickets")
	public String workerTickets(Model model)  throws Exception  {
		if (ticketService.getTicketsByWorker() == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "USER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByWorker());
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
	
	@RequestMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}

	//@RequestMapping("/reg", method = RequestMethod.POST)
	@PostMapping("/reg")
	public String greetingSubmit(@ModelAttribute User user) {
		System.out.println("Új User");
		log.info("Uj User Regisztracio.");
		log.debug(user.getName());
		log.debug(user.getPassword());
		return("/auth/login");
	}
	
	@PostMapping("/tickets/edit/{id}")
	public String editTicket(@PathVariable(value="id") Long id, @ModelAttribute("solution") String solution) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Mi a péklapát?");
		System.out.println("Ticket Editoring....");
		Ticket jegy = ticketService.getSpecificTicket(id);
		System.out.println(jegy.getId());
		System.out.println(jegy.getSolution());
		System.out.println(jegy.toString());
		jegy.setSolution(solution);
		ticketRepository.save(jegy);
		System.out.println(jegy.getId());
		System.out.println(jegy.getSolution());
		System.out.println(jegy.getTask());
		return("tickets");
	}
	
	
	@RequestMapping("/tickets/{id}/editor")
	public String editor(@PathVariable(value="id") Long id, Model model) {
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		System.out.println("ticketeditor request mapping");
		return "ticketeditor";
	}
	
	
	
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(HttpServletRequest rA, Exception ex, Model model) {
		model.addAttribute("errmessage", ex.getMessage());
		return "exceptionHandler";
	}

}
