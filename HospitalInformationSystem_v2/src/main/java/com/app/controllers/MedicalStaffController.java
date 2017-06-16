package com.app.controllers;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.converters.MedicalStaffScheduleConverter;
import com.app.dto.ExaminationOperationDetailsDTO;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.dto.PersonLiteDTO;
import com.app.model.Examination;
import com.app.model.MedicalStaff;
import com.app.model.Operation;
import com.app.model.Person;
import com.app.security.AESencryption;
import com.app.security.Base64Utility;
import com.app.security.TokenUtils;
import com.app.service.ExaminationService;
import com.app.service.MedicalStaffService;
import com.app.service.OperationService;
import com.app.service.PersonService;

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
	
	@Autowired
	private AESencryption aesEncription;
	
	@Autowired
	private Base64Utility base64Utility;

	/**
	 * Function that returns all doctors.
	 * @param token
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_medical_staff')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<List<PersonLiteDTO>> getAllDoctors(@RequestHeader("X-Auth-Token") String token) {
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
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function that return schedule for logged medical stuff.
	 * (all operations and examinations of logged medical stuff)
	 * @param token
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_medical_staff_schedule')")
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
	 * Function that return details of operation or examination.
	 * @param token
	 * @param type
	 * @param id
	 * @return
	 * @throws NoSuchPaddingException 
	 * @throws NoSuchProviderException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 * @throws IOException 
	 */
	@PreAuthorize("hasAnyAuthority('View_operation', 'View_examination')")
	@RequestMapping(value = "/operationExaminationDetails/{type}/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExaminationOperationDetailsDTO> getDetails(@RequestHeader("X-Auth-Token") String token, @PathVariable String type, @PathVariable int id) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IOException {
		ExaminationOperationDetailsDTO retVal = new ExaminationOperationDetailsDTO();

		if (type.equalsIgnoreCase("operacija")) {
			Operation operation = operationService.findById(id);
			
			if (operation == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			String encodedPersonalId = operation.getRecordOperation().getId();
			byte[] personalIdBytes = base64Utility.decode(encodedPersonalId);
			byte[] decriptetBytes = aesEncription.decrypt(personalIdBytes);
			String decripted = new String(decriptetBytes);
			Long personalIdDecoded = Long.parseLong(decripted);
			Person patient = personService.findByPersonalID(personalIdDecoded);

			if (patient == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			retVal.setDate(operation.getDate().toString());
			retVal.setDoctorId(operation.getHeadDoctor().getId());
			retVal.setDoctor(operation.getHeadDoctor().getName() + " " + operation.getHeadDoctor().getSurname());
			retVal.setName(operation.getName());
			retVal.setType(type);
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
		} else {
			Examination examination = examinationService.findById(id);
			
			if (examination == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			String encodedPersonalId = examination.getRecord().getId();
			byte[] personalIdBytes = base64Utility.decode(encodedPersonalId);
			byte[] decriptetBytes = aesEncription.decrypt(personalIdBytes);
			String decripted = new String(decriptetBytes);
			Long personalIdDecoded = Long.parseLong(decripted);
			Person patient = personService.findByPersonalID(personalIdDecoded);
			

			if (patient == null)
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
			retVal.setDate(examination.getDate().toString());
			retVal.setDoctorId(examination.getDoctor().getId());
			retVal.setDoctor(examination.getDoctor().getName() + " " + examination.getDoctor().getSurname());
			retVal.setName(examination.getName());
			retVal.setType(type);
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
			
			retVal.setSymptons(examination.getSymptons());
			retVal.setDiagnosis(examination.getDiagnosis());
			retVal.setTherapy(examination.getTherapy());
		}

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

//	//ovo se mozda vise ne koristi
//	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
//	public ResponseEntity<PersonLiteDTO> getDoctor(@PathVariable int id) {
//		MedicalStaff ms = medicalStaffService.findOne(id);
//		PersonLiteDTO retVal = new PersonLiteDTO();
//		if (ms != null) {
//			retVal.setName(ms.getName());
//			retVal.setSurname(ms.getSurname());
//			return new ResponseEntity<>(retVal, HttpStatus.OK);
//		}
//		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//	}

}
