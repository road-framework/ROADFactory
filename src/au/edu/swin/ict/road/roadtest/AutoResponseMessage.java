package au.edu.swin.ict.road.roadtest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import au.edu.swin.ict.road.roadtest.Message;

/**
 * AutoRespnseMessage class contains the information for an auto response
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class AutoResponseMessage extends Message {
    // private String inMsgSignature = null;
    private String outMsgSignature = null;
    private UUID uid;
    // private String msgContent = null;
    private long delay;
    private List<Date> timeStamps;

    /**
     * Constructor of this class which gets necessary information
     * 
     * @param inMsgSignature
     *            The in message signature to which this has to response as
     *            <code>String</code>
     * @param outMsgSignature
     *            The response signature which is going to be called as
     *            <code>String</code>
     * @param msgContent
     *            The content of the response message as <code>String</code>
     * @param delay
     *            the delay as <code>long</code>
     */
    public AutoResponseMessage(String inMsgSignature, String outMsgSignature,
	    String msgContent, long delay) {
	super(msgContent, inMsgSignature, "", false);
	// this.inMsgSignature = inMsgSignature;
	this.outMsgSignature = outMsgSignature;
	// this.msgContent = msgContent;
	this.delay = delay;
	this.timeStamps = new ArrayList<Date>();
    }

    // /**
    // * Gets the message signature to which it has to response automatically
    // *
    // * @return The message signature to which it has to response automatically
    // * as <code>String</code>
    // */
    // public String getInMsgSignature() {
    // return this.inMsgSignature;
    // }

    /**
     * Gets the message signature with which the auto response will be sent
     * 
     * @return The message signature with which the auto response will be sent
     *         as <code>String</code>
     */
    public String getOutMsgSignature() {
	return this.outMsgSignature;
    }

    /**
	 * 
	 */
    public void addTimeStamp() {
	Calendar cal = Calendar.getInstance();
	Date t = cal.getTime();
	timeStamps.add(t);
	super.setNewTimeStamp(t);
    }

    /**
     * Gets the amount of messages which has been already sent
     * 
     * @return The size of the sent messages
     */
    public int getSentMessageCount() {
	return this.timeStamps.size();
    }

    /**
     * Gets the time stamps
     * 
     * @return timestamps
     */
    public List<Date> getTimeStamps() {
	return this.timeStamps;
    }

    // /**
    // * Gets the message content which will be sent as auto response
    // *
    // * @return The message content which will be sent as auto response as
    // * <code>String</code>
    // */
    // public String getMsgContent() {
    // return this.msgContent;
    // }

    /**
     * Gets the milliseconds the response should be delayed
     * 
     * @return The milliseconds the response should be delayed as
     *         <code>long</code>
     */
    public long getDelay() {
	return this.delay;
    }

    /**
     * @param uid
     *            the uid to set
     */
    public void setUid(UUID uid) {
	this.uid = uid;
    }

    /**
     * @return the uid
     */
    public UUID getUid() {
	return uid;
    }
}
