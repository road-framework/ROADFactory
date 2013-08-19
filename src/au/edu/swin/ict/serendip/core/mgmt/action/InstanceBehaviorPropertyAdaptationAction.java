package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.PerformanceProperty;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class InstanceBehaviorPropertyAdaptationAction implements
	InstanceAdaptAction {
    private String btId;
    private String proeprtyId;
    private String newVal;
    public static final String ISABSTRACT = "isAbstract";
    public static final String EXTEND = "extends";
    public static final String DESCR = "descr";

    public InstanceBehaviorPropertyAdaptationAction(String btId,
	    String proeprtyId, String newVal) {
	super();
	this.btId = btId;
	this.proeprtyId = proeprtyId;
	this.newVal = newVal;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	// Get the bt
	BehaviorTerm bTerm = null;
	for (BehaviorTerm bt : pi.getBtVec()) {
	    if (bt.getId().equals(this.btId)) {
		// Got the bTerm
		bTerm = bt;
	    }
	}
	if (null == bTerm) {
	    throw new AdaptationException("Cannot find behavior  " + this.btId);
	}
	// adapt
	if (this.proeprtyId.equals(ISABSTRACT)) {
	    if (this.newVal.equals("true")) {
		bTerm.setAbstract(true);
	    } else {
		bTerm.setAbstract(false);
	    }
	} else if (this.proeprtyId.equals(EXTEND)) {
	    bTerm.setExtendsFrom(this.newVal);
	} else if (this.proeprtyId.equals(DESCR)) {
	    //
	} else {
	    throw new AdaptationException(
		    "Unknown proeprty for behavior adaptation "
			    + this.proeprtyId);
	}

	return true;
    }

    public String toString() {
	return "Adapting Behavior " + this.btId;
    }
}
