package com.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Room;

public interface RoomRepository extends JpaRepository<Room, Integer> {
	
	Page<Room> findAll(Pageable page);

}
