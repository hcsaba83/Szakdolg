package com.szakdolg.service;

import org.springframework.stereotype.Service;

import com.szakdolg.domain.User;

public interface UserService {

	public User findByEmail(String email);
}
