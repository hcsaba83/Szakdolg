package com.szakdolg.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="users")
public class User {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	@JsonIgnore
	private Long id;
	private String name;
	@JsonIgnore
	private String password;
	@JsonIgnore
	private String phone;
	@JsonIgnore
	private String address;
	private Date regdate;
	@JsonIgnore
	private Integer token;
	private Boolean active;
	private String role;
	@JsonBackReference
	@OneToMany(mappedBy = "user")
	private List<Ticket> tickets;
	
	
	public User() {
	}
	
	public User(Long id, String name, String password, String phone, String address, Date regdate, Integer token,
			Boolean active, String role, List<Ticket> tickets) {
		this.id = id;
		this.name = name;
		this.password = password;
		this.phone = phone;
		this.address = address;
		this.regdate = regdate;
		this.token = token;
		this.active = active;
		this.role = role;
		this.tickets = tickets;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public Integer getToken() {
		return token;
	}
	public void setToken(Integer token) {
		this.token = token;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public List<Ticket> getTickets() {
		return tickets;
	}
	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}
	
	

}
