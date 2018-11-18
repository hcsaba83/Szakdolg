package com.szakdolg.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	
	@Value("${spring.mail.username}") // application.properties-ből
	private String MESSAGE_FROM;
	
	private JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}

	public void sendMessage(String email, String name, String code) {
		SimpleMailMessage message = null;
		
		try {
			 message = new SimpleMailMessage();
			 message.setFrom(MESSAGE_FROM);
			 message.setTo(email);
			 message.setSubject("SZAKDOLG - Sikeresen regisztráció");
			 message.setText("Kedves " + name + "! \n \n Köszönjük, hogy regisztrált oldalunkra! \n \n "
			 		+ "Az alábbi linkre kattintva tudja aktiválni regisztrációját: \n \n"
			 		+ "http://localhost:8080/activation/"+code+ "\n \n"
			 		+ "Amennyiben nem Ön regisztrált oldalunkra, úgy e levelet tekintse tárgytalannak. \n \n"
			 		+ "Tisztelettel: Horváth László Csaba - uu74p5");
			 
			 
			 javaMailSender.send(message);
			 log.debug("Email küldés sikeres ide: " + email);
		} catch (Exception e) {
			log.error("Hiba az email küldésekor ide: " + email + " " + e);
		}
	}
	
	
	
}
