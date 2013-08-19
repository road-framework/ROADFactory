package au.edu.swin.ict.serendip.event;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;

/**
 * Represent an event record in Serendip. Each event associated with a process
 * instance. Once an event is created, a timestamp is set. The duration can be
 * set to expire the events.
 * 
 * @author Malinda Kapuruge
 * 
 */
public class EventRecord {
    private static Logger log = Logger.getLogger(EventRecord.class.getName());
    String eventId = null;
    String pid = null;
    private Date callDateTime = null;
    private Date expDateTime = null;

    /**
     * An event record.
     * 
     * @param eventId
     *            Event id should be surrounded by []. e.g, [MyEvent]
     * @param processInstanceId
     *            the process instacne in which this event belong
     */
    public EventRecord(String eventId, String processInstanceId) {
	super();
	if (null != eventId) {
	    this.eventId = eventId.trim();
	}
	if (null != processInstanceId) {
	    this.pid = processInstanceId.trim();
	}
	this.callDateTime = new Date();
	// Set expiration to inifinity as default
	Calendar cal = new GregorianCalendar();
	cal.set(Calendar.YEAR, 9999);// Date(Long.MAX_VALUE) is buggy
	expDateTime = cal.getTime();
    }

    public boolean hasExpired() {
	if (this.expDateTime.compareTo(new Date()) < 0) {
	    return true;
	} else {
	    return false;
	}

    }

    public String getPid() {
	return pid;
    }

    public void setPid(String processInstanceId) {
	if (null != processInstanceId) {
	    this.pid = processInstanceId.trim();
	}
    }

    public Date getCallDateTime() {
	return callDateTime;
    }

    public void setCallDateTime(Date callDateTime) {
	this.callDateTime = callDateTime;
    }

    public Date getExpiration() {
	return this.expDateTime;
    }

    /**
     * Use this method to specifically set the expiration time. By default the
     * event expires when the process instance is complete.
     * 
     * @param expirationDate
     */
    public void setExpiration(Date expirationDate) {
	this.expDateTime = expirationDate;
    }

    /**
     * Set/Reset the expiration time using a number
     * 
     * @param field
     *            e.g. Calendar.DATE
     * @param amount
     *            e.g., 10 (ten days)
     * @see void java.util.Calendar.add(int field, int amount)
     */
    public void setExpiration(int field, int amount) {
	Calendar cal = Calendar.getInstance();
	cal.add(field, amount);
	this.setExpiration(cal.getTime());
    }

    public String getEventId() {
	return eventId;
    }

    public String toString() {
	return this.eventId + ", " + this.pid;
    }

    public void setEventId(String eventId) {
	if (null != eventId) {
	    this.eventId = eventId.trim();
	}
    }

    public static void main(String[] args) {
	log.debug(new java.util.Date(Long.MAX_VALUE).toString());
    }

}
