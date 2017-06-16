package com.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import com.app.converters.OperationConverter;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.dto.OperationDTO;
import com.app.dto.OperationUpdateDTO;
import com.app.model.Operation;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.model.Record;
import com.app.model.Room;
import com.app.model.RoomSchedule;
import com.app.service.OperationService;
import com.app.service.PatientService;
import com.app.service.PersonService;
import com.app.service.RecordService;
import com.app.service.RoomScheduleService;
import com.app.service.RoomService;

@RestController
@RequestMapping(value = "operations")
public class OperationController {
	
	private static final int DEFAULT_PAGE_SIZE = 1;
	private static final int DEFAULT_PAGE_NUMBER = 0;

	@Autowired
	OperationService operationService;

	@Autowired
	PersonService personService;

	@Autowired
	RecordService recordService;
	
	@Autowired
	PatientService patientService;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	RoomScheduleService roomScheduleService;

	@RequestMapping(value = "/saveOperation", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Operation> saveOperation(@RequestBody MedicalStaffScheduleDTO operation)
			throws ParseException {

		// check personal ID of patient
		Person patient = personService.findByPersonalID(operation.getPersonalId());
		if (patient == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// find patient record
		Record record = recordService.findById(patient.getPersonalID());
		if (record == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		// find doctor
		Person doctor = personService.findOne(operation.getDoctorId());

		Operation o = new Operation();
		o.setName(operation.getName());

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		Date convertedDate = sdf.parse(operation.getDate());
		o.setDate(convertedDate);
		o.setHeadDoctor(doctor);
		o.setDuration(operation.getDuration());
		o.setRecordOperation(record);

		Operation retVal = operationService.save(o);

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	/**
	 * Function that update time and room for operation
	 * @return 
	 * @throws ParseException 
	 */
	@RequestMapping(value = "/saveTimeAndRoom", method = RequestMethod.PUT)
	public ResponseEntity<Operation> updateOperation(@RequestBody OperationUpdateDTO o) throws ParseException{
		Operation retVal = operationService.findById(o.getOperationId());
		
		Room room = roomService.findOne(o.getRoomId());
		retVal.setRoom(room);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		Date convertedDate = sdf.parse(o.getDate());
		retVal.setDate(convertedDate);
		
		retVal = operationService.save(retVal);
		
		//save room schedule
		RoomSchedule roomSchedule = new RoomSchedule();
		roomSchedule.setDate(sdf.format(convertedDate));
		roomSchedule.setDuration(retVal.getDuration());
		roomSchedule.setRoomID(String.valueOf(room.getId()));
		roomScheduleService.save(roomSchedule);
		
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	/** Function gets data about one operation.
	 * @param id of Operation.
	 * @return Data about Operation.
	 */
	@RequestMapping(value= "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OperationDTO> getOperation(@PathVariable int id){
		
		Operation o = operationService.findById(id);
		
		
		if (o != null){
			try {
				Person p = personService.findByPersonalID(o.getRecordOperation().getId());
				OperationDTO retVal = OperationConverter.toDTO(o, (Patient)p);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/** Function returns operations.
	 * @param page
	 * @return Page of operations.
	 */
	@RequestMapping(value= "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getAllOperationsPageable(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		
		Page<Operation> operations = operationService.findAllPage(page);
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}
	
	/** Function returns all new operations.
	 * @param page
	 * @return Page of new operations.
	 */
	@RequestMapping(value= "/newOperations", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getNewOperationsPageable(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		
		Page<Operation> operations = operationService.findNewOperationsPage(page);
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}
	
	/**
	 * Function that deletes operation from DB
	 * @return 
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id){
		operationService.delete(id);
	}
	
	/** Function returns all operations of patient.
	 * @param page
	 * @return Page of new operations.
	 */
	@RequestMapping(value= "/patient/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getOperationsOfPatientPageable(
			@PathVariable int id, @PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		
		Person patient = personService.findOne(id);
		
		Page<Operation> operations = operationService.findByRecordId(page, patient.getPersonalID());
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}
	
	
	
	
}
