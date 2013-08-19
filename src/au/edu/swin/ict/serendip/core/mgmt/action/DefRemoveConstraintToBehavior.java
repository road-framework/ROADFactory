package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefRemoveConstraintToBehavior implements DefAdaptAction {
    private String bId;

    public DefRemoveConstraintToBehavior(String bId, String cId) {
	this.bId = bId;
	this.cId = cId;
    }

    private String cId;

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole()
		.removeBehaviorConstraint(bId, cId);
	return res.getResult();
    }
}
