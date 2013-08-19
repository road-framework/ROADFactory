package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefUpdateProcessDef implements DefAdaptAction {
    private String pdId;
    String property;
    String value;

    public DefUpdateProcessDef(String pdId, String property, String value) {
	this.pdId = pdId;
	this.property = property;
	this.value = value;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	OrganiserOperationResult res = comp.getOrganiserRole().updatePD(pdId,
		property, value);
	return res.getResult();
    }
}