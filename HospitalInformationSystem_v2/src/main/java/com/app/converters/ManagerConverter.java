package com.app.converters;

import com.app.dto.ManagerDTO;
import com.app.model.Manager;

public class ManagerConverter {
	
	public static ManagerDTO toDTO(Manager m) {
		
		ManagerDTO retVal = new ManagerDTO();
		
		retVal.setName(m.getName());
		retVal.setSurname(m.getSurname());
		retVal.setPersonalID(m.getPersonalID()+"");
		retVal.setUsername(m.getUsername());
		retVal.setEmail(m.getEmail());
		retVal.setRole(m.getRole());
		
		return retVal;
		
	}

}
