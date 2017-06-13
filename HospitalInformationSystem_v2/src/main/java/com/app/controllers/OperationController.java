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
import com.app.model.MedicalStaff;
import com.app.model.Operation;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.model.Record;
import com.app.model.Room;
import com.app.model.RoomSchedule;
import com.app.security.AESencryption;
import com.app.security.Base64Utility;
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
	
	@Autowired
	private AESencryption aesEncription;
	
	@Autowired
	private Base64Utility base64Utility;

	/**
	 * Function that creates new operation.
	 * @param token
	 * @param operation
	 * @return
	 * @throws ParseException
	 */
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
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		// find patient record
		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Record record = recordService.findById(personalIdEncoded);
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
	 * Function that update time and room for operation.
	 * @param token
	 * @param o
	 * @return
	 */
	@PreAuthorize("hasAuthority('Edit_operation')")
	@RequestMapping(value = "/saveTimeAndRoom", method = RequestMethod.PUT)
	public ResponseEntity<Operation> updateOperation(@RequestHeader("X-Auth-Token") String token,
			@RequestBody OperationUpdateDTO o) {
		Operation retVal = operationService.findById(o.getOperationId());

		if (retVal == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		Room room = roomService.findOne(o.getRoomId());
		
		if (room == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		retVal.setRoom(room);

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm");
		Date convertedDate;
		try {
			convertedDate = sdf.parse(o.getDate());
			retVal.setDate(convertedDate);
		} catch (ParseException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		
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
	 * @param token
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_operation')")
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<OperationDTO> getOperation(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id) {

		String username = tokenUtils.getUsernameFromToken(token);
		Person loggedPerson = personService.findByUsername(username);
		
		Operation o = operationService.findById(id);

		if (o != null) {
			try {
				//return 401 if this is not operation of logged patient
				if((loggedPerson instanceof Patient) && (!o.getRecordOperation().getId().equals(base64Utility.encode(aesEncription.encrypt(loggedPerson.getPersonalID() + ""))))){
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				String encodedPersonalId = o.getRecordOperation().getId();
				byte[] personalIdBytes = base64Utility.decode(encodedPersonalId);
				byte[] decriptetBytes = aesEncription.decrypt(personalIdBytes);
				String decripted = new String(decriptetBytes);
				Long personalIdDecoded = Long.parseLong(decripted);
				Person p = personService.findByPersonalID(personalIdDecoded);
				
				//return 401 if this is not operation of logged doctor
				if((loggedPerson instanceof MedicalStaff) && (((Patient) p).getChosenDoctor().getPersonalID() != loggedPerson.getPersonalID())){
					return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
				}
				
				OperationDTO retVal = OperationConverter.toDTO(o, (Patient) p);
				return new ResponseEntity<>(retVal, HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	/**
	 * Function returns operations pageable.
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_operations')")
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getAllOperationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Page<Operation> operations = operationService.findAllPage(page);
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	/**
	 * Function returns all new operations pageable.
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_all_operations')")
	@RequestMapping(value = "/newOperations", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getNewOperationsPageable(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Page<Operation> operations = operationService.findNewOperationsPage(page);
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	/**
	 * Function that deletes operation from DB.
	 * @param token
	 * @param id
	 */
	@PreAuthorize("hasAuthority('Delete_operation')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void delete(@RequestHeader("X-Auth-Token") String token, @PathVariable int id) {
		operationService.delete(id);
	}

	/**
	 * Function returns all operations of patient pageable.
	 * @param token
	 * @param id
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_patient_record')")
	@RequestMapping(value = "/patients/{id}", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getOperationsOfPatientPageable(@RequestHeader("X-Auth-Token") String token,
			@PathVariable int id,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		Patient patient = patientService.findOne(id);
		
		if (patient == null)
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		String username = tokenUtils.getUsernameFromToken(token);
		Person person = personService.findByUsername(username);

		if (patient.getChosenDoctor().getId() != person.getId())
			return new ResponseEntity<>(HttpStatus.FORBIDDEN);

		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Page<Operation> operations = operationService.findByRecordId(page, recordService.findById(personalIdEncoded).getId());
		return new ResponseEntity<>(operations, HttpStatus.OK);
	}

	/**
	 * Function that returns all operations of logged patient.
	 * @param token
	 * @param page
	 * @return
	 */
	@PreAuthorize("hasAuthority('View_patient_record')")
	@RequestMapping(value = "/my", method = RequestMethod.GET)
	public ResponseEntity<Page<Operation>> getLoggedPatientOperations(@RequestHeader("X-Auth-Token") String token,
			@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page) {

		String username = tokenUtils.getUsernameFromToken(token);
		Person patient = personService.findByUsername(username);

		String personalIdEncoded = base64Utility.encode(aesEncription.encrypt( patient.getPersonalID() + ""));
		Page<Operation> retVal = operationService.findByRecordId(page, recordService.findById(personalIdEncoded).getId());
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}

}
