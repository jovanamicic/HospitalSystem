package com.app.controllers;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.app.dto.ExaminationReportDTO;
import com.app.dto.ExaminationReportDTOWrapper;
import com.app.dto.OperationReportDTO;
import com.app.dto.OperationReportDTOWrapper;
import com.app.model.Examination;
import com.app.model.Operation;
import com.app.service.ExaminationService;
import com.app.service.OperationService;

@RestController
@RequestMapping(value = "government")
public class GovernmentController {

	private static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 

	@Autowired
	ExaminationService examinationsService;
	
	@Autowired
	OperationService operationsService;

	@RequestMapping(value="examinations",
				    method = RequestMethod.POST, 
				    consumes = "application/xml", 
				    produces = "application/xml")
	public ResponseEntity<String> getExaminationsReport(@RequestBody String xml) {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(false);
		dbFactory.setIgnoringComments(false);
		dbFactory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder dBuilder;
		String diagnosis = "";
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
			
			diagnosis = getString("diagnosis", doc.getDocumentElement());
			diagnosis = diagnosis.replaceAll("\\s+","");
			
			List<Examination> retVal = new ArrayList<>();
			
			if (diagnosis.isEmpty())
				retVal = examinationsService.findAll();
			else
				retVal = examinationsService.findByDiagnosis(diagnosis);
				
			List<ExaminationReportDTO> ret = new ArrayList<>();

			for (Examination e : retVal) {
				ExaminationReportDTO tmp = new ExaminationReportDTO();
				tmp.setDate(dt.format(e.getDate()));
				tmp.setDiagnosis(e.getDiagnosis());
				tmp.setId(Integer.toString(e.getId()));
				tmp.setName(e.getName());
				tmp.setSymptons(e.getSymptons());
				ret.add(tmp);

			}

			ExaminationReportDTOWrapper wrapper = new ExaminationReportDTOWrapper();
			wrapper.getExaminations().addAll(ret);

			JAXBContext jaxbContext;
			String xmlString;
			jaxbContext = JAXBContext.newInstance(ExaminationReportDTOWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(wrapper, sw);
			xmlString = sw.toString();

			return new ResponseEntity<String>(xmlString, HttpStatus.OK);

		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (SAXException e1) {
			e1.printStackTrace(); 
			//XEE attack! 
			System.exit(0); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} 
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="operations",
					method = RequestMethod.POST, 
					consumes = "application/xml", 
					produces = "application/xml")
	public ResponseEntity<String> getOperationsReport(@RequestBody String xml) throws Exception {
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

		dbFactory.setValidating(false);
		dbFactory.setNamespaceAware(false);
		dbFactory.setIgnoringComments(false);
		dbFactory.setIgnoringElementContentWhitespace(true);

		DocumentBuilder dBuilder;
		
		String name = "";
		String startDate = "";
		String endDate = "";
		
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new InputSource(new StringReader(xml)));
			
			name = getString("diagnosis", doc.getDocumentElement());
			name = name.replaceAll("\\s+","");
			
			startDate = getString("startDate", doc.getDocumentElement());
			startDate = startDate.replaceAll("\\s+","");
		
			endDate = getString("endDate", doc.getDocumentElement());
			endDate = endDate.replaceAll("\\s+","");
		
			if (name.isEmpty())
				name = "%"; //any name
			else
				name = "%" + name + "%";
		
		
			List<Operation> retVal = new ArrayList<>();
			
			//get all by name (if name is null, return all)
			if (startDate.isEmpty() && endDate.isEmpty())  
				retVal = operationsService.findByName(name);
			
			//get all by date between
			else if (!startDate.isEmpty() && !endDate.isEmpty()) 
				retVal = operationsService.findByDate(dt.parse(startDate), dt.parse(endDate), name);
			
			//get all after date
			else if (!startDate.isEmpty() && endDate.isEmpty()) 
				retVal = operationsService.findByDateAfter(dt.parse(startDate), name);
			
			//get all before date
			else if (startDate.isEmpty() && !endDate.isEmpty()) 
				retVal = operationsService.findByDateBefore(dt.parse(endDate), name);

			List<OperationReportDTO> ret = new ArrayList<>();

			for (Operation e : retVal) {
				OperationReportDTO tmp = new OperationReportDTO();
				tmp.setDate(e.getDate());
				tmp.setId(Integer.toString(e.getId()));
				tmp.setName(e.getName());
				ret.add(tmp);

			}

			OperationReportDTOWrapper wrapper = new OperationReportDTOWrapper();
			wrapper.getOperations().addAll(ret);

			JAXBContext jaxbContext;
			String xmlString;
			jaxbContext = JAXBContext.newInstance(OperationReportDTOWrapper.class);
			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			StringWriter sw = new StringWriter();
			jaxbMarshaller.marshal(wrapper, sw);
			xmlString = sw.toString();

			return new ResponseEntity<String>(xmlString, HttpStatus.OK);
			
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (SAXException e1) {
			e1.printStackTrace(); 
			//XEE attack! 
			System.exit(0); 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (IOException e1) {
			e1.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (JAXBException e1) {
			e1.printStackTrace();
		} 
		
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		
		
	}

	protected String getString(String tagName, Element element) {

		// badly configured DOM parser will return value of last element -> good
		// for XML Injection attack

		NodeList list = element.getElementsByTagName(tagName);
		if (list != null && list.getLength() > 0) {
			NodeList subList = list.item(list.getLength() - 1).getChildNodes();

			if (subList != null && subList.getLength() > 0) {
				return subList.item(subList.getLength() - 1).getNodeValue();
			}
		}

		return "";
	}

}
