package com.szakdolg.service;

import com.szakdolg.entity.User;

public interface UserService {
	
	public String registerUser(User user);

	public User findByEmail(String email);
	
}
