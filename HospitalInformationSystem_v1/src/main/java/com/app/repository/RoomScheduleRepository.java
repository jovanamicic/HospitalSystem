package com.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.RoomSchedule;

public interface RoomScheduleRepository extends JpaRepository<RoomSchedule, Integer> {

	List<RoomSchedule> findByRoomIDAndDateLike(String id, String date);
}
