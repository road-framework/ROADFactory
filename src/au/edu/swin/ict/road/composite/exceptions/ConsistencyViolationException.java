package au.edu.swin.ict.road.composite.exceptions;

/**
 * ConsistencyViolationException is thrown if a change to a composite violates
 * its structural integrity, e.g. a contract bound to a role that does not exist
 * or the use of a duplicate unique id.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class ConsistencyViolationException extends Exception {

    /**
     * The default ConsistencyViolationException constructor. Throws the
     * exception with a default message.
     */
    public ConsistencyViolationException() {
	super();
    }

    /**
     * This constructor allows a custom message to be passed.
     * 
     * @param msg
     *            the custom message.
     */
    public ConsistencyViolationException(String msg) {
	super(msg);
    }
}
