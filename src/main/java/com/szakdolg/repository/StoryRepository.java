package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.Story;

@Repository
public interface StoryRepository extends CrudRepository<Story, Long> {
	
	//Itt felülírtam a CrudRep findAll() függvényét
	//SELECT * FROM STORY
	List<Story> findAll();
	
	//Visszaad egy Story-t
	//SELECT * FROM STORY WHERE posted IN (SELECT max(posted) FROM STORY) LIMIT 1
	//Story findFirstBy(); így már lefut
	Story findFirstByOrderByPostedDesc();


	Story findByTitle(String title);
	
	List<Story> findAllByBloggerNameIgnoreCaseOrderByPostedDesc(String name);

	
	
	

}
