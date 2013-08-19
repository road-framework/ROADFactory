package au.edu.swin.ict.road.composite.rules;

import java.util.ArrayList;
import java.util.List;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.roadtest.events.Event;
import au.edu.swin.ict.serendip.event.EventRecord;

/**
 * When a contractual rule is evaluated, the contract/rule triggers events. The
 * contract uses current facts (contextual+messages) to interpret the events.
 * This class will store all such interpreted events.
 * 
 * @author Malinda
 * 
 */
public class MessageProcessResult {
    private boolean isBlocked = false; // To comply with the current
				       // implementation
    private List<EventRecord> interpretedEvents = new ArrayList<EventRecord>();
    private Term term = null;
    private Contract contract = null;
    private String correlationId = null;

    public MessageProcessResult() {
	super();
    }

    public void addInterpretedEvent(EventRecord event) {
	this.interpretedEvents.add(event);
    }

    public List<EventRecord> getAllInterprettedEvents() {
	return this.interpretedEvents;
    }

    public void addInterpretedEvents(List<EventRecord> events) {
	this.interpretedEvents.addAll(events);
    }

    public boolean isBlocked() {
	return isBlocked;
    }

    public void setBlocked(boolean isBlocked) {
	this.isBlocked = isBlocked;
    }

    public Term getTerm() {
	return term;
    }

    public void setTerm(Term term) {
	this.term = term;
    }

    public Contract getContract() {
	return contract;
    }

    public void setContract(Contract contract) {
	this.contract = contract;
    }

    public String getCorrelationId() {
	return correlationId;
    }

    public void setCorrelationId(String correlationId) {
	this.correlationId = correlationId;
    }

}
