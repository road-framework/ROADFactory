package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefBehaviorAddAction implements DefAdaptAction {
    String id;
    String extendfrom;

    public DefBehaviorAddAction(String id, String extendfrom) {
	this.id = id;
	this.extendfrom = extendfrom;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().addNewBehavior(
		id, extendfrom);
	return res.getResult();
    }

}
