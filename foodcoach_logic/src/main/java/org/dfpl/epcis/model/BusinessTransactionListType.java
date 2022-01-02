//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.11 at 04:47:34 PM KST 
//

package org.dfpl.epcis.model;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for BusinessTransactionListType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="BusinessTransactionListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bizTransaction" type="{urn:epcglobal:epcis:xsd:2}BusinessTransactionType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BusinessTransactionListType", propOrder = { "bizTransaction" })
public class BusinessTransactionListType {

	@XmlElement(required = true)
	protected List<BusinessTransactionType> bizTransaction;

	public BusinessTransactionListType(List<BusinessTransactionType> bizTransaction) {
		this.bizTransaction = bizTransaction;
	}

	public BusinessTransactionListType() {
	}

	public void setBizTransaction(List<BusinessTransactionType> bizTransaction) {
		this.bizTransaction = bizTransaction;
	}

	/**
	 * Gets the value of the bizTransaction property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot.
	 * Therefore any modification you make to the returned list will be present
	 * inside the JAXB object. This is why there is not a <CODE>set</CODE> method
	 * for the bizTransaction property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getBizTransaction().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list
	 * {@link BusinessTransactionType }
	 * 
	 * 
	 */
	public List<BusinessTransactionType> getBizTransaction() {
		if (bizTransaction == null) {
			bizTransaction = new ArrayList<BusinessTransactionType>();
		}
		return this.bizTransaction;
	}

}
