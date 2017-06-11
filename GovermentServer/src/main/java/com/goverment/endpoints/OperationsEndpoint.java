package com.goverment.endpoints;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.goverment.model.GetOperationsRequest;
import com.goverment.model.GetOperationsResponse;
import com.goverment.model.Operation;
import com.goverment.model.Report;
import com.goverment.service.OperationService;

@Endpoint
public class OperationsEndpoint {
	
	private static final String NAMESPACE_URI = "com.goverment.model";

	private OperationService operationService;

	@Autowired
	public OperationsEndpoint(OperationService operationService) {
		this.operationService = operationService;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getOperationsRequest")
	@ResponsePayload
	public GetOperationsResponse getOperation(@RequestPayload GetOperationsRequest request) {
		GetOperationsResponse response = new GetOperationsResponse();
		
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
		
		String name = request.getName();
		String startDate = request.getStartDate();
		String endDate = request.getEndDate();
		
		if (name == null)
			name = "%"; //any name
		else
			name = "%" + name + "%";
		
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
		
		Report report = new Report();
		report.getOperation().addAll(retVal);
		response.setReport(report);
		
		return response;
	}

}
