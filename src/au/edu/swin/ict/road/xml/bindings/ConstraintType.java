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
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for ConstraintType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="ConstraintType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="Id" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Expression" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="enabled" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="language" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="soft" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ConstraintType")
public class ConstraintType {

    @XmlAttribute(name = "Id")
    protected String id;
    @XmlAttribute(name = "Expression")
    protected String expression;
    @XmlAttribute
    protected Boolean enabled;
    @XmlAttribute
    protected String language;
    @XmlAttribute
    protected Boolean soft;

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
     * Gets the value of the expression property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getExpression() {
	return expression;
    }

    /**
     * Sets the value of the expression property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setExpression(String value) {
	this.expression = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isEnabled() {
	return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setEnabled(Boolean value) {
	this.enabled = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getLanguage() {
	return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setLanguage(String value) {
	this.language = value;
    }

    /**
     * Gets the value of the soft property.
     * 
     * @return possible object is {@link Boolean }
     * 
     */
    public Boolean isSoft() {
	return soft;
    }

    /**
     * Sets the value of the soft property.
     * 
     * @param value
     *            allowed object is {@link Boolean }
     * 
     */
    public void setSoft(Boolean value) {
	this.soft = value;
    }

}
