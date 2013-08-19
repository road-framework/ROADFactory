package au.edu.swin.ict.road.roadtest.events;

/**
 * @deprecated. Not in use. Remove
 * @author Malinda
 * 
 */
public class Event {
    private String eventId = null;
    private String processInstanceId = null;

    /**
     * 
     * @param eventId
     * @param processInstanceId
     */
    public Event(String eventId, String processInstanceId) {
	super();
	this.eventId = eventId;
	this.processInstanceId = processInstanceId;
    }

    public String getEventId() {
	return eventId;
    }

    public void setEventId(String eventId) {
	this.eventId = eventId;
    }

    public String getProcessInstanceId() {
	return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
	this.processInstanceId = processInstanceId;
    }

}
