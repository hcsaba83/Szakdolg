package com.szakdolg.service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.szakdolg.entity.Role;
import com.szakdolg.entity.User;
import com.szakdolg.repository.RoleRepository;
import com.szakdolg.repository.TicketRepository;
import com.szakdolg.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
	
	private final String USER_ROLE = "USER";
	private final String ADMIN_ROLE = "ADMIN";
	
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private EmailService emailService;
	private TicketRepository ticketRepository;
	
	@Autowired
	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}
	
	@Autowired
	public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}

	private final Logger log = LoggerFactory.getLogger(this.getClass());


	@Override
	public User findByEmail(String email) {
		return userRepository.findByEmail(email);
	}


	public void deleteByEmail(String email) {
		log.debug("Törlendő: " + email);
		User u = userRepository.findByEmail(email);
		log.debug("OBJ: " + u.getName() +" "+ u.getRoles());
		u.getRoles().clear();
		userRepository.deleteRoles(email);
		log.debug("OBJ: " + u.getName() +" "+ u.getRoles());
		userRepository.deleteByEmail(email);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Belépett User: " + username);
		User user = findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}
	
	public User findByName(String name) {
		return userRepository.findByName(name);
	}
	
	public List<User> findAllEditor() {
		//log.debug("findAllEditor");
		return userRepository.findAllEditor();
	}
	
	public List<User> findAllUser() {
		//log.debug("findAllUser");
		//System.out.println(userRepository.findAllUser());
		return userRepository.findAllUser();
	}

	
	public Boolean emailExists(String email) {
		return userRepository.exists(email);
	}
	

	@Override
	public String registerUser(User userToRegister) {
		
		User userCheck = userRepository.findByEmail(userToRegister.getEmail());
		
		if (userCheck != null) 
			return "alreadyExists";
		
		byte[] encoded = Base64.encode(userToRegister.getPassword().getBytes());
		String pw = new String(encoded);
		log.debug(userToRegister.getPassword() +" "+ pw);
		
		userToRegister.setRegdate(new Date());
		userToRegister.setToken(generateKey());
		userToRegister.setPassword(pw);
		userToRegister.setActive(false);
		
		Role userRole = roleRepository.findByRole(USER_ROLE);
		if (userRole != null) {
			userToRegister.getRoles().add(userRole);
		} else {
			userToRegister.addRoles("USER_ROLE");
		}
		userRepository.save(userToRegister);
		log.debug("Sikeres registráció! " + userToRegister.getName());
		//emailService.sendMessage(userToRegister.getEmail(), userToRegister.getName(), userToRegister.getToken());
		return "ok";	
	}

	public String userActivation(String code) {
		User user = userRepository.findByToken(code);
		if (user == null)
			return "noresult";
		user.setActive(true);
		user.setToken("");
		log.debug("Sikeres aktiváció! " +user.getName());
		userRepository.save(user);
		return "ok";
	}
		
	//Random String
	public String generateKey()
    {
		Random random = new Random();
		char[] word = new char[16]; 
		for (int j = 0; j < word.length; j++) {
			word[j] = (char) ('a' + random.nextInt(26));
		}
		String toReturn = new String(word);
		log.debug("Random kód: " + toReturn);
		return new String(word);
    }



}
