package com.app.dto;

public class PersonLiteDTO {

	private int id;
	private String role;
	private String name;
	private String surname;
	private String email;
	
	public PersonLiteDTO(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "PersonLiteDTO [id=" + id + ", role=" + role + ", name=" + name + ", surname=" + surname + "]";
	}
	
}
