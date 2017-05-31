package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Manager;
import com.app.repository.ManagerRepository;

@Service
public class ManagerService {
	
	@Autowired
	private ManagerRepository managerRepository;
	
	public Page<Manager> findAll(Pageable page) {
		return managerRepository.findAll(page);
	}
	
	public List<Manager> findAll() {
		return managerRepository.findAll();
	}
	
	public Manager save(Manager manager){
		return managerRepository.save(manager);
	}
	
	public Manager findOne(int id){
		return managerRepository.findOne(id);
	}

}
