package au.edu.swin.ict.road.roadtest;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import au.edu.swin.ict.road.roadtest.Message;

/**
 * Interval MEssage which extends Message. Will store additional informaiton
 * about times the message was sent, uuid, and interval
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class IntervalMessage extends Message {

    private List<Date> timeStamps;
    private long intervalUnit;
    private UUID uid;

    /**
     * Gets the interval in which a message is going to be sent
     * 
     * @return The value of the interval in which a the messages are going to be
     *         sent
     */
    public long getIntervalUnit() {
	return this.intervalUnit;
    }

    /**
     * Constructor
     * 
     * @param msgContent
     * @param operationName
     * @param originRoleId
     * @param interval
     */
    public IntervalMessage(String msgContent, String operationName,
	    String originRoleId, long interval) {
	super(msgContent, operationName, originRoleId, false);
	this.intervalUnit = interval;
	this.timeStamps = new ArrayList<Date>();
    }

    /**
	 * 
	 */
    public void addTimeStamp() {
	Calendar cal = Calendar.getInstance();
	Date t = cal.getTime();
	timeStamps.add(t);
	setNewTimeStamp(t);
    }

    /**
     * Gets the time stamps
     * 
     * @return timestamps
     */
    public List<Date> getTimeStamps() {
	return this.timeStamps;
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
