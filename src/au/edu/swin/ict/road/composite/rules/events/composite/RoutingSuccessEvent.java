package au.edu.swin.ict.road.composite.rules.events.composite;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

public class RoutingSuccessEvent {

    private String operationName;
    private MessageWrapper messageWrapper;
    private String roleId;
    private String contractId;

    public RoutingSuccessEvent(MessageWrapper mw, String roleId,
	    String contractId) {
	operationName = mw.getOperationName();
	messageWrapper = mw;
	this.roleId = roleId;
	this.contractId = contractId;
    }

    public String getOperationName() {
	return operationName;
    }

    public void setOperationName(String operationName) {
	this.operationName = operationName;
    }

    public MessageWrapper getMessageWrapper() {
	return messageWrapper;
    }

    public void setMessageWrapper(MessageWrapper messageWrapper) {
	this.messageWrapper = messageWrapper;
    }

    public String getRoleId() {
	return roleId;
    }

    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    public String getContractId() {
	return contractId;
    }

    public void setContractId(String contractId) {
	this.contractId = contractId;
    }
}