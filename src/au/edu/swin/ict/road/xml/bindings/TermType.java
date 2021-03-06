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
 * Java class for TermType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="TermType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Operation" type="{http://www.swin.edu.au/ict/road/term}OperationType"/>
 *         &lt;element name="Direction" type="{http://www.swin.edu.au/ict/road/term}DirectionType"/>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="messageType" default="push">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="push"/>
 *             &lt;enumeration value="pull"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="deonticType" default="permission">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="permission"/>
 *             &lt;enumeration value="obligation"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TermType", namespace = "http://www.swin.edu.au/ict/road/term", propOrder = {
	"operation", "direction", "description" })
public class TermType {

    @XmlElement(name = "Operation", required = true)
    protected OperationType operation;
    @XmlElement(name = "Direction", required = true)
    protected DirectionType direction;
    @XmlElement(name = "Description")
    protected String description;
    @XmlAttribute(required = true)
    protected String id;
    @XmlAttribute
    protected String name;
    @XmlAttribute
    protected String messageType;
    @XmlAttribute
    protected String deonticType;

    /**
     * Gets the value of the operation property.
     * 
     * @return possible object is {@link OperationType }
     * 
     */
    public OperationType getOperation() {
	return operation;
    }

    /**
     * Sets the value of the operation property.
     * 
     * @param value
     *            allowed object is {@link OperationType }
     * 
     */
    public void setOperation(OperationType value) {
	this.operation = value;
    }

    /**
     * Gets the value of the direction property.
     * 
     * @return possible object is {@link DirectionType }
     * 
     */
    public DirectionType getDirection() {
	return direction;
    }

    /**
     * Sets the value of the direction property.
     * 
     * @param value
     *            allowed object is {@link DirectionType }
     * 
     */
    public void setDirection(DirectionType value) {
	this.direction = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDescription(String value) {
	this.description = value;
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

    /**
     * Gets the value of the messageType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getMessageType() {
	if (messageType == null) {
	    return "push";
	} else {
	    return messageType;
	}
    }

    /**
     * Sets the value of the messageType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setMessageType(String value) {
	this.messageType = value;
    }

    /**
     * Gets the value of the deonticType property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getDeonticType() {
	if (deonticType == null) {
	    return "permission";
	} else {
	    return deonticType;
	}
    }

    /**
     * Sets the value of the deonticType property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setDeonticType(String value) {
	this.deonticType = value;
    }

}
