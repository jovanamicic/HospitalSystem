package com.government.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
   "report"
})
@XmlRootElement(name = "getExaminationsResponse")
public class GetExaminationsResponse {

   @XmlElement(name = "Report", required = true)
   protected Report report;

   /**
    * Gets the value of the report property.
    * 
    * @return
    *     possible object is
    *     {@link Report }
    *     
    */
   public Report getReport() {
       return report;
   }

   /**
    * Sets the value of the report property.
    * 
    * @param value
    *     allowed object is
    *     {@link Report }
    *     
    */
   public void setReport(Report value) {
       this.report = value;
   }

}
