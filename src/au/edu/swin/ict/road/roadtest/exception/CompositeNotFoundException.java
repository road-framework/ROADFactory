package au.edu.swin.ict.road.roadtest.exception;

import java.rmi.NotBoundException;

/**
 * This exception is thrown if a composite can not be found on the server
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class CompositeNotFoundException extends NotBoundException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor of this exception with the message
     * 
     * @param string
     *            The message for this exception
     */
    public CompositeNotFoundException(String string) {
	super(string);
    }

}
