package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Record;

public interface RecordRepository extends JpaRepository<Record, String>{

	
}
