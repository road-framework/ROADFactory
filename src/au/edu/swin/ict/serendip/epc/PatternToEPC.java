package au.edu.swin.ict.serendip.epc;

import java.util.ArrayList;

import javax.swing.JFrame;

import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCConfigurableObject;
import org.processmining.framework.models.epcpack.EPCConnector;
import org.processmining.framework.models.epcpack.EPCDataObject;
import org.processmining.framework.models.epcpack.EPCEdge;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;
import org.processmining.framework.models.epcpack.EPCOrgObject;
import org.processmining.framework.models.epcpack.EPCSubstFunction;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.collections.AST;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.epc.test.MergeTest;
import au.edu.swin.ict.serendip.grammar.ep.EventPatternRecognizer;
//import au.edu.swin.ict.serendip.epc.eventpattern.EventPatternRecognizer;
import au.edu.swin.ict.serendip.tool.gui.SerendipEPCView;

/**
 * See more at org.processmining.framework.models.epcpack; I have done some
 * modifications to that code to make it suiable for Serendip visualizations.
 * The jar is SerendipProm_version.jar available under lib/
 * 
 * @author Malinda
 * 
 */
public class PatternToEPC {
    static Logger log = Logger.getLogger(PatternToEPC.class);
    /**
     * This is the overloaded  method implemented for the EPClets
     * TODO: Integrate with the other methos if possible
     * @param prePattern
     * @param taskId
     * @param postPattern
     * @param orgUnit
     * @param input
     * @param output
     * @return
     * @throws SerendipException
     */
    public static ConfigurableEPC convertToEPC(String prePattern,
	    String taskId, String postPattern, String orgUnit, String input, String output, String toProcess) throws SerendipException {
	AST preTree = null, postTree = null;

	try {
	    if (null != prePattern) {
		preTree = EventPatternRecognizer.patternToAST(prePattern);
	    }

	    if (null != postPattern) {
		postTree = EventPatternRecognizer.patternToAST(postPattern);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SerendipException("Cannot parse the event pattern " + e);
	}

	// Create an EPC and a function in it

	ConfigurableEPC epc = new ConfigurableEPC();
	if((null ==taskId) || taskId.equals("")){
	    throw new SerendipException("Task id is NULL or empty ");
	}
	EPCFunction  f = epc.addFunction(new EPCFunction(null, epc)); 
	f.setIdentifier(taskId.trim());
	f.setToProcess(toProcess);
//	if(null!= toProcess){
//	    EPCFunction subf= epc.addFunction(new EPCSubstFunction(f, epc));
//	} 
	 
	if((null !=orgUnit)){
	    f.removeAllOrgObjects();
	    f.addOrgObject(new EPCOrgObject(orgUnit, f));
	} 
	
	
	if(null != input){
	   f.addDataObject(new EPCDataObject(input, f)); 
	}
	//TODO:Outout . No Support from PROM
	construct(preTree, epc, f, true);
	construct(postTree, epc, f, false);
	
	return epc;
    }
    /**
     * If u need to translate to DNF form please refer
     * http://www.stephan-brumme.com/programming/Joole/ OR
     * http://www.izyt.com/BooleanLogic/applet.php
     * 
     * @param prePattern
     *            in Disjunctive Normal Form
     * @param taskId
     * @param postPattern
     *            in Disjunctive Normal Form
     * @return
     * @throws SerendipException
     */
    public static ConfigurableEPC convertToEPC(String prePattern,
	    String taskId, String postPattern) throws SerendipException {
	AST preTree = null, postTree = null;

	try {
	    if (null != prePattern) {
		preTree = EventPatternRecognizer.patternToAST(prePattern);
	    }

	    if (null != postPattern) {
		postTree = EventPatternRecognizer.patternToAST(postPattern);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SerendipException("Cannot parse the event pattern " + e);
	}

	// Create an EPC and a function in it

	ConfigurableEPC epc = new ConfigurableEPC();

	EPCFunction f = epc.addFunction(new EPCFunction(null, epc));
	f.setIdentifier(taskId.trim());

	// /<TEST
	// f.addDataObject(new EPCDataObject("x"+taskId.trim(), f));
	// /ENDTEST>

	// Traverse thru the pre tree
	construct(preTree, epc, f, true);
	construct(postTree, epc, f, false);

	return epc;
    }

    // private static EPCFunction constructPredecessors(EPCFunction f){
    //
    // }

    private static void construct(AST ast, ConfigurableEPC epc,
	    EPCConfigurableObject node, boolean isPre) {
	// logger.info("parsing tree...");
	if ((null == ast)) {
	    return;
	}

	switch (ast.getType()) {
	case EventPatternRecognizer.OR:

	    EPCConnector orCon = epc.addConnector(new EPCConnector(
		    EPCConnector.OR, epc));
	    EPCEdge edge1 = null;
	    if (isPre) {
		edge1 = epc.addEdge(orCon, node);
		log.debug(edge1.getKey());
	    } else {
		edge1 = epc.addEdge(node, orCon);
		log.debug(edge1.getKey());
	    }

	    // int numofChildren = ast.getNumberOfChildren();

	    AST childOfOR = ast.getFirstChild();
	    construct(childOfOR, epc, orCon, isPre);// Too bad that there is no
						    // ast.getChild(index);

	    while (null != childOfOR) {
		childOfOR = childOfOR.getNextSibling();
		construct(childOfOR, epc, orCon, isPre);
	    }

	    break;

	case EventPatternRecognizer.XOR:
	    EPCConnector xorCon = epc.addConnector(new EPCConnector(
		    EPCConnector.XOR, epc));
	    EPCEdge edge3 = null;
	    if (isPre) {
		edge3 = epc.addEdge(xorCon, node);
		log.debug(edge3.getKey());
	    } else {
		edge3 = epc.addEdge(node, xorCon);
		log.debug(edge3.getKey());
	    }
	    AST childOfXOR = ast.getFirstChild();
	    construct(childOfXOR, epc, xorCon, isPre);
	    while (null != childOfXOR) {
		childOfXOR = childOfXOR.getNextSibling();
		construct(childOfXOR, epc, xorCon, isPre);
	    }

	    //
	    break;
	case EventPatternRecognizer.AND:
	    EPCConnector andCon = epc.addConnector(new EPCConnector(
		    EPCConnector.AND, epc));
	    EPCEdge edge2 = null;
	    if (isPre) {
		edge2 = epc.addEdge(andCon, node);
		log.debug(edge2.getKey());
	    } else {
		edge2 = epc.addEdge(node, andCon);
		log.debug(edge2.getKey());
	    }

	    AST childOfAnd = ast.getFirstChild();
	    construct(childOfAnd, epc, andCon, isPre);// Too bad that there is
						      // no
						      // ast.getChild(index);

	    while (null != childOfAnd) {
		childOfAnd = childOfAnd.getNextSibling();
		construct(childOfAnd, epc, andCon, isPre);
	    }

	    break;
	case EventPatternRecognizer.WORD:
	    // Add event
	    String eventId = ast.getText().trim();

	    // eventId = eventId.substring(eventId.indexOf("[") + 1,
	    // eventId.indexOf("]"));

	    EPCEvent e = new EPCEvent(eventId, epc);
	    epc.addEvent(e);
	    // add arcs
	    if (isPre) {
		epc.addEdge(e, node);
	    } else {
		epc.addEdge(node, e);
	    }

	    // construct(ast.getNextSibling(), epc, node, isPre);
	    break;

	}

    }

    public static void main(String[] args) {
	try {

	    ConfigurableEPC epc = PatternToEPC.convertToEPC(
		    "eTowRequested * ePickupLocKnown ", "TT.tTow",
		    "eCarTowed ^ eTowFailed");
	    ;

	    SerendipEPCView epcView = new SerendipEPCView("test", epc);
	    MergeTest.frameIt(epcView);

	    // ////////////////////////////////////coloring///////////
	    // epc.getFunctions().get(0).setAttribute(EPCFunction.FILLCOLOR_ATTR,
	    // "red");

	    // epc.getEvent("TowFailed").setAttribute(EPCEvent.FILLCOLOR_ATTR,
	    // "red");
	    // epc.getEvent("E6").setAttribute(EPCEvent.FILLCOLOR_ATTR, "blue");
	    // ////////////////////////////////////

	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
