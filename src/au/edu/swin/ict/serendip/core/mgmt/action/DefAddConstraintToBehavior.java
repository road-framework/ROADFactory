package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefAddConstraintToBehavior implements DefAdaptAction {
    private String bId;
    private String cId;
    private String expression;
    private boolean enabled;

    public DefAddConstraintToBehavior(String bId, String cId,
	    String expression, boolean enabled) {
	this.bId = bId;
	this.cId = cId;
	this.expression = expression;
	this.enabled = enabled;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole()
		.addNewBehaviorConstraint(bId, cId, expression, enabled);
	return res.getResult();
    }
}
