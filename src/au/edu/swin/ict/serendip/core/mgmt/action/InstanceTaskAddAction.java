package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.PerformanceProperty;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * @author Malinda
 */
public class InstanceTaskAddAction implements InstanceAdaptAction, Constants {

    private String btId, taskId, preep, postep, obligrole, pp;

    public InstanceTaskAddAction(String btid, String taskId, String preep,
	    String postep, String obligrole, String pp) {
	this.btId = btid;
	this.obligrole = obligrole;
	this.taskId = taskId;
	this.preep = preep;
	this.postep = postep;
	this.pp = pp;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {

	BehaviorTerm parentBt = null;
	for (BehaviorTerm bt : pi.getBtVec()) {
	    if (bt.getId().equals(this.btId)) {
		// Got the BT
		parentBt = bt;
	    }
	}
	// No such BT
	if (null == parentBt) {
	    throw new AdaptationException("Cannot find behaviour term "
		    + this.btId);
	}

	// TODO: Do we need to check for pre-conditions before adding the Task.

	// TODO: Check for existance of role
	if (null == pi.getEngine().getComposition().getComposite()
		.getRoleByID(this.obligrole)) {
	    throw new AdaptationException("Cannot find role " + this.obligrole);
	}
	// End of pre-checks

	// Lets add task
	Task task = new Task(pi.getEngine(), pi, this.taskId, this.preep,
		this.postep, this.obligrole, null, new PerformanceProperty(
			this.pp), parentBt);

	pi.addTask(this.btId, task);

	// then subscribe to event cloud
	return true;
    }

    public String toString() {
	return "Task add " + this.taskId;

    }
}
