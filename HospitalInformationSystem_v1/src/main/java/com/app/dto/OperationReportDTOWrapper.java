package com.app.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "operations" })
@XmlRootElement(name = "Operations")
public class OperationReportDTOWrapper {
	
	@XmlElement(name = "operation")
    private List<OperationReportDTO> operations = null;
 
    public List<OperationReportDTO> getOperations() {
    	
    	if (operations == null)
    		operations = new ArrayList<OperationReportDTO>();
    	
        return operations;
    }
 
    public void setOperations(List<OperationReportDTO> operations) {
        this.operations = operations;
    }

}

