package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefTaskDefUpdateAction implements DefAdaptAction {
    String rId;
    String tId;

    public DefTaskDefUpdateAction(String rId, String tId, String property,
	    String value) {
	this.rId = rId;
	this.tId = tId;
	this.property = property;
	this.value = value;
    }

    String property;
    String value;

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().updateTaskDef(
		rId, tId, property, value);
	return res.getResult();
    }

}
