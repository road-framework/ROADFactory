package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * TODO documentation
 */
public class DefSetOutMsgOnTaskAction implements DefAdaptAction {
    private String operationName;
    private String operationReturnType;
    private String deliveryType;
    private boolean isResponse = false;
    private Parameter[] parameters;
    private String taskId;
    private String rId;

    public DefSetOutMsgOnTaskAction(String operationName,
	    String operationReturnType, String deliveryType, boolean response,
	    Parameter[] parameters, String taskId, String rId) {
	this.operationName = operationName;
	this.operationReturnType = operationReturnType;
	this.deliveryType = deliveryType;
	this.isResponse = response;
	this.parameters = parameters;
	this.taskId = taskId;
	this.rId = rId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {

	OrganiserOperationResult res = comp.getOrganiserRole()
		.setOutMessageType(deliveryType, isResponse, operationName,
			operationReturnType, parameters, taskId, rId);
	return res.getResult();
    }
}