package com.app.converters;

import com.app.dto.ExaminationDTO;
import com.app.model.Examination;
import com.app.model.Patient;

public class ExaminationConverter {
	
	public static ExaminationDTO toDTO(Examination e, Patient p) {
		
		ExaminationDTO retVal = new ExaminationDTO();
		
		retVal.setDiagnosis(e.getDiagnosis());
		retVal.setPatientID(p.getId());
		retVal.setSymptons(e.getSymptons());
		retVal.setTherapy(e.getTherapy());
		retVal.setNewEx(e.isNewEx());
		retVal.setPatient(p);
		retVal.setDate(e.getDate());
		retVal.setName(e.getName());
		retVal.setId(e.getId());
		
		return retVal;
		
	}

}
