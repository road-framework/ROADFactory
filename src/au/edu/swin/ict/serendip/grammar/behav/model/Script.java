package au.edu.swin.ict.serendip.grammar.behav.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Scripting specific Script model A Script can hold multiple behaviors. (But we
 * really allow one in the GUI tooling level)
 * 
 * @author Malinda
 * 
 */
public class Script {
    private List<Behavior> bHvrs = new ArrayList<Behavior>();

    public List<Behavior> getBHvrs() {
	return bHvrs;
    }

    public void setBHvrs(List<Behavior> hvrs) {
	bHvrs = hvrs;
    }

    public void addBehaviour(Behavior b) {
	this.bHvrs.add(b);
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();

	for (int i = 0; i < this.bHvrs.size(); i++) {
	    buf.append("\n" + this.bHvrs.get(i).toString());
	}

	return buf.toString();
    }
}
