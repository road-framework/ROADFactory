package au.edu.swin.ict.road.roadtest;

import java.util.UUID;

import org.apache.log4j.Level;

import au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent;
import au.edu.swin.ict.road.roadtest.listeners.MessageArrivedEventListener;

/**
 * AutoResponder class which manages the auto response for a players inbox
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class AutoResponder {
    private UUID uid = null;
    private boolean stopped = false;
    private Player player = null;
    private boolean running = false;
    private AutoResponseMessage autoRespMessage = null;
    private MessageArrivedEventListener msgArrivedEvtList = null;

    /**
     * Constructor for the AutoResponse Class which will get the player whom
     * this belongs and the settings for the auto response
     * 
     * @param player
     *            The player whom this auto responder belongs as
     *            <code>Player</code>
     * @param autoRespMessage
     *            The message which contains the settings for the auto response
     */
    public AutoResponder(Player player, AutoResponseMessage autoRespMessage) {
	this.autoRespMessage = autoRespMessage;
	this.player = player;
	this.uid = UUID.randomUUID();
	this.msgArrivedEvtList = new MessageArrivedEventListener() {
	    public void messageArrived(MessageArrivedEvent msgArrivedEvent) {
		messageArrivedCheck(msgArrivedEvent.getMessage());
	    }
	};
	this.autoRespMessage.setUid(this.uid);
    }

    /**
     * Gets the uid of this auto responder
     * 
     * @return The uid of this auto responder as <code>UUID</code>
     */
    public UUID getUid() {
	return this.uid;
    }

    /**
     * Set the stopped to false if the auto response once has been stopped and
     * need to be started again
     * 
     * @param stopped
     *            the stopped to set true so the auto response can not start
     *            false it can
     */
    public void setStopped(boolean stopped) {
	this.stopped = stopped;
    }

    /**
     * Is this auto responder stopped before
     * 
     * @return the stopped true for autoresponder has been stoped before false
     *         if not
     */
    public boolean isStopped() {
	return stopped;
    }

    /**
     * Is this responder running
     * 
     * @return <code>True</code> if this auto responder is active
     */
    public boolean isRunning() {
	return this.running;
    }

    /**
     * Starts the previously set up (in constructor) responder by listening to
     * the inbox
     */
    public void start() {
	if (!this.stopped) {
	    ROADTest.logROADTest.log(Level.INFO, "Player with id:"
		    + this.player.getPlayerId()
		    + " starts auto response for the signature: "
		    + this.autoRespMessage.getOperationName()
		    + " and respond with the signature: "
		    + this.autoRespMessage.getOutMsgSignature()
		    + " and the uid of this responder is: " + this.getUid());
	    this.player.getInbox().addMessageArrivedEventListener(
		    this.msgArrivedEvtList);
	    this.running = true;
	}
    }

    /**
     * Stops the previously started responder by unregistering from the inbox
     * listener
     */
    public void stop() {
	this.player.getInbox().removeMessageArrivedEventListener(
		this.msgArrivedEvtList);
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.player.getPlayerId()
			+ " stops auto response for the signature: "
			+ this.autoRespMessage.getOperationName()
			+ " and response with the signature: "
			+ this.autoRespMessage.getOutMsgSignature()
			+ " and the uid of this responder is: " + this.getUid()
			+ " " + this.autoRespMessage.getSentMessageCount()
			+ " has been send");
	this.running = false;
	this.stopped = true;
    }

    private void messageArrivedCheck(Message message) {
	if (message.getOperationName().equalsIgnoreCase(
		this.autoRespMessage.getOperationName())) {
	    try {
		Thread.sleep(this.autoRespMessage.getDelay());
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	    this.player.sendMessage(this.autoRespMessage);
	}
    }
}
