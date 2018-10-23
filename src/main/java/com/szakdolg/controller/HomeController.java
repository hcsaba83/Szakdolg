package com.szakdolg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.szakdolg.service.StoryService;
import com.szakdolg.service.TicketService;

@Controller
public class HomeController {
	
	StoryService storyService;
	TicketService ticketService;
	
	@Autowired
	public void setStoryService(StoryService storyService) {
		this.storyService = storyService;
	}
	
	@Autowired
	public void setStoryService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@RequestMapping("/")
	public String stories(Model model) {
		model.addAttribute("pageTitle", "Hibabejelentő oldal");
		model.addAttribute("stories", storyService.getStories());
		return "stories";
	}
	
	@RequestMapping("/tickets")
	public String tickets(Model model) {
		model.addAttribute("pageTitle", "TICKETS");
		model.addAttribute("tickets", ticketService.getTickets());
		return "tickets";
	}
	
	@RequestMapping("/tickets/{id}")
	public String searchForStory(@PathVariable(value="id") Long id, Model model) throws Exception {
		if (id == null)
			throw new Exception("Nincs ilyen bejelentés!");
		//ha RestController lenne... és persze akkor nincs model
		//return storyService.getSpecificStory(title);
		model.addAttribute("pageTitle", "Ticket részletei");
		model.addAttribute("ticket", ticketService.getSpecificTicket(id));
		return "ticket";
	}
	

	// RestController miatt ezek kikommentelve
	
//	@RequestMapping("/story")
//	public String story(Model model) {
//		model.addAttribute("pageTitle", "Hibabejelentő oldal");
//		model.addAttribute("story", storyService.getStory());
//		return "story";
//	}
//	
//	@RequestMapping("/title/{title}")
//	public String searchForStory(@PathVariable(value="title") String title, Model model) throws Exception {
//		if (title == null)
//			throw new Exception("Nincs ilyen című sztori!");
//		//ha RestController lenne... és persze akkor nincs model
//		//return storyService.getSpecificStory(title);
//		model.addAttribute("pageTitle", "Hibabejelentő oldal");
//		model.addAttribute("story", storyService.getSpecificStory(title));
//		return "story";
//	}
//	
//	@RequestMapping("/user/{id}")
//	public String searchUser(@PathVariable(value="id") String id) throws Exception {
//		if (id == null)
//			throw new Exception("Nincs ilyen felhasználónk!");
//		return "user";
//	}
	
//	@ExceptionHandler(Exception.class)
//	public String exceptionHandler(ServletWebRequest rA, Exception ex, Model model) {
//		model.addAttribute("errmessage", ex.getMessage());
//		return "exceptionHandler";
//	}
	
	
	

	

}
