package au.edu.swin.ict.road.composite.rules.events.contract;

public class TermExecutedEvent {

    private String term;
    private Object message;
    private long correlationId;

    public String getTerm() {
	return term;
    }

    public void setTerm(String term) {
	this.term = term;
    }

    public Object getMessage() {
	return message;
    }

    public void setMessage(Object message) {
	this.message = message;
    }

    public long getCorrelationId() {
	return correlationId;
    }

    public void setCorrelationId(long correlationId) {
	this.correlationId = correlationId;
    }
}
