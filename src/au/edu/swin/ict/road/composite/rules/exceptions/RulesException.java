package au.edu.swin.ict.road.composite.rules.exceptions;

/**
 * Exception class for rules related exception.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class RulesException extends Exception {

    /**
     * Constructor for a custom exception message to be placed
     * 
     * @param msg
     */
    public RulesException(String exceptionStr) {
	super(exceptionStr);
    }

    /**
     * Default constructor
     */
    public RulesException() {
	super();
    }
}
