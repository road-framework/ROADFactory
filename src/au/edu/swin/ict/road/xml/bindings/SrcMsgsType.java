//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.01 at 06:11:07 PM EST 
//

package au.edu.swin.ict.road.xml.bindings;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for SrcMsgsType complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="SrcMsgsType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SrcMsg" type="{http://www.ict.swin.edu.au/serendip/types}SrcMsgType" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="transformation" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SrcMsgsType", propOrder = { "srcMsg" })
public class SrcMsgsType {

    @XmlElement(name = "SrcMsg", namespace = "http://www.ict.swin.edu.au/serendip/types", required = true)
    protected List<SrcMsgType> srcMsg;
    @XmlAttribute
    protected String transformation;

    /**
     * Gets the value of the srcMsg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the srcMsg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * 
     * <pre>
     * getSrcMsg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SrcMsgType }
     * 
     * 
     */
    public List<SrcMsgType> getSrcMsg() {
	if (srcMsg == null) {
	    srcMsg = new ArrayList<SrcMsgType>();
	}
	return this.srcMsg;
    }

    /**
     * Gets the value of the transformation property.
     * 
     * @return possible object is {@link String }
     * 
     */
    public String getTransformation() {
	return transformation;
    }

    /**
     * Sets the value of the transformation property.
     * 
     * @param value
     *            allowed object is {@link String }
     * 
     */
    public void setTransformation(String value) {
	this.transformation = value;
    }

}