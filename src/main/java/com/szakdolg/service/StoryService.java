package com.szakdolg.service;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.szakdolg.domain.Blogger;
import com.szakdolg.domain.Story;
import com.szakdolg.repository.BloggerRepository;
import com.szakdolg.repository.StoryRepository;

@Service
public class StoryService {
	
	private StoryRepository storyRepo;
	private BloggerRepository bloggerRepo;
	
	@Autowired
	public void setStoryRepo(StoryRepository storyRepo) {
		this.storyRepo = storyRepo;
	}
	
	@Autowired
	public void setBloggerRepo(BloggerRepository bloggerRepo) {
		this.bloggerRepo = bloggerRepo;
	}


	public List<Story> getStories() {
		return storyRepo.findAll();
	}
	
	public Story getStory() {
		return storyRepo.findFirstByOrderByPostedDesc();
	}

	@PostConstruct
	public void init() {
		Blogger blogger = new Blogger("Chubacca", 188);
		bloggerRepo.save(blogger);
		Story story = new Story("Napi Belső Tartalom", "Az ég kék, a fű zöld.", new Date(), blogger);
		Story story2 = new Story("Napi Belső Tartalom 2", "Ez az amaz.", new Date(), blogger);
		storyRepo.save(story);
		storyRepo.save(story2);
	}

	public Story getSpecificStory(String title) {
		
		return storyRepo.findByTitle(title);
	}

	public List<Story> getStoriesByBloggerName(String name) {
		return storyRepo.findAllByBloggerNameIgnoreCaseOrderByPostedDesc(name);
	}

}
