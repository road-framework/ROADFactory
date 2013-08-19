package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefUpdateTermAction implements DefAdaptAction {
    private String ctId = null;
    private String tmId = null;
    private String property;
    private String value;

    public DefUpdateTermAction(String ctId, String tmId, String property,
	    String value) {
	this.ctId = ctId;
	this.tmId = tmId;
	this.property = property;
	this.value = value;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().updateTerm(tmId,
		ctId, property, value);
	return res.getResult();
    }
}
