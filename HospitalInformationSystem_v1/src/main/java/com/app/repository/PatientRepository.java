package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

	Page<Patient> findAll(Pageable page);
	List<Patient> findAll();
	Page<Patient> findByChosenDoctorId(int id, Pageable page);
	Patient findOne(int id);
	Page<Patient> findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCaseOrPersonalIDLike(String s, String s2, Long personalID, Pageable page);
	Page<Patient> findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(String s, String s2, Pageable page);

}
