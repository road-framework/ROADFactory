package au.edu.swin.ict.road.composite.routing.exceptions;

/**
 * Class that acts as an exception for a response message without any requests.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class NoRequestException extends Exception {

    public NoRequestException() {
	super();
    }

    public NoRequestException(String msg) {
	super(msg);
    }
}
