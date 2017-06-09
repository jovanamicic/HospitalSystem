package com.app.dto;

public class MedicalStaffScheduleDTO {

	private int id;
	private String type;
	private String name;
	private String date;
	private int doctorId;
	private long personalId;
	private int operationExaminationId;
	private int duration;
	
	public MedicalStaffScheduleDTO() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(int doctorId) {
		this.doctorId = doctorId;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public Long getPersonalId() {
		return personalId;
	}

	public void setPersonalId(long personalId) {
		this.personalId = personalId;
	}

	public int getOperationExaminationId() {
		return operationExaminationId;
	}

	public void setOperationExaminationId(int operationExaminationId) {
		this.operationExaminationId = operationExaminationId;
	}
	
	
}
