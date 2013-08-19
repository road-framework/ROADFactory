package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class InstanceProcessConstraintAddAction implements InstanceAdaptAction,
	Constants {

    private String cid;
    private String expression;
    private boolean enabled;

    public InstanceProcessConstraintAddAction(String cid, String expression,
	    boolean enabled) {
	super();
	this.cid = cid;
	this.expression = expression;
	this.enabled = enabled;
    }

    @Override
    public boolean adapt(ProcessInstance pi) throws AdaptationException {
	// TODO Auto-generated method stub
	pi.addConstraint(cid, expression, enabled);

	return true;
    }

}
