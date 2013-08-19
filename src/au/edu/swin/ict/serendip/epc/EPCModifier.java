package au.edu.swin.ict.serendip.epc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import org.processmining.framework.models.ModelGraphEdge;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCConnector;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;
import org.processmining.framework.models.epcpack.EPCObject;

public class EPCModifier {

    /* These methods should be replaced with proper implementation */
    public static ConfigurableEPC addPreEvent(ConfigurableEPC epc,
	    String functionId, String eventId, String connectorStr) {
	// We'll first create the event
	EPCEvent e = new EPCEvent(eventId, epc);
	epc.addEvent(e);
	// Now lets find the funciton
	ArrayList<EPCFunction> funcs = epc.getAllFunctions(functionId);

	if (funcs.size() != 1) {
	    return null;
	}// There should be only 1

	EPCFunction func = funcs.get(0);// Get the first match.
	// Get the predecessor
	ArrayList<EPCObject> preFuncNodes = epc.getPreceedingElements(func);
	if (preFuncNodes.size() == 0) {
	    // There are no predecessors
	    epc.addEdge(e, func);
	} else {// means size=1
	    EPCObject preNode = preFuncNodes.get(0);// Only one predecessor is
						    // possible

	    // remove the current link btwn Node->Func
	    epc.delEdge(preNode, func);
	    EPCConnector connector = null;
	    if (connectorStr.equals("AND")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.AND,
			epc));
	    } else if (connectorStr.equals("OR")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.OR,
			epc));
	    } else if (connectorStr.equals("XOR")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.XOR,
			epc));
	    } else {
		return null;
	    }
	    // Add new connector AND

	    // link em all
	    epc.addEdge(connector, func); // AND->F
	    epc.addEdge(e, connector);// e->AND
	    epc.addEdge(preNode, connector);// preNode->AND

	}

	return epc;

    }

    public static ConfigurableEPC addPostEvent(ConfigurableEPC epc,
	    String functionId, String eventId, String connectorStr) {
	// We'll first create the event
	EPCEvent e = new EPCEvent(eventId, epc);
	epc.addEvent(e);
	// Now lets find the funciton
	ArrayList<EPCFunction> funcs = epc.getAllFunctions(functionId);

	if (funcs.size() != 1) {
	    return null;
	}// There should be only 1

	EPCFunction func = funcs.get(0);// Get the first match.
	// Get the predecessor
	ArrayList<EPCObject> postFuncNodes = epc.getSucceedingElements(func);
	if (postFuncNodes.size() == 0) {
	    // There are no successors
	    epc.addEdge(func, e);
	} else {// means size=1
	    EPCObject node = postFuncNodes.get(0);// Only one predecessor is
						  // possible

	    // remove the current link btwn F->node
	    epc.delEdge(func, node);
	    EPCConnector connector = null;
	    if (connectorStr.equals("AND")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.AND,
			epc));
	    } else if (connectorStr.equals("OR")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.OR,
			epc));
	    } else if (connectorStr.equals("XOR")) {
		connector = epc.addConnector(new EPCConnector(EPCConnector.XOR,
			epc));
	    } else {
		return null;
	    }
	    // Add new connector AND

	    // link em all
	    epc.addEdge(func, connector); // F->C
	    epc.addEdge(connector, e);// C->e
	    epc.addEdge(connector, node);// C->Node

	}

	return epc;

    }

    public static ConfigurableEPC setPP(ConfigurableEPC epc, String functionId,
	    String val) {
	ArrayList<EPCFunction> funcs = epc.getAllFunctions(functionId);

	if (funcs.size() != 1) {
	    return null;
	}// There should be only 1

	EPCFunction func = funcs.get(0);// Get the first match.

	// Now change its identifier
	// Syntax for Task = [source, messages]->{RoleId.TaskId(prop)}
	String newFunctionId = functionId.replaceFirst("\\(.+\\)", "(" + val
		+ ")");
	func.setIdentifier(newFunctionId);
	return epc;
    }
}
