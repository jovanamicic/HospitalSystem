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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
import com.app.security.TokenUtils;
import com.app.service.OperationService;
import com.app.service.PatientService;
import com.app.service.PersonService;
import com.app.service.RecordService;
import com.app.service.RoomScheduleService;
import com.app.service.RoomService;

@RestController
@RequestMapping(value = "operations")
public class OperationController {

	private static final int DEFAULT_PAGE_SIZE = 10;
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

	@Autowired
	private TokenUtils tokenUtils;

	@PreAuthorize("hasAuthority('Add_new_operation')")
	@RequestMapping(value = "/saveOperation", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Operation> saveOperation(@RequestHeader("X-Auth-Token") String token,
			@RequestBody MedicalStaffScheduleDTO operation) throws ParseException {

		// find doctor
		String username = tokenUtils.getUsernameFromToken(token);
		Person doctor = personService.findByUsername(username);

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
	 * 
	 * @return
	 * @throws ParseException
	 */
	@PreAuthorize("hasAuthority('Edit_operation')")
	@RequestMapping(value = "/saveTimeAndRoom", method = RequestMethod.PUT)
	public ResponseEntity<Operation> updateOperation(@RequestHeader("X-Auth-Token") String token,
			@RequestBody OperationUpdateDTO o) throws ParseException {
		Operation retVal = operationService.findById(o.getOperationId());

		Room room = roomService.findOne(o.getRoomId());
		retVal.setRoom(room);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		Date convertedDate = sdf.parse(o.getDate());
		retVal.setDate(convertedDate);

		retVal = operationService.save(retVal);

		// save room schedule
		RoomSchedule roomSchedule = new RoomSchedule();
		roomSchedule.setDate(sdf.format(convertedDate));
		roomSchedule.setDuration(retVal.getDuration());
		roomSchedule.setRoomID(String.valueOf(room.getId()));
		roomScheduleService.save(roomSchedule);

		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

	/**
	 * Function gets data about one operation.
	 * 
	 * @param id
	 *            of Operation.
	 * @return Data about Operation.
	 */
	@PreAuthorize("hasAuthority('View_operation')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OperationDTO> getOperation(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id) {

		Operation o = operationService.findById(id);

		if (o != null) {
			try {
				Person p = personService.findByPersonalID(o.getRecordOperation().getId());
				OperationDTO retVal = OperationConverter.toDTO(o, (Patient) p);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function returns operations.
	 * 
	 * @param page
	 * @return Page of operations.
	 */
	@PreAuthorize("hasAuthority('View_all_operations')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getAllOperationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Page<Operation> operations = operationService.findAllPage(page);
		System.out.println(operations.getContent().size());
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	/**
	 * Function returns all new operations.
	 * 
	 * @param page
	 * @return Page of new operations.
	 */
	@PreAuthorize("hasAuthority('View_all_operations')")
	@RequestMapping(value = "/newOperations", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getNewOperationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Page<Operation> operations = operationService.findNewOperationsPage(page);
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	/**
	 * Function that deletes operation from DB
	 * 
	 * @return
	 */
	@PreAuthorize("hasAuthority('Delete_operation')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable int id) {
		operationService.delete(id);
	}

	/**
	 * Function returns all operations of patient.
	 * 
	 * @param page
	 * @return Page of new operations.
	 */
	@PreAuthorize("hasAuthority('View_patient_record')")
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getOperationsOfPatientPageable(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Patient patient = patientService.findOne(id);

		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		if (patient.getChosenDoctor().getId() != person.getId())
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		Page<Operation> operations = operationService.findByRecordId(page, patient.getPersonalID());
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getLoggedPatientOperations(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		String username = tokenUtils.getUsernameFromToken(token);
		Person patient = personService.findByUsername(username);

		Page<Operation> retVal = operationService.findByRecordId(page, patient.getPersonalID());
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

}
