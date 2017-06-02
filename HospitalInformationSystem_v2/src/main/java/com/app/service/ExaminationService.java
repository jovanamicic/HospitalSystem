package com.app.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Examination;
import com.app.repository.ExaminationRepository;

@Service
public class ExaminationService {

	@Autowired
	private ExaminationRepository examinationRepository;
	
	public Examination findById(int id){
		return examinationRepository.findOne(id);
	}
	
	public Examination save(Examination examination){
		return examinationRepository.save(examination);
	}
	
	public List<Examination> findByDoctorId(int id){
		return examinationRepository.findByDoctorId(id);
	}
	
	public List<Examination> findByRecordId(long id){
		return examinationRepository.findByRecordId(id);
	}
	
	public Page<Examination> findByRecordIdPage(Pageable page, long id){
		return examinationRepository.findByRecordId(page, id);
	}
	
	public List<Examination> findByDate(Date date){
		return examinationRepository.findByDate(date);
	}
	
	public List<Examination> findAll(){
		return examinationRepository.findAll();
	}
	
	public Page<Examination> findAllPage(Pageable page) {
		return examinationRepository.findAll(page);
	}
	
	public Page<Examination> findNewExaminationsPage(Pageable page) {
		return examinationRepository.findByNewExIsTrue(page);
	}
	
	public void delete(int id){
		examinationRepository.delete(id);
	}
}
