package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Patient;
import com.app.repository.PatientRepository;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository patientRepository;
	
	
	public Page<Patient> findAllPage(Pageable page) {
		return patientRepository.findAll(page);
	}
	
	public List<Patient> findAll() {
		return patientRepository.findAll();
	}
	
	public Page<Patient> findByChosenDoctor(int id, Pageable page) {
		return patientRepository.findByChosenDoctorId(id, page);
	}
	
	public Patient save(Patient patient){
		return patientRepository.save(patient);
	}
	
	public Patient findOne(int id){
		return patientRepository.findOne(id);
	}
	
	
	public Page<Patient> findBySearchData(String searchData, Pageable page){
		String s = "%" + searchData + "%";
		String personalIDStr = searchData.replaceAll("[^0-9]", "");
		
		if (personalIDStr.equals(""))
			return patientRepository.findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCase(s, s, page);
		
		return patientRepository.findByNameLikeIgnoreCaseOrSurnameLikeIgnoreCaseOrPersonalIDLike(s, s, Long.parseLong(personalIDStr), page);
	}
}
