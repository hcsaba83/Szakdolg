package com.szakdolg.entity;

import java.util.Date;

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
	User client;
	@ManyToOne
	User worker;
	String task;
	Date startdate;
	Date enddate;
	@Column(columnDefinition="varchar(50) default 'opened'")
	String status;
	String solution;
	
	public Ticket() {
	}
	
	public User getWorker() {
		return worker;
	}
	public void setWorker(User worker) {
		this.worker = worker;
	}
	public User getClient() {
		return client;
	}
	public void setClient(User client) {
		this.client = client;
	}
	public void setTask(String task) {
		this.task = task;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
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

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", client=" + client + ", worker=" + worker + ", task=" + task + ", startdate="
				+ startdate + ", enddate=" + enddate + ", status=" + status + ", solution=" + solution + "]";
	}
	
	

}