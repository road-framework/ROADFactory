package au.edu.swin.ict.serendip.constraint.parser;

/**
 * @depreciated Still not in use.
 * @author Malinda Kapuruge
 * 
 */
public abstract class ConstraintProperty {
    private String expression = null;

    public ConstraintProperty(String expression) {
	this.expression = expression;
    }

    public abstract String convert();
}
