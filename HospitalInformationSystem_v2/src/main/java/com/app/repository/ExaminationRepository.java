package com.app.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Examination;

public interface ExaminationRepository extends JpaRepository<Examination, Integer> {

	List<Examination> findByDoctorId(int id);
	List<Examination> findByRecordId(String id);
	Page<Examination> findByRecordId(Pageable page, String id);
	List<Examination> findByDate(Date date);
	Page<Examination> findAll(Pageable page);
	Page<Examination> findByNewExIsTrue(Pageable page);
}
