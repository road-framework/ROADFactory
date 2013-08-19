package au.edu.swin.ict.serendip.verficiation;

/**
 * The place holder for verification results
 * 
 * @author Malinda Kapuruge
 * 
 */
public class VerificationResult {
    private boolean valid = false;
    private String message = null;
    private String behaviorName = null;
    private String propertyName = null;

    public VerificationResult() {

    }

    public VerificationResult(boolean valid, String message,
	    String behaviorName, String propertyName) {
	super();
	this.valid = valid;
	this.message = message;
	this.behaviorName = behaviorName;
	this.propertyName = propertyName;
    }

    public boolean isValid() {
	return valid;
    }

    public void setValid(boolean valid) {
	this.valid = valid;
    }

    public String getMessage() {
	return "Invalid behavior: " + this.behaviorName + " due to property "
		+ this.propertyName + "\n " + this.message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public String getBehaviorName() {
	return behaviorName;
    }

    public void setBehaviorName(String behaviorName) {
	this.behaviorName = behaviorName;
    }

    public String getPropertyName() {
	return propertyName;
    }

    public void setPropertyName(String propertyName) {
	this.propertyName = propertyName;
    }

}
