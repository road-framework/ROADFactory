package au.edu.swin.ict.road.composite.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.MessageWrapper.SyncType;
import au.edu.swin.ict.road.composite.rules.IContractRules;
import au.edu.swin.ict.road.composite.rules.MessageProcessResult;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;
import au.edu.swin.ict.road.xml.bindings.OperationType;
import au.edu.swin.ict.road.xml.bindings.ParamsType;
import au.edu.swin.ict.road.xml.bindings.ParamsType.Parameter;
import au.edu.swin.ict.road.xml.bindings.TermType;

/**
 * A Term represents a single allowable interaction between two roles in a ROAD
 * composite. Terms exist only inside contracts.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class Term {

    private static Logger log = Logger.getLogger(Composite.class.getName());

    private String id;
    private String name;
    private String messageType;
    private String deonticType;
    private IContractRules rules;
    private String description;
    private boolean complianceChangedOnLastRun;
    private String direction;
    private Operation operation;

    /**
     * Empty constructor for Term (sets all values to null).
     */
    public Term() {
	id = null;
	name = null;
	messageType = null;
	deonticType = null;
	description = null;
	rules = null;
	this.complianceChangedOnLastRun = false;
	direction = null;
    }

    public Term(String id, String name, String messageType, String deonticType,
	    String description, IContractRules rules, String direction) {
	this();

	this.id = id;
	this.name = name;
	this.messageType = messageType;
	this.deonticType = deonticType;
	this.description = description;
	this.rules = rules;
	this.direction = direction;
    }

    public Term(TermType termBinding, IContractRules rules) {
	this();

	this.rules = rules;
	this.extractData(termBinding);

    }

    /**
     * Processes term rules to determine if the message is blocked or not.
     * 
     * @param msg
     *            the message to evaluate with rules
     * @return true if blocked, false is allowed
     */
    public MessageProcessResult processMessage(MessageWrapper msg) {
	MessageProcessResult mpr = null;
	try {

	    mpr = rules.insertMessageRecievedAtContractEvent(msg);
	    boolean isBlocked = mpr.isBlocked();
	    if (!isBlocked) {
		msg.setMessageType(messageType);
	    }
	    if (!msg.isResponse()) {
		// this syncType will be used by ROAD4WS to determine whether it
		// should collect the response from player or not
		if (this.getOperation().getReturnType().equals("void")) {
		    msg.setSyncType(SyncType.OUT);
		} else {
		    msg.setSyncType(SyncType.OUTIN);
		}
	    }
	} catch (RulesException e) {
	    log.info(e.getMessage());
	}
	// Set the term
	mpr.setTerm(this);
	return mpr;
    }

    /**
     * Checks if on the last execution of this terms obligations, was there a
     * compliance change. After calling this method one, the value is reset.
     * 
     * @return true if the was a compliance change, false if not
     */
    public boolean didComplianceChangedOnLastRun() {
	boolean temp = complianceChangedOnLastRun;
	complianceChangedOnLastRun = false;
	return temp;
    }

    /**
     * Get the <code>Operation</code> of this <code>Term</code>
     * 
     * @return the <code>Operation</code> of this <code>Term</code>
     */
    public Operation getOperation() {
	return operation;
    }

    /**
     * Set the <code>Operation</code> for this <code>Term</code>
     * 
     * @param operation
     *            the <code>Operation</code> for this <code>Term</code>.
     */
    public void setOperation(Operation operation) {
	this.operation = operation;
    }

    /**
     * Get the Id of this term
     * 
     * @return the id of this term.
     */
    public String getId() {
	return id;
    }

    /**
     * Set the id of this term
     * 
     * @param id
     *            the id of this term.
     */
    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    /**
     * Get the message type of this term
     * 
     * @return the message type of this term
     */
    public String getMessageType() {
	return messageType;
    }

    /**
     * set the message type of this term.
     * 
     * @param messageType
     *            the message type of this term.
     */
    public void setMessageType(String messageType) {
	this.messageType = messageType;
    }

    /**
     * Get the deontic type of this term
     * 
     * @return the deontic type of this term
     */
    public String getDeonticType() {
	return deonticType;
    }

    /**
     * Set the deontic type of this term
     * 
     * @return the deontic type of this term
     */
    public void setDeonticType(String deonticType) {
	this.deonticType = deonticType;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public IContractRules getRules() {
	return rules;
    }

    public void setRules(IContractRules rules) {
	this.rules = rules;
    }

    public String getDirection() {
	return direction;
    }

    public void setDirection(String direction) {
	this.direction = direction;
    }

    private void extractData(TermType termBinding) {
	if (termBinding.getId() != null) {
	    id = termBinding.getId();
	}
	if (termBinding.getName() != null) {
	    name = termBinding.getName();
	}
	if (termBinding.getDescription() != null) {
	    description = termBinding.getDescription();
	}
	if (termBinding.getMessageType() != null) {
	    this.messageType = termBinding.getMessageType();
	}
	if (termBinding.getDeonticType() != null) {
	    this.deonticType = termBinding.getDeonticType();
	}
	if (termBinding.getDirection() != null) {
	    direction = termBinding.getDirection().value();
	}
	if (termBinding.getOperation() != null) {
	    extractOperation(termBinding.getOperation());
	}
    }

    private void extractOperation(OperationType operation) {
	// make the new operation and parameter list
	Operation op = new Operation();
	List<au.edu.swin.ict.road.composite.contract.Parameter> parameterList = new ArrayList<au.edu.swin.ict.road.composite.contract.Parameter>();

	// extract all the XML data
	op.setName(operation.getName());
	op.setReturnType(operation.getReturn());

	ParamsType paramsType = operation.getParameters();
	List<Parameter> paramList = paramsType.getParameter();
	for (Parameter p : paramList) {
	    parameterList
		    .add(new au.edu.swin.ict.road.composite.contract.Parameter(
			    p.getType(), p.getName()));
	}

	op.setParameters(parameterList);
	this.operation = op;
    }

    public String toString() {
	return "Term: '" + id + "' (operation: '" + operation.getName()
		+ "'; direction: '" + direction + "')";
    }
}
