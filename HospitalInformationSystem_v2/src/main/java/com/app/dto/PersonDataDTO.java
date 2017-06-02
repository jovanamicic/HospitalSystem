package com.app.dto;

public class PersonDataDTO {

	private String name;
	private String surname;
	private String username;
	private String photo;
	private String email;
	
	public PersonDataDTO() {
		super();
	}
	
	
	public PersonDataDTO(String name, String surname, String username, String photo, String email) {
		super();
		this.name = name;
		this.surname = surname;
		this.username = username;
		this.photo = photo;
		this.email = email;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
