package com.app.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "record")
public class Record  implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "record_id", unique = true, nullable = false)
	private String id;
	
	@JsonBackReference
	@OneToMany(mappedBy = "record", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Examination> examinations = new HashSet<>();
	
	@JsonBackReference
	@OneToMany(mappedBy = "recordOperation", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
	private Set<Operation> operations = new HashSet<>();
	
	public Record(){}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<Examination> getExaminations() {
		return examinations;
	}

	public void setExaminations(Set<Examination> examinations) {
		this.examinations = examinations;
	}

	public Set<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}

}
