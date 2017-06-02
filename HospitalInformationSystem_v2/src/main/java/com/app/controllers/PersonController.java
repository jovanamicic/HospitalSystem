package com.app.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PasswordDTO;
import com.app.dto.PersonDTO;
import com.app.dto.PersonDataDTO;
import com.app.dto.PersonLiteDTO;
import com.app.model.Manager;
import com.app.model.MedicalStaff;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.service.PersonService;


@RestController
@RequestMapping(value = "persons")
public class PersonController {
	
	@Autowired
	private PersonService personService;

	/** 
	 * Function for logging on system.
	 * @param personDTO contains username and password from form.
	 * @param session
	 * @return ID of logged person.
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<PersonLiteDTO> login(@RequestBody PersonDTO personDTO, HttpSession session) {
		String username = personDTO.getUsername();
		String password = personDTO.getPassword();
		PersonLiteDTO retVal = new PersonLiteDTO();
		
		if (username != "" && password != ""){
			Person person = personService.findByUsername(username);
			if (person != null && person.getPassword().equals(password)){
				session.setAttribute("person", person.getId());
				retVal.setId(person.getId());
				retVal.setName(person.getName());
				retVal.setSurname(person.getSurname());
				retVal.setEmail(person.getEmail());
				if (person instanceof Manager)
					retVal.setRole(((Manager)person).getRole() + "Manager");
				else if(person instanceof MedicalStaff)
					retVal.setRole("medical staff");
				else if(person instanceof Patient)
					retVal.setRole("patient");
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
	}
	
	/**
	 * Function that return person based on its ID.
	 * @param ID of a person.
	 * @return Person object.
	 */
	@RequestMapping(value = "/getPerson/{personID}", method = RequestMethod.GET)
	public ResponseEntity<PersonDataDTO> getPerson(@PathVariable int personID){
		Person person = personService.findOne(personID);
		
		if(person == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		PersonDataDTO retVal = new PersonDataDTO(person.getName(), person.getSurname(), person.getUsername(), person.getPhoto(), person.getEmail());
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	/** Function that update users password.
	 * @param dto old and new passwords.
	 * @param session
	 * @return Status code.
	 */
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO dto, HttpSession session){
		int personID = (int) session.getAttribute("person");
		Person p = personService.findOne(personID);
		if(p != null){
			if (p.getPassword().equals(dto.getOldPassword())){
				p.setPassword(dto.getNewPassword());
				p = personService.save(p);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}
