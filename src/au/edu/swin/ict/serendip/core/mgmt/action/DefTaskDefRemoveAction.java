package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefTaskDefRemoveAction implements DefAdaptAction {
    String rId;
    String id;

    public DefTaskDefRemoveAction(String rId, String id) {
	this.rId = rId;
	this.id = id;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole()
		.removeTaskDefFromRole(id, rId);
	return res.getResult();
    }

}
