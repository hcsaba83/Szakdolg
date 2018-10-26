package com.szakdolg.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

//Ezzel az annotációval tudjuk megszabni, hogy milyen szintű felhasználó milyen metódust tudjon használni
@EnableGlobalMethodSecurity(securedEnabled = true)
//Ezzel jelezzük a rendszer felé, hogy ez egy konfigurációs osztály (ez kell a felette lévőhöz)
@Configuration
//A kiterjesztés egy nagyon alapvető Security osztály
public class SecurityConf extends WebSecurityConfigurerAdapter {
	
	@Autowired
	public void configureAuth(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
				.withUser("admin")
				.password("pass")
				.roles("ADMIN")
			.and()
				.withUser("user")
				.password("pass")
				.roles("USER")
			.and()
			.withUser("sa")
			.password("")
			.roles("ADMIN");
	}
	
	@Override
	protected void configure(HttpSecurity httpSec) throws Exception {
		httpSec
				.authorizeRequests()
					.antMatchers(HttpMethod.GET,"/").permitAll()
					.antMatchers("/reg").permitAll()
					.antMatchers("/registration").permitAll()
					.antMatchers("/db/*").permitAll()
					.antMatchers("/tickets").hasRole("USER")
					.antMatchers("/tickets/**").hasRole("ADMIN")
				.and()
					.formLogin().loginPage("/login").permitAll()
					.and()
					.logout().logoutSuccessUrl("/login?logout").permitAll();
	}

}
