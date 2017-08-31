package com.app.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "examinations" })
@XmlRootElement(name = "Examinations")
public class ExaminationReportDTOWrapper {
	
	@XmlElement(name = "examination")
    private List<ExaminationReportDTO> examinations = null;
 
    public List<ExaminationReportDTO> getExaminations() {
    	
    	if (examinations == null)
    		examinations = new ArrayList<ExaminationReportDTO>();
    	
        return examinations;
    }
 
    public void setExaminations(List<ExaminationReportDTO> examinations) {
        this.examinations = examinations;
    }

}
