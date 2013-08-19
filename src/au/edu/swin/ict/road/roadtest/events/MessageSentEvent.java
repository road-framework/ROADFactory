package au.edu.swin.ict.road.roadtest.events;

import java.util.EventObject;

import au.edu.swin.ict.road.roadtest.Message;

/**
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class MessageSentEvent extends EventObject {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private Message message;

    /**
     * @param source
     * @param message
     */
    public MessageSentEvent(Object source, Message message) {
	super(source);
	this.message = message;
    }

    /**
     * @return the message
     */
    public Message getMessage() {
	return this.message;
    }

}
