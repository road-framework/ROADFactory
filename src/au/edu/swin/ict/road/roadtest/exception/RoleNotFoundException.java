package au.edu.swin.ict.road.roadtest.exception;

/**
 * This exception is thrown if role is not found
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class RoleNotFoundException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * @param string
     */
    public RoleNotFoundException(String string) {
	super(string);
    }

}
