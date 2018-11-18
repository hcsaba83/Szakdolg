package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.szakdolg.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	List<User> findAll();
	

	
	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 2 OR role_id = 3)", nativeQuery = true)
	List<User> findAllEditor();
	
	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 1)", nativeQuery = true)
	List<User> findAllUser();
	
	User findByName(String name);
	
	User findByEmail(String email);

	User findByToken(String code);
}
