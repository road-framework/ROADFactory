package au.edu.swin.ict.road.roadtest.listeners;

import java.util.EventListener;

import au.edu.swin.ict.road.roadtest.events.MessageSentEvent;

/**
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public interface MessageSentEventListener extends EventListener {
    /**
     * @param msgSentEvent
     */
    void messageSent(MessageSentEvent msgSentEvent);
}
