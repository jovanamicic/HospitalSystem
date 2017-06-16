package com.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.converters.MedicalStaffScheduleConverter;
import com.app.dto.ExaminationOperationDetailsDTO;
import com.app.dto.ExaminationOperationIdDTO;
import com.app.dto.MedicalStaffScheduleDTO;
import com.app.dto.ObjectIDDTO;
import com.app.dto.PatientDTO;
import com.app.model.Address;
import com.app.model.Examination;
import com.app.model.MedicalStaff;
import com.app.model.Operation;
import com.app.model.Patient;
import com.app.model.Person;
import com.app.model.Record;
import com.app.service.AddressService;
import com.app.service.ExaminationService;
import com.app.service.MedicalStaffService;
import com.app.service.OperationService;
import com.app.service.PatientService;
import com.app.service.PersonService;
import com.app.service.RecordService;

@RestController
@RequestMapping(value = "patients")
public class PatientController {
	
	private static final int DEFAULT_PAGE_SIZE = 10;
	private static final int DEFAULT_PAGE_NUMBER = 0;
	private SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); 
	
	
	@Autowired
	private PatientService patientService;
	
	@Autowired
	private MedicalStaffService medicalStaffService;
	
	@Autowired
	private AddressService addressService;
	
	@Autowired
	private RecordService recordService;
	
	@Autowired
	private OperationService operationService;
	
	@Autowired
	private ExaminationService examinationService;
	
	@Autowired
	private PersonService personService;
	
	
	/** Function that register new patient on system.
	 * @param dto Data about user from form.
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<ObjectIDDTO> registerPatient(@RequestBody PatientDTO dto) {
		Patient patient = new Patient();
		if (dto.getCountry() != "" || dto.getCity() != "" || dto.getZipCode() != "" || dto.getStreet() != "" || dto.getNumber()!= ""){
			Address address = new Address();
			if (dto.getCountry() != "")
				address.setCountry(dto.getCountry());
			if(dto.getCity() != "")
				address.setCity(dto.getCity());
			if(dto.getZipCode() != "")
				address.setZipCode(Integer.parseInt(dto.getZipCode()));
			if(dto.getStreet() != "")
				address.setStreet(dto.getStreet());
			if(dto.getNumber() != "")
				address.setNumber(dto.getNumber());
	
			address = addressService.save(address);
			patient.setAddress(address);
		}
		patient.setName(dto.getName());
		patient.setSurname(dto.getSurname());
		patient.setPersonalID(Long.parseLong(dto.getPersonalID()));
		patient.setGender(dto.getGender());
		if(dto.getEmail() != "")
			patient.setEmail(dto.getEmail());
		MedicalStaff doctor = medicalStaffService.findOne(dto.getDoctor());
		patient.setChosenDoctor(doctor);
		if(dto.getBirthday() != ""){
			Date birthday = null;
			try {
				birthday = formatter.parse(dto.getBirthday());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			patient.setBirthday(birthday);
		}
		patient.setUsername(patient.getName().toLowerCase()+patient.getSurname().toLowerCase());
		patient.setPassword("lozinka");
		
		patient = patientService.save(patient);
		
		Record record = new Record();
		record.setId(patient.getPersonalID());
		record.setExaminations(new HashSet<Examination>());
		record.setOperations(new HashSet<Operation>());
		
		record = recordService.save(record);
		
		ObjectIDDTO retVal = new ObjectIDDTO(patient.getId());
		return new ResponseEntity<>(retVal, HttpStatus.CREATED);
	}
	
	
	/**
	 * Function that returns all patients pageable
	 * @param page
	 * @return Page of patients
	 */
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public ResponseEntity<Page<Patient>> getAllPatients(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page){
		Page<Patient> patients = patientService.findAllPage(page);
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}
	
	
	/**
	 * Function that returns all patients of logged doctor pageable
	 * @param page and doctors ID
	 * @return Page of patients
	 */
	@RequestMapping(value = "/my", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<Page<Patient>> getMyPatients(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page, @RequestBody ObjectIDDTO person){
		int id = person.getId();
		Page<Patient> patients = patientService.findByChosenDoctor(id, page);
		return new ResponseEntity<>(patients, HttpStatus.OK);
	}
	
	/** Function gets data about one patient.
	 * @param id of patient.
	 * @return Data about patient.
	 */
	@RequestMapping(value= "/{id}", method = RequestMethod.GET)
	public ResponseEntity<PatientDTO> getPatient(@PathVariable int id){
		Patient p = patientService.findOne(id);
		
		PatientDTO retVal = new PatientDTO();
		if (p != null){
			
			retVal.setName(p.getName());
			retVal.setSurname(p.getSurname());
			if(p.getBirthday() != null)
				retVal.setBirthday(formatter.format(p.getBirthday()));
			retVal.setPersonalID(p.getPersonalID()+"");
			if(p.getGender() != null)
				retVal.setGender(p.getGender());
			
			if (p.getAddress() != null) {
				Address a = p.getAddress();
				retVal.setCountry(a.getCountry());
				retVal.setCity(a.getCity());
				retVal.setZipCode(a.getZipCode()+"");
				retVal.setStreet(a.getStreet());
				retVal.setNumber(a.getNumber());
				retVal.setDoctor(p.getChosenDoctor().getId());
			}
			
			retVal.setUsername(p.getUsername());
			if(p.getEmail() != null)
				retVal.setEmail(p.getEmail());
			
			if (p.getChosenDoctor() != null)
				retVal.setDoctor(p.getChosenDoctor().getId());
			
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	/**
	 * Function that return schedule for logged patient
	 * @param page
	 * @param person id
	 * @return
	 */
	@RequestMapping(value = "/schedule", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<List<MedicalStaffScheduleDTO>> getMySchedule(@RequestBody ObjectIDDTO person){
		int id = person.getId();
		
		Patient patient = patientService.findOne(id);		
		
		List<Examination> examinations = examinationService.findByRecordId(patient.getPersonalID());
		List<Operation> operations = operationService.findByRecordId(patient.getPersonalID());
		
		List<MedicalStaffScheduleDTO> retVal = MedicalStaffScheduleConverter.toSchedule(operations, examinations);
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	
	/**
	 * Function that return details of operation or examination
	 * @param type and id of examination/operation
	 * @return
	 */
	@RequestMapping(value = "/operationExaminationDetails", method = RequestMethod.POST, consumes = "application/json")
	public ResponseEntity<ExaminationOperationDetailsDTO> getDetails(@RequestBody ExaminationOperationIdDTO eo){
		int id = eo.getId();
		String type = eo.getType();
		
		ExaminationOperationDetailsDTO retVal = new ExaminationOperationDetailsDTO();
		
		if(type.equalsIgnoreCase("operacija")){
			Operation operation = operationService.findById(id);
			Person patient = personService.findByPersonalID(operation.getRecordOperation().getId());
			
			retVal.setDate(operation.getDate().toString());
			retVal.setDoctorId(operation.getHeadDoctor().getId());
			retVal.setDoctor(operation.getHeadDoctor().getName() + " " + operation.getHeadDoctor().getSurname());
			retVal.setName(operation.getName());
			retVal.setType(eo.getType());
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
		}
		else {
			Examination examination = examinationService.findById(id);
			Person patient = personService.findByPersonalID(examination.getRecord().getId());
			
			retVal.setDate(examination.getDate().toString());
			retVal.setDoctorId(examination.getDoctor().getId());
			retVal.setDoctor(examination.getDoctor().getName() + " " + examination.getDoctor().getSurname());
			retVal.setName(examination.getName());
			retVal.setType(eo.getType());
			retVal.setPatient(patient.getName() + " " + patient.getSurname());
			retVal.setPatientId(patient.getId());
			
			retVal.setDiagnosis(examination.getDiagnosis());
			retVal.setSymptons(examination.getSymptons());
			retVal.setTherapy(examination.getTherapy());
		}
		
		return new ResponseEntity<>(retVal, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.PUT, consumes = "application/json")
	public ResponseEntity<Void> changeProfile(@RequestBody PatientDTO dto, HttpSession session) {
		int id = (int) session.getAttribute("person");
		Patient p = patientService.findOne(id);
		
		if (p.getAddress() != null) {
			Address address = p.getAddress();
			
			address.setCountry(dto.getCountry());
			address.setCity(dto.getCity());
			address.setZipCode(Integer.parseInt(dto.getZipCode()));
			address.setStreet(dto.getStreet());
			address.setNumber(dto.getNumber());
	
			address = addressService.save(address);
			p.setAddress(address);
		}
		
		if(personService.emailUnique(dto.getEmail()))
			p.setEmail(dto.getEmail());
		
		if(personService.usernameUnique(dto.getUsername()))
			p.setUsername(dto.getUsername());
		
		patientService.save(p);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	
	/**
	 * Function that returns patients based on serach string.
	 * Doctor can search patients by personal ID, name and surname
	 * @param searchData
	 * @return list of patients
	 */
	@RequestMapping(value= "/search/{searchData}", method = RequestMethod.GET)
	public ResponseEntity<Page<Patient>> serachPatients(@PageableDefault(page = DEFAULT_PAGE_NUMBER, size = DEFAULT_PAGE_SIZE) Pageable page,@PathVariable String searchData){
		
		Page<Patient> retVal = patientService.findBySearchData(searchData, page);
		if (retVal.getNumberOfElements() != 0 )
			return new ResponseEntity<>(retVal, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			
	}
	
	@RequestMapping(value= "/username", method = RequestMethod.GET)
	public ResponseEntity<Void> checkUsername(@RequestBody String username){
		if (personService.usernameUnique(username)){
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value= "/email", method = RequestMethod.POST)
	public ResponseEntity<Void> checkEmail(@RequestBody String email){
		if (personService.emailUnique(email)){
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value= "/personalID", method = RequestMethod.POST)
	public ResponseEntity<Void> checkPID(@RequestBody String personalID){
		if (personService.personalIDUnique(personalID)){
			return new ResponseEntity<>(HttpStatus.OK);
		}
		else{
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value= "/birthday", method = RequestMethod.POST)
	public ResponseEntity<Void> checkBirthday(@RequestBody String birthday){
		Date bday = null;
		try {
			bday = formatter.parse(birthday);
		} catch (ParseException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		if (bday.after(new Date())){
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		else {
			return new ResponseEntity<>(HttpStatus.OK);
		}
	}
}
