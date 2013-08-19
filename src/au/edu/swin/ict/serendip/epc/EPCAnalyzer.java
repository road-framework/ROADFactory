package au.edu.swin.ict.serendip.epc;

import java.util.ArrayList;
import java.util.Vector;

import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCEvent;

import att.grappa.Edge;

public class EPCAnalyzer {
    ConfigurableEPC epc = null;

    public EPCAnalyzer(ConfigurableEPC epc) {
	this.epc = epc;
    }

    /**
     * Identify the initial events of the epc
     * 
     * @return
     */
    public String[] getInitEvents() {
	// Traverse thru the epc and find events that do not have incoming edges
	Vector<String> vec = new Vector<String>();
	ArrayList<EPCEvent> events = epc.getEvents();
	for (int i = 0; i < events.size(); i++) {
	    EPCEvent event = events.get(i);
	    ArrayList<Edge> edges = event.getInEdges();
	    if ((null == edges) || (edges.size() == 0)) {
		vec.add(event.getIdentifier());
	    }
	}
	String[] eventsArr = new String[vec.size()];
	return vec.toArray(eventsArr);
    }

    /**
     * Identify the unused events of the epc
     * 
     * @return
     */
    public String[] getUnusedEvents() {
	// Traverse thru the epc and find events that do not have incoming edges
	Vector<String> vec = new Vector<String>();
	ArrayList<EPCEvent> events = epc.getEvents();
	for (int i = 0; i < events.size(); i++) {
	    EPCEvent event = events.get(i);
	    ArrayList<Edge> edges = event.getOutEdges();
	    if (edges.size() == 0) {
		vec.add(event.getIdentifier());
	    }
	}
	String[] eventsArr = new String[vec.size()];
	return vec.toArray(eventsArr);
    }

}
