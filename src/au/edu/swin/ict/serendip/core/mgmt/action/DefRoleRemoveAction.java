package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefRoleRemoveAction implements DefAdaptAction {
    private String roleId = null;

    public DefRoleRemoveAction(String roleId) {
	super();
	this.roleId = roleId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO pre-check: Is the role currently being used (possibly) process
	// instances
	// Need to be implemented in the organiser

	OrganiserOperationResult res = comp.getOrganiserRole().removeRole(
		roleId);
	return res.getResult();
    }

}
