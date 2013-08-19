/**
 * 
 */
package au.edu.swin.ict.road.regulator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;

/**
 * @author Aditya
 * 
 */
public class FactTupleSpace {
    private static Logger log = Logger
	    .getLogger(FactTupleSpace.class.getName());
    private List<FactTupleSpaceRow> FTSMemory;

    public FactTupleSpace() {
	FTSMemory = new ArrayList<FactTupleSpaceRow>();
    }

    public void createFactRow(FactObject fact) {
	for (FactTupleSpaceRow factRow : FTSMemory) {
	    String factType = factRow.getFactType();
	    if (fact.getFactType().equalsIgnoreCase(factType)) {
		return;
	    }
	}

	FTSMemory.add(new FactTupleSpaceRow(fact));

    }

    public void updateFact(FactObject fact) {

	boolean isNotificationRequired = false;

	for (FactTupleSpaceRow factRow : FTSMemory) {

	    if (fact.getFactType().equalsIgnoreCase(factRow.getFactType())) {
		List<FactObject> FTSFactList = factRow.getFactList();

		if (FTSFactList.contains(fact)) {
		    FactObject FTSMemoryFactObject = FTSFactList
			    .get(FTSFactList.indexOf(fact));
		    FTSFactList.set(FTSFactList.indexOf(FTSMemoryFactObject),
			    fact.clone());
		    isNotificationRequired = true;
		} else {

		    if (fact.equalsInStructure(factRow.getMasterFact())) {
			FTSFactList.add(fact);
			isNotificationRequired = true;
		    }
		}
		factRow.setTime(new Date());
		if (isNotificationRequired) {
		    notifySubscribers(fact);
		}
		return;
	    }
	}
	// fact tow does not exist create and then update the row with fact.
	createFactRow(fact);
	updateFact(fact);

    }

    public void registerSource(Object source, String name) {
	FactTupleSpaceRow factRow = getFactTupleSpaceRow(name);
	if (factRow != null) {
	    factRow.getSources().add(source);
	}
    }

    public void registerSubscribers(Object subscriber, String name) {
	FactTupleSpaceRow factRow = getFactTupleSpaceRow(name);
	if (factRow != null) {
	    factRow.getSubscribers().add(subscriber);
	}
    }

    public FactTupleSpaceRow getFactTupleSpaceRow(String factType) {
	for (FactTupleSpaceRow factRow : FTSMemory) {
	    if (factRow.getFactType().equalsIgnoreCase(factType)) {
		return factRow;
	    }
	}
	return null;
    }

    public String toString() {
	String FTSString = "";

	for (FactTupleSpaceRow fact : FTSMemory) {
	    FTSString += "\n" + fact.getFactType() + "\t" + fact.getFactList()
		    + "\t" + fact.getTime() + "\t" + fact.getSources() + "\t"
		    + fact.getSubscribers();
	}

	return FTSString;
    }

    public List<FactTupleSpaceRow> getFTSMemory() {
	return FTSMemory;
    }

    public void setFTSMemory(List<FactTupleSpaceRow> memory) {
	FTSMemory = memory;
    }

    public void notifySubscribers(FactObject fact) {

	for (FactTupleSpaceRow factRow : FTSMemory) {
	    if (factRow.getFactType().equalsIgnoreCase(fact.getFactType())) {
		List<Object> subscribers = factRow.getSubscribers();
		for (Object subscriber : subscribers) {
		    if (subscriber instanceof Role) {
			Role subscriberRole = (Role) subscriber;
			boolean synchronizationStatus = subscriberRole
				.getFactSynchroniser(fact.getFactType())
				.factsChanged(factRow.getFactList());
			log.debug("FACT SYNCHRONISED WITH "
				+ subscriberRole.getName() + " and STATUS IS: "
				+ synchronizationStatus);
		    }
		}
	    }
	}
    }

    public synchronized void synchronizeFacts(FactObject fact) {
	for (FactTupleSpaceRow factRow : FTSMemory) {

	    if (fact.getFactType().equalsIgnoreCase(factRow.getFactType())) {
		List<FactObject> FTSFactList = factRow.getFactList();

		if (FTSFactList.contains(fact)) {
		    FactObject FTSMemoryFactObject = FTSFactList
			    .get(FTSFactList.indexOf(fact));

		    if (fact.isUpdated(FTSMemoryFactObject)) {
			updateFact(fact);
		    }
		} else {
		    updateFact(fact);
		}
	    }
	}
    }
}
