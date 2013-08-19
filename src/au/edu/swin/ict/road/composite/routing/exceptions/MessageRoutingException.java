package au.edu.swin.ict.road.composite.routing.exceptions;

/**
 * Exception class for message routing
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class MessageRoutingException extends Exception {

    /**
     * Constructor for a custom exception message to be placed
     * 
     * @param msg
     */
    public MessageRoutingException(String exceptionStr) {
	super(exceptionStr);
    }

    /**
     * Default constructor
     */
    public MessageRoutingException() {
	super();
    }
}
