package au.edu.swin.ict.serendip.event;

import java.util.EventListener;

import au.edu.swin.ict.serendip.core.SerendipException;

/**
 * Any class that need to subscribe for event patterns should extend the
 * <code>SerendipEventListener</code> Current child classes are
 * <ul>
 * <li>Task</li>
 * <li>ProcessInstance</li>
 * </ul>
 * 
 * @author Malinda Kapuruge
 * 
 */
public abstract class SerendipEventListener implements EventListener {
    public String eventPattern = null;
    public String pId = null;

    public abstract void eventPatternMatched(String ep, String pId)
	    throws SerendipException;

    public abstract String getId();

    private boolean alive = true;

    public boolean isAlive() {
	return alive;
    }

    public void setAlive(boolean alive) {
	this.alive = alive;
    }

    public String getEventPattern() {
	return eventPattern;
    }

    public void setEventPattern(String eventPattern) {
	this.eventPattern = eventPattern;
    }

    public String getPId() {
	return pId;
    }

    public void setPId(String id) {
	pId = id;
    }

}
