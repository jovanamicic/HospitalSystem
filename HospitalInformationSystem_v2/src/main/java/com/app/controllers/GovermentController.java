package com.app.controllers;

import java.util.List;

import javax.annotation.security.PermitAll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.app.model.Operation;
import com.app.service.OperationService;

@RestController
@RequestMapping(value = "goverment")

public class GovermentController {
	
	@Autowired
	OperationService operationService;
	
	@PreAuthorize("permitAll()")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<Operation>> get() {
		System.out.println("heheheh");
		List<Operation> retVal = operationService.findAll();
		return new ResponseEntity<List<Operation>>(retVal, HttpStatus.OK);
	}

}
