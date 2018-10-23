package com.szakdolg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.szakdolg.domain.Story;
import com.szakdolg.domain.Ticket;
import com.szakdolg.service.StoryService;
import com.szakdolg.service.TicketService;

@RestController
public class ApiController {
	
	private StoryService storyService;
	private TicketService ticketService;
	
	@Autowired
	public void setTicketService(TicketService ticketService) {
		this.ticketService = ticketService;
	}

	@Autowired
	public void setStoryService(StoryService storyService) {
		this.storyService = storyService;
	}

	@RequestMapping("/story")
	public Story story() {
		return storyService.getStory();
	}
	
	@RequestMapping("/title/{title}")
	public String searchByTitle(@PathVariable(value="title") String title) {

		return storyService.getSpecificStory(title).toString();
	}
	
	@RequestMapping("/stories/{name}")
	public List<Story> searchStoriesByBlogger(@PathVariable(value="name") String name) {

		return storyService.getStoriesByBloggerName(name);
	}
	
	@RequestMapping("/tickets_rest")
	public List<Ticket> tickets_rest() {
		return ticketService.getTickets();
	}

}
