package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefRemovePlayerBindingAction implements DefAdaptAction {
    private String id = null;

    public DefRemovePlayerBindingAction(String pbId) {
	super();
	this.id = pbId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	OrganiserOperationResult res = comp.getOrganiserRole()
		.removePlayerBinding(id);
	return res.getResult();
    }
}
