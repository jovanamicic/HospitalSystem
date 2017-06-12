package com.government.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.government.model.Operation;

public interface OperationRepository extends JpaRepository<Operation, String> {
	
	List<Operation> findByNameLike(String name);
	List<Operation> findByDateBetweenAndNameLike(Date dateAfter, Date dateBefore, String name);
	List<Operation> findByDateAfterAndNameLike(Date dateAfter, String name);
	List<Operation> findByDateBeforeAndNameLike(Date dateBefore, String name);
}