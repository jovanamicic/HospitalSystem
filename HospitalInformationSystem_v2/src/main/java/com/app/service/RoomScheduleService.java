package com.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.RoomSchedule;
import com.app.repository.RoomScheduleRepository;

@Service
public class RoomScheduleService {
	
	@Autowired
	private RoomScheduleRepository roomScheduleRepository;
	
	public RoomSchedule save(RoomSchedule roomSchedule){
		return roomScheduleRepository.save(roomSchedule);
	}
	
	public List<RoomSchedule> findByRoomIdAndDate(String id, String date) {
		return roomScheduleRepository.findByRoomIDAndDateLike(id, date);
	}

}
