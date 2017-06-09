package com.app.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.RoomSchedule;
import com.app.service.RoomScheduleService;

@RestController
@RequestMapping(value = "roomSchedules")
public class RoomScheduleController {
	
	
	@Autowired
	private RoomScheduleService roomScheduleService;
	
	/**
	 * Function that returns time when room with passed id is free on specific day.
	 * @param token
	 * @param roomId
	 * @param date
	 * @return
	 */
	@PreAuthorize("hasAuthority('Schedule_rooms')")
	@RequestMapping(value = "/available/{roomId}/{date}", method = RequestMethod.GET)
	public ResponseEntity<List<String>> getAvailableTimes(
			@RequestHeader("X-Auth-Token") String token,
			@PathVariable String roomId, @PathVariable String date){
		
		List<RoomSchedule> schedule = roomScheduleService.findByRoomIdAndDate(roomId, "%" + date + "%");

		List<String> busy = new ArrayList<>();
		for (RoomSchedule s : schedule) {
			String time = s.getDate().split(" ")[1];
			
			int startHour = Integer.parseInt(time.split(":")[0]);
			for (int i = 0; i < s.getDuration(); i++) {
				int currTime = startHour + i;
				if ( currTime < 10)
					busy.add("0" + currTime + ":00");
				else
					busy.add(currTime + ":00");
			}
		}
		
		List<String> retVal = new ArrayList<>();

		for (int i = 8; i <= 22; i++) {
			String currTime;
			if (i < 10)
				currTime = "0" + i + ":00";
			else
				currTime = i + ":00";
			
			if (!busy.contains(currTime))
				retVal.add(currTime);
		}
		
		return new ResponseEntity<List<String>>(retVal, HttpStatus.OK);
	}

}
