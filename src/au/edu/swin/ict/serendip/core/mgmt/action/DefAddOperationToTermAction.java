package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefAddOperationToTermAction implements DefAdaptAction {
    private String operationName;
    private String operationReturnType;
    private Parameter[] parameters;
    private String termId;
    private String cId;

    public DefAddOperationToTermAction(String operationName,
	    String operationReturnType, Parameter[] parameters, String termId,
	    String cId) {
	this.operationName = operationName;
	this.operationReturnType = operationReturnType;
	this.parameters = parameters;
	this.termId = termId;
	this.cId = cId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {

	OrganiserOperationResult res = comp.getOrganiserRole().addNewOperation(
		operationName, operationReturnType, parameters, termId, cId);
	return res.getResult();
    }
}
