package com.szakdolg.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.szakdolg.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

	List<User> findAll();
}
