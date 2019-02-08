package com.szakdolg.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.security.crypto.codec.Base64;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
public class User {
	
	@Id
	private String email;
	private String name;
	private String password;
	private String phone;
	private String address;
	private Date regdate;
	private String token;
	private Boolean active;
	@JsonBackReference
	@OneToMany(mappedBy = "client")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private List<Ticket> usertickets;
	@JsonBackReference
	@OneToMany(mappedBy = "worker")
	private List<Ticket> workertickets;
	@JsonBackReference
	@ManyToMany (cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	@JoinTable (
			name = "users_roles",
			joinColumns = {@JoinColumn(name="user_id")},
			inverseJoinColumns = {@JoinColumn(name="role_id")}
			)
	private Set<Role> roles = new HashSet<Role>();
	
	
	public User() {
	}
	
	public User(String email, String name, String password, String phone, String address, Date regdate, String token,
			Boolean active, List<Ticket> usertickets, List<Ticket> workertickets, Set<Role> roles) {
		super();
		this.email = email;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.regdate = regdate;
		this.token = token;
		this.active = active;
		this.usertickets = usertickets;
		this.workertickets = workertickets;
		this.roles = roles;
	}


	public String getEmail() {
		return email;
	}
	
	public String getEmailEncoded() {
		byte[] encoded = Base64.encode(email.getBytes());
		String emailencoded = new String(encoded);
		return emailencoded;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void addRoles(String roleName) {
		if (this.roles == null || this.roles.isEmpty())
			this.roles = new HashSet<>();
		this.roles.add(new Role(roleName));
	}
	
	public void deleteRoles() {
		this.roles.clear();
		System.out.println("roles deleted");
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getRegdate() {
		return regdate;
	}
	public void setRegdate(Date regdate) {
		this.regdate = regdate;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}

	public List<Ticket> getUsertickets() {
		return usertickets;
	}

	public void setUsertickets(List<Ticket> usertickets) {
		this.usertickets = usertickets;
	}

	public List<Ticket> getWorkertickets() {
		return workertickets;
	}

	public void setWorkertickets(List<Ticket> workertickets) {
		this.workertickets = workertickets;
	}




}