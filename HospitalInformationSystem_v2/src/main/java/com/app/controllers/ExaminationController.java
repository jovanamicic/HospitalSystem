package com.app.controllers;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.converters.ExaminationConverter;
import com.app.dto.ExaminationDTO;
import com.app.dto.ExaminationUpdateDTO;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.model.Examination;
import com.app.model.MedicalStaff;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.model.Record;
import com.app.security.AESencryption;
import com.app.security.Base64Utility;
import com.app.security.TokenUtils;
import com.app.service.ExaminationService;
import com.app.service.PatientService;
import com.app.service.PersonService;
import com.app.service.RecordService;

@RestController
@RequestMapping(value = "examinations")
public class ExaminationController {

	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_NUMBER = 0;

	@Autowired
	ExaminationService examinationService;

	@Autowired
	PersonService personService;

	@Autowired
	PatientService patientService;

	@Autowired
	RecordService recordService;

	@Autowired
	private TokenUtils tokenUtils;
	
	@Autowired
	private AESencryption aesEncription;
	
	@Autowired
	private Base64Utility base64Utility;

	/**
	 * Function returns examinations pageable.
	 * 
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_examinations')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getAllExaminationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {
		Page<Examination> examinations = examinationService.findAllPage(page);
		return new ResponseEntity<>(examinations, HttpStatus.OK);
	}

	/**
	 * Function returns all new examinations pageable.
	 * 
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_examinations')")
	@RequestMapping(value = "/newExaminations", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getNewExaminationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Page<Examination> examinations = examinationService.findNewExaminationsPage(page);
		return new ResponseEntity<>(examinations, HttpStatus.OK);
	}

	/**
	 * Function that returns add new examination to database.
	 * 
	 * @param token
	 * @param examination
	 * @return saved examination
	 * @throws ParseException
	 */
	@PreAuthorize("hasAuthority('Add_new_examination')")
	@RequestMapping(value = "/saveExamination", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Examination> saveExamination(@RequestHeader("X-Auth-Token") String token,
			@RequestBody MedicalStaffScheduleDTO examination) throws ParseException {

		// find doctor
		String username = tokenUtils.getUsernameFromToken(token);
		Person doctor = personService.findByUsername(username);

		// check personal ID of patient
		Person patient = personService.findByPersonalID(examination.getPersonalId());
		if (patient == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// find patient record
		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Record record = recordService.findById(personalIdEncoded);
		if (record == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Examination e = new Examination();
		e.setName(examination.getName());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date convertedDate = sdf.parse(examination.getDate());
		e.setDate(convertedDate);
		e.setDoctor(doctor);
		e.setRecord(record);
		e.setNewEx(true);

		Examination retVal = examinationService.save(e);

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	/**
	 * Function that adds new examination if it is urgent or updates scheduled
	 * examination.
	 * 
	 * @param token
	 * @param dto
	 * @return
	 */
	@PreAuthorize("hasAuthority('Add_new_examination')")
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Void> finishedExamination(@RequestHeader("X-Auth-Token") String token,
			@RequestBody ExaminationDTO dto) {
		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		Person doctor = personService.findOne(person.getId());
		Person patient = personService.findOne(dto.getPatientID());
		
		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Record record = recordService.findById(personalIdEncoded);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		List<Examination> examinations = examinationService.findByDate(date);

		if (examinations.isEmpty()) { // there is no scheduled examination
			Examination e = new Examination();
			e.setDate(date);
			e.setDoctor(doctor);
			e.setDiagnosis(dto.getDiagnosis());
			e.setSymptons(dto.getSymptons());
			e.setTherapy(dto.getTherapy());
			e.setName("Hitan pregled");
			e.setPassed(true);
			e.setRecord(record);
			e = examinationService.save(e);
		} else { // find examination of that patient and change it
			boolean found = false;
			for (Examination ex : examinations) {
				if (record.getExaminations().contains(ex)) {
					if (!ex.isPassed()) {
						found = true;
						ex.setDiagnosis(dto.getDiagnosis());
						ex.setTherapy(dto.getTherapy());
						ex.setSymptons(dto.getSymptons());
						ex.setPassed(true);
						ex = examinationService.save(ex);
					}
				}
			}
			if (!found) { // there is no scheduled (just passed examinations
							// were in list)
				Examination e = new Examination();
				e.setDate(date);
				e.setDoctor(doctor);
				e.setDiagnosis(dto.getDiagnosis());
				e.setSymptons(dto.getSymptons());
				e.setTherapy(dto.getTherapy());
				e.setName("Hitan pregled");
				e.setPassed(true);
				e.setRecord(record);
				e = examinationService.save(e);
			}
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Function that returns examinations of patient.
	 * 
	 * @param token
	 * @param id
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_patient_record')")
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getPatientExaminations(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {
		Patient patient = patientService.findOne(id);

		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		if (patient.getChosenDoctor().getId() != person.getId())
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Page<Examination> retVal = examinationService.findByRecordIdPage(page, personalIdEncoded);
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	/**
	 * Function that return examinations of logged patient.
	 * 
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_patient_record')")
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getLoggedPatientExaminations(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		String username = tokenUtils.getUsernameFromToken(token);
		Person patient = personService.findByUsername(username);

		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Page<Examination> retVal = examinationService.findByRecordIdPage(page, personalIdEncoded);
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	/**
	 * Function that deletes examination from DB.
	 * 
	 * @param token
	 * @param id
	 */
	@PreAuthorize("hasAuthority('Delete_examination')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable int id) {
		examinationService.delete(id);
	}

	/**
	 * Function gets data about one examination.
	 * 
	 * @param token
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_examination')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExaminationDTO> getExamination(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id) {

		String username = tokenUtils.getUsernameFromToken(token);
		Person loggedPerson = personService.findByUsername(username);
		
		Examination ex = examinationService.findById(id);
		

		if (ex != null) {
			try {
				//return 401 if this is not examination of logged patient
				if((loggedPerson instanceof Patient) && (!ex.getRecord().getId().equals(base64Utility.encode(aesEncription.encrypt(loggedPerson.getPersonalID() + ""))))){
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				String encodedPersonalId = ex.getRecord().getId();
				byte[] personalIdBytes = base64Utility.decode(encodedPersonalId);
				byte[] decriptetBytes = aesEncription.decrypt(personalIdBytes);
				String decripted = new String(decriptetBytes);
				Long personalIdDecoded = Long.parseLong(decripted);
				Person p = personService.findByPersonalID(personalIdDecoded);
				
				//return 401 if this is not examination of logged doctor
				if((loggedPerson instanceof MedicalStaff) && (((Patient) p).getChosenDoctor().getPersonalID() != loggedPerson.getPersonalID())){
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				
				ExaminationDTO retVal = ExaminationConverter.toDTO(ex, (Patient) p);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function that update time for examination.
	 * 
	 * @param token
	 * @param o
	 * @return
	 * @throws ParseException
	 */
	@PreAuthorize("hasAuthority('Edit_examination')")
	@RequestMapping(value = "/saveTime", method = RequestMethod.PUT)
	public ResponseEntity<Examination> updateExamination(@RequestHeader("X-Auth-Token") String token,
			@RequestBody ExaminationUpdateDTO o) {

		Examination retVal = examinationService.findById(o.getExaminationId());

		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		Date convertedDate;
		try {
			convertedDate = sdf.parse(o.getDate());

			retVal.setDate(convertedDate);
			retVal.setNewEx(false);
			retVal = examinationService.save(retVal);

			return new ResponseEntity<>(retVal, HttpStatus.OK);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

	}

}
