package com.app.converters;

import java.util.ArrayList;
import java.util.List;

import com.app.dto.MedicalStaffScheduleDTO;
import com.app.model.Examination;
import com.app.model.Operation;

public class MedicalStaffScheduleConverter {

	public static List<MedicalStaffScheduleDTO> toSchedule(List<Operation> operations, List<Examination> examinations){
		List<MedicalStaffScheduleDTO> retVal = new ArrayList<>();
		
		for (Operation operation : operations) {
			MedicalStaffScheduleDTO o = new MedicalStaffScheduleDTO();
			o.setId(operation.getId());
			o.setDate(operation.getDate().toString());
			o.setName(operation.getName());
			o.setType("Operacija");
			o.setOperationExaminationId(operation.getId());
			retVal.add(o);
		}
		
		for (Examination examination : examinations) {
			MedicalStaffScheduleDTO e = new MedicalStaffScheduleDTO();
			e.setId(examination.getId());
			e.setDate(examination.getDate().toString());
			e.setName(examination.getName());
			e.setType("Pregled");
			e.setOperationExaminationId(examination.getId());
			retVal.add(e);
		}
		
		return retVal;
	}
}
