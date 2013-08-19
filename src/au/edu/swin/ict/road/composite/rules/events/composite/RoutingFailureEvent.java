package au.edu.swin.ict.road.composite.rules.events.composite;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

public class RoutingFailureEvent {

    private String operationName;
    private MessageWrapper messageWrapper;
    private String roleId;

    public RoutingFailureEvent(MessageWrapper mw, String roleId) {
	operationName = mw.getOperationName();
	messageWrapper = mw;
	this.roleId = roleId;
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
}