//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.01 at 06:11:07 PM EST 
//

package au.edu.swin.ict.road.xml.bindings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for OperationType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="OperationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Parameters" type="{http://www.swin.edu.au/ict/road/term}ParamsType" minOccurs="0"/>
 *         &lt;element name="Return" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "OperationType", namespace = "http://www.swin.edu.au/ict/road/term", propOrder = {
	"parameters", "_return" })
public class OperationType {

    @XmlElement(name = "Parameters")
    protected ParamsType parameters;
    @XmlElement(name = "Return")
    protected String _return;
    @XmlAttribute(required = true)
    protected String name;

    /**
     * Gets the value of the parameters property.
     * 
     * @return possible object is {@link ParamsType }
     * 
     */
    public ParamsType getParameters() {
	return parameters;
    }

    /**
     * Sets the value of the parameters property.
     * 
     * @param value
     *            allowed object is {@link ParamsType }
     * 
     */
    public void setParameters(ParamsType value) {
	this.parameters = value;
    }

    /**
     * Gets the value of the return property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getReturn() {
	return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setReturn(String value) {
	this._return = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getName() {
	return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setName(String value) {
	this.name = value;
    }

}