package com.app.service;

import java.util.Date;
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

	public Operation findById(int id) {
		return operationRepository.findOne(id);
	}

	public List<Operation> findByDoctorId(int id) {
		return operationRepository.findByHeadDoctorId(id);
	}

	public List<Operation> findByRecordId(long id) {
		return operationRepository.findByRecordOperationId(id);
	}

	public Operation save(Operation operation) {
		return operationRepository.save(operation);
	}

	public Page<Operation> findNewOperationsPage(Pageable page) {
		return operationRepository.findByRoomIdIsNull(page);
	}

	public Page<Operation> findAllPage(Pageable page) {
		return operationRepository.findAll(page);
	}

	public void delete(int id) {
		operationRepository.delete(id);
	}

	public Page<Operation> findByRecordId(Pageable page, long id) {
		return operationRepository.findByRecordOperationId(page, id);
	}

	// ---------------- Methods for Government Report ----------------

	public List<Operation> findByName(String name) {
		return operationRepository.findByNameLike(name);
	}

	public List<Operation> findByDate(Date dateAfter, Date dateBefore, String name) {
		return operationRepository.findByDateBetweenAndNameLike(dateAfter, dateBefore, name);
	}

	public List<Operation> findByDateAfter(Date dateAfter, String name) {
		return operationRepository.findByDateAfterAndNameLike(dateAfter, name);
	}

	public List<Operation> findByDateBefore(Date dateBefore, String name) {
		return operationRepository.findByDateBeforeAndNameLike(dateBefore, name);
	}
}
