package au.edu.swin.ict.serendip.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.grammar.ep.EventPatternRecognizer;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;

/**
 * The event cloud. A simple implementation to check event patterns
 * 
 * @author Malinda
 * 
 */
public class EventCloud {
    static Logger log = Logger.getLogger(EventCloud.class);

    // A temporary events queue to accept new events.
    private Queue<EventRecord> newEventsQ = new ConcurrentLinkedQueue<EventRecord>();
    // The permanent repository of events.
    private Vector<EventRecord> eventRecords = new Vector<EventRecord>();
    private SerendipEngine engine = null;
    Vector<SerendipEventListener> eventListeners = new Vector<SerendipEventListener>();
    private boolean sleep = true;
    private boolean terminated = false;

    public EventCloud(SerendipEngine engine) {
	super();
	this.engine = engine;
    }

    public Vector<EventRecord> getEventRecords() {
	return eventRecords;
    }

    public Vector<EventRecord> getEventRecordsForPid(String pid) {
	Vector<EventRecord> eventRecordsOfPid = new Vector<EventRecord>();
	for (EventRecord er : this.eventRecords) {
	    if (er.getPid().equals(pid)) {
		eventRecordsOfPid.add(er);
	    }
	}
	return eventRecordsOfPid;
    }

    public void printEventListerners() {
	for (int i = 0; i < this.eventListeners.size(); i++) {
	    SerendipEventListener sel = this.eventListeners.get(i);
	    log.debug(sel.getId() + " is subscribed to " + sel.eventPattern);
	}
    }

    public Vector<SerendipEventListener> getCurrentSubscriptions() {
	return this.eventListeners;
    }

    /**
     * Event record is a combination of event id and a process instnce id. Event
     * id should be surrounded by []. e.g, [MyEvent]
     * 
     * @param e
     * @throws SerendipException
     */
    public synchronized void addEvent(EventRecord e) throws SerendipException {
	this.newEventsQ.add(e); // This was needed only for threaded behavior
				// (previuosly)?
	this.eventRecords.add(e);
	this.sleep = false;
	log.debug("Added event  " + e.getEventId() + " for pid=" + e.getPid());
	this.fire(this.newEventsQ.poll());
    }

    public void subscribe(SerendipEventListener sel) {
	// logger.info("Subscribed for "+sel.eventPattern+" pid="+sel.pId);
	this.eventListeners.add(sel);
    }

    /**
     * Call <code>eventPatternMatched</code> of the
     * <code>SerendipEventListener</code> when an event pattern is matched.
     * 
     * @param sourceEvent
     * @throws SerendipException
     */
    private void fire(EventRecord sourceEvent) throws SerendipException {
	log.info("Event added to cloud " + sourceEvent);
	// first remove the expired events
	this.removeExpiredEvents();

	// Iterate thru the event listeners
	for (int i = 0; i < this.eventListeners.size(); i++) {
	    SerendipEventListener sel = this.eventListeners.get(i);
	    if (null == sel.getEventPattern()) {
		log.error("Ignored event listener " + sel.getId()
			+ " . The event pattern is not set");
		continue;// For some reason the event pattern is null
	    } else {
		String selId = sel.getId();
		log.debug(selId + " needs event pattern " + sel.eventPattern);
	    }

	    if (sel.isAlive()) {
		// Performance: With this step we eliminate large number of
		// subscribers who are not interested about this event.
		if (sel.getEventPattern().contains(sourceEvent.getEventId())) {

		    if (this.isPatternMatched(sel.eventPattern, sel.pId)) {
			sel.eventPatternMatched(sel.eventPattern, sel.pId);
		    } else {
			log.debug("Pattern did not match " + sel.eventPattern);
		    }
		} else {
		    // ignore the sel
		    log.debug(sel.getId() + " ignored. EP="
			    + sel.getEventPattern()
			    + " Not contains the event id"
			    + sourceEvent.getEventId());
		}
	    } else {
		// ignore the sel
		log.debug(sel.getId() + " ignored. Its not live.");
	    }

	}
    }

    /**
     * Check whether an event pattern is matched
     * 
     * @param eventPattern
     * @param pId
     * @return
     */
    public boolean isPatternMatched(String eventPattern, String pId) {

	try {
	    return EventPatternRecognizer.isPatternMatched(eventPattern, pId,
		    this);
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return false;
    }

    /**
     * Compare time stamps of two event records of the same process instance
     * 
     * @param e1
     * @param e2
     * @return the value 0 if the argument Date is equal to this Date; a value
     *         less than 0 if this Date is before the Date argument; and a value
     *         greater than 0 if this Date is after the Date argument.
     */
    public int compareEventDate(String event1Id, String event2Id, String pId) {
	EventRecord e1, e2 = null;

	e1 = this.getEventReocrd(event1Id, pId);
	e2 = this.getEventReocrd(event2Id, pId);

	return e1.getCallDateTime().compareTo(e2.getCallDateTime());
    }

    /**
     * If the record is found, return it. Else return null
     * 
     * @param eventId
     * @param pId
     * @return
     */
    public EventRecord getEventReocrd(String eventId, String pId) {
	for (int i = 0; i < this.eventRecords.size(); i++) {
	    EventRecord e = this.eventRecords.get(i);
	    if (e.getEventId().equals(eventId) && e.getPid().equals(pId)) {
		return e;
	    }
	}
	return null;
    }

    /**
     * Check whether a particular event has occurred for a given process
     * instance.
     * 
     * @param eventId
     * @param pId
     * @return
     */
    public boolean isEventRecorded(String eventId, String pId) {
	// eventId = eventId.substring(1, eventId.length() - 1);
	Iterator<EventRecord> eventRecordsIter = this.eventRecords.iterator();
	while (eventRecordsIter.hasNext()) {
	    EventRecord event = eventRecordsIter.next();
	    if (event.getEventId().trim().equals(eventId.trim())
		    && event.getPid().trim().equals(pId.trim())) {
		return true;
	    }
	}
	return false; // No event to this eventId yet
    }

    /**
     * Removes an event from the event record
     * 
     * @param eventId
     * @param pId
     */
    public void expireEvent(String eventId, String pId) {
	for (int i = 0; i < this.eventRecords.size(); i++) {
	    EventRecord e = this.eventRecords.get(i);
	    if (e.getEventId().equals(eventId) && e.getPid().equals(pId)) {
		this.eventRecords.remove(i);
	    }
	}
    }

    /**
     * Remove the expired events
     */
    private void removeExpiredEvents() {
	for (EventRecord er : this.eventRecords) {
	    if (er.hasExpired()) {
		this.eventRecords.remove(er);
	    }
	}
    }

    private void initLogger() {
	log.setLevel(Level.INFO);

    }

    public void terminate() {
	this.terminated = true;
    }
    // @Override
    // public void run() {
    // //loop
    // while(!terminated){
    // log.info(".");
    // while (!this.newEventsQ.isEmpty() ){
    // log.debug("*");
    // //If so fire
    // EventRecord e = this.newEventsQ.poll();
    // try {
    // this.fire(e);//Fire event
    // } catch (SerendipException e1) {
    // // TODO Auto-generated catch block
    // e1.printStackTrace();
    // }
    // }//finished firing all the new events. Go back to sleep.
    // Thread.yield();
    // }
    //
    // }

}
