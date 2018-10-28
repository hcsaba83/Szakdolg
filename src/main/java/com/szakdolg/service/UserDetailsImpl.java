package com.szakdolg.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.szakdolg.domain.Role;
import com.szakdolg.domain.User;

//Jól belenyúlunk a motorháztető alá, megmondjuk a Spring Securitynak, hogy az adatbázisból vesse össze
//az adatokat 
public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = -4772871954166589912L;
	
	private User user;

	//DI
	public UserDetailsImpl(User user) {
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		Set<Role> roles = user.getRoles();
		for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
			System.out.println("Role: "+authorities);
		}
		return authorities;
	}

	//Jelszó átadása a Spring Securitynak és a többit......
	@Override
	public String getPassword() {
		System.out.print("pw for Security: ");
		System.out.println(user.getPassword() );
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		System.out.print("name for Security: ");
		System.out.println(user.getName() );
		return user.getName();
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
		return true;
	}

}
