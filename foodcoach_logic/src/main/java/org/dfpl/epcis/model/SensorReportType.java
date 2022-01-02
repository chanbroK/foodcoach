//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.08.11 at 04:47:34 PM KST 
//

package org.dfpl.epcis.model;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

/**
 * <p>
 * Java class for SensorReportType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SensorReportType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="type" use="required" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="value" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="component" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="stringValue" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="booleanValue" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="hexBinaryValue" type="{http://www.w3.org/2001/XMLSchema}hexBinary" />
 *       &lt;attribute name="uriValue" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="uom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="minValue" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="maxValue" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="sDev" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="chemicalSubstance" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="microorganism" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="deviceID" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="deviceMetadata" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="rawData" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;attribute name="time" type="{http://www.w3.org/2001/XMLSchema}dateTime" />
 *       &lt;attribute name="meanValue" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="percRank" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="percValue" type="{http://www.w3.org/2001/XMLSchema}float" />
 *       &lt;attribute name="dataProcessingMethod" type="{http://www.w3.org/2001/XMLSchema}anyURI" />
 *       &lt;anyAttribute processContents='lax'/>
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SensorReportType", propOrder = { "value" })
public class SensorReportType {

	@XmlValue
	protected String value;
	@XmlAttribute(name = "type", required = true)
	@XmlSchemaType(name = "anyURI")
	protected String type;
	@XmlAttribute(name = "value")
	protected Float valueAttribute;
	@XmlAttribute(name = "component")
	protected String component;
	@XmlAttribute(name = "stringValue")
	protected String stringValue;
	@XmlAttribute(name = "booleanValue")
	protected Boolean booleanValue;
	@XmlAttribute(name = "hexBinaryValue")
	@XmlJavaTypeAdapter(HexBinaryAdapter.class)
	@XmlSchemaType(name = "hexBinary")
	protected byte[] hexBinaryValue;
	@XmlAttribute(name = "uriValue")
	@XmlSchemaType(name = "anyURI")
	protected String uriValue;
	@XmlAttribute(name = "uom")
	protected String uom;
	@XmlAttribute(name = "minValue")
	protected Float minValue;
	@XmlAttribute(name = "maxValue")
	protected Float maxValue;
	@XmlAttribute(name = "sDev")
	protected Float sDev;
	@XmlAttribute(name = "chemicalSubstance")
	@XmlSchemaType(name = "anyURI")
	protected String chemicalSubstance;
	@XmlAttribute(name = "microorganism")
	@XmlSchemaType(name = "anyURI")
	protected String microorganism;
	@XmlAttribute(name = "deviceID")
	@XmlSchemaType(name = "anyURI")
	protected String deviceID;
	@XmlAttribute(name = "deviceMetadata")
	@XmlSchemaType(name = "anyURI")
	protected String deviceMetadata;
	@XmlAttribute(name = "rawData")
	@XmlSchemaType(name = "anyURI")
	protected String rawData;
	@XmlAttribute(name = "time")
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar time;
	@XmlAttribute(name = "meanValue")
	protected Float meanValue;
	@XmlAttribute(name = "percRank")
	protected Float percRank;
	@XmlAttribute(name = "percValue")
	protected Float percValue;
	@XmlAttribute(name = "dataProcessingMethod")
	@XmlSchemaType(name = "anyURI")
	protected String dataProcessingMethod;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	public Float getsDev() {
		return sDev;
	}

	public void setsDev(Float sDev) {
		this.sDev = sDev;
	}

	public Boolean getBooleanValue() {
		return booleanValue;
	}

	public void setOtherAttributes(Map<QName, String> otherAttributes) {
		this.otherAttributes = otherAttributes;
	}

	/**
	 * Gets the value of the value property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Gets the value of the type property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the value of the type property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setType(String value) {
		this.type = value;
	}

	/**
	 * Gets the value of the valueAttribute property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getValueAttribute() {
		return valueAttribute;
	}

	/**
	 * Sets the value of the valueAttribute property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setValueAttribute(Float value) {
		this.valueAttribute = value;
	}

	/**
	 * Gets the value of the component property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getComponent() {
		return component;
	}

	/**
	 * Sets the value of the component property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setComponent(String value) {
		this.component = value;
	}

	/**
	 * Gets the value of the stringValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getStringValue() {
		return stringValue;
	}

	/**
	 * Sets the value of the stringValue property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setStringValue(String value) {
		this.stringValue = value;
	}

	/**
	 * Gets the value of the booleanValue property.
	 * 
	 * @return possible object is {@link Boolean }
	 * 
	 */
	public Boolean isBooleanValue() {
		return booleanValue;
	}

	/**
	 * Sets the value of the booleanValue property.
	 * 
	 * @param value allowed object is {@link Boolean }
	 * 
	 */
	public void setBooleanValue(Boolean value) {
		this.booleanValue = value;
	}

	/**
	 * Gets the value of the hexBinaryValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public byte[] getHexBinaryValue() {
		return hexBinaryValue;
	}

	/**
	 * Sets the value of the hexBinaryValue property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setHexBinaryValue(byte[] value) {
		this.hexBinaryValue = value;
	}

	/**
	 * Gets the value of the uriValue property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUriValue() {
		return uriValue;
	}

	/**
	 * Sets the value of the uriValue property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setUriValue(String value) {
		this.uriValue = value;
	}

	/**
	 * Gets the value of the uom property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * Sets the value of the uom property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setUom(String value) {
		this.uom = value;
	}

	/**
	 * Gets the value of the minValue property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getMinValue() {
		return minValue;
	}

	/**
	 * Sets the value of the minValue property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setMinValue(Float value) {
		this.minValue = value;
	}

	/**
	 * Gets the value of the maxValue property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getMaxValue() {
		return maxValue;
	}

	/**
	 * Sets the value of the maxValue property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setMaxValue(Float value) {
		this.maxValue = value;
	}

	/**
	 * Gets the value of the sDev property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getSDev() {
		return sDev;
	}

	/**
	 * Sets the value of the sDev property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setSDev(Float value) {
		this.sDev = value;
	}

	/**
	 * Gets the value of the chemicalSubstance property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getChemicalSubstance() {
		return chemicalSubstance;
	}

	/**
	 * Sets the value of the chemicalSubstance property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setChemicalSubstance(String value) {
		this.chemicalSubstance = value;
	}

	/**
	 * Gets the value of the microorganism property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getMicroorganism() {
		return microorganism;
	}

	/**
	 * Sets the value of the microorganism property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setMicroorganism(String value) {
		this.microorganism = value;
	}

	/**
	 * Gets the value of the deviceID property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeviceID() {
		return deviceID;
	}

	/**
	 * Sets the value of the deviceID property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDeviceID(String value) {
		this.deviceID = value;
	}

	/**
	 * Gets the value of the deviceMetadata property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDeviceMetadata() {
		return deviceMetadata;
	}

	/**
	 * Sets the value of the deviceMetadata property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDeviceMetadata(String value) {
		this.deviceMetadata = value;
	}

	/**
	 * Gets the value of the rawData property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getRawData() {
		return rawData;
	}

	/**
	 * Sets the value of the rawData property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setRawData(String value) {
		this.rawData = value;
	}

	/**
	 * Gets the value of the time property.
	 * 
	 * @return possible object is {@link XMLGregorianCalendar }
	 * 
	 */
	public XMLGregorianCalendar getTime() {
		return time;
	}

	/**
	 * Sets the value of the time property.
	 * 
	 * @param value allowed object is {@link XMLGregorianCalendar }
	 * 
	 */
	public void setTime(XMLGregorianCalendar value) {
		this.time = value;
	}

	/**
	 * Gets the value of the meanValue property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getMeanValue() {
		return meanValue;
	}

	/**
	 * Sets the value of the meanValue property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setMeanValue(Float value) {
		this.meanValue = value;
	}

	/**
	 * Gets the value of the percRank property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getPercRank() {
		return percRank;
	}

	/**
	 * Sets the value of the percRank property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setPercRank(Float value) {
		this.percRank = value;
	}

	/**
	 * Gets the value of the percValue property.
	 * 
	 * @return possible object is {@link Float }
	 * 
	 */
	public Float getPercValue() {
		return percValue;
	}

	/**
	 * Sets the value of the percValue property.
	 * 
	 * @param value allowed object is {@link Float }
	 * 
	 */
	public void setPercValue(Float value) {
		this.percValue = value;
	}

	/**
	 * Gets the value of the dataProcessingMethod property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getDataProcessingMethod() {
		return dataProcessingMethod;
	}

	/**
	 * Sets the value of the dataProcessingMethod property.
	 * 
	 * @param value allowed object is {@link String }
	 * 
	 */
	public void setDataProcessingMethod(String value) {
		this.dataProcessingMethod = value;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed property
	 * on this class.
	 * 
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string
	 * value of the attribute.
	 * 
	 * the map returned by this method is live, and you can add new attribute by
	 * updating the map directly. Because of this design, there's no setter.
	 * 
	 * 
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}
