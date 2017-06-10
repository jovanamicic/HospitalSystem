package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Operation;
import com.app.repository.OperationRepository;

@Service
public class OperationService {

	@Autowired
	private OperationRepository operationRepository;
	
	public Operation findById(int id){
		return operationRepository.findOne(id);
	}
	
	public List<Operation> findByDoctorId(int id){
		return operationRepository.findByHeadDoctorId(id);
	}
	
	public List<Operation> findByRecordId(String id){
		return operationRepository.findByRecordOperationId(id);
	}
	
	public Operation save(Operation operation){
		return operationRepository.save(operation);
	}

	public Page<Operation> findNewOperationsPage(Pageable page) {
		return operationRepository.findByRoomIdIsNull(page);
	}
	
	public Page<Operation> findAllPage(Pageable page) {
		return operationRepository.findAll(page);
	}
	
	public void delete(int id){
		operationRepository.delete(id);
	}
	
	public Page<Operation> findByRecordId(Pageable page, String id){
		return operationRepository.findByRecordOperationId(page, id);
	}
}
