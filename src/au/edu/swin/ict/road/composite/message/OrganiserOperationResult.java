package au.edu.swin.ict.road.composite.message;

public class OrganiserOperationResult {

    private boolean result;
    private StringBuffer message = new StringBuffer();

    private int execTime = 0;

    public OrganiserOperationResult(boolean result, String message) {
	this.result = result;
	this.message.append(message + ".");
    }

    public boolean getResult() {
	return result;
    }

    public void setResult(boolean result) {
	this.result = result;
    }

    public String getMessage() {
	return message.toString();
    }

    public void addMessage(String message) {
	this.message.append(message + ".");
    }

    public int getExecTime() {
	return execTime;
    }

    public void setExecTime(int execTime) {
	this.execTime = execTime;
    }
}
