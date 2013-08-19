package au.edu.swin.ict.road.roadtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Level;

import au.edu.swin.ict.road.roadtest.events.MessageSentEvent;
import au.edu.swin.ict.road.roadtest.listeners.MessageSentEventListener;
import au.edu.swin.ict.road.roadtest.IntervalMessage;
import au.edu.swin.ict.road.roadtest.Message;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * The outbox of a player is a storage of all sent messages.
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class Outbox {
    private List<Message> messageHistory = null;

    /**
	 * 
	 */
    protected EventListenerList listenerList = new EventListenerList();

    /**
     * Method allows to register for event
     * 
     * @param listener
     */
    public void addMessageSentEventListener(MessageSentEventListener listener) {
	listenerList.add(MessageSentEventListener.class, listener);
    }

    /**
     * Method allows to unregister for event
     * 
     * @param listener
     */
    public void removeIntervalMessageSentEventListener(
	    MessageSentEventListener listener) {
	listenerList.remove(MessageSentEventListener.class, listener);
    }

    /**
     * Constructor which initiates variables
     */
    public Outbox() {
	this.messageHistory = new ArrayList<Message>();
	ROADTest.logROADTest.log(Level.INFO,
		"ROADTest Outbox created for player ");
    }

    /**
     * Returns a sorted List of Messages which has been sent
     * 
     * @return <code>List Message </code> will be returned and the newest
     *         message will be the first element.
     */
    public List<Message> getMessageHistory() {
	Collections.sort(this.messageHistory);
	return this.messageHistory;
    }

    /**
     * Gets all interval messages which has been sent
     * 
     * @return a list object with message which contains all interval messages
     */
    public List<Message> getIntervalMessageHistory() {
	List<Message> ml = new ArrayList<Message>();
	for (Message m : this.messageHistory) {
	    if (m instanceof IntervalMessage) {
		ml.add(m);
	    }
	}
	return ml;
    }

    /**
     * Gets all non interval messages which has been sent
     * 
     * @return a list object with message which contains all non interval
     *         messages
     */
    public List<Message> getNonIntervalMessageHistory() {
	List<Message> ml = new ArrayList<Message>();
	for (Message m : this.messageHistory) {
	    if (!(m instanceof IntervalMessage)) {
		ml.add(m);
	    }
	}
	return ml;
    }

    /**
     * Adds the <code>message</code> to the message history of this outbox
     * 
     * @param message
     *            which needs to be added to the history
     */
    public void addOutboxMessage(Message message) {
	boolean found = false;
	if (message instanceof IntervalMessage) {
	    for (Message im : messageHistory) {
		if (im.equals(message))
		    found = true;
	    }
	}
	if (!found)
	    this.messageHistory.add(message);
	fireMyEvent(new MessageSentEvent(this, message));
    }

    private void fireMyEvent(MessageSentEvent evt) {
	Object[] listeners = listenerList.getListenerList();
	// Each listener occupies two elements - the first is the listener class
	// and the second is the listener instance
	for (int i = 0; i < listeners.length; i += 2) {
	    if (listeners[i] == MessageSentEventListener.class) {
		((MessageSentEventListener) listeners[i + 1]).messageSent(evt);
	    }
	}
    }
}
