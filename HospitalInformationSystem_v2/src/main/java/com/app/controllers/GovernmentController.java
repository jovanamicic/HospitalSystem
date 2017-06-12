package com.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Operation;
import com.app.service.OperationService;

@RestController
@RequestMapping(value = "government")

public class GovernmentController {
	
	@Autowired
	OperationService operationService;
	
	@PreAuthorize("permitAll()")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Operation>> get(
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "startDate", required = false) String startDate,
			@RequestParam(value = "endDate", required = false) String endDate) {
		
		if (name == null)
			name = "%"; //any name
		else
			name = "%" + name + "%";
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
		
		List<Operation> retVal = new ArrayList<>();
		
		//get all by name (if name is null, return all)
		if (startDate == null && endDate == null)  
			retVal = operationService.findByName(name);
		
		//get all by date between
		else if (startDate != null && endDate != null) 
			try {
				retVal = operationService.findByDate(dt.parse(startDate), dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all after date
		else if (startDate != null && endDate == null) 
			try {
				retVal = operationService.findByDateAfter(dt.parse(startDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		//get all before date
		else if (startDate == null && endDate != null) 
			try {
				retVal = operationService.findByDateBefore(dt.parse(endDate), name);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		return new ResponseEntity<List<Operation>>(retVal, HttpStatus.OK);
	}
	

}
