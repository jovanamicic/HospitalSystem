package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.model.Person;
import com.app.repository.PersonRepository;

@Service
public class PersonService {

	@Autowired
	PersonRepository personRepository;
	
	public Person findByUsername(String username){
		return personRepository.findByUsername(username);
	}
	
	public Person findByPersonalID(long id){
		return personRepository.findByPersonalID(id);
	}
	
	public Person findOne(int id){
		return personRepository.findOne(id);
	}
	
	public Person save(Person person){
		return personRepository.save(person);
	}

	public Boolean emailUnique(String email) {
		Person p = personRepository.findByEmail(email);
		if (p != null)
			return false;
		else
			return true;
	}

	public Boolean usernameUnique(String username) {
		Person p = personRepository.findByUsername(username);
		if (p != null)
			return false;
		else
			return true;
	}

	public Boolean personalIDUnique(String personalID) {
		Person p = personRepository.findByPersonalID(Long.parseLong(personalID));
		if (p != null)
			return false;
		else
			return true;
	}

	
}
