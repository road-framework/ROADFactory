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
public class InstanceTaskDeleteAction implements InstanceAdaptAction, Constants {

    private String taskId;

    public InstanceTaskDeleteAction(String taskId) {
	this.taskId = taskId;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	Task task = null;
	BehaviorTerm parentBt = null;
	for (BehaviorTerm bt : pi.getBtVec()) {
	    for (Task t : bt.getAllTasks()) {
		if (t.getId().equals(this.taskId)) {
		    // Got the task and its parent behavior term
		    parentBt = bt;
		    task = t;
		}
	    }
	}
	// No such task
	if (null == task) {
	    throw new AdaptationException("Cannot find task " + this.taskId);
	}

	// If the task is currently in progress and the property is change in
	// eppre or performance property then reject
	if (task.getCurrentStatus() == Task.status.active) {
	    throw new AdaptationException("Task is active " + this.taskId
		    + "\nCannot be deleted");
	}
	// End of pre-checks
	// Delete the task
	parentBt.removeTask(task.getId());

	return true;
    }

    public String toString() {
	return "Task deletion " + this.taskId;

    }
}
