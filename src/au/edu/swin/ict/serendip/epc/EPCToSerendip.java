package au.edu.swin.ict.serendip.epc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.processmining.framework.models.ModelGraphVertex;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCConnector;
import org.processmining.framework.models.epcpack.EPCEdge;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;
import att.grappa.Edge;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.PerformanceProperty;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;

import au.edu.swin.ict.road.xml.bindings.TaskType;

public class EPCToSerendip {

 
    /**
     * Traverse thru a ConfigurableEPC to identify the Tasks defined in it.
     * Syntax for Task = [source, messages]->{RoleId.TaskId(prop)} e.g.
     * [m1,m2]->{CO.doTask(24)}
     * 
     * @param engine
     * @param epc
     * @param bt
     */
    public static void convrtEPCToSerendip(SerendipEngine engine,
	    ProcessInstance pi, ConfigurableEPC epc, BehaviorTerm parentBT) {

	ArrayList<EPCFunction> functions = epc.getFunctions();

	for (int i = 0; i < functions.size(); i++) {
	    EPCFunction f = functions.get(i);

	    // Add to the runtime
	    Task t = parseFunction(f, engine, pi, epc, parentBT);
	    parentBT.addTask(t);

	}
    }

    public static Task parseFunction(EPCFunction f, SerendipEngine engine,
	    ProcessInstance pi, ConfigurableEPC epc, BehaviorTerm bt) {
	String functionId = f.getIdentifier();
	String[] messages = null;
	String role = null, taskId = null, prop = null, preEP = null, postEP = null;
	// Parse function id
	Task t = parseTaskId(functionId, engine, pi, bt);

	// Get pre-event pattern
	preEP = getPreEventPatternOfFunction(f);

	t.setEventPattern(preEP);
	// Get post-event pattern
	postEP = getPostEventPatternOfFunction(f);

	t.setPostEventPattern(postEP);

	return t;
    }

    public static Task parseTaskId(String functionId, SerendipEngine engine,
	    ProcessInstance pi, BehaviorTerm bt) {

	String msgsStr = null, outMsgStr = null, taskStr = null;
	;
	if ((functionId.indexOf("[") > 0) && (functionId.indexOf("]") > 0)) {
	    // "[m1,m2]->{CO.doTask(24)}";
	    msgsStr = functionId.substring(functionId.indexOf("[") + 1,
		    functionId.indexOf("]"));
	    outMsgStr = functionId.substring(functionId.lastIndexOf("[") + 1,
		    functionId.lastIndexOf("]"));
	    taskStr = functionId.substring(functionId.indexOf("{") + 1,
		    functionId.indexOf("}"));
	} else {
	    // or CO.doTask(24)
	    taskStr = functionId;
	}

	String[] taskSplit = taskStr.split("\\.");
	String role = taskSplit[0];
	String prop = "0";
	String taskId = taskSplit[1];
	// If there are parenthesis we need to separate the process property
	// from the task id
	if (taskSplit[1].contains("(")) {
	    prop = taskSplit[1].substring(taskSplit[1].indexOf("(") + 1,
		    taskSplit[1].indexOf(")"));
	    taskId = taskSplit[1].substring(taskSplit[1].indexOf(".") + 1,
		    taskSplit[1].indexOf("("));
	}

	// Task t = new Task(engine, pi, taskId, null, null, msgsStr,
	// taskId+"_Msg", role, new PerformanceProperty(new Integer(prop)), bt);
	Task t = new Task(engine, pi, taskId, null, null, role, null, // TODO:
								      // Need to
								      // update
		new PerformanceProperty(prop), bt);
	t.setTaskDetailedId(functionId);// This is to track back the epc
					// function if needed
	return t;
    }

    /**
     * FOr a given function generates the post-event pattern
     * 
     * @param f
     * @return
     */
    public static String getPostEventPatternOfFunction(EPCFunction f) {
	String s = null;
	ArrayList<Edge> functionEdges = f.getOutEdges();

	// A well-formed epc |.f|=1 and |f.|=1
	String connectorStr = Constants.SERENDIP_SYMBOL_AND;// All the out going
							    // connectors of the
							    // function got AND
							    // semantics
	for (int i = 0; i < functionEdges.size(); i++) {
	    String temPatternStr = null;
	    EPCEdge edge = (EPCEdge) functionEdges.get(0);
	    temPatternStr = getPostEventPattern(edge.getDest());

	    if (0 == i) {// In the first iteration we skip the AND
		s = temPatternStr;
	    } else {
		s = s + connectorStr + temPatternStr; //
	    }
	}
	return s;
    }

    /**
     * For a given function generates the pre-event pattern
     * 
     * @param f
     * @return
     */
    public static String getPreEventPatternOfFunction(EPCFunction f) {
	String s = null;
	ArrayList<Edge> functionEdges = f.getInEdges();

	String connectorStr = Constants.SERENDIP_SYMBOL_AND;// All the incoming
							    // connectors of the
							    // function got AND
							    // semantics
	for (int i = 0; i < functionEdges.size(); i++) {
	    String temPatternStr = null;
	    EPCEdge edge = (EPCEdge) functionEdges.get(0);
	    temPatternStr = getPreEventPattern(edge.getSource());

	    if (0 == i) {// In the first iteration we skip the AND
		s = temPatternStr;
	    } else {
		s = s + connectorStr + temPatternStr; //
	    }
	}
	return s;
    }

    /**
     * Gets the post event pattern of a given node. Leap nodes = Events
     * 
     * @param node
     * @return
     */
    private static String getPostEventPattern(ModelGraphVertex node) {
	String str = null;

	if (node instanceof EPCEvent) {
	    // we return the event id
	    return "[" + ((EPCEvent) node).getIdentifier() + "]";
	} else if (node instanceof EPCConnector) {

	    // we process further
	    EPCConnector connector = (EPCConnector) node;
	    String connectorStr = getTypeAsString(connector.getType());
	    ArrayList<Edge> connectorEdges = connector.getOutEdges();
	    for (int i = 0; i < connectorEdges.size(); i++) {
		EPCEdge edge = (EPCEdge) connectorEdges.get(i);
		ModelGraphVertex edgeDest = edge.getDest();// we get what is
							   // connected to the
							   // operator. can be
							   // another connector
							   // or an event
		String temPatternStr = getPostEventPattern(edgeDest);
		if (0 == i) {// In the first iteration we skip the connectorStr
			     // (AND/OR)
		    str = temPatternStr;
		} else {
		    str = str + connectorStr + temPatternStr; //
		}
	    }
	    return "(" + str + ")";
	} else if (node instanceof EPCFunction) {
	    return "";// We stop when we see a function
	}

	return str;

    }

    // Gets the pre event pattern of a given node. Leap nodes = Events
    private static String getPreEventPattern(ModelGraphVertex node) {
	String str = null;

	if (node instanceof EPCEvent) {
	    // we return the event id
	    return "[" + ((EPCEvent) node).getIdentifier() + "]";
	} else if (node instanceof EPCConnector) {

	    // we process further
	    EPCConnector connector = (EPCConnector) node;
	    String connectorStr = getTypeAsString(connector.getType());
	    ArrayList<Edge> connectorEdges = connector.getInEdges();
	    for (int i = 0; i < connectorEdges.size(); i++) {
		EPCEdge edge = (EPCEdge) connectorEdges.get(i);
		ModelGraphVertex edgeSource = edge.getSource();// we get what is
							       // connected to
							       // the operator.
							       // can be
							       // another
							       // connector or
							       // an event
		String temPatternStr = getPreEventPattern(edgeSource);
		if (0 == i) {// In the first iteration we skip the connectorStr
			     // (AND/OR)
		    str = temPatternStr;
		} else {
		    str = str + connectorStr + temPatternStr; //
		}
	    }
	    return "(" + str + ")";
	} else if (node instanceof EPCFunction) {
	    return "";// We stop when we see a function
	}

	return str;

    }

    private static String getTypeAsString(int type) {
	if (type == EPCConnector.AND) {
	    return Constants.SERENDIP_SYMBOL_AND;
	}
	if (type == EPCConnector.OR) {
	    return Constants.SERENDIP_SYMBOL_OR;
	}
	if (type == EPCConnector.XOR) {
	    return Constants.SERENDIP_SYMBOL_XOR;
	}
	return "";
    }

}
