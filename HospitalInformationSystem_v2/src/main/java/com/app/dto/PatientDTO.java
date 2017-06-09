package com.app.dto;

public class PatientDTO {
	
	private String name;
	private String surname;
	private String personalID;
	private String email;
	private String birthday;
	private String gender;
	private String country;
	private String city;
	private String zipCode;
	private String street;
	private String number;
	private int doctor;
	private String username;
	
	public PatientDTO(){}

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

	public String getPersonalID() {
		return personalID;
	}

	public void setPersonalID(String personalID) {
		this.personalID = personalID;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getDoctor() {
		return doctor;
	}

	public void setDoctor(int doctor) {
		this.doctor = doctor;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "PatientDTO [name=" + name + ", surname=" + surname + ", personalID=" + personalID + ", email=" + email
				+ ", birthday=" + birthday + ", gender=" + gender + ", country=" + country + ", city=" + city
				+ ", zipCode=" + zipCode + ", street=" + street + ", number=" + number + ", doctor=" + doctor
				+ ", username=" + username + "]";
	}
	
}
