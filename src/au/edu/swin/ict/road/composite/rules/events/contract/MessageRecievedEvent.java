package au.edu.swin.ict.road.composite.rules.events.contract;

import au.edu.swin.ict.road.composite.message.IMessageExaminer;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.serendip.event.EventRecord;

import java.util.ArrayList;
import java.util.Date;

public class MessageRecievedEvent {

    public String operationName;
    private MessageWrapper messageWrapper;
    private boolean blocked;
    public boolean response;
    private String correlationId;
    // This list keeps event records, triggered by the rule as an interpretation
    // of a message
    private final ArrayList<EventRecord> triggeredEvents = new ArrayList<EventRecord>();
    private IMessageExaminer examiner;

    public IMessageExaminer getExaminer() {
	return examiner;
    }

    public void setExaminer(IMessageExaminer examiner) {
	this.examiner = examiner;
    }

    public MessageRecievedEvent(MessageWrapper mw) {
	operationName = mw.getOperationName();
	// MUST for Process Driven task enactment
	correlationId = mw.getCorrelationId();
	messageWrapper = mw;
	response = mw.isResponse();
	blocked = true;
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

    public boolean isBlocked() {
	return blocked;
    }

    public String getCorrelationId() {
	return correlationId;
    }

    public void setCorrelationId(String correlationId) {
	this.correlationId = correlationId;
    }

    public void setBlocked(boolean blocked) {
	this.blocked = blocked;
    }

    /**
     * The rule writer should call this method to trigger events, upon a message
     * interpretation. We will use the pid of the message to relate events to a
     * particular process instance
     * 
     * @param eid
     *            Mandatory
     */
    public void triggerEvent(String eid) {
	this.triggerEvent(eid, this.correlationId);
    }

    /**
     * The rule writer should call this method to trigger events, upon a message
     * interpretation. Can be used to set a different or a null pid NOTE: null
     * pids can lead to new process instance if the event is a condition of
     * start(CoS) of a process definition
     * 
     * @param eid
     *            Mandatory
     * @param pid
     *            Optional
     */
    public void triggerEvent(String eid, String pid) {

	this.triggeredEvents.add(new EventRecord(eid, pid));
    }

    /**
     * The rule writer should call this method to trigger events, upon a message
     * interpretation. Can be used to expire events by setting the a past date
     * or let the event to be expired in a future date. Can be used to set a
     * different or a null pid NOTE: null pids can lead to new process instance
     * if the event is a condition of start(CoS) of a process definition
     * 
     * @param eid
     *            Mandatory
     * @param pid
     *            Optional
     * @param date
     *            Mandatory
     */
    public void triggerEvent(String eid, String pid, Date date) {
	EventRecord er = new EventRecord(eid, pid);
	er.setExpiration(date);
	this.triggeredEvents.add(er);
    }

    /**
     * To get all the triggered events during the rule evaluation
     * 
     * @return all the triggered events
     */
    public ArrayList<EventRecord> getAllTriggeredEvents() {
	return this.triggeredEvents;
    }

    public boolean isResponse() {
	return response;
    }

    public void setResponse(boolean response) {
	this.response = response;
    }
}
