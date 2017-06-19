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
import com.app.model.Examination;
import com.app.service.ExaminationService;

@RestController
@RequestMapping(value = "government")
public class GovernmentController {

	public static final String ENTITY_EXPANSION_LIMIT = "jdk.xml.entityExpansionLimit";
	
	private static SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 

	@Autowired
	ExaminationService examinationService;

	@RequestMapping(value="operations",
				    method = RequestMethod.POST, 
				    consumes = "application/xml", 
				    produces = "application/xml")
	public ResponseEntity<String> getAllExaminationsReport(@RequestBody String xml) {
		System.setProperty(ENTITY_EXPANSION_LIMIT, "0");

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
			
			if (diagnosis == null || diagnosis.isEmpty())
				retVal = examinationService.findAll();
			else
				retVal = examinationService.findByDiagnosis(diagnosis);
				
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

		return null;
	}

}
