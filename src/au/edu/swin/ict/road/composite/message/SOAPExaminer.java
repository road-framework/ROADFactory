package au.edu.swin.ict.road.composite.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.om.OMElement;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.exceptions.MessageException;

/**
 * Class for checking message and extract the query keyword from the content.
 * For SOAP messages, it utilises the Apache Axiom library to handle SOAP
 * messages. <br/>
 * It is extended from the MessageTypeChecker class.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class SOAPExaminer extends MessageTypeExaminer {
    private Object message;
    private String contentType;
    private List<String> queryNameList;
    private Map<String, String> queryValueMap;

    public SOAPExaminer(Object message, List<Contract> contracts) {
	super(message, contracts);
	this.message = message;
	this.queryNameList = new ArrayList<String>();
	this.queryValueMap = new HashMap<String, String>();
    }

    public SOAPExaminer(MessageWrapper mw, List<Contract> contracts) {
	super(mw, contracts);
	this.message = mw.getMessage();
	this.queryNameList = new ArrayList<String>();
	this.queryValueMap = new HashMap<String, String>();
    }

    /**
     * Procedure to evaluate the message based on certain tag provided in the
     * routing rules, called query name. It retrieves the content of the tag by
     * using external library. <br />
     * This method is extended from the MessageTypeChecker class.
     * 
     * @throws MessageException
     */
    @Override
    public void evaluateMessage() throws MessageException {
	try {
	    SOAPEnvelope env = null;
	    if (this.contentType.equalsIgnoreCase("soap")) {
		// If the message is an object of SOAPEnvelope
		env = (SOAPEnvelope) message;
		env.build();

		// If the message is not null
		if (env != null) {
		    for (String query : queryNameList) {
			// Retrieve the iterator for child elements and traverse
			Iterator children = env.getBody().getChildElements();
			while (children.hasNext())
			    traverseSOAP((OMElement) children.next(), query);
		    }
		}
	    }
	} catch (Exception ex) {
	    throw new MessageException(ex.toString());
	}
    }

    /**
     * Procedure to traverse the SOAP message content to search for a specific
     * query or tag name. It provides the in-depth search of a specific tag name
     * inside the SOAP message, which the Axiom library does not provide. It is
     * called recursively and ultimately putting the query name with the content
     * in a map. The recursive function uses a modified depth-first search
     * algorithm.
     * 
     * @param element
     *            the element to be traversed.
     * @param queryName
     *            the query name to be matched.
     */
    private void traverseSOAP(OMElement element, String queryName) {
	// Retrieve all child elements
	Iterator children = element.getChildElements();

	// Base case scenario
	// When the element name matches the query name and no child elements
	if (element.getLocalName().equals(queryName) && !children.hasNext()) {
	    // Put the query and the contents to the map and return
	    queryValueMap.put(queryName, element.getText());
	    return;
	} else {
	    // When the element name does not match or there is still child
	    // elements, traverse the element again
	    while (children.hasNext())
		traverseSOAP((OMElement) children.next(), queryName);
	}
    }

    /* Getter and setter methods extended from MessageTypeChecker class */
    public IMessageExaminer getMessageExaminer() {
	return super.getMessageExaminer();
    }

    public void addQueryName(String queryName) {
	this.queryNameList.add(queryName);
    }

    public Set<String> getQueryResult() {
	return (Set<String>) this.queryValueMap.keySet();
    }

    public List<String> getAddedQueryNames() {
	return queryNameList;
    }

    public Map<String, String> getQueryValues() {
	return queryValueMap;
    }

    public String getContentType() {
	return contentType;
    }

    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    public String getDestinationRole() {
	return super.getDestinationRole();
    }

    public void setDestinationRole(String roleId) {
	super.setDestinationRole(roleId);
    }

    public Contract getDestinationContract() {
	return super.getDestinationContract();
    }

    public void setDestinationContract(String contractId) {
	super.setDestinationContract(contractId);
    }
}
