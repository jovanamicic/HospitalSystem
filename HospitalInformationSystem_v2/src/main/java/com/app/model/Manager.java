package com.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "person")
@DiscriminatorValue("MANAGER")
public class Manager extends Person implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column(name = "role", unique = false, nullable = true)
	private String role;
	
	public Manager(){
		super();
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	@Override
	public String toString() {
		
		return "Manager [role=" + role + "]";
	}
	
}
