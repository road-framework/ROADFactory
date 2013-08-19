package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefContractRemoveAction implements DefAdaptAction {
    private String contractId = null;

    public DefContractRemoveAction(String contractId) {
	super();
	this.contractId = contractId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	OrganiserOperationResult res = comp.getOrganiserRole().removeContract(
		contractId);
	return res.getResult();
    }

}
