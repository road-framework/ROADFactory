package au.edu.swin.ict.serendip.constraint.parser;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;

import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.SerendipException;

public class SimplePetriNetConstraintParser implements ConstraintParser {
    static Logger logger = Logger
	    .getLogger(SimplePetriNetConstraintParser.class);

    /**
     * Constraints Syntax: [E1] exist [E1] before [E2] [E1] before [E2] within T
     * units
     * 
     */
    @Override
    // (M(1)>0)-->[0,7](M(3)>0)
    // ([E1]>0)-->[0,7](E2>0)
    public String parse(String expression, PetriNet basePN)
	    throws SerendipException {
	if (null == expression) {
	    throw new SerendipException(
		    "Cannot parse to TCTL. Expression is empty or not in correct format :"
			    + expression);
	}
	logger.debug("Parsing expression :" + expression);
	String eventId1 = null, eventId2 = null;
	// TODO Auto-generated method stub
	// Parse the expression and return the TCTL
	String[] tokens = expression.split("\\s+");// split with space

	if (expression.contains(BEFORE) && !expression.contains(WITHIN)) {
	    // [E1] before [E2]
	    String E1 = this.getMappingNode(tokens[0], basePN);
	    String E2 = this.getMappingNode(tokens[2], basePN);

	    if ((null == E1) || (null == E1)) {
		throw new SerendipException(
			"Cannot parse to TCTL. Cannot find corresponding events in the generated petri nets");
	    }
	    return "(" + E1 + ">0)-->[" + 0 + ",inf](" + E2 + ">0)";// inf =
								    // infinite

	} else if (expression.contains(BEFORE) && expression.contains(WITHIN)) {
	    // [E1] before [E2] within T units
	    String E1 = this.getMappingNode(tokens[0], basePN);
	    String E2 = this.getMappingNode(tokens[2], basePN);
	    String T = tokens[4];

	    if ((null == E1) || (null == E1)) {
		throw new SerendipException(
			"Cannot parse to TCTL. Cannot find corresponding events in the generated petri nets");
	    }
	    return "(" + E1 + ">0)-->[" + 0 + "," + T + "](" + E2 + ">0)";
	} else {

	}
	return null;
    }

    /**
     * Go thru the places of the petrinet and identify the corresponding
     * petrinet id
     */
    private String getMappingNode(String eventId, PetriNet basePN) {
	ArrayList<Place> places = basePN.getPlaces();

	for (int i = 0; i < places.size(); i++) {
	    Place p = places.get(i);

	    if (eventId.equals("[" + p.getIdentifier() + "]")) {// Note that in
								// Petrinet we
								// have only the
								// taskid
		return "M(" + p.getNumber() + ")";
	    }
	}

	return null;
    }

}
