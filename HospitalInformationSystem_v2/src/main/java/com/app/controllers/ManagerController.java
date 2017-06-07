package com.app.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.converters.ManagerConverter;
import com.app.dto.ManagerDTO;
import com.app.dto.PatientDTO;
import com.app.model.Manager;
import com.app.model.Person;
import com.app.security.TokenUtils;
import com.app.service.ManagerService;
import com.app.service.PersonService;

@RestController
@RequestMapping(value = "managers")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PersonService personService;
	
	@Autowired
	private TokenUtils tokenUtils;
	
	
	/** Function gets data about one manager.
	 * @param id of Manager.
	 * @return Data about Manager.
	 */
	@RequestMapping(value= "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ManagerDTO> getManager(@PathVariable int id){
		Manager m = managerService.findOne(id);
		
		if (m != null){
			try {
				ManagerDTO retVal = ManagerConverter.toDTO(m);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@PreAuthorize("hasAuthority('Edit_manager_profile')")
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Void> changeProfile(@RequestHeader("X-Auth-Token") String token, @RequestBody PatientDTO dto) {
		
		String username = tokenUtils.getUsernameFromToken(token);
		Manager m = managerService.findByUsername(username);
		
		if (m == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if (!personService.emailUnique(dto.getEmail()) && 
				(!m.getEmail().equalsIgnoreCase(dto.getEmail())))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(personService.emailUnique(dto.getEmail()))
			m.setEmail(dto.getEmail());
		
		if (!personService.usernameUnique(dto.getEmail()) && 
				(!m.getUsername().equalsIgnoreCase(dto.getUsername())))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(personService.usernameUnique(dto.getUsername()))
			m.setUsername(dto.getUsername());
		
		managerService.save(m);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
