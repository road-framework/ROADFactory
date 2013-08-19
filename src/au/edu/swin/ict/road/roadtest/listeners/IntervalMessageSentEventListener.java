package au.edu.swin.ict.road.roadtest.listeners;

import java.util.EventListener;

import au.edu.swin.ict.road.roadtest.events.IntervalMessageSentEvent;

/**
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public interface IntervalMessageSentEventListener extends EventListener {

    /**
     * @param intMsgSentEvent
     */
    void intervalMessageSent(IntervalMessageSentEvent intMsgSentEvent);
}
