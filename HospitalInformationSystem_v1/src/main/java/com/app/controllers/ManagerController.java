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

import com.app.converters.ManagerConverter;
import com.app.dto.ManagerDTO;
import com.app.dto.PatientDTO;
import com.app.model.Manager;
import com.app.service.ManagerService;
import com.app.service.PersonService;

@RestController
@RequestMapping(value = "managers")
public class ManagerController {
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private PersonService personService;
	
	/** Function gets data about one managert.
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
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Void> changeProfile(@RequestBody PatientDTO dto, HttpSession session) {
		
		int id = (int) session.getAttribute("person");
		
		Manager m = managerService.findOne(id);
		
		if (m == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if (!personService.emailUnique(dto.getEmail()) && 
				(!m.getEmail().equalsIgnoreCase(dto.getEmail())))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(personService.emailUnique(dto.getEmail()))
			m.setEmail(dto.getEmail());
		
		if (!personService.usernameUnique(dto.getUsername()) && 
				(!m.getUsername().equalsIgnoreCase(dto.getUsername())))
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		if(personService.usernameUnique(dto.getUsername()))
			m.setUsername(dto.getUsername());
		
		System.out.println(m.getUsername());
		managerService.save(m);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
}
