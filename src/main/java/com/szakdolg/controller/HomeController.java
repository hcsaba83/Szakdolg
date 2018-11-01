package com.szakdolg.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.szakdolg.service.UserServImp;

@Controller
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	private TicketService ticketService;
	UserDetailsImpl userDetails;
	private TicketRepository ticketRepository;
	private UserRepository userRepository;
	private UserServImp userService;

	
	@Autowired
	public UserDetailsImpl getUserDetails() {
		return userDetails;
	}

	@Autowired
	public UserServImp getUserService() {
		return userService;
	}

	// EZZEL KEZDJ MÁR VALAMIT! VAGY LEGYEN SETTER VAGY .... VAGY MiND ÍGY LEGYEN
	public HomeController(TicketRepository ticketRepository, UserRepository userRepository, UserServImp userService) {
		this.ticketRepository = ticketRepository;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	@Autowired
	public TicketRepository getTicketRepository() {
		return ticketRepository;
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
	public String searchForTicket(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítószámú hibajegy.");
		model.addAttribute("pageTitle", "Ticket részletei");
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticket";
	}
	
	//@Secured("ROLE_ADMIN")
	@RequestMapping("/usertickets/{id}")
	public String searchForUserTicket(@PathVariable(value="id") Long id, Model model) throws Exception {
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
		log.debug(user.getEmail());
		log.debug(user.getPassword());
		return("/auth/login");
	}
	
	@PostMapping("/usertickets/new")
	public String postNewTicket(@ModelAttribute Ticket ticket) {
		System.out.println("Új Ticket");
		String ez;
		System.out.println(ez = SecurityContextHolder.getContext().getAuthentication().getName());
		System.out.println(ticket.getTask());
		System.out.println(new Date());
		ticket.setStartdate(new Date());
		ticket.setClient(userService.findByEmail(ez));
		ticketRepository.save(ticket);
		log.info("New ticket sent.");
		log.debug(ticket.getTask());
		log.debug(ticket.getClient().getName());
		return("/usertickets");
	}
	
	@RequestMapping("/newticket")
	public String newTicket(Model model) {
		model.addAttribute("ticket", new Ticket());
		return "newticket";
	}
	
	@PostMapping("/tickets/edit/{id}")
	public String editTicket(@PathVariable(value="id") Long id, @ModelAttribute("solution") String solution) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Mi a péklapát?");
		System.out.println("Ticket Editoring....");
		Ticket jegy = ticketService.getSpecificTicket(id);
		jegy.setEnddate(new Date());
		jegy.setSolution(solution);
		ticketRepository.save(jegy);
		System.out.println(jegy.getId());
		System.out.println(jegy.getTask());
		System.out.println(jegy.getSolution());
		System.out.println(jegy.getClient().getRoles());
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
