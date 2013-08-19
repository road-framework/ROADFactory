package au.edu.swin.ict.road.composite.exceptions;

/**
 * Is thrown when there is a problem building an object containing a roles
 * operations.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class RoleDescriptionGenerationException extends Exception {

    /**
     * The default RoleDescriptionGenerationException constructor. Throws the
     * exception with a default message.
     */
    public RoleDescriptionGenerationException() {
	super();
    }

    /**
     * This constructor allows a custom message to be passed.
     * 
     * @param msg
     *            the custom message.
     */
    public RoleDescriptionGenerationException(String msg) {
	super(msg);
    }
}
