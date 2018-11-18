package com.szakdolg.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import com.szakdolg.entity.Role;
import com.szakdolg.entity.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

//Jól belenyúlunk a motorháztető alá, megmondjuk a Spring Securitynak, hogy az adatbázisból vesse össze
//az adatokat 

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = -4772871954166589912L;
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private User user;

	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
			log.debug("Kiosztott Role: "+authorities);
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		byte[] decoded = Base64.decode(user.getPassword().getBytes());
		String pw = new String(decoded);
		return pw;
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return user.getActive();
	}

}
