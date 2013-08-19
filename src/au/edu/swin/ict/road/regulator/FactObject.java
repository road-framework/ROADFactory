package au.edu.swin.ict.road.regulator;

import java.util.HashMap;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

/**
 * Class to represent facts used in the evaluation of the permission and
 * obligation rules. <code>FactObject</code>s are representation of knowledge of
 * a state, such as a transaction or an object, that are able to be manipulated
 * and inserted to the Drools engine.
 * <p/>
 * <code>FactObject</code>s are categorised into one single entity under the
 * term of fact type. This fact type is the class or group of the same fact,
 * such as cars, computers, or users. This fact type must be set upon
 * initialisation and have to correlate with the attribute
 * <code><em>name</em></code> in the <code>&lt;Fact&gt;</code> tag inside the
 * composite XML file. This fact type is <em>unable</em> to be changed.
 * <p/>
 * Each fact is distinguished by a single unique identifier that must be set
 * upon the initialisation of <code>FactObject</code>, and have to correlate
 * with the proper <code>&lt;Identifier&gt;</code> tag inside the
 * <code>&lt;Fact&gt;</code> tag in the composite XML. Without the unique
 * identifier, the <code>FactObject</code> cannot be differentiated from one
 * another. This unique identifier acts as some sort of primary key for each
 * fact. As a result, the unique identifier is <em>unable</em> to be changed.
 * <p/>
 * This <code>FactObject</code> class is specific to the
 * <code>ROADfactory</code> regulator extension. It is represented using a
 * <code>Map</code>, which stores the domain-specific attributes of the
 * <code>FactObject</code>, such as the transaction number. These attributes are
 * <em>able</em> to be changed if necessary.
 * <p/>
 * Each fact <em>type</em> also has a special element called <code>source</code>
 * . This element relates to the priority in which the facts will be overwritten
 * during an update. The fact <code>source</code> correlates to the
 * <em>source</em> attribute in the <code>&lt;Fact&gt;</code> tag within the
 * composite XML. It can be set to two values, <code>external</code> or
 * <code>internal</code> and by default will always revert to <em>internal</em>
 * source. It can be set using static call, as follows:
 * <p/>
 * <code>factObj.setFactSource(FactObject.EXTERNAL_SOURCE);</code>
 * <p/>
 * This class implements the <code>Cloneable</code> interface to be able to
 * clone its instances.
 * 
 * @author Jovino Margathe (jmargathe@gmail.com)
 * @author mtalib
 */
/**
 * @author Aditya
 * 
 */
public class FactObject implements Cloneable {
    private Logger log = Logger.getLogger(FactObject.class.getName());

    /*
     * Variables related to facts identifier is the name of the unique
     * identifier of each fact identifierValue is the value of the unique
     * identifier factType is the type/class/group of fact attributes is the
     * attribute of the fact
     */
    private String identifier;
    private String identifierValue;
    private String factType;
    private int factSource;
    private Map<String, Object> attributes;

    public final static int EXTERNAL_SOURCE = 0;
    public final static int INTERNAL_SOURCE = 1;

    /**
     * Default constructor for the <code>FactObject</code> class. This
     * constructor is <strong>NOT</strong> recommended to be used, since the
     * unique identifier is <em>unable</em> to be changed later on.
     */
    public FactObject() {
	this.factType = null;
	this.identifier = null;
	this.identifierValue = null;
	this.factSource = FactObject.INTERNAL_SOURCE;
	this.attributes = new HashMap<String, Object>();
	log.warn("Fact created with no type and unique identifier");
    }

    public FactObject(String factType, String identifier, String identifierValue) {
	this.factType = factType;
	this.identifier = identifier;
	this.identifierValue = identifierValue;
	this.factSource = FactObject.EXTERNAL_SOURCE;
	this.attributes = new HashMap<String, Object>();
    }

    /**
     * Function to get the unique identifier name.
     * 
     * @return the name of the unique identifier.
     */
    public String getIdentifier() {
	return identifier;
    }

    /**
     * Function to retrieve the unique identifier value of the fact.
     * 
     * @return the value of the unique identifier.
     */
    public String getUniqueId() {
	return identifierValue;
    }

    /**
     * Function to retrieve a specific attribute contained in the
     * <code>FactObject</code>.
     * 
     * @param attributeName
     *            the attribute name to be retrieved.
     * @return the value of the attribute contained in the
     *         <code>FactObject</code>. It will return <code>null</code> if
     *         there are no attributes with the specified name.
     */
    public Object getAttribute(String attributeName) {
	return attributes.get(attributeName);
    }

    /**
     * Function to retrieve all the attributes contained in the
     * <code>FactObject</code>.
     * 
     * @return a <code>Map</code> containing all the attributes.
     */
    public Map<String, Object> getAttributes() {
	return attributes;
    }

    /**
     * Procedure to set a value to an attribute contained in the
     * <code>FactObject</code>. If there are previous values associated with the
     * attribute, it will be replaced.
     * 
     * @param attributeName
     *            the name of the attribute.
     * @param attributeValue
     *            the value of the attribute to be set.
     */
    public void setAttribute(String attributeName, Object attributeValue) {
	attributes.put(attributeName, attributeValue);
    }

    /**
     * Procedure to set the attributes of the <code>FactObject</code>.
     * 
     * @param attributes
     *            a <code>Map</code> containing the attributes contained in the
     *            <code>FactObject</code>.
     */
    public void setAttributes(Map<String, Object> attributes) {
	this.attributes = attributes;
    }

    /**
     * Function to retrieve the type of the fact of this <code>FactObject</code>
     * instance.
     * 
     * @return the type of the <code>FactObject</code>.
     */
    public String getFactType() {
	return factType;
    }

    /**
     * Procedure to set the fact source of this <code>FactObject</code>
     * instance. The fact source determines the overwrite priority when there is
     * an update on the fact. External source means that the <em>internal</em>
     * fact will be overwritten by <em>external</em> facts when there is an
     * update.
     * <p/>
     * The fact source is set statically using static call from
     * <code>FactObject</code> class with either
     * <code>FactObject.EXTERNAL_SOURCE</code> or
     * <code>FactObject.INTERNAL_SOURCE</code>.
     * 
     * @param source
     *            the fact source.
     */
    public void setFactSource(int source) {
	this.factSource = source;
    }

    /**
     * Function to retrieve the fact source. By default, each
     * <code>FactObject</code> instance defaults to <em>internal</em> source.
     * 
     * @return the fact source, 0 if <em>external</em> and 1 if
     *         <em>internal</em>.
     */
    public int getFactSource() {
	if (this.factSource == FactObject.EXTERNAL_SOURCE)
	    return FactObject.EXTERNAL_SOURCE;
	else
	    return FactObject.INTERNAL_SOURCE;
    }

    /**
     * Overridden function to check whether this <code>FactObject</code> equals
     * to another <code>FactObject</code>. This <code>FactObject</code> equals
     * to another <code>FactObject</code> if and only if:
     * <ul>
     * <li>it is not <code>null</code>,</li>
     * <li>it belongs to the same class of <code>FactObject</code>,</li>
     * <li>it has the same fact type,</li>
     * <li>it has the same identifier, and</li>
     * <li>it has the same unique id.</li>
     * </ul>
     * 
     * @param obj
     *            another <code>FactObject</code>, however it is possible to put
     *            generic <code>Object</code> at the expense of returning
     *            <code>false</code>.
     * @return <code>true</code> if the object is the same object,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean equals(Object obj) {
	if (obj == null)
	    return false;
	else if (obj.getClass() != this.getClass())
	    return false;
	else {
	    FactObject factObj = (FactObject) obj;
	    if (factObj.getFactType().equalsIgnoreCase(this.getFactType())) {
		if (factObj.getIdentifier().equals(this.getIdentifier())) {
		    if (factObj.getUniqueId().equals(this.getUniqueId()))
			return true;
		}
	    }
	}

	return false;
    }

    /**
     * Function to return the hash code value for equal objects. Since the
     * identifier value for equal objects is the same and is unique, the hash
     * code returned would be correct
     * 
     * @return the integer that represents the hash code of the identifier value
     */
    @Override
    public int hashCode() {
	return identifierValue.hashCode();
    }

    /**
     * Function to check whether this <code>FactObject</code> is structurally
     * equal with another <code>FactObject</code>. A <code>FactObject</code> is
     * structurally equal to another <em>if and only if</em>:
     * <ul>
     * <li>it is not <code>null</code>,</li>
     * <li>it belongs to the same class of <code>FactObject</code>,</li>
     * <li>it has the same fact type,</li>
     * <li>it has the same identifier, and</li>
     * <li>it has the same list of attributes.</li>
     * </ul>
     * 
     * @param obj
     *            another <code>FactObject</code>, however it is possible to put
     *            generic <code>Object</code> at the expense of returning
     *            <code>false</code>.
     * @return <code>true</code> if the object is the same object,
     *         <code>false</code> otherwise.
     */
    public boolean equalsInStructure(Object obj) {
	if (obj == null)
	    return false;
	else if (obj.getClass() != this.getClass())
	    return false;
	else {
	    FactObject factObj = (FactObject) obj;
	    if (factObj.getFactType().equalsIgnoreCase(this.getFactType())) {
		if (factObj.getIdentifier().equals(this.getIdentifier())) {
		    return this.getAttributes().keySet()
			    .equals(factObj.getAttributes().keySet());
		}
	    }
	}

	return false;
    }

    /**
     * Function to check whether this <code>FactObject</code> is updated from
     * another <code>FactObject</code>. The term <em>update</em> only emphasises
     * on the attribute changes, and not structural or type changes. A
     * <code>FactObject</code> is updated from another <code>FactObject</code>
     * <em>if and only if</em>:
     * <ul>
     * <li>it is <em>equal</em> to the other <code>FactObject</code>,</li>
     * <li>it is <em>structurally equal</em> to the other
     * <code>FactObject</code>, and</li>
     * <li>it has <em>different</em> attribute values of the same
     * attribute.</code>
     * </ul>
     * 
     * @param obj
     *            another <code>FactObject</code> to be compared to. Generally,
     *            this is the previous <code>FactObject</code> before the
     *            changes.
     * @return <code>true</code> if this <code>FactObject</code> is updated,
     *         <code>false</code> otherwise.
     */
    public boolean isUpdated(Object obj) {
	if (this.equals(obj) && this.equalsInStructure(obj)) {
	    FactObject factObj = (FactObject) obj;
	    for (String attr : this.getAttributes().keySet()) {
		if (!this.getAttribute(attr).equals(factObj.getAttribute(attr)))
		    return true;
	    }
	}

	return false;
    }

    /**
     * Function to create a clone of <code>FactObject</code>. This function
     * performs a <em>deep copy</em> of the original object, resulting in two
     * different objects with two different object references. Since it is
     * performing deep copy, the attributes and attribute values within the
     * <code>FactObject</code> are also cloned.
     * 
     * @return the cloned object of <code>FactObject</code>.
     */
    @Override
    public FactObject clone() {
	FactObject factObj = null;

	try {
	    // Creating a clone of the FactObject
	    factObj = (FactObject) super.clone();

	    // Cloning the attributes of the original FactObject
	    Map<String, Object> attrClone = new HashMap<String, Object>();
	    for (String attr : this.getAttributes().keySet())
		attrClone.put(attr, this.getAttribute(attr));

	    // Setting the cloned attributes to the cloned object
	    factObj.setAttributes(attrClone);
	} catch (CloneNotSupportedException ex) {
	    log.warn("Unable to clone fact [" + this.getFactType() + "]");
	    ex.printStackTrace();
	}

	return factObj;
    }

    @Override
    public String toString() {
	StringBuilder strBldr = new StringBuilder();
	strBldr.append("Contents of FactObject " + this.getFactType());
	strBldr.append("\n\tIdentifier name: " + this.getIdentifier());
	strBldr.append("\n\tUnique identifier: " + this.getUniqueId());
	if (this.getFactSource() == FactObject.EXTERNAL_SOURCE)
	    strBldr.append("\n\tSource: external");
	else
	    strBldr.append("\n\tSource: internal");
	for (String attr : this.getAttributes().keySet()) {
	    strBldr.append("\n\t\tAttribute: " + attr);
	    strBldr.append("\n\t\tValue: " + this.getAttribute(attr));
	}

	return strBldr.toString();
    }

    /**
     * Function to return an XML string representing this
     * <code>FactObject</code>. The XML string returned will be in the format
     * of:
     * 
     * <pre>
     * &lt;Fact name=&quot;factFoo&quot; source=&quot;internal&quot;&gt;
     *     &lt;Identifier key=&quot;fooId&quot;&gt;123&lt;/Identifier&gt;
     *     &lt;Attributes&gt;
     *         &lt;Attribute key=&quot;fooName&quot;&gt;foo&lt;/Attribute&gt;
     *         &lt;Attribute key=&quot;barName&quot;&gt;bar&lt;/Attribute&gt;
     *     &lt;/Attributes&gt;
     * &lt;/Fact&gt;
     * </pre>
     * 
     * @return an XML string representation of this <code>FactObject</code>
     *         instance.
     */
    public String toXMLString() {
	StringBuilder strBldr = new StringBuilder();
	// String priority = (this.factSource == FactObject.EXTERNAL_SOURCE ?
	// "external"
	// : "internal");
	// strBldr.append("<Fact name=\"" + this.getFactType() + "\" source=\""
	// + priority + "\">");
	// strBldr.append("<Identifier key=\"" + this.getIdentifier() + "\">"
	// + this.getUniqueId() + "</Identifier>");
	// strBldr.append("<Attributes>");
	// for (String attr : this.getAttributes().keySet())
	// strBldr.append("<Attribute key=\"" + attr + "\">"
	// + this.getAttribute(attr).toString() + "</Attribute>");
	// strBldr.append("</Attributes>");
	// strBldr.append("</Fact>");

	String factSource = (this.factSource == FactObject.EXTERNAL_SOURCE ? "external"
		: "internal");
	strBldr.append("<attributes>");
	for (String attr : this.getAttributes().keySet()) {
	    strBldr.append("<attribute><attributeKey>" + attr
		    + "</attributeKey>" + "<attributeValue>"
		    + this.getAttribute(attr).toString() + "</attributeValue>"
		    + "</attribute>");
	}

	strBldr.append("</attributes>");
	strBldr.append("<identifier><identifierKey>" + this.getIdentifier()
		+ "</identifierKey>" + "<identifierValue>" + this.getUniqueId()
		+ "</identifierValue></identifier>");
	strBldr.append("<name>" + this.getFactType() + "</name>" + "<source>"
		+ factSource + "</source>");
	// log.info("THE STRBLDR IS: "+strBldr.toString());

	return strBldr.toString();
    }

    public String getIdentifierValue() {
	return identifierValue;
    }

    // public OMElement createJAXBFact()
    // {
    // OMFactory omFactory = OMAbstractFactory.getOMFactory();
    // OMNamespace ns = omFactory.createOMNamespace(
    // "http://ws.apache.org/axis2", "ft");
    // OMElement fact = omFactory.createOMElement("fact", ns);
    // OMElement attributes = omFactory.createOMElement("attributes", ns);
    //
    // }

}
