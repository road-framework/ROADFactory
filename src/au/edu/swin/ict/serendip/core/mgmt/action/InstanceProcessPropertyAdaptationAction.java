package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * Think about a weay to define the adaptaiotn API {ADD/DEL/EDIT}+{PROP}+{}
 * 
 * 
 */
public class InstanceProcessPropertyAdaptationAction implements
	InstanceAdaptAction {
    private String proeprtyId;
    private String newVal;

    public InstanceProcessPropertyAdaptationAction(String proeprtyId,
	    String newVal) {
	super();
	this.proeprtyId = proeprtyId;
	this.newVal = newVal;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	// validate
	if ((pi.getCurrentStatus() == ProcessInstance.status.completed)
		|| (pi.getCurrentStatus() == ProcessInstance.status.abort)) {
	    throw new AdaptationException(
		    "Overdue adaptation. Process instance is either complete or aborted "
			    + this.proeprtyId);
	}
	// adapt
	if (this.proeprtyId.equals(ProcessInstance.propertyAttribute.cot
		.toString())) {
	    pi.setEventPattern(this.newVal);
	} else {
	    throw new AdaptationException(
		    "Unknown proeprty for process adaptation "
			    + this.proeprtyId);
	}
	return true;
    }

    public String toString() {
	return "Adapting process proeprtyId" + this.proeprtyId;

    }
}
