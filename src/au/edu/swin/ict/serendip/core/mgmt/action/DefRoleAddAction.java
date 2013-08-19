package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefRoleAddAction implements DefAdaptAction {
    private String id = null;
    private String name = null;
    private String description = null;

    public DefRoleAddAction(String id, String name, String description) {
	super();
	this.id = id;
	this.name = name;
	this.description = description;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	OrganiserOperationResult res = comp.getOrganiserRole().addNewRole(id,
		name, description);
	return res.getResult();
    }

}
