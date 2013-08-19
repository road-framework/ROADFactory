package au.edu.swin.ict.road.composite.message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.exceptions.MessageException;

/**
 * Class for checking the message preference and extracting the contents of the
 * message from an XML string. <br/>
 * This class is extended from the MessageTypeChecker class.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class XMLStringExaminer extends MessageTypeExaminer {
    private Object message;
    private String contentType;
    private List<String> queryNameList;
    private Map<String, String> queryValueMap;

    public XMLStringExaminer(Object message, List<Contract> contracts) {
	super(message, contracts);
	this.message = message;
	this.queryNameList = new ArrayList<String>();
	this.queryValueMap = new HashMap<String, String>();
    }

    public XMLStringExaminer(MessageWrapper mw, List<Contract> contracts) {
	super(mw, contracts);
	this.message = mw.getMessage();
	this.queryNameList = new ArrayList<String>();
	this.queryValueMap = new HashMap<String, String>();
    }

    /**
     * Procedure to retrieve the contents of the query names specified in the
     * query list. It would always assume that the message passed is an XML
     * string with proper opening and closing tags.
     */
    @Override
    public void evaluateMessage() throws MessageException {
	try {
	    String xmlString = null;
	    if (this.contentType.equalsIgnoreCase("xmlstring")) {
		// Casting the message to string
		xmlString = (String) message;

		// Iterate through the query list
		for (String query : queryNameList) {
		    // If the XML string contains the said query
		    if (xmlString.contains(query)) {
			// Retrieve the content of the query by splitting the
			// tags
			String value = xmlString.substring(
				xmlString.indexOf(query) + query.length() + 1,
				xmlString.lastIndexOf("</" + query));

			// Put it inside the query map
			queryValueMap.put(query, value);
		    }
		}
	    }
	} catch (Exception ex) {
	    throw new MessageException(ex.toString());
	}
    }

    /* Getter and setter methods extended from MessageTypeChecker class */
    @Override
    public IMessageExaminer getMessageExaminer() {
	return super.getMessageExaminer();
    }

    @Override
    public void addQueryName(String queryName) {
	this.queryNameList.add(queryName);
    }

    @Override
    public Set<String> getQueryResult() {
	return (Set<String>) this.queryValueMap.keySet();
    }

    @Override
    public List<String> getAddedQueryNames() {
	return queryNameList;
    }

    @Override
    public Map<String, String> getQueryValues() {
	return queryValueMap;
    }

    @Override
    public String getContentType() {
	return contentType;
    }

    @Override
    public void setContentType(String contentType) {
	this.contentType = contentType;
    }

    @Override
    public String getDestinationRole() {
	return super.getDestinationRole();
    }

    @Override
    public void setDestinationRole(String roleId) {
	super.setDestinationRole(roleId);
    }

    @Override
    public Contract getDestinationContract() {
	return super.getDestinationContract();
    }

    @Override
    public void setDestinationContract(String contractId) {
	super.setDestinationContract(contractId);
    }
}