package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.szakdolg.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	List<User> findAll();
	
	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 2 OR role_id = 3)", nativeQuery = true)
	List<User> findAllEditor();

	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 4)", nativeQuery = true)
	List<User> findAllEditorByDeleted();
	
	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 1)", nativeQuery = true)
	List<User> findAllUser();
	
	@Query(value= "SELECT * FROM Users WHERE email IN (select user_id FROM Users_Roles where role_id = 5)", nativeQuery = true)
	List<User> findAllUserByDeleted();
	
	@Transactional
	@Modifying
	@Query(value= "DELETE FROM Users_Roles where user_id = :email", nativeQuery = true)
	void deleteRoles(@Param("email") String email);

	@Transactional
	@Modifying
	@Query(value= "DELETE FROM Users where email = :email", nativeQuery = true)
	void deleteByEmail(@Param("email") String email);
	
	
	User findByName(String name);
	
	User findByEmail(String email);

	User findByToken(String code);


	




}
