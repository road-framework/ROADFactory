package au.edu.swin.ict.road.roadtest;

import java.util.UUID;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Level;

import au.edu.swin.ict.road.roadtest.events.IntervalMessageSentEvent;
import au.edu.swin.ict.road.roadtest.listeners.IntervalMessageSentEventListener;
import au.edu.swin.ict.road.roadtest.IntervalMessage;
import au.edu.swin.ict.road.roadtest.Player;
import au.edu.swin.ict.road.roadtest.ROADTest;

/**
 * The IntervalMessageSender which takes the work of sending a message in
 * intervals
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public class IntervalMessageSender extends Thread {

    /**
	 * 
	 */
    protected EventListenerList listenerList = new EventListenerList();

    private boolean terminate;
    private boolean stopped = false;
    private UUID uid;
    private Player player;
    private IntervalMessage intervalMessage;

    /**
     * Returns the uuid of this object
     * 
     * @return the uid the uuid of this object
     */
    public UUID getUid() {
	return uid;

    }

    /**
     * Constructor which takes the object <code>IntervalMessage</code> and a
     * Reference to the player.
     * 
     * @param intervalMessage
     *            The intervalMessage object which contains the information of
     *            the message to be sent
     * @param player
     *            The reference to the player which sends the message in
     *            intervals
     */
    public IntervalMessageSender(IntervalMessage intervalMessage, Player player) {
	this.uid = UUID.randomUUID();
	this.player = player;
	this.intervalMessage = intervalMessage;
	this.intervalMessage.setUid(uid);
    }

    /**
     * Terminates the message sending
     * 
     * @param terminate
     *            set to true to stop sending messages
     */
    public void setTerminate(boolean terminate) {
	this.terminate = terminate;
	ROADTest.logROADTest.log(
		Level.INFO,
		"Player with id:" + this.player.getPlayerId()
			+ " stops interval message with the signature: "
			+ this.intervalMessage.getOperationName()
			+ " and the uid of this interval message is: "
			+ this.getUid() + " "
			+ this.intervalMessage.getSentMessageCount()
			+ " has been send");
	this.stopped = true;
    }

    /**
     * Set the stopped to false so the Interval Message can start with run
     * 
     * @param stopped
     *            to false if this interval message needs to started again
     */
    public void setStopped(boolean stopped) {
	this.stopped = stopped;
    }

    /**
     * Gets the stopped parameter which indicates if this interval message has
     * been stopped before
     * 
     * @return The stopped parameter which indicate if this interval message has
     *         been stopped before
     */
    public boolean getStopped() {
	return this.stopped;
    }

    /**
     * Method allows to register for event
     * 
     * @param listener
     */
    public void addIntervalMessageSentEventListener(
	    IntervalMessageSentEventListener listener) {
	listenerList.add(IntervalMessageSentEventListener.class, listener);
    }

    /**
     * Method allows to unregister for event
     * 
     * @param listener
     */
    public void removeIntervalMessageSentEventListener(
	    IntervalMessageSentEventListener listener) {
	listenerList.remove(IntervalMessageSentEventListener.class, listener);
    }

    public void run() {
	if (!this.stopped) {
	    ROADTest.logROADTest.log(Level.INFO,
		    "Player with id:" + this.player.getPlayerId()
			    + " starts interval message with the signature: "
			    + this.intervalMessage.getOperationName()
			    + " and the uid of this interval message is: "
			    + this.getUid());
	    do {
		this.player.sendMessage(this.intervalMessage);
		try {
		    Thread.sleep(this.intervalMessage.getIntervalUnit());
		} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		fireMyEvent(new IntervalMessageSentEvent(this,
			this.intervalMessage));
	    } while (!terminate);
	}
    }

    private void fireMyEvent(IntervalMessageSentEvent evt) {
	Object[] listeners = listenerList.getListenerList();
	// Each listener occupies two elements - the first is the listener class
	// and the second is the listener instance
	for (int i = 0; i < listeners.length; i += 2) {
	    if (listeners[i] == IntervalMessageSentEventListener.class) {
		((IntervalMessageSentEventListener) listeners[i + 1])
			.intervalMessageSent(evt);
	    }
	}
    }
}
