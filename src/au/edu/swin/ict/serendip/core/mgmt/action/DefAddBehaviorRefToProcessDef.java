package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefAddBehaviorRefToProcessDef implements DefAdaptAction {
    private String pdId = null;
    private String bId = null;

    public DefAddBehaviorRefToProcessDef(String pdId, String bId) {
	this.pdId = pdId;
	this.bId = bId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	OrganiserOperationResult res = comp.getOrganiserRole()
		.addBehaviorRefToPD(pdId, bId);
	return res.getResult();
    }
}
