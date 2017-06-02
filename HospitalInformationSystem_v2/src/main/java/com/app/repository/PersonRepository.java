package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.model.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

	Person findByUsername(String username);
	Person findByPersonalID(long id);
	Person findOne(int id);
	Person findByEmail(String email);
	
}
