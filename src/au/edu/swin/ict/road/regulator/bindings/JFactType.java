//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.14 at 06:32:41 PM EST 
//

package au.edu.swin.ict.road.regulator.bindings;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for JFactType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="JFactType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributes" type="{http://www.swin.edu.au/ict/road/regulator/Facts}JFactAttributesType"/>
 *         &lt;element name="identifier" type="{http://www.swin.edu.au/ict/road/regulator/Facts}JFactIdentifierType"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="source" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "JFactType", propOrder = { "attributes", "identifier", "name",
	"source" })
public class JFactType {

    @XmlElement(required = true)
    protected JFactAttributesType attributes;
    @XmlElement(required = true)
    protected JFactIdentifierType identifier;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String source;

    /**
     * Gets the value of the attributes property.
     * 
     * @return possible object is {@link JFactAttributesType }
     * 
     */
    public JFactAttributesType getAttributes() {
	return attributes;
    }

    /**
     * Sets the value of the attributes property.
     * 
     * @param value
     *            allowed object is {@link JFactAttributesType }
     * 
     */
    public void setAttributes(JFactAttributesType value) {
	this.attributes = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return possible object is {@link JFactIdentifierType }
     * 
     */
    public JFactIdentifierType getIdentifier() {
	return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *            allowed object is {@link JFactIdentifierType }
     * 
     */
    public void setIdentifier(JFactIdentifierType value) {
	this.identifier = value;
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
     * Gets the value of the source property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getSource() {
	return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setSource(String value) {
	this.source = value;
    }

}
