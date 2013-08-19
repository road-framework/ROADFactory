package au.edu.swin.ict.road.classbuilder;

/**
 * Builds an operator descriptor to input to a ClassBuilder.
 * 
 * @author Malinda Kapuruge
 */
public class ClassOperations {
    public static final String CLOADER_VISIBILITY_PUBLIC = "public";
    public static final String CLOADER_VISIBILITY_PRIVATE = "private";
    public static final String CLOADER_VISIBILITY_POTECTED = "protected";
    public static final String CLOADER_VISIBILITY_DEFAULT = "";

    // The method visibility, either public, private or protected
    private String visibility = CLOADER_VISIBILITY_PUBLIC;
    private String opName; // The name of the operation of the class. e.g.
			   // addPerson
    private String inParams; // Parameters as a String. e.g. String name, int
			     // age, boolean married
    private String outParam; // Return type as a String, e.g. boolean
    private String methodBody; // The contents of the method, e.g. System.out

    /**
     * The constructor is used to create a description for a Java operation. For
     * example, the code <code>
     * ClassOperations = new ClassOperations(ClassOperations.CLOADER_VISIBILITY_PUBLIC, "void", "setName", "String name", "firstName = name;");
     * </code> is equal in comparison to method <code>
     * public void setName(String name) {
     * 		firstName = name;
     * }
     * </code>
     * 
     * @param visibility
     *            visibility of the operation e.g. public
     * @param outParam
     *            the return value
     * @param opName
     *            operation name
     * @param inParams
     *            input parameters in comma separated String
     */
    public ClassOperations(String visibility, String outParam, String opName,
	    String inParams, String methodBody) {
	super();
	this.visibility = visibility;
	this.outParam = outParam;
	this.opName = opName;
	this.inParams = inParams;
	this.methodBody = methodBody;
    }

    /* Getter and setter methods */

    public String getOpName() {
	return opName;
    }

    public void setOpName(String opName) {
	this.opName = opName;
    }

    public String getVisibility() {
	return visibility;
    }

    public void setVisibility(String visibility) {
	this.visibility = visibility;
    }

    public String getInParams() {
	return inParams;
    }

    public void setInParams(String inParams) {
	this.inParams = inParams;
    }

    public String getOutParam() {
	return outParam;
    }

    public void setOutParam(String outParam) {
	this.outParam = outParam;
    }

    public String getMethodBody() {
	return methodBody;
    }

    public void setMethodBody(String methodBody) {
	this.methodBody = methodBody;
    }

    public String toString() {
	return "" + this.visibility + " " + this.outParam + " " + this.opName
		+ " (" + this.outParam + "){" + this.methodBody + "}";
    }

}
