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
	private final String USER_DELETED_ROLE = "USER_DELETED";
	
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

// DELETE
	public void deleteByEmail(String email) {
		User user = userRepository.findByEmail(email);
		user.setActive(false);
		user.getRoles().clear();
		Role userRole = roleRepository.findByRole(USER_DELETED_ROLE);
		if (userRole != null) {
			user.getRoles().add(userRole);
		} else {
			user.addRoles(USER_DELETED_ROLE);
		}
		user.setDeleted(true);
		userRepository.save(user);
		log.debug("Törölt user: " + email);
		
	}
	
	
//	public void deleteByEmail(String email) {
//		log.debug("Törlendő: " + email);
//		User u = userRepository.findByEmail(email);
//		log.debug("OBJ: " + u.getName() +" "+ u.getRoles());
//		u.getRoles().clear();
//		userRepository.deleteRoles(email);
//		log.debug("OBJ: " + u.getName() +" "+ u.getRoles());
//		userRepository.deleteByEmail(email);
//	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("Belépett User: " + username);
		User user = findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new UserDetailsImpl(user);
	}
	
	//USER KERESÉSEK
	public User findByName(String name) {
		return userRepository.findByName(name);
	}
	public List<User> findAllEditor() {
		return userRepository.findAllEditor();
	}
	public List<User> findAllEditorByDeleted() {
		return userRepository.findAllEditorByDeleted();
	}
	public List<User> findAllUser() {
		return userRepository.findAllUser();
	}
	public List<User> findAllUserByDeleted() {
		return userRepository.findAllUserByDeleted();
	}
	
	//USER LÉTEZIK-E
	public Boolean emailExists(String email) {
		return userRepository.exists(email);
	}
	
	
	//USER MÓDOSÍTÁS
	public void modifyUser(String email, String name, String address, String phone) {
		User user = userRepository.findByEmail(email);
		user.setName(name);
		user.setAddress(address);
		user.setPhone(phone);
		userRepository.save(user);
		log.debug("Felhasználó adatai módosítva: " +user.getName());
	}
	
	//USER REGISZTRÁCIÓ
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
		userToRegister.setDeleted(false);
		
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
	
	public void activateByEmail(String email) {
		User user = userRepository.findByEmail(email);
		if (user.getActive() == true)
			user.setActive(false);
		else user.setActive(true);
		log.debug("Aktív állapot változtatva: " +user.getName()+" felhasználónak erre: "+user.getActive());
		userRepository.save(user);
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
