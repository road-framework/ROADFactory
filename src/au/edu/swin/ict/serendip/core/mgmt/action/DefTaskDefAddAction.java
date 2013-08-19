package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefTaskDefAddAction implements DefAdaptAction {
    String rId;
    String tId;
    String usingMsgs;
    String resultingMsgs;
    String transFile;

    public DefTaskDefAddAction(String rId, String tId, String usingMsgs,
	    String resultingMsgs, String transFile) {
	this.rId = rId;
	this.tId = tId;
	this.usingMsgs = usingMsgs;
	this.resultingMsgs = resultingMsgs;
	this.transFile = transFile;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole()
		.addTaskDefToRole(rId, tId, false, usingMsgs, resultingMsgs,
			transFile);
	return res.getResult();
	// TODO Auto-generated method stub
    }

}
