package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Operation;


public interface OperationRepository extends JpaRepository<Operation, Integer> {
	
	List<Operation> findByHeadDoctorId(int id);
	List<Operation> findByRecordOperationId(long id);
	Page<Operation> findByRoomIdIsNull(Pageable page);
	Page<Operation> findAll(Pageable page);
	Page<Operation> findByRecordOperationId(Pageable page, long id);
}
