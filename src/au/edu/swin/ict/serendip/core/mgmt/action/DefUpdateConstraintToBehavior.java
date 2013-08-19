package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefUpdateConstraintToBehavior implements DefAdaptAction {
    private String bId;
    private String cId;
    private String property;
    private String value;

    public DefUpdateConstraintToBehavior(String bId, String cId,
	    String property, String value) {
	this.bId = bId;
	this.cId = cId;
	this.property = property;
	this.value = value;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole()
		.updateBehaviorConstraint(bId, cId, property, value);
	return res.getResult();
    }
}
