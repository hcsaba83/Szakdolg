package com.szakdolg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.szakdolg.domain.User;
import com.szakdolg.repository.UserRepository;

@Service
public class UserServImp implements UserService, UserDetailsService {
	
	private UserRepository userRepository;

//	// Konstruktor alap DI
	@Autowired
	public UserServImp(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("***** Belépett User: "+username+ " *****");
		User user = findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}
	
	public User findByName(String name) {
		return userRepository.findByName(name);
	}
	
	

}
