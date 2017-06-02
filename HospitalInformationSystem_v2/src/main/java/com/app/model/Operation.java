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
@Table(name = "operation")
public class Operation implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "operation_id", unique = true, nullable = false)
	private int id;

	@Column(name = "name", unique = false, nullable = false)
	private String name;
	
	@Column(name = "date", unique = false, nullable = false)
	private Date date;
	
	@Column(name = "duration", unique = false, nullable = false)
	private int duration;
	
	@Column(name = "report", unique = false, nullable = true)
	private String report;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "room_id", referencedColumnName = "room_id", nullable = true)
	private Room room;
	
	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "record_id", referencedColumnName = "record_id", nullable = false)
	private Record recordOperation;
	
	@ManyToOne
	@JoinColumn(name = "head_doctor_id", referencedColumnName = "person_id", nullable = false)
	private Person headDoctor;
	
	
	public Operation(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getReport() {
		return report;
	}

	public void setReport(String report) {
		this.report = report;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Person getHeadDoctor() {
		return headDoctor;
	}

	public void setHeadDoctor(Person headDoctor) {
		this.headDoctor = headDoctor;
	}

	public Record getRecordOperation() {
		return recordOperation;
	}

	public void setRecordOperation(Record recordOperation) {
		this.recordOperation = recordOperation;
	}


}
