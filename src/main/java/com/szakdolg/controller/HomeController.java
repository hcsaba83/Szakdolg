package com.szakdolg.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.szakdolg.entity.Ticket;
import com.szakdolg.entity.User;

import com.szakdolg.service.TicketService;
import com.szakdolg.service.UserServiceImpl;

@Controller
public class HomeController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private TicketService ticketService;
	private UserServiceImpl userService;

	@Autowired
	public void setUserService(UserServiceImpl userService) {
		this.userService = userService;
	}
	@Autowired
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}
	
	@RequestMapping("/")
	public String mainPage(Model model) {
		model.addAttribute("user", userService.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
		model.addAttribute("ticketsinprogress", ticketService.getTicketsByStatusByWorkerLimit("inprogress"));
		model.addAttribute("ticketsclosed", ticketService.getTicketsByStatusByWorkerLimit("closed"));
		model.addAttribute("ticketsopened", ticketService.getTicketsNoWorkerLimit());
		model.addAttribute("ticketsbyopened_num", Integer.toString(ticketService.getTicketsByStatusByWorkerCount("inprogress")));
		model.addAttribute("ticketsbyclosed_num", Integer.toString(ticketService.getTicketsByStatusByWorkerCount("closed")));
		model.addAttribute("ticketsopened_num", Integer.toString(ticketService.getTicketsByNoWorkerCount()));
		model.addAttribute("ticketsbyworker_num", Integer.toString(ticketService.getTicketsByWorkerCount()));
		return "index";
	}
	
	@RequestMapping("/tickets")
	public String ticketsWithNoWorker(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsNoWorker());
		model.addAttribute("status", "tickets");
		return "tickets";
	}
	
	@RequestMapping("/alltickets")
	public String allTickets(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getAllTickets());
		model.addAttribute("status", "alltickets");
		return "tickets";
	}
	
	@RequestMapping("/alltickets/{status}")
	public String allTicketsByStatus(@PathVariable(value="status") String status, Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByStatus(status));
		model.addAttribute("status", status);
		model.addAttribute("all", "true");
		return "tickets";
	}
	
	@RequestMapping("/usertickets")
	public String userTickets(Model model)  throws Exception  {
		if (ticketService.getTicketsByClient() == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "USER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByClient());
		model.addAttribute("status", "allusertickets");
		return "tickets";
	}
	
	@RequestMapping(value = "/usertickets/{status}", method = RequestMethod.GET)
	public String userTicketsByStatus(@PathVariable(value="status") String status, Model model)  throws Exception  {
		if (ticketService.getTicketsByClientByStatus(status) == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "USER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByClientByStatus(status));
		model.addAttribute("status", status);
		model.addAttribute("all", "false");
		return "tickets";
	}

	@RequestMapping("/workertickets")
	public String workerTicketsByWorker(Model model)  throws Exception  {
		if (ticketService.getTicketsByWorker() == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "WORKER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByWorker());
		model.addAttribute("status", "allworkertickets");
		return "tickets";
	}
	
	@RequestMapping("/workertickets/{status}")
	public String workerTicketsByStatus(@PathVariable(value="status") String status, Model model)  throws Exception  {
		if (ticketService.getTicketsByWorkerByStatus(status) == null)
			throw new Exception("Nincs egy darab hibajegy sem.");
		model.addAttribute("pageTitle", "WORKER'S TICKETS");
		model.addAttribute("tickets", ticketService.getTicketsByWorkerByStatus(status));
		model.addAttribute("status", status);
		model.addAttribute("all", "false");
		return "tickets";
	}

	@RequestMapping("/tickets/{id}")
	public String searchForTicket(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítószámú hibajegy: ( "+id+ " ).");
		model.addAttribute("pageTitle", "Ticket részletei");
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		model.addAttribute("editors", userService.findAllEditor());
		return "ticket";
	}
	
	@RequestMapping("/usertickets/{id}")
	public String searchForUserTicket(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítószámú hibajegy: ( "+id+ " ).");
		model.addAttribute("pageTitle", "Ticket részletei");
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticket";
	}
	
	//REGISZTRÁCIÓ ****************************************************************
	@RequestMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	@PostMapping("/reg")
	public String greetingSubmit(@ModelAttribute User user) {
		userService.registerUser(user);
		return("/auth/login");
	}
	
	// AKTIVÁCIÓ ****************************************************************
	 @RequestMapping(path = "/activation/{code}", method = RequestMethod.GET)
	    public String activation(@PathVariable("code") String code, HttpServletResponse response) {
		userService.userActivation(code);
		return "auth/login";
	 }
	
	// ÚJ HIBAJEGY ****************************************************************
	@PostMapping("/usertickets/new")
	public String postNewTicket(@ModelAttribute Ticket ticket) {
		ticketService.createTicket(ticket);
		return("redirect:/usertickets");
	}
	@RequestMapping("/newticket")
	public String newTicket(Model model) {
		model.addAttribute("ticket", new Ticket());
		return "newticket";
	}
	
	//Hibajegy MEGOLDÁS ****************************************************************
	@PostMapping("/tickets/edit/{id}")
	public String solveTicket(@PathVariable(value="id") Long id, @ModelAttribute("solution") String solution) throws Exception {
		ticketService.solveTicket(id, solution);
		return("redirect:/tickets/{id}");
	}
	
	@RequestMapping("/tickets/{id}/editor")
	public String editor(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet szerkeszteni.");
		
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticketeditor";
	}
	
	//Hibajegy TÖRLÉS ****************************************************************
	@RequestMapping("/tickets/{id}/delete")
	public String deleteTicket(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (ticketService.idExists(id) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet törölni.");
		
		ticketService.deleteTicket(id);
		return ("redirect:/tickets");
	}
	
	
	//Megoldó hozzáadás ADMIN által ****************************************************************
	@PostMapping("/tickets/editworker/{id}")
	public String addWorkerByAdmin(@PathVariable(value="id") Long id, @ModelAttribute("worker") String worker) throws Exception {
		ticketService.addWorkerByAdmin(id, worker);
		return("redirect:/tickets/{id}");
	}
	
	//Megoldó hozzáadás saját részre ****************************************************************
	@RequestMapping("/vallalom/{id}")
	public String ticketAccept(@PathVariable(value="id") Long id) throws Exception {
		ticketService.ticketAccept(id);
		return("redirect:/tickets");
	}
	
	//USER LISTÁK
	
	@RequestMapping("/users")
	public String usersByClients(Model model) {
		model.addAttribute("users", userService.findAllUser());
		return "users";
	}
	
	@RequestMapping("/usersdeleted")
	public String usersByClientsByDeleted(Model model) {
		model.addAttribute("users", userService.findAllUserByDeleted());
		return "users";
	}
	
	@RequestMapping("/workers")
	public String usersByWorkers(Model model) {
		model.addAttribute("users", userService.findAllEditor());
		return "users";
	}
	
	@RequestMapping("/workersdeleted")
	public String usersByWorkersByDeleted(Model model) {
		model.addAttribute("users", userService.findAllEditorByDeleted());
		return "users";
	}
	
//	@RequestMapping("/users/{email}")
//	public String searchForUser(@PathVariable(value="email") String email, Model model) throws Exception {
//		if (userService.emailExists(email) == false)
//			throw new Exception("Nincs ilyen azonosítójú felhasználó");
//		log.info("email: " +email);
//		model.addAttribute("pageTitle", "Felhasználó részletes adatai");
//		model.addAttribute("user", userService.findByEmail(email));
//		return "user";
//	}
	
	@RequestMapping("/users/{id}")
	public String searchForUser(@PathVariable(value="id") String id, Model model) throws Exception {
		byte[] decoded = Base64.decode(id.getBytes());
		String email = new String(decoded);
		if (userService.emailExists(email) == false)
			throw new Exception("Nincs ilyen azonosítójú felhasználó");
		//log.debug("email: " +email);
		model.addAttribute("pageTitle", "Felhasználó részletes adatai");
		model.addAttribute("user", userService.findByEmail(email));
		return "user";
	}
	
	//USER TÖRLÉS ****************************************************************
	@RequestMapping("/users/{id}/delete")
	public String deleteUser(@PathVariable(value="id") String id, Model model) throws Exception {
		byte[] decoded = Base64.decode(id.getBytes());
		String email = new String(decoded);
		if (userService.emailExists(email) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet törölni.");
		
		userService.deleteByEmail(email);
		return ("redirect:/users");
	}
	
	//USER AKTIVÁL ****************************************************************
	@RequestMapping("/users/{id}/active")
	public String activeteUser(@PathVariable(value="id") String id, Model model) throws Exception {
		byte[] decoded = Base64.decode(id.getBytes());
		String email = new String(decoded);
		if (userService.emailExists(email) == false)
			throw new Exception("Nincs ilyen azonosítójú hibajegy, ezért nem is lehet törölni.");
		
		userService.activateByEmail(email);
		return ("redirect:/users");
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