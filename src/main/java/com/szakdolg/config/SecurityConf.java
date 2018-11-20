package com.szakdolg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

//Ezzel az annotációval tudjuk megszabni, hogy milyen szintű felhasználó milyen metódust tudjon használni
@EnableGlobalMethodSecurity(securedEnabled = true)
//Ezzel jelezzük a rendszer felé, hogy ez egy konfigurációs osztály (ez kell a felette lévőhöz)
@Configuration
//A kiterjesztés egy nagyon alapvető Security osztály
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsService userService;

	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService);
	}
	
	@Override
	protected void configure(HttpSecurity httpSec) throws Exception {
		httpSec
				.authorizeRequests()
					.antMatchers("/").permitAll()
					.antMatchers("/usertickets", "/usertickets/**").hasAnyAuthority("USER", "ADMIN")
					.antMatchers("/tickets", "/tickets/**").hasAnyAuthority("EDITOR", "ADMIN")
					.antMatchers("/workertickets", "/workertickets/**").hasAnyAuthority("EDITOR", "ADMIN")
					.antMatchers("/alltickets", "/alltickets/**").hasAuthority("ADMIN")
					.antMatchers("/users", "/users/**").hasAuthority("ADMIN")
					.antMatchers("/workers", "/workers/**").hasAuthority("ADMIN")
					.antMatchers("/reg").permitAll()
					.antMatchers("/tickets_rest").permitAll()
					.antMatchers("/registration").permitAll()
					.antMatchers("/activation", "/activation/**").permitAll()
					.anyRequest().authenticated()
				.and()
					.formLogin().loginPage("/login").permitAll()
					.and()
					.logout().logoutSuccessUrl("/login?logout").permitAll();
		
		//httpSec.headers().frameOptions().disable();
		//httpSec.csrf().disable();
	}

}
