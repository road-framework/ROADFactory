package au.edu.swin.ict.road.composite.routing.exceptions;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

/**
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class MessageRoutingUndeterminedException extends
	MessageRoutingException {

    private MessageWrapper message;

    public MessageRoutingUndeterminedException() {
	super();
    }

    public MessageRoutingUndeterminedException(MessageWrapper message,
	    String exceptionStr) {
	super(exceptionStr);
	this.message = message;
    }

    public MessageWrapper getMessageWrapper() {
	return message;
    }
}
