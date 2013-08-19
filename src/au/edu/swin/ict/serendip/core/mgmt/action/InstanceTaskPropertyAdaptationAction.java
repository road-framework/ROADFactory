package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.PerformanceProperty;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * 
 * @author Malinda
 * 
 */
public class InstanceTaskPropertyAdaptationAction implements
	InstanceAdaptAction, Constants {

    private String taskId;
    private String proeprtyId;
    private String newVal;

    public InstanceTaskPropertyAdaptationAction(String taskId,
	    String propertyId, String newVal) {
	this.taskId = taskId;
	this.proeprtyId = propertyId;
	this.newVal = newVal;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	Task task = null;
	for (BehaviorTerm bt : pi.getBtVec()) {
	    for (Task t : bt.getAllTasks()) {
		if (t.getId().equals(this.taskId)) {
		    // Got the task
		    task = t;
		}
	    }
	}
	// No such task
	if (null == task) {
	    throw new AdaptationException("Cannot find task " + this.taskId);
	}

	// if the task is currently is already completed, reject the property
	// change
	if (task.getCurrentStatus() == Task.status.completed) {
	    throw new AdaptationException(
		    "Task already completed. Cannot perform adaptation. Task="
			    + this.taskId);
	}
	// If the task is currently in progress and the property is change in
	// eppre or performance property then reject
	if (task.getCurrentStatus() == Task.status.active) {
	    if (this.proeprtyId.equals(Task.propertyAttribute.preep.toString())
		    || this.proeprtyId.equals(Task.propertyAttribute.pp
			    .toString())) {
		throw new AdaptationException("Task is active " + this.taskId
			+ "\nCannot change the " + this.proeprtyId);
	    }
	}
	// End of pre-checks

	// adapt
	if (this.proeprtyId.equals(Task.propertyAttribute.preep.toString())) {
	    task.setEventPattern(this.newVal);
	} else if (this.proeprtyId.equals(Task.propertyAttribute.postep
		.toString())) {
	    task.setPostEventPattern(this.newVal);
	} else if (this.proeprtyId.equals(Task.propertyAttribute.pp.toString())) {
	    task.setProperty(new PerformanceProperty(this.newVal));
	} else if (this.proeprtyId.equals(Task.propertyAttribute.descr
		.toString())) {
	    //
	} else {
	    throw new AdaptationException(
		    "Unknown proeprty for task adaptation " + this.proeprtyId);
	}
	return true;
    }

    public String toString() {
	return "Adapting task " + this.taskId;

    }
}
