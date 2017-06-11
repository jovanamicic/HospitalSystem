package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.Record;
import com.app.repository.RecordRepository;

@Service
public class RecordService {
	
	@Autowired
	RecordRepository recordRepository;

	public Record findById(String id){
		return recordRepository.findOne(id);
	}
	
	public Record save(Record record) {
		return recordRepository.save(record);
	}
	
	

}
