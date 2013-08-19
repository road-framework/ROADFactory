package au.edu.swin.ict.road.composite.exceptions;

/**
 * Is thrown when there is a problem instantiating a composite from XML
 * bindings.
 * 
 * @author The ROAD team, Swinburne University of Technology)
 */
@SuppressWarnings("serial")
public class CompositeInstantiationException extends Exception {

    /**
     * The default CompositeInstantiationException constructor. Throws the
     * exception with a default message.
     */
    public CompositeInstantiationException() {
	super();
    }

    /**
     * This constructor allows a custom message to be passed.
     * 
     * @param msg
     *            the custom message.
     */
    public CompositeInstantiationException(String msg) {
	super(msg);
    }
}
