//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-833 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.08.14 at 06:32:41 PM EST 
//

package au.edu.swin.ict.road.regulator.bindings;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each Java content interface and Java
 * element interface generated in the au.edu.swin.ict.road.regulator.bindings
 * package.
 * <p>
 * An ObjectFactory allows you to programatically construct new instances of the
 * Java representation for XML content. The Java representation of XML content
 * can consist of schema derived interfaces and classes representing the binding
 * of schema type definitions, element declarations and model groups. Factory
 * methods for each of these are provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of
     * schema derived classes for package:
     * au.edu.swin.ict.road.regulator.bindings
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link JFactType }
     * 
     */
    public JFactType createJFactType() {
	return new JFactType();
    }

    /**
     * Create an instance of {@link JFactAttributeType }
     * 
     */
    public JFactAttributeType createJFactAttributeType() {
	return new JFactAttributeType();
    }

    /**
     * Create an instance of {@link Facts }
     * 
     */
    public Facts createFacts() {
	return new Facts();
    }

    /**
     * Create an instance of {@link JFactAttributesType }
     * 
     */
    public JFactAttributesType createJFactAttributesType() {
	return new JFactAttributesType();
    }

    /**
     * Create an instance of {@link JFactIdentifierType }
     * 
     */
    public JFactIdentifierType createJFactIdentifierType() {
	return new JFactIdentifierType();
    }

}