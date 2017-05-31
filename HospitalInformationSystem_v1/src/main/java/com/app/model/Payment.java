package com.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "payment")
public class Payment implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "payment_id", unique = true, nullable = false)
	private int id;

	@Column(name = "date", unique = false, nullable = false)
	private Date date;

	@Column(name = "recipient", unique = false, nullable = false)
	private String recipient;
	
	@Column(name = "account", unique = false, nullable = false)
	private String account;
	
	@Column(name = "amount", unique = false, nullable = false)
	private Double amount;
	
	@Column(name = "currency", unique = false, nullable = false)
	private String currency;
	
	@ManyToOne
	@JoinColumn(name="manager_id", referencedColumnName = "person_id", nullable = false)
	private Person manager;
	
	public Payment(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public Person getManager() {
		return manager;
	}

	public void setManager(Person manager) {
		this.manager = manager;
	}

	
}
