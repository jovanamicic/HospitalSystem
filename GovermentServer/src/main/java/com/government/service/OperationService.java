package com.government.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.government.model.Operation;
import com.government.repository.OperationRepository;

@Component
public class OperationService {
	
	@Autowired
	OperationRepository operationsRepo;
	
	public List<Operation> findByName(String name) {
		return operationsRepo.findByNameLike(name);
	}
	
	public List<Operation> findAll() {
		return operationsRepo.findAll();
	}
	
	public List<Operation> findByDate(Date dateAfter, Date dateBefore, String name) {
		return operationsRepo.findByDateBetweenAndNameLike(dateAfter, dateBefore, name);
	}
	
	public List<Operation> findByDateAfter(Date dateAfter, String name) {
		return operationsRepo.findByDateAfterAndNameLike(dateAfter, name);
	}
	
	public List<Operation> findByDateBefore(Date dateBefore, String name) {
		return operationsRepo.findByDateBeforeAndNameLike(dateBefore, name);
	}
}
