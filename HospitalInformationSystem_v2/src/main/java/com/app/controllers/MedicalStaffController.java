package com.app.controllers;

import java.util.ArrayList;
import java.util.List;

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

import com.app.dto.ExaminationOperationDetailsDTO;
import com.app.dto.ExaminationOperationIdDTO;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.dto.ObjectIDDTO;
import com.app.dto.PersonLiteDTO;
import com.app.model.Examination;
import com.app.model.MedicalStaff;
import com.app.model.Operation;
import com.app.model.Person;
import com.app.security.TokenUtils;
import com.app.service.ExaminationService;
import com.app.service.MedicalStaffService;
import com.app.service.OperationService;
import com.app.service.PersonService;
import com.app.converters.*;

@RestController
@RequestMapping(value = "medicalstaff")
public class MedicalStaffController {

	@Autowired
	private MedicalStaffService medicalStaffService;

	@Autowired
	private OperationService operationService;

	@Autowired
	private ExaminationService examinationService;

	@Autowired
	private PersonService personService;

	@Autowired
	private TokenUtils tokenUtils;

	/**
	 * Function that returns all doctors.
	 * 
	 * @param page
	 * @return Page of patients
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<PersonLiteDTO>> getAllDoctors() {
		List<MedicalStaff> doctors = medicalStaffService.findAll();
		if (doctors != null) {
			List<PersonLiteDTO> retVal = new ArrayList<PersonLiteDTO>();
			for (MedicalStaff ms : doctors) {
				PersonLiteDTO doctor = new PersonLiteDTO();
				doctor.setName(ms.getName());
				doctor.setSurname(ms.getSurname());
				doctor.setId(ms.getId());
				retVal.add(doctor);
			}
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/**
	 * Function that return schedule for logged medical stuff
	 * 
	 * @param person
	 *            id
	 * @return all operations and examinations of logged medical stuff
	 */
	@PreAuthorize("hasAuthority('ROLE_MEDICAL_STUFF')")
	@RequestMapping(value = "/schedule", method = RequestMethod.GET)
	public ResponseEntity<List<MedicalStaffScheduleDTO>> getMySchedule(@RequestHeader("X-Auth-Token") String token) {
		
		String username = tokenUtils.getUsernameFromToken(token);
		Person doctor = personService.findByUsername(username);

		List<Examination> examinations = examinationService.findByDoctorId(doctor.getId());
		List<Operation> operations = operationService.findByDoctorId(doctor.getId());

		List<MedicalStaffScheduleDTO> retVal = MedicalStaffScheduleConverter.toSchedule(operations, examinations);
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	/**
	 * Function that return details of operation or examination
	 * 
	 * @param type
	 *            and id of examination/operation
	 * @return details about operation or examination
	 */
	@RequestMapping(value = "/operationExaminationDetails/{type}/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExaminationOperationDetailsDTO> getDetails(@RequestHeader("X-Auth-Token") String token, @PathVariable String type, @PathVariable int id) {
		ExaminationOperationDetailsDTO retVal = new ExaminationOperationDetailsDTO();

		if (type.equalsIgnoreCase("operacija")) {
			Operation operation = operationService.findById(id);
			Person patient = personService.findByPersonalID(operation.getRecordOperation().getId());

			retVal.setDate(operation.getDate().toString());
			retVal.setDoctorId(operation.getHeadDoctor().getId());
			retVal.setDoctor(operation.getHeadDoctor().getName() + " " + operation.getHeadDoctor().getSurname());
			retVal.setName(operation.getName());
			retVal.setType(type);
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
		} else {
			Examination examination = examinationService.findById(id);
			Person patient = personService.findByPersonalID(examination.getRecord().getId());

			retVal.setDate(examination.getDate().toString());
			retVal.setDoctorId(examination.getDoctor().getId());
			retVal.setDoctor(examination.getDoctor().getName() + " " + examination.getDoctor().getSurname());
			retVal.setName(examination.getName());
			retVal.setType(type);
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
		}

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<PersonLiteDTO> getDoctor(@PathVariable int id) {
		MedicalStaff ms = medicalStaffService.findOne(id);
		PersonLiteDTO retVal = new PersonLiteDTO();
		if (ms != null) {
			retVal.setName(ms.getName());
			retVal.setSurname(ms.getSurname());
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
