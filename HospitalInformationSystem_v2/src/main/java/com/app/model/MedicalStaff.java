package com.app.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "person")
@DiscriminatorValue("MEDICAL_STAFF")
public class MedicalStaff extends Person implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "education", unique = false, nullable = true)
	private String education;
	
	@Column(name = "specialization", unique = false, nullable = true)
	private String specialization;
	

	public MedicalStaff(){
		super();
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getSpecialization() {
		return specialization;
	}

	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

	
}
