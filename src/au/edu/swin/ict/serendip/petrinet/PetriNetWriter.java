package au.edu.swin.ict.serendip.petrinet;

import java.io.*;
import java.util.*;

import org.processmining.framework.models.ModelGraphPanel;
import org.processmining.framework.models.petrinet.*;

import att.grappa.*;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.epc.EPCToSerendip;

/**
 * Writes a PetriNet to a Remoe format to analyze time petri nets Tokens are not
 * stored in the PNML file.
 * 
 * @author Malinda Kapuruge
 * @version 1.0
 */

public class PetriNetWriter {

    private static int Y = 0;
    private static int X = 0;

    private PetriNetWriter() {
    }

    // my method
    public synchronized static void write(PetriNet net, BufferedWriter bw,
	    String fileName, String[] initPlaces) throws IOException {
	ArrayList<Place> places = net.getPlaces();
	ArrayList<Transition> transitions = net.getTransitions();
	ArrayList edges = net.getEdges();
	int pIndex = 1, tIndex = 1, eIndex = 1;
	bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	bw.write("<TPN name=\"" + fileName + "\">\n");

	// Process places
	for (int i = 0; i < places.size(); i++) {
	    Place p = places.get(i);
	    p.setNumber(i + 1);

	    int x = 100, y = 100;// need to set later
	    int initialMarking = 0;

	    /*
	     * If there is an initPlaces array and the currnt place label is in
	     * the array
	     */
	    if ((null != initPlaces)
		    && (isInArray(p.getIdentifier(), initPlaces))) {// check if
								    // the place
								    // need to
								    // be marked
								    // with a
								    // token
		initialMarking = 1;
	    }

	    bw.write("  <place id=\"" + p.getNumber() + "\" label=\""
		    + p.getIdentifier() + "\"  initialMarking=\""
		    + initialMarking + " \">\n"
		    + "      <graphics  color=\"0\">\n"
		    + "         <position   x=\"" + (x + X) + "\" y=\""
		    + (y + Y) + "\" />\n"
		    + "         <deltaLabel deltax=\"11\" deltay=\"10\"/>\n "
		    + "      </graphics>\n" + "      <scheduling gamma=\"" + 0
		    + "\" omega=\"" + 0 + "\"/> \n");

	    bw.write("  </place>");
	    bw.newLine();
	    bw.newLine();
	    Y = Y + 50;

	}// Eof places
	Y = 0;

	// process Transitions
	for (int i = 0; i < transitions.size(); i++) {
	    Transition t = transitions.get(i);
	    t.setNumber(i + 1);
	    int x = 700, y = 100; // need to set later

	    // Here we set the eft and lft values based on the function id
	    int eft = 0, lft = 0;
	    String tLabel = t.getIdentifier();// This will be reset if the
					      // transition is from a function

	    if (t.getIdentifier().length() > 5) {// Assumption: the assigned
						 // values do not have the
						 // length>5
		Task task = EPCToSerendip.parseTaskId(t.getIdentifier(), null,
			null, null);
		eft = lft = Integer.parseInt(task.getProperty().getValue());
		tLabel = task.getId();
	    }
	    bw.write("  <transition id=\"" + t.getNumber() + "\" label=\""
		    + tLabel + "\" eft=\"" + eft + "\" lft=\"" + lft + "\">\n"
		    + "     <graphics  color=\"0\">\n"
		    + "        <position   x=\"" + (x + X) + "\" y=\""
		    + (y + Y) + "\"/>\n"
		    + "        <deltaLabel deltax=\"20\" deltay=\"20\"/>\n "
		    + "     </graphics>\n");

	    bw.write("  </transition>");
	    bw.newLine();
	    bw.newLine();

	    Y = Y + 60;
	}// Eof transitions
	Y = 0;

	// Process edges
	for (int i = 0; i < edges.size(); i++) {

	    // String source, target;
	    String placeStr = null, transStr = null;
	    String transType = "";

	    PNEdge pnEdge = (PNEdge) edges.get(i);

	    Transition t = pnEdge.getTransition();
	    Place p = (Place) pnEdge.getOpposite(t);

	    transStr = "" + t.getNumber();
	    placeStr = "" + p.getNumber();

	    if (pnEdge.isPT()) {
		transType = "PlaceTransition";

	    } else if (pnEdge.isTP()) {
		transType = "TransitionPlace";

	    } else {
		transType = "unknown";
	    }

	    bw.write("  <arc " + "place=\"" + placeStr + "\" "
		    + "transition=\"" + transStr + "\" " + "type=\""
		    + transType + "\" " + "weight=\"" + 1 + "\"" + ">\n"
		    + "     <nail xnail=\"0\" ynail=\"0\"/>\n"
		    + "     <graphics color=\"0\">\n" + "     </graphics> \n");

	    bw.write("    </arc>\n\n");

	}// EoF edges/arcs

	// Final steps
	bw.write("  <preferences> \n"
		+ "		<colorPlace  c0=\"SkyBlue2\"  c1=\"gray\"  c2=\"cyan\"  c3=\"green\"  c4=\"yellow\"  c5=\"brown\" />\n"
		+ "		<colorTransition  c0=\"yellow\"  c1=\"gray\"  c2=\"cyan\"  c3=\"green\"  c4=\"SkyBlue2\"  c5=\"brown\" />\n"
		+ "		<colorArc  c0=\"black\"  c1=\"gray\"  c2=\"blue\"  c3=\"#beb760\"  c4=\"#be5c7e\"  c5=\"#46be90\" />\n"
		+ " 	</preferences>\n");

	bw.write("</TPN>");

    }

    private static boolean isInArray(String placeId, String[] initPlaces) {
	for (int i = 0; i < initPlaces.length; i++) {
	    if (placeId.equals(initPlaces[i])) {
		return true;
	    }
	}
	return false;
    }

    // //DEPRICATED////

    /**
     * @deprecated
     */
    public synchronized static void write2(PetriNet net, BufferedWriter bw,
	    String fileName) throws IOException {

	ModelGraphPanel grpahPanel = net.getGrappaVisualization();
	Subgraph graph = grpahPanel.getSubgraph();

	Iterator it;
	int i = 0;
	boolean PNKernel = false;
	boolean withSpline = false;
	bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
	bw.write("<TPN name=\"" + fileName + "\">\n");

	Y = Math.abs((int) graph.getBoundingBox().getY());
	X = Math.abs((int) graph.getBoundingBox().getX());

	writePlaces(PNKernel, graph, bw, 1); // I changed the initial value to
					     // i=1
	writeTransitions(PNKernel, graph, bw, 1);// I changed the initial value
						 // to i=1
	writeArcs(withSpline, graph, bw, 1);// I changed the initial value to
					    // i=1

	bw.write("  <preferences> \n"
		+ "		<colorPlace  c0=\"SkyBlue2\"  c1=\"gray\"  c2=\"cyan\"  c3=\"green\"  c4=\"yellow\"  c5=\"brown\" />\n"
		+ "		<colorTransition  c0=\"yellow\"  c1=\"gray\"  c2=\"cyan\"  c3=\"green\"  c4=\"SkyBlue2\"  c5=\"brown\" />\n"
		+ "		<colorArc  c0=\"black\"  c1=\"gray\"  c2=\"blue\"  c3=\"#beb760\"  c4=\"#be5c7e\"  c5=\"#46be90\" />\n"
		+ " 	</preferences>\n");

	bw.write("</TPN>");
    }

    private static int writeArcs(boolean withSpline, Subgraph graph,
	    BufferedWriter bw, int i) throws IOException {
	Enumeration e = graph.edgeElements();
	String transType = "";

	while (e.hasMoreElements()) {
	    Edge edge = (Edge) e.nextElement();
	    Node head = edge.getHead(), tail = edge.getTail();
	    // String source, target;
	    String placeStr = null, transStr = null;

	    // Now we need to know the P->T or T->P
	    if ((tail.object instanceof Transition)
		    && (head.object instanceof Place)) {
		placeStr = "" + ((Place) head.object).getNumber();
		transStr = "" + ((Transition) tail.object).getNumber();
		transType = "TransitionPlace";

	    } else if ((tail.object instanceof Place)
		    && (head.object instanceof Transition)) {
		placeStr = "" + ((Place) tail.object).getNumber();
		transStr = "" + ((Transition) head.object).getNumber();
		transType = "PlaceTransition";

	    }

	    // Faulty
	    // if(edge.getHead().object instanceof Transition){
	    // transType ="TransitionPlace";
	    // }else{
	    // transType ="PlaceTransition";
	    // }

	    /**
	     * <arc place="19" transition="11"
	     * type="TransitionPlace/PlaceTransition" weight="1"> <nail
	     * xnail="0" ynail="0"/> <graphics color="0"> </graphics> </arc>
	     */
	    bw.write("  <arc " + "place=\"" + placeStr + "\" "
		    + "transition=\"" + transStr + "\" " + "type=\""
		    + transType + "\" " + "weight=\"" + 1 + "\"" + ">\n"
		    + "     <nail xnail=\"0\" ynail=\"0\"/>\n"
		    + "     <graphics color=\"0\">\n" + "     </graphics> \n");

	    bw.write("    </arc>\n\n");
	    i++;
	}

	// Now enumerate the nodes on lower levels
	e = graph.subgraphElements();
	while (e.hasMoreElements()) {
	    i = writeArcs(withSpline, (Subgraph) e.nextElement(), bw, i);
	}
	return i;
    }

    private static int writePlaces(boolean PNKernel, Subgraph graph,
	    BufferedWriter bw, int i) throws IOException {
	Enumeration e = graph.nodeElements();

	// first, enumerate the nodes on this level
	while (e.hasMoreElements()) {
	    Element el = (Element) e.nextElement();

	    if (el.object != null && el.object instanceof Place) {
		Node n = (Node) el;
		Place p = (Place) el.object;
		int x = (int) n.getCenterPoint().getX();
		int y = (int) n.getCenterPoint().getY();
		double w = ((Double) n.getAttributeValue(Grappa.WIDTH_ATTR))
			.doubleValue();
		double h = ((Double) n.getAttributeValue(Grappa.HEIGHT_ATTR))
			.doubleValue();

		p.setNumber(i);
		i++;

		/**
		 * 
		 <place id="1" label="P 1" initialMarking="0"> <graphics
		 * color="0"> <position x="48.999994" y="85.999996"/>
		 * <deltaLabel deltax="10" deltay="10"/> </graphics> <scheduling
		 * gamma="0" omega="0"/> </place>
		 */
		// Kau - this is the bug. It gets the number instead of name
		// bw.write("    <place id=\"place_" + p.getNumber() + "\">\n" +
		bw.write("  <place id=\""
			+ p.getNumber()
			+ "\" label=\""
			+ p.getIdentifier()
			+ "\"  initialMarking=\""
			+ 0
			+ " \">\n"
			+ "      <graphics  color=\"0\">\n"
			+ "         <position   x=\""
			+ (x + X)
			+ "\" y=\""
			+ (y + Y)
			+ "\" />\n"
			+ "         <deltaLabel deltax=\"11\" deltay=\"10\"/>\n "
			+ "      </graphics>\n" + "      <scheduling gamma=\""
			+ 0 + "\" omega=\"" + 0 + "\"/> \n");

		bw.write("  </place>");
		bw.newLine();
		bw.newLine();
	    }
	}

	// Now enumerate the nodes on lower levels
	e = graph.subgraphElements();
	while (e.hasMoreElements()) {
	    i = writePlaces(PNKernel, (Subgraph) e.nextElement(), bw, i);
	}
	return i;
    }

    private static int writeTransitions(boolean PNKernel, Subgraph graph,
	    BufferedWriter bw, int i) throws IOException {
	Enumeration e = graph.nodeElements();

	// first, enumerate the nodes on this level
	while (e.hasMoreElements()) {
	    Element el = (Element) e.nextElement();

	    if (el.object != null && el.object instanceof Transition) {
		Node n = (Node) el;
		Transition t = (Transition) el.object;
		int x = (int) n.getCenterPoint().getX();
		int y = (int) n.getCenterPoint().getY();
		double w = ((Double) n.getAttributeValue(Grappa.WIDTH_ATTR))
			.doubleValue();
		double h = ((Double) n.getAttributeValue(Grappa.HEIGHT_ATTR))
			.doubleValue();
		t.setNumber(i);
		i++;
		/*
		 * 
		 * <transition id="11" label="T2" eft="0" lft="33"> <graphics
		 * color="0"> <position x="124.0" y="177.0"/> <deltaLabel
		 * deltax="20" deltay="20"/> </graphics> </transition>
		 */
		bw.write("  <transition id=\""
			+ t.getNumber()
			+ "\" label=\""
			+ t.getIdentifier()
			+ "\" eft=\""
			+ 0
			+ "\" lft=\""
			+ 0
			+ "\">\n"
			+ "     <graphics  color=\"0\">\n"
			+ "        <position   x=\""
			+ (x + X)
			+ "\" y=\""
			+ (y + Y)
			+ "\"/>\n"
			+ "        <deltaLabel deltax=\"20\" deltay=\"20\"/>\n "
			+ "     </graphics>\n");

		bw.write("  </transition>");
		bw.newLine();
		bw.newLine();
	    }
	}

	// Now enumerate the nodes on lower levels
	e = graph.subgraphElements();
	while (e.hasMoreElements()) {
	    i = writeTransitions(PNKernel, (Subgraph) e.nextElement(), bw, i);
	}
	return i;
    }
}
