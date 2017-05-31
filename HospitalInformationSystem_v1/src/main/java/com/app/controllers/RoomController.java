package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Room;
import com.app.service.RoomService;

@RestController
@RequestMapping(value = "rooms")
public class RoomController {
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_NUMBER = 0;
	
	@Autowired
	private RoomService roomService;
	
	/**
	 * Function that returns all rooms pageable
	 * @param page
	 * @return Page of rooms
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Room>> getAllRooms(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		Page<Room> rooms = roomService.findAll(page);
		return new ResponseEntity<>(rooms, HttpStatus.OK);
	}

}
