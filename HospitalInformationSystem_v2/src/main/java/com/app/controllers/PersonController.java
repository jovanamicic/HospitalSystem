package com.app.controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.dto.PasswordDTO;
import com.app.dto.PersonDataDTO;
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
	 * Function that update users password.
	 * @param dto Old and new passwords
	 * @param token
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('Edit_patient_password', 'Edit_manager_password')")
	@RequestMapping(value = "/password", method = RequestMethod.PUT)
	public ResponseEntity<Void> changePassword(@RequestBody PasswordDTO dto, @RequestHeader("X-Auth-Token") String token){
		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);
		Person p = personService.findOne(person.getId());
		
		String newPassword = dto.getNewPassword();
		Pattern pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(newPassword);
		
		if(p != null){
			if (passwordEncoder.matches(dto.getOldPassword(), p.getPassword())) {
				
				//check new password
				if (newPassword.length() < 8)
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				else if (newPassword.equals(newPassword.toLowerCase()))
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				else if (!newPassword.matches(".*\\d+.*"))
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				else if (!matcher.find())
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				else {
					p.setPassword(passwordEncoder.encode(newPassword));
					p = personService.save(p);
					return new ResponseEntity<>(HttpStatus.OK);
				}
			}
			else{
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Function that returns logged person role.
	 * @param token
	 * @return
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
	 * Function that return logged person.
	 * @param token
	 * @return
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
	
	
	/**
	 * Function that returns data of person.
	 * @param token
	 * @param personID
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('View_patient_profile', 'View_all_patients')")
	@RequestMapping(value = "/{personID}", method = RequestMethod.GET)
	public ResponseEntity<PersonDataDTO> getPersonData(@RequestHeader("X-Auth-Token") String token, @PathVariable int personID){
		Person person = personService.findOne(personID);
		
		if(person == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		PersonDataDTO retVal = new PersonDataDTO();
		retVal.setName(person.getName());
		retVal.setSurname(person.getSurname());
		retVal.setUsername(person.getUsername());
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
}
