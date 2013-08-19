package au.edu.swin.ict.road.roadtest;

import java.util.Calendar;
import java.util.Date;

import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.roadtest.Message;

/**
 * Message class is the container class for each arrived and stored message
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public class Message implements Comparable<Message> {
    private Date timeStamp = null;
    private String operationName = null;
    private Object messageContent = null;
    private String originRoleId = null;
    private String messageType = null;
    private boolean response = false;

    /**
     * Construct a message with the information of the
     * <code>MessageWrapper</code>
     * 
     * @param mw
     *            The <code>MessageWrapper</code> of which this message is going
     *            to be created.
     */
    public Message(MessageWrapper mw) {
	Calendar cal = Calendar.getInstance();
	this.timeStamp = cal.getTime();
	this.messageContent = mw.getMessage();
	this.operationName = mw.getOperationName();
	this.originRoleId = mw.getOriginRoleId();
	this.messageType = mw.getMessageType();
	this.response = mw.isResponse();
    }

    /**
     * @param msgContent
     * @param operationName
     * @param originRoleId
     */
    public Message(String msgContent, String operationName,
	    String originRoleId, boolean response) {
	Calendar cal = Calendar.getInstance();
	this.timeStamp = cal.getTime();
	this.messageContent = msgContent;
	this.operationName = operationName;
	this.originRoleId = originRoleId;
	this.response = response;
    }

    /**
     * Gets the Time stamp of this message.
     * 
     * @return the timeStamp as <code>Date</code> of this message.
     */
    public Date getTimeStamp() {
	return timeStamp;
    }

    /**
     * Sets a new timestamp to the message
     */
    protected void setNewTimeStamp() {
	Calendar cal = Calendar.getInstance();
	this.timeStamp = cal.getTime();

    }

    /**
     * @param time
     */
    protected void setNewTimeStamp(Date time) {
	this.timeStamp = time;
    }

    /**
     * Gets the operation name of this message (means which message signature
     * was invoked)
     * 
     * @return the operationName as <code>String</code> of this message.
     */
    public String getOperationName() {
	return operationName;
    }

    /**
     * Gets the message content of this message.
     * 
     * @return the messageContent as <code>Object</code> of this message.
     */
    public Object getMessageContent() {
	return messageContent;
    }

    /**
     * Gets the role id who sent this message.
     * 
     * @return the originRoleId as <code>String</code> of this message.
     */
    public String getOriginRoleId() {
	return originRoleId;
    }

    /**
     * Gets the message type of this message.
     * 
     * @return the messageType as a <code>String</code> of this message.
     */
    public String getMessageType() {
	return this.messageType;
    }

    /**
     * Is it a Response type
     * 
     * @return true if it is a response type
     */
    public boolean isResponse() {
	return this.response;
    }

    /**
     * Gets the message entire details of message.
     * 
     * @return the message as a <code>String</code>.
     */
    @Override
    public String toString() {
	return this.getOperationName() + "," + this.getMessageContent() + ","
		+ this.getOriginRoleId() + "," + this.getMessageType() + ","
		+ this.getTimeStamp();
    }

    public int compareTo(Message m) {
	if (this.timeStamp.equals(m.getTimeStamp())) {
	    return 0;
	} else if (this.timeStamp.before(m.getTimeStamp())) {
	    return -1;
	} else if (this.timeStamp.after(m.getTimeStamp())) {
	    return 1;
	} else
	    return 0;
    }

}
