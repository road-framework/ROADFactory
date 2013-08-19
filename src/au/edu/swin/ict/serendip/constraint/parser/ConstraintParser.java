package au.edu.swin.ict.serendip.constraint.parser;

import org.processmining.framework.models.petrinet.PetriNet;

import au.edu.swin.ict.serendip.core.SerendipException;

public interface ConstraintParser {
    public static final String BEFORE = "before";
    public static final String WITHIN = "within";
    public static final String UNITS = "units";

    public abstract String parse(String expression, PetriNet basePetriNet)
	    throws SerendipException;
}
