package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.app.model.MedicalStaff;

public interface MedicalStaffRepository extends JpaRepository<MedicalStaff, Integer> {
	
	List<MedicalStaff> findAll();
	MedicalStaff findOne(int id);
}
