package au.edu.swin.ict.serendip.event;

public class EventQuery {
    private String event1Id;
    private String event2Id;
    private String operator;
    private String pid;
    public boolean matched = false;

    /**
     * 
     * @param event1Id
     *            the id attribute of event 1
     * @param event2Id
     *            the id attribute of event 2
     * @param operator
     *            AND, OR
     * @param pid
     *            the process instance id
     */
    public EventQuery(String event1Id, String event2Id, String operator,
	    String pid) {
	super();
	this.event1Id = event1Id;
	this.event2Id = event2Id;
	this.operator = operator;
	this.pid = pid;
    }

    public String getPid() {
	return pid;
    }

    public void setPid(String pid) {
	this.pid = pid;
    }

    public String getEvent1Id() {
	return event1Id;
    }

    public void setEvent1Id(String event1Id) {
	this.event1Id = event1Id;
    }

    public String getEvent2Id() {
	return event2Id;
    }

    public void setEvent2Id(String event2Id) {
	this.event2Id = event2Id;
    }

    public String getOperator() {
	return operator;
    }

    public void setOperator(String operator) {
	this.operator = operator;
    }

    public boolean isMatched() {
	return matched;
    }

    public void setMatched(boolean matched) {
	this.matched = matched;
    }

}
