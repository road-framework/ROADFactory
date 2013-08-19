package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefAddTermAction implements DefAdaptAction {

    private String ctId = null;
    private String name = null;
    private String tmId = null;
    private String messageType = null;
    private String deonticType = null;
    private String description = null;
    private String direction;

    public DefAddTermAction(String tmId, String ctId, String name,
	    String messageType, String deonticType, String description,
	    String direction) {
	this.ctId = ctId;
	this.name = name;
	this.tmId = tmId;
	this.messageType = messageType;
	this.deonticType = deonticType;
	this.description = description;
	this.direction = direction;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().addNewTerm(tmId,
		name, messageType, deonticType, description, direction, ctId);
	return res.getResult();
    }
}
