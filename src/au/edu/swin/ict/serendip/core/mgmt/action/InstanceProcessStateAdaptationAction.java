package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class InstanceProcessStateAdaptationAction implements
	InstanceAdaptAction {
    private String status = null;

    public InstanceProcessStateAdaptationAction(String status) {
	this.status = status;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	// TODO Auto-generated method stub
	if (null == this.status) {
	    throw new AdaptationException("Status not specified");
	}

	if (this.status.equals(ProcessInstance.status.abort.toString())) {
	    pi.setCurrentStatus(ProcessInstance.status.abort);
	} else if (this.status.equals(ProcessInstance.status.paused.toString())) {
	    pi.setCurrentStatus(ProcessInstance.status.paused);
	} else if (this.status.equals(ProcessInstance.status.active.toString())) {
	    pi.setCurrentStatus(ProcessInstance.status.active);
	} else if (this.status.equals(ProcessInstance.status.completed
		.toString())) {
	    pi.setCurrentStatus(ProcessInstance.status.completed);
	} else {
	    throw new AdaptationException("Unknown process instance status");
	}
	return true;

    }

}
