package com.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Room;
import com.app.service.RoomService;

@RestController
@RequestMapping(value = "rooms")
public class RoomController {
	
	@Autowired
	private RoomService roomService;
	
	/**
	 * Function that returns all rooms.
	 * @param token
	 * @return
	 */
	@PreAuthorize("hasAuthority('Schedule_rooms')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Room>> getAllRooms(@RequestHeader("X-Auth-Token") String token){
		List<Room> rooms = roomService.findAll();
		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

}
