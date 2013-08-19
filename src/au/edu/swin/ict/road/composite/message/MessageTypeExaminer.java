package au.edu.swin.ict.road.composite.message;

import java.util.List;
import java.util.Map;
import java.util.Set;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.exceptions.MessageException;

/**
 * Class that checks the message type and instantiates the corresponding message
 * checker. This class needs to be updated whenever a new message content type
 * is implemented or supported in the system. There is only one method that has
 * to be implemented.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class MessageTypeExaminer implements IMessageExaminer {
    private Object message;
    private String destinationRole;
    private Contract destinationContract;
    private List<Contract> contractList;

    public MessageTypeExaminer(Object message, List<Contract> contracts) {
	this.message = message;
	this.contractList = contracts;
    }

    public MessageTypeExaminer(MessageWrapper mw, List<Contract> contracts) {
	this.message = mw.getMessage();
	this.contractList = contracts;
    }

    @Override
    public IMessageExaminer getMessageExaminer() {
	IMessageExaminer messageChecker = null;

	if (message.getClass().getName()
		.equals("org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl")) {
	    messageChecker = new SOAPExaminer(message, contractList);
	    messageChecker.setContentType("SOAP");
	} else if (message.getClass().getName().equals("java.lang.String")) {
	    messageChecker = new XMLStringExaminer(message, contractList);
	    messageChecker.setContentType("XMLString");
	}

	return messageChecker;
    }

    /* Getter and setter methods implemented from the IMessageChecker interface */
    @Override
    public String getDestinationRole() {
	return this.destinationRole;
    }

    @Override
    public void setDestinationRole(String roleId) {
	this.destinationRole = roleId;

	// Setting the destination contract
	if (destinationRole != null) {
	    // Iterating through the contract list and extract the other role
	    for (Contract c : contractList)
		if (c.getRoleB().getId().equalsIgnoreCase(roleId))
		    destinationContract = c;
	}
	/*
	 * else { // Workload allocator int i =
	 * contractList.get(0).getRoleB().getOutPushQueueSize();
	 * 
	 * for(Contract c : contractList) if(c.getRoleB().getOutPushQueueSize()
	 * <= i) { i = c.getRoleB().getOutPushQueueSize(); destinationContract =
	 * c; } }
	 */
    }

    @Override
    public Contract getDestinationContract() {
	return destinationContract;
    }

    @Override
    public void setDestinationContract(String contractId) {
	// Iterate through the contract list and set the destination contract
	for (Contract c : contractList)
	    if (c.getId().equals(contractId))
		destinationContract = c;
    }

    /*
     * Leave it empty by default since they are not required for this class to
     * function properly
     */
    /* These methods are to be implemented in the specific message checker */
    @Override
    public void evaluateMessage() throws MessageException {
	// Leave it empty by default
    }

    @Override
    public void addQueryName(String queryName) {
	// Leave it empty by default
    }

    @Override
    public List<String> getAddedQueryNames() {
	// Leave it empty by default
	return null;
    }

    @Override
    public Set<String> getQueryResult() {
	// Leave it empty by default
	return null;
    }

    @Override
    public Map<String, String> getQueryValues() {
	// Leave it empty by default
	return null;
    }

    @Override
    public void setContentType(String contentType) {
	// Leave it empty by default
    }

    @Override
    public String getContentType() {
	// Leave it empty by default
	return null;
    }
}
