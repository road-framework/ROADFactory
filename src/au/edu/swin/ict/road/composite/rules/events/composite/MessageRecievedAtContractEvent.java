package au.edu.swin.ict.road.composite.rules.events.composite;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

public class MessageRecievedAtContractEvent {

    private String operationName;
    private MessageWrapper messageWrapper;
    private String contractId;

    public MessageRecievedAtContractEvent(MessageWrapper mw, String contractId) {
	operationName = mw.getOperationName();
	messageWrapper = mw;
	this.contractId = contractId;
    }

    public String getContractId() {
	return contractId;
    }

    public void setContractId(String contractId) {
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
}
