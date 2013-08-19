package au.edu.swin.ict.road.composite.message.exceptions;

/**
 * Exception to be thrown when a message cannot be parsed. Can be used to wrap
 * the SOAP Message exception etc.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
@SuppressWarnings("serial")
public class MessageParsingException extends MessageException {

    public MessageParsingException() {
	super();
    }

    public MessageParsingException(String exceptionStr) {
	super(exceptionStr);
    }

}
