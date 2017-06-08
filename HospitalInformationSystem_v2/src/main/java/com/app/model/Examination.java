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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "examination")
public class Examination implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "examination_id", unique = true, nullable = false)
	private int id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "date", unique = false, nullable = false)
	private Date date;
	
	@Column(name = "passed", unique = false, nullable = false)
	private boolean passed;
	
	@Column(name = "new", unique = false, nullable = false)
	private boolean newEx;
	
	@Column(name = "symptons", unique = false, nullable = true)
	private String symptons;
	
	@Column(name = "diagnosis", unique = false, nullable = true)
	private String diagnosis;

	@Column(name = "therapy", unique = false, nullable = true)
	private String therapy;
	
	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "person_id", nullable = false)
	private Person doctor;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "record_id", referencedColumnName = "record_id", nullable = false)
	private Record record;

	public Examination(){this.newEx = true;}

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

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
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

	public Person getDoctor() {
		return doctor;
	}

	public void setDoctor(Person doctor) {
		this.doctor = doctor;
	}

	public Record getRecord() {
		return record;
	}

	public void setRecord(Record record) {
		this.record = record;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isNewEx() {
		return newEx;
	}

	public void setNewEx(boolean newEx) {
		this.newEx = newEx;
	}
	
	

}
