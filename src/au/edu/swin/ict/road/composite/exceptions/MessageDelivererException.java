package au.edu.swin.ict.road.composite.exceptions;

/**
 * MessageDelivererException is thrown if a problem occurs with one of the
 * MessageDeliverers moving messages about the composite.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class MessageDelivererException extends Exception {

    /**
     * The default MessageDelivererException constructor. Throws the exception
     * with a default message.
     */
    public MessageDelivererException() {
	super();
    }

    /**
     * This constructor allows a custom message to be passed.
     * 
     * @param msg
     *            the custom message.
     */
    public MessageDelivererException(String msg) {
	super(msg);
    }
}
