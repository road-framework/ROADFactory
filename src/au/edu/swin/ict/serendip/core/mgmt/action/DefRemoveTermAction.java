package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefRemoveTermAction implements DefAdaptAction {
    private String ctId = null;
    private String tmId = null;

    public DefRemoveTermAction(String ctId, String tmId) {
	this.ctId = ctId;
	this.tmId = tmId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().removeTerm(ctId,
		tmId);
	return res.getResult();
    }
}
