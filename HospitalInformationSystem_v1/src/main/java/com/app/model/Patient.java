package com.app.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "person")
@DiscriminatorValue("PATIENT")
public class Patient extends Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "birthday", unique = false, nullable = true)
	private Date birthday;
	
	@Column(name = "gender", unique = false, nullable = true)
	private String gender;
	
	@ManyToOne
	@JoinColumn(name="address", referencedColumnName = "address_id", nullable = true)
	private Address address;
	
	@ManyToOne
	@JoinColumn(name="chosen_doctor", referencedColumnName = "person_id", nullable = true)
	private MedicalStaff chosenDoctor;
	
	public Patient(){
		super();
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public MedicalStaff getChosenDoctor() {
		return chosenDoctor;
	}

	public void setChosenDoctor(MedicalStaff chosenDoctor) {
		this.chosenDoctor = chosenDoctor;
	}


}
