package com.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	Page<Manager> findAll(Pageable page);
	List<Manager> findAll();
	Manager findOne(int id);
	Manager findByUsername(String username);
	
}
