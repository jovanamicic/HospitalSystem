package com.app.converters;

import com.app.dto.OperationDTO;
import com.app.model.Operation;
import com.app.model.Patient;

public class OperationConverter {

	public static OperationDTO toDTO(Operation o, Patient p) {
		
		OperationDTO retVal = new OperationDTO();
		
		retVal.setName(o.getName());
		retVal.setDate(o.getDate());
		retVal.setDuration(o.getDuration());
		retVal.setId(o.getId());
		retVal.setPatient(p);
		retVal.setRoom(o.getRoom());
		
		return retVal;
		
	}

}
