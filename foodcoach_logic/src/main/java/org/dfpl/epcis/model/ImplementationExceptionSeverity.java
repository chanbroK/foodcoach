//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.11 at 04:47:34 PM KST 
//


package org.dfpl.epcis.model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ImplementationExceptionSeverity.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="ImplementationExceptionSeverity">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NCName">
 *     &lt;enumeration value="ERROR"/>
 *     &lt;enumeration value="SEVERE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "ImplementationExceptionSeverity", namespace = "urn:epcglobal:epcis-query:xsd:1")
@XmlEnum
public enum ImplementationExceptionSeverity {

    ERROR,
    SEVERE;

    public String value() {
        return name();
    }

    public static ImplementationExceptionSeverity fromValue(String v) {
        return valueOf(v);
    }

}