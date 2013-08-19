package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefTaskRefAddAction implements DefAdaptAction {
    private String bId;
    private String tId;
    private String preEP;
    private String postEP;
    private String pp;

    public DefTaskRefAddAction(String bId, String tId, String preEP,
	    String postEP, String pp) {
	this.bId = bId;
	this.tId = tId;
	this.preEP = preEP;
	this.postEP = postEP;
	this.pp = pp;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	OrganiserOperationResult res = comp.getOrganiserRole()
		.addTaskToBehavior(bId, tId, preEP, postEP, pp);
	return res.getResult();
    }
}
