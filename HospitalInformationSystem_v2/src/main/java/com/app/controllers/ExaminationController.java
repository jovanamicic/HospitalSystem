package com.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.converters.ExaminationConverter;
import com.app.dto.ExaminationDTO;
import com.app.dto.ExaminationUpdateDTO;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.model.Examination;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.model.Record;
import com.app.service.ExaminationService;
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
	RecordService recordService;
	
	/** Function returns examinations.
	 * @param page
	 * @return Page of examinations.
	 */
	@RequestMapping(value= "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getAllExaminationsPageable(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		
		Page<Examination> examinations = examinationService.findAllPage(page);
		return new ResponseEntity<>(examinations, HttpStatus.OK);
	}
	
	/** Function returns all new examinations.
	 * @param page
	 * @return Page of new examinations.
	 */
	@RequestMapping(value= "/newExaminations", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getNewExaminationsPageable(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		
		Page<Examination> examinations = examinationService.findNewExaminationsPage(page);
		return new ResponseEntity<>(examinations, HttpStatus.OK);
	}

	@RequestMapping(value = "/scheduleExamination",  method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Examination> saveExamination(@RequestBody MedicalStaffScheduleDTO examination) throws ParseException {
		
		//check personal ID of patient
		Person patient = personService.findByPersonalID(examination.getPersonalId());
		if (patient == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//find patient record
		Record record = recordService.findById(patient.getPersonalID());
		if (record == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
		//find doctor
		Person doctor = personService.findOne(examination.getDoctorId());
		
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
	
	/** Function that adds new examination if it is urgent or updates scheduled examination.
	 * @param dto
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Void> finishedExamination(@RequestBody ExaminationDTO dto, HttpSession session){
		int id = (int) session.getAttribute("person");
		Person doctor = personService.findOne(id);
		Person patient = personService.findOne(dto.getPatientID());
		Record record = recordService.findById(patient.getPersonalID());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date date = null;
		try {
			date = sdf.parse(sdf.format(new Date()));
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		List<Examination> examinations = null;
		try {
			examinations = examinationService.findByDate(date);
		} catch (Exception e) {
		}
		if(examinations.isEmpty()){  //there is no scheduled examination
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
		else{ //find examination of that patient and change it
			boolean found = false;
			for (Examination ex : examinations) {
				if (record.getExaminations().contains(ex)){
					if (!ex.isPassed()){
						found = true;
						ex.setDiagnosis(dto.getDiagnosis());
						ex.setTherapy(dto.getTherapy());
						ex.setSymptons(dto.getSymptons());
						ex.setPassed(true);
						ex = examinationService.save(ex);
					}
				}
			}
			if (!found){ //there is no scheduled (just passed examinations were in list)
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
	
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<Examination>> getPatientExaminations(@PathVariable int id, @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		Person patient = personService.findOne(id);

		Page<Examination> retVal= examinationService.findByRecordIdPage(page, patient.getPersonalID());
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	/**
	 * Function that deletes examination from DB
	 * @return 
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id){
		examinationService.delete(id);
	}
	
	/** Function gets data about one examination.
	 * @param id of Examination.
	 * @return Data about Examination.
	 */
	@RequestMapping(value= "/{id}", method = RequestMethod.GET)
	public ResponseEntity<ExaminationDTO> getExamination(@PathVariable int id){
		
		Examination ex = examinationService.findById(id);
		
		if (ex != null){
			try {
				Person p = personService.findByPersonalID(ex.getRecord().getId());
				ExaminationDTO retVal = ExaminationConverter.toDTO(ex, (Patient)p);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Function that update time for examination
	 * @return 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/saveTime", method = RequestMethod.PUT)
	public ResponseEntity<Examination> updateExamination(@RequestBody ExaminationUpdateDTO o) throws ParseException{
		Examination retVal = examinationService.findById(o.getExaminationId());
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		Date convertedDate = sdf.parse(o.getDate());
		retVal.setDate(convertedDate);
		retVal.setNewEx(false);
		retVal = examinationService.save(retVal);
		
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
