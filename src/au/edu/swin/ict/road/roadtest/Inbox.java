package au.edu.swin.ict.road.roadtest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Level;

import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.listeners.RolePushMessageListener;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent;
import au.edu.swin.ict.road.roadtest.listeners.MessageArrivedEventListener;
import au.edu.swin.ict.road.roadtest.Message;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * The Inbox which is assigned to each Player who is playing a Role in a
 * composite.
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public class Inbox {

    private List<Message> messageHistory = null;

    // private boolean terminated;
    private Role role = null;
    private IOrganiserRole organiser = null;

    /**
     * The list of who is listing to message arrived events
     */
    protected javax.swing.event.EventListenerList listenerList = new EventListenerList();

    /**
     * Method allows to register to for event
     * 
     * @param listener
     *            Register a <code>listener</code> to the listener list
     */
    public void addMessageArrivedEventListener(
	    MessageArrivedEventListener listener) {
	listenerList.add(MessageArrivedEventListener.class, listener);
    }

    /**
     * Method allows to unregister for event
     * 
     * @param listener
     *            Unregister a <code>listener</code> to the listener list
     */
    public void removeMessageArrivedEventListener(
	    MessageArrivedEventListener listener) {
	listenerList.remove(MessageArrivedEventListener.class, listener);
    }

    /**
     * Construct the <code>Inbox</code> and provide the <code>Role</code> to
     * which this In-box belongs
     * 
     * @param role
     *            The <code>Role</code> this <code>Inbox</code> belongs
     */
    public Inbox(Role role) {
	this.messageHistory = new ArrayList<Message>();
	this.role = role;
	role.registerNewPushListener(new RolePushMessageListener() {
	    public void pushMessageRecieved(IRole role) {
		startMessageProcessingLoop();
	    }
	});
	role.bind("ROADtest UI");
	ROADTest.logROADTest.log(Level.INFO,
		"ROADTest Inbox created for player id: " + role.getId()
			+ " and listening to push notifications.");
    }

    /**
     * Returns a sorted List of Messages which has been received
     * 
     * @return <code>List<Message></code> will be returned and the newest
     *         message will be the first element.
     */
    public List<Message> getMessageHistory() {
	Collections.sort(this.messageHistory);
	return this.messageHistory;
    }

    /**
     * Asks for all available PULL messages at its role and returns it as a
     * <code>List<Message></code>
     * 
     * @return The List Message object which contains the Pull messages sorted
     */
    public List<Message> getPullMessages() {
	MessageWrapper mw = null;
	List<Message> pullMessageList = new ArrayList<Message>();
	do {
	    mw = role.getNextMessage(1, TimeUnit.SECONDS);

	    if (mw != null) {
		Message m = new Message(mw);
		messageHistory.add(m);
		pullMessageList.add(m);
	    }
	} while (mw != null);
	Collections.sort(pullMessageList);
	return pullMessageList;
    }

    private void startMessageProcessingLoop() {
	MessageWrapper mw = role.getNextPushMessage(5, TimeUnit.SECONDS);
	if (mw != null) {
	    Message m = new Message(mw);
	    // this.messages.add(m);
	    MessageArrivedEvent ev = new MessageArrivedEvent(this, m);
	    messageHistory.add(m);
	    fireMyEvent(ev);
	}
    }

    private void fireMyEvent(MessageArrivedEvent evt) {
	Object[] listeners = listenerList.getListenerList();
	// Each listener occupies two elements - the first is the listener class
	// and the second is the listener instance
	for (int i = 0; i < listeners.length; i += 2) {
	    if (listeners[i] == MessageArrivedEventListener.class) {
		((MessageArrivedEventListener) listeners[i + 1])
			.messageArrived(evt);
	    }
	}
    }
}
