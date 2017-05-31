package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.MedicalStaff;
import com.app.repository.MedicalStaffRepository;

@Service
public class MedicalStaffService {
	
	@Autowired
	private MedicalStaffRepository medicalStaffRepository;
	
	public MedicalStaff findOne(int id){
		return medicalStaffRepository.findOne(id);
	}
	
	public List<MedicalStaff> findAll() {
		return medicalStaffRepository.findAll();
	}
	

}
