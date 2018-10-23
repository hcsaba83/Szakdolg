package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.Blogger;

@Repository
public interface BloggerRepository extends CrudRepository<Blogger, Long> {
	//Itt felülírtam a CrudRep findAll() függvényét
	List<Blogger> findAll();

}
