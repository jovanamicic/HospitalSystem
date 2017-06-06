package com.app.dto;

import java.util.Date;

import com.app.model.Patient;

public class ExaminationDTO {

	private int patientID;
	private String symptons;
	private String diagnosis;
	private String therapy;
	private boolean newEx;
	private Patient patient;
	private Date date;
	private String name;
	private int id;

	public ExaminationDTO() {

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	public boolean isNewEx() {
		return newEx;
	}

	public void setNewEx(boolean newEx) {
		this.newEx = newEx;
	}

	public int getPatientID() {
		return patientID;
	}

	public void setPatientID(int patientID) {
		this.patientID = patientID;
	}

	public String getSymptons() {
		return symptons;
	}

	public void setSymptons(String symptons) {
		this.symptons = symptons;
	}

	public String getDiagnosis() {
		return diagnosis;
	}

	public void setDiagnosis(String diagnosis) {
		this.diagnosis = diagnosis;
	}

	public String getTherapy() {
		return therapy;
	}

	public void setTherapy(String therapy) {
		this.therapy = therapy;
	}
	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "ExaminationDTO [patientID=" + patientID + ", symptons=" + symptons + ", diagnosis=" + diagnosis
				+ ", therapy=" + therapy + "]";
	}

}
