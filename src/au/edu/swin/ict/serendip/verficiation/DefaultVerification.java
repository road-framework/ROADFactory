package au.edu.swin.ict.serendip.verficiation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.processmining.framework.models.ModelGraphEdge;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCConnector;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCObject;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.ConstraintType;
import au.edu.swin.ict.road.xml.bindings.EventType;
import au.edu.swin.ict.road.xml.bindings.EventsType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.TaskRefType;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.constraint.parser.ConstraintParser;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.util.CompositionUtil;
import au.edu.swin.ict.serendip.util.StrUtil;

public class DefaultVerification implements SerendipVerification {
    private static Logger log = Logger.getLogger(DefaultVerification.class
	    .getName());

    @Override
    public VerificationResult verify(ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException {
	// TODO Auto-generated method stub
	VerificationResult result = new VerificationResult();
	Composition comp = modelProviderFactory.getEngine().getComposition();
	EventsType eventTypes = comp.getComposite().getSmcBinding().getEvents();
	List<ProcessDefinitionType> processDefinitionTypeList = comp
		.getComposite().getSmcBinding().getProcessDefinitions()
		.getProcessDefinition();
	for (ProcessDefinitionType pdt : processDefinitionTypeList) {
	    result = verifyProcessDef(modelProviderFactory, pdt.getId());
	    if (!result.isValid()) {
		// Fail
		return result;
	    }
	}

	result.setValid(true);
	return result;
    }

    @Override
    public VerificationResult verifyBehavior(
	    ModelProviderFactory modelProviderFactory, String behaviorId)
	    throws SerendipVerificationException {
	// TODO Auto-generated method stub
	return new VerificationResult(true,
		"Default verification completed on " + behaviorId, null, null);
    }

    @Override
    public VerificationResult verifyProcessDef(
	    ModelProviderFactory modelProviderFactory, String definitionId)
	    throws SerendipVerificationException {
	// TODO Auto-generated method stub
	Composition comp = modelProviderFactory.getEngine().getComposition();
	List<ProcessDefinitionType> processDefinitionTypeList = comp
		.getComposite().getSmcBinding().getProcessDefinitions()
		.getProcessDefinition();
	for (ProcessDefinitionType pdt : processDefinitionTypeList) {
	    if (pdt.getId().equals(definitionId)) {
		// Match
		return this.verifyProcessDefinition(comp, pdt);
	    }
	}

	// If no exceptions we are good!
	return new VerificationResult(false, "Cannot find " + definitionId
		+ " . Check the id ", null, null);
    }

    /**
     * Check if a process definition contains an event pattern EP, such that an
     * event Ei of EP is not get triggered before Algo: Split EP to get Ei[]
     * for(each event Ei of EP){ Collection outEvents }
     * 
     * @param compType
     * @param pd
     * @return
     * @throws SerendipVerificationException
     */
    // DONE
    private VerificationResult verifyProcessDefinition(Composition comp,
	    ProcessDefinitionType pd) throws SerendipVerificationException {
	String[] inputEventsIdArray = new String[0];
	String[] outputEventsIdArray = new String[0];
	String eventsStr = "";

	List<String> btRefIdList = pd.getBehaviorTermRefs().getBehavirTermId();
	String[] btRefIdArr = new String[btRefIdList.size()];
	BehaviorTermType[] btArr = CompositionUtil.getBehaviorTermsForBTIDs(
		btRefIdList.toArray(btRefIdArr), comp);
	for (int i = 0; i < btArr.length; i++) {
	    List<TaskRefType> taskTypeList = btArr[i].getTaskRefs()
		    .getTaskRef();
	    for (TaskRefType tt : taskTypeList) {
		String ep = tt.getPreEP();
		String[] tempInEventArr = StrUtil.getEventsFromEventPattern(ep);
		inputEventsIdArray = (String[]) ArrayUtils.addAll(
			inputEventsIdArray, tempInEventArr);

		String postPattern = tt.getPostEP();
		String[] tempOutEventArr = StrUtil
			.getEventsFromEventPattern(postPattern);
		outputEventsIdArray = (String[]) ArrayUtils.addAll(
			outputEventsIdArray, tempOutEventArr);
	    }
	}

	// Verification.logger.debug("InputEvents = "+StrUtil.ArrayToStr(inputEventsIdArray,
	// "\n"));
	// Verification.logger.debug("OutputEvents = "+StrUtil.ArrayToStr(outputEventsIdArray,
	// "\n"));

	// Check if each input event is an output event at least once.
	for (int m = 0; m < inputEventsIdArray.length; m++) {
	    // Skip initial events e.g. <tns:Event id="BreakDown" isInit="true"
	    // />
	    if (this.isInitEvent(comp, inputEventsIdArray[m])) {
		continue;
	    }
	    if (!ArrayUtils
		    .contains(outputEventsIdArray, inputEventsIdArray[m])) {
		// Warning. Someone has forgotten to fire an event
		// Verification.logger.debug("Checking input event "+inputEventsIdArray[m]);
		eventsStr += inputEventsIdArray[m] + ",";
	    }
	}
	if (!eventsStr.equals("")) {
	    throw new SerendipVerificationException(" Events " + eventsStr
		    + " may not get triggered for PD=" + pd.getId());
	}

	return new VerificationResult(true,
		"Default verification completed on " + pd.getId(), null, null);
    }

    /**
     * Check whether a given event type is an initial event or not
     * 
     * @param eventId
     * @return
     */
    // DONE
    private boolean isInitEvent(Composition comp, String eventId) {
	List<EventType> eventTypeList = comp.getComposite().getSmcBinding()
		.getEvents().getEvent();
	for (EventType et : eventTypeList) {
	    // TODO : Check is init event
	    if (et.getId().equals(eventId)) {
		return et.isIsInit();
	    }

	}

	return false;
    }

    @Override
    public VerificationResult verifyProcessInstance(ProcessInstance pi,
	    ModelProviderFactory modelProviderFactory)
	    throws SerendipVerificationException {
	ConfigurableEPC epc = null;
	try {
	    epc = pi.getCurrentProcessView().getViewAsEPC();
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new SerendipVerificationException("Failed to verify "
		    + pi.getPId() + ". Reason:" + e.getMessage());
	}
	// ProcessDefinitionType pdType = pi.getPDef();
	ConstraintType[] constraints = pi.getAllConstriants();

	// Go thru the constriants one by one and identify whether those are
	// satisfiable
	for (int i = 0; i < constraints.length; i++) {
	    ConstraintType constraint = constraints[i];
	    boolean success = this.verifyConstriant(constraint, epc);
	    if (!success) {
		return new VerificationResult(false,
			"Failed to verify constraint " + constraint.getId()
				+ " " + constraint.getExpression(), null,
			constraint.getId());
	    }
	}

	return new VerificationResult(true, "Dummy " + pi.getId(), null, null);
    }

    /**
     * Verify the validity of a given constraint
     * 
     * @param constraint
     * @param epc
     * @return
     * @throws SerendipVerificationException
     */
    private boolean verifyConstriant(ConstraintType constraint,
	    ConfigurableEPC epc) throws SerendipVerificationException {
	String expression = constraint.getExpression();

	EPCEvent event1 = null, event2 = null;
	String[] tokens = expression.split("\\s+");// split with space
	// [E1] before [E2] within T units
	// Identify the type of constraint.
	if (expression.contains(ConstraintParser.BEFORE)
		&& !expression.contains(ConstraintParser.WITHIN)) {

	} else if (expression.contains(ConstraintParser.BEFORE)
		&& expression.contains(ConstraintParser.WITHIN)) {
	    // [E1] before [E2] within T units
	    String event1Id = tokens[0]
		    .substring(1, tokens[0].lastIndexOf("]"));
	    String event2Id = tokens[2]
		    .substring(1, tokens[2].lastIndexOf("]"));
	    String time = tokens[5];
	    // Identify the corresponding event from EPC
	    // tokens[0] = "[event1]"
	    event1 = epc.getEvent(event1Id);
	    event2 = epc.getEvent(event2Id);
	    if ((null == event1) || (null == event2)) {
		throw new SerendipVerificationException("Cannot find event "
			+ event1Id + " or " + event2Id);
	    }

	    HashSet edgesSetBtwn = epc.getEdgesBetween(event1, event2);
	    Iterator iter = edgesSetBtwn.iterator();
	    while (iter.hasNext()) {
		ModelGraphEdge edge = (ModelGraphEdge) iter.next();
		log.debug(edge.getHead().getName() + "->"
			+ edge.getTail().getName());
	    }
	    // Now we need to calculate what is the time taken from e1 to e2

	}

	return true;
    }

    /**
     * 
     algorithm dft(x) visit(x) FOR each y such that (x,y) is an edge DO IF y
     * was not visited yet THEN dft(y)
     * 
     * @param event1
     * @param event2
     * @param functions
     * @return
     */
    private ArrayList dftFromE1ToE2(EPCEvent event1, EPCEvent event2,
	    ArrayList functions) {
	int value = 0;
	Set<EPCObject> toVisit = new HashSet<EPCObject>();
	toVisit.addAll(event1.getSuccessors());

	ConfigurableEPC epc = event1.getEPC();

	Iterator<EPCObject> succIter = epc.getSucceedingElements(event1)
		.iterator();
	while (succIter.hasNext()) {
	    EPCObject next = succIter.next();
	    if (next instanceof EPCConnector) {
		int type = ((EPCConnector) next).getType();
		if (type == ((EPCConnector) next).OR) {

		} else if (type == ((EPCConnector) next).AND) {

		} else if (type == ((EPCConnector) next).XOR) {

		}
	    }
	}

	return null;
    }

}
