package au.edu.swin.ict.road.roadtest.exception;

/**
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class PlayerNotFoundException extends Exception {
    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
     * @param msg
     */
    public PlayerNotFoundException(String msg) {
	super(msg);
    }
}
