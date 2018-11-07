package com.szakdolg.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private UserServImp userService;

	@Autowired
	public UserDetailsImpl getUserDetails() {
		return userDetails;
	}

	@Autowired
	public UserServImp getUserService() {
		return userService;
	}

	public HomeController(TicketRepository ticketRepository, UserRepository userRepository, UserServImp userService) {
		this.ticketRepository = ticketRepository;
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
		model.addAttribute("tickets", ticketService.getTicketsNoWorker());
		return "tickets";
	}
	
	@RequestMapping("/alltickets")
	public String allTickets(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getAllTickets());
		return "tickets";
	}
	
	@RequestMapping("/alltickets/{status}")
	public String allTicketsByStatus(@PathVariable(value="status") String status, Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByStatus(status));
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
		model.addAttribute("pageTitle", "WORKER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByWorker());
		return "tickets";
	}
	
	@RequestMapping("/workertickets/{status}")
	public String workerTicketsClosed(@PathVariable(value="status") String status, Model model)  throws Exception  {
		if (ticketService.getTicketsByWorkerByStatus(status) == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "WORKER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByWorkerByStatus(status));
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
		log.info("Új hibajegy elkészítve.");
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
			throw new Exception("Nincs ilyen azonosítójú hibajegy.");
		log.info("Ticket Módosítás. Ticket ID: " +id);
		Ticket jegy = ticketService.getSpecificTicket(id);
		jegy.setEnddate(new Date());
		jegy.setWorker(userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
		jegy.setSolution(solution);
		jegy.setStatus("closed");
		ticketRepository.save(jegy);
		return("redirect:/tickets");
	}
	
	@RequestMapping("/vallalom/{id}")
	public String vallalTicket(@PathVariable(value="id") Long id) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet elvállalni.");
		
		Ticket jegy = ticketService.getSpecificTicket(id);
		
		jegy.setWorker(userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
		jegy.setStatus("inprogress");
		
		ticketRepository.save(jegy);
		log.info("Hibajegy elvállalva. Hibajegy ID: " +id+ " Vállalta: " + jegy.getWorker().getName());

		return("redirect:/tickets");
	}
	
	@RequestMapping("/tickets/{id}/editor")
	public String editor(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet szerkeszteni.");
		
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticketeditor";
	}
	
	@ExceptionHandler(Exception.class)
	public String exceptionHandler(HttpServletRequest rA, Exception ex, Model model) {
		model.addAttribute("errmessage", ex.getMessage());
		return "exceptionHandler";
	}
}