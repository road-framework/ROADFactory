package au.edu.swin.ict.road.roadtest.listeners;

import java.util.EventListener;

import au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent;

/**
 * An EventListener which can be listened if a message has arrived. While a
 * message arrives the method messageArrived will be fired.
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public interface MessageArrivedEventListener extends EventListener {

    /**
     * <code>messageArrived</code> will be fired if a message arrives in the
     * in-box of a player
     * 
     * @param msgArrivedEvent
     *            The event object which contains the message as
     *            <code>Message</code> object
     */
    void messageArrived(MessageArrivedEvent msgArrivedEvent);
}
