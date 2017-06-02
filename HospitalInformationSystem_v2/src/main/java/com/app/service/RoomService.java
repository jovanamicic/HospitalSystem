package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.app.model.Room;
import com.app.repository.RoomRepository;

@Service
public class RoomService {
	
	@Autowired
	private RoomRepository roomRepository;
	

	public Page<Room> findAll(Pageable page) {
		return roomRepository.findAll(page);
	}


	public Room findOne(int id) {
		return roomRepository.findOne(id);
	}

}
