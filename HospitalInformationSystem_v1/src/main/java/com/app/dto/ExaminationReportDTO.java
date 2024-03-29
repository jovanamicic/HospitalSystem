package com.app.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="diagnosis" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="symptons" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *         &lt;element name="record_id" type="{http://www.w3.org/2001/XMLSchema}anyType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "name", "date", "diagnosis", "symptons" })
@XmlRootElement(name = "Examination")
public class ExaminationReportDTO {

	@XmlElement(required = true)
	protected String name;
	@XmlElement(required = true)
	protected String date;
	@XmlElement(required = true)
	protected String diagnosis;
	@XmlElement(required = true)
	protected String symptons;
	@XmlAttribute(name = "id")
	@XmlSchemaType(name = "anySimpleType")
	protected String id;

	/**
	 * Gets the value of the name property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the value of the name property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setName(String value) {
		this.name = value;
	}

	/**
	 * Gets the value of the date property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public String getDate() {
		return date;
	}

	/**
	 * Sets the value of the date property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setDate(String value) {
		this.date = value;
	}

	/**
	 * Gets the value of the diagnosis property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public String getDiagnosis() {
		return diagnosis;
	}

	/**
	 * Sets the value of the diagnosis property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setDiagnosis(String value) {
		this.diagnosis = value;
	}

	/**
	 * Gets the value of the symptons property.
	 * 
	 * @return possible object is {@link Object }
	 * 
	 */
	public String getSymptons() {
		return symptons;
	}

	/**
	 * Sets the value of the symptons property.
	 * 
	 * @param value
	 *            allowed object is {@link Object }
	 * 
	 */
	public void setSymptons(String value) {
		this.symptons = value;
	}

	/**
	 * Gets the value of the id property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setId(String value) {
		this.id = value;
	}

}
