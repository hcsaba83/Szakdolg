package com.szakdolg.domain;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="tickets")
public class Ticket {
	
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Id
	Long id;
	@ManyToOne
	User user;
	@ManyToOne
	Worker worker;
	String task;
	Date startdate;
	Date enddate;
	Date solutiondate;
	@Column(columnDefinition="varchar(50) default 'Elvégzendő'")
	String status;
	String solution;
	
	public Ticket() {
	}
	
	
	
	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Date getSolutiondate() {
		return solutiondate;
	}

	public void setSolutiondate(Date solutiondate) {
		this.solutiondate = solutiondate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSolution() {
		return solution;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getTask() {
		return task;
	}
	public void setTas(String task) {
		this.task = task;
	}
	public Date getStartdate() {
		return startdate;
	}
	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}
	
	

}
