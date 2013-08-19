package au.edu.swin.ict.road.demarshalling.exceptions;

/**
 * CompositeDemarshallingException can be thrown when an error occurs
 * transforming the xml version of a composite into the concrete java version,
 * for example if the xml file can not be read or is not well formed.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class CompositeDemarshallingException extends Exception {

    /**
     * The default CompositeDemarshallingException constructor. Throws the
     * exception with a default message.
     */
    public CompositeDemarshallingException() {
	super("The composite could not be demarshelled. It is likely one more"
		+ "elements in the composite xml data file was incorrect.");
    }

    /**
     * This constructor allows a custom message to be passed.
     * 
     * @param msg
     *            the custom message.
     */
    public CompositeDemarshallingException(String msg) {
	super(msg);
    }
}
