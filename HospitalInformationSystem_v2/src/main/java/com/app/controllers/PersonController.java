package com.app.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PasswordDTO;
import com.app.dto.PersonDTO;
import com.app.dto.PersonDataDTO;
import com.app.dto.PersonLiteDTO;
import com.app.dto.RoleDTO;
import com.app.model.Manager;
import com.app.model.MedicalStaff;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.security.TokenUtils;
import com.app.service.PersonService;


@RestController
@RequestMapping(value = "persons")
public class PersonController {
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private TokenUtils tokenUtils;

	@Autowired 
	private PasswordEncoder passwordEncoder;
	
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
	@PreAuthorize("hasAnyAuthority('Edit_patient_password', 'Edit_manager_password')")
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO dto, @RequestHeader("X-Auth-Token") String token){
		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);
		Person p = personService.findOne(person.getId());
		if(p != null){
			System.out.println(dto.getOldPassword());
			System.out.println(p.getPassword());
			if (passwordEncoder.matches(dto.getOldPassword(), p.getPassword())) {
				p.setPassword(passwordEncoder.encode(dto.getNewPassword()));
				System.out.println("upisao u bazu");
				p = personService.save(p);
				return new ResponseEntity<>(HttpStatus.OK);
			}
			else{
				System.out.println("lozinka ee ne posklapa");
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Function that returns logged person role.
	 * 
	 * @param token
	 *            Session token from header.
	 * @return Person role.
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/roleByToken", method = RequestMethod.GET)
	public ResponseEntity<RoleDTO> roleByToken(@RequestHeader("X-Auth-Token") String token) {
		
		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		RoleDTO retVal = new RoleDTO();
		
		if (person instanceof Manager)
			retVal.setRole(((Manager) person).getRole() + " manager");
		else if (person instanceof MedicalStaff)
			retVal.setRole("medical staff");
		else if (person instanceof Patient)
			retVal.setRole("patient");

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	/**
	 * Function that return logged person
	 * @param token
	 * @return logged person details
	 */
	@PreAuthorize("isAuthenticated()")
	@RequestMapping(value = "/personByToken", method = RequestMethod.GET)
	public ResponseEntity<PersonDataDTO> personByToken(@RequestHeader("X-Auth-Token") String token) {
		
		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		PersonDataDTO retVal = new PersonDataDTO(person.getName(), person.getSurname(), person.getUsername(), person.getPhoto(), person.getEmail());
		if (person instanceof Patient)
			retVal.setGender(((Patient) person).getGender());

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
}
