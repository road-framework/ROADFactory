package au.edu.swin.ict.road.composite.message.exceptions;

/**
 * A base class for message related exceptions.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class MessageException extends Exception {

    /**
     * Constructor for a custom exception message to be placed
     * 
     * @param msg
     */
    public MessageException(String exceptionStr) {
	super(exceptionStr);
    }

    /**
     * Default constructor
     */
    public MessageException() {
	super();
    }
}
