package au.edu.swin.ict.serendip.composition;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.processmining.framework.models.epcpack.ConfigurableEPC;

import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;

import au.edu.swin.ict.serendip.epc.PatternToEPC;
import au.edu.swin.ict.serendip.event.EventCloud;
import au.edu.swin.ict.serendip.event.EventEngineSubscriber;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.event.SerendipEventListener;
import au.edu.swin.ict.serendip.event.TaskPerformAction;

/**
 * The representation for a Task in a behavior/process. Tasks get initiated as
 * per the defined behaviors.
 * 
 * @author Malinda
 * 
 */
public class Task extends SerendipEventListener {
    static Logger log = Logger.getLogger(SerendipEngine.class);

    // We specifically use lower case letters for enums to support the
    // scripting. Change with care.
    public enum status {
	init, active, completed
    };

    public enum propertyAttribute {
	preep, postep, pp, role, descr
    };

    private String taskDetailedId = null;// This is the function id of the epc
    private String taskId = null;
    // private TaskRefType taskType = null;

    private SerendipEngine engine = null;
    private String obligatedRoleId = null;
    private status currentStatus = status.init;

    private String taskDescr = null;
    private String srcMsgs = null;
    private String endMsg = null;
    private BehaviorTerm behaviorTerm = null;
    private boolean override = false;
    private ProcessInstance pi = null;
    private PerformanceProperty property = null;
    private String postEventPattern = null;
    private ConfigurableEPC epc = null;
    private String taskType = null;

    /**
     * A task description
     * 
     * @param preEP
     * @param obligatedRoleId
     * @param taskId
     * @param postEP
     */
    public Task(String preEP, String obligatedRoleId, String taskId,
	    String postEP) {
	this.obligatedRoleId = obligatedRoleId;
	this.taskId = taskId;
	this.eventPattern = preEP;
	this.postEventPattern = postEP;

    }

    /** New constructor */
    public Task(SerendipEngine engine, ProcessInstance pi, String taskId,
	    String preEP, String postEP, String obligatedRoleId,
	    String taskType, PerformanceProperty property,
	    BehaviorTerm parentBehaviorTerm) {
	super();
	this.engine = engine;
	this.taskId = taskId;
	this.eventPattern = preEP;
	this.postEventPattern = postEP;
	this.obligatedRoleId = obligatedRoleId;

	this.property = property;
	this.behaviorTerm = parentBehaviorTerm;

	if (null != pi) {
	    this.pi = pi;
	    this.pId = pi.getPId();
	} else {
	    // log.error("Process instance is null.");//Note: The visualisation
	    // at au.edu.swin.ict.serendip.petrinet.PetriNetWriter use this
	    // constructor with null process instance/engine.
	}

    }

    // We need to explicitly subscribe a task to an event cloud
    public void subscribeTo(EventCloud ec) {
	if (null != ec) {
	    ec.subscribe(this);
	}
    }

    public ConfigurableEPC constructEPC() {
	ConfigurableEPC epc = null;
	try {
	    log.debug("Converting Task" + this.toString());
	    epc = PatternToEPC.convertToEPC(this.eventPattern,
		    this.obligatedRoleId + "." + this.taskId,
		    this.postEventPattern);
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.epc = epc;
	return epc;
    }

    public ConfigurableEPC getEpc() {
	return epc;
    }

    public void setEpc(ConfigurableEPC epc) {
	this.epc = epc;
    }

    /**
     * This method get executed when the EP-Pre is matched
     * 
     * @throws SerendipException
     */
    @Override
    public void eventPatternMatched(String epPre, String id)
	    throws SerendipException {
	// If the task is not alive, do nothing
	if (!this.isAlive()) {
	    return;// Do nothing
	}

	// If the status is paused, keep looping until it become active
	while (this.pi.currentStatus == ProcessInstance.status.paused) {
	    log.debug("Pi status (pause?) " + this.pi.currentStatus);
	}

	// If the current status is abort, then do not perform the task. We
	// immediately seize the execution.
	if (this.pi.currentStatus == ProcessInstance.status.abort) {
	    log.debug("Pi status (abort?) " + this.pi.currentStatus);
	    this.setAlive(false);
	}
	// Now we can perform the task. First set the status to active.
	this.currentStatus = status.active;
	// Notify the obligated Role
	log.debug("EP= " + epPre + " matched. Role " + obligatedRoleId
		+ " can do Task " + this.taskId + " in behavior "
		+ this.behaviorTerm.getId());

	// NOTE: SHORTCUT for TESTING. TO ENABLE, Set the TEST_FILE property in
	// the serendip.Properties file
	// e.g., TEST_FILE=./sample/Scenario/RoSaS_test.properties
	String testFileName = ModelProviderFactory.getProperty("TEST_FILE");
	if (null != testFileName) {
	    try {
		log.info("-------------TESTING ENABLED FOR SERENDIP -----------------------------------------");
		bypassRoadWithPropFileToPerformTasks(testFileName);

		// given proces instance
	    } catch (SerendipException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	} else { // THE REAL PATH with ROAD

	    if (null != this.engine.getSubscribers()) {
		EventEngineSubscriber sub = this.engine.getSubscribers().get(
			obligatedRoleId);
		if (null == sub) {
		    throw new SerendipException("No such obligated role="
			    + obligatedRoleId
			    + " subscribed to perform the task" + this.taskId);
		}
		log.info(sub.getId() + " is supposed to perform " + this.taskId);
		// For visualization purposes
		if (null != pi) {
		    this.pi.addToCurrentTasks(this);
		}
		// We ask the role to perform the task
		sub.performTask(new TaskPerformAction(this));

	    } else {
		log.error("No subecribers (Roles) in the engine to do tasks. What a lazy world we live in!!!");
	    }

	}
	this.engine.writeLogMessage("TASK", taskId + " executed by "
		+ this.obligatedRoleId);
	this.setCurrentStatus(status.completed);
	this.setAlive(false);
	this.pi.removeFromCurrentTasks(this.getId());
	this.pi.addToCompletedTasks(this);
    }

    /**
     * This is for testing only
     * 
     * @param testFileName
     * @throws SerendipException
     */
    private void bypassRoadWithPropFileToPerformTasks(String testFileName)
	    throws SerendipException {

	Properties props = new Properties();
	try {
	    props.load(new FileInputStream(testFileName));
	} catch (Exception e1) {
	    // TODO Auto-generated catch block
	    throw new SerendipException(e1.getMessage());
	}

	String taskId = this.getId();
	String pid = this.getPId();
	if (null == this.engine) {
	    throw new SerendipException("Engine is null. Cannot perform tasks ");
	}

	if (null != pi) {
	    this.pi.addToCurrentTasks(this);
	}
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// Fire events

	if (this.postEventPattern.contains(Constants.SERENDIP_SYMBOL_OR)
		|| this.postEventPattern
			.contains(Constants.SERENDIP_SYMBOL_XOR)) {
	    // We need to avoid the conflicts
	    String eventStrs = props.getProperty(taskId);
	    if (null == eventStrs) {
		throw new SerendipException("Testing failed: no such task "
			+ taskId + " specified in the file " + testFileName);
	    }

	    String[] splits = eventStrs.trim().split(",");
	    for (int i = 0; i < splits.length; i++) {
		this.engine.addEvent(new EventRecord(splits[i].trim(), pid));
	    }
	} else if (this.postEventPattern
		.contains(Constants.SERENDIP_SYMBOL_AND)) {
	    String[] events = this.postEventPattern.split("\\"
		    + Constants.SERENDIP_SYMBOL_AND);
	    for (String s : events) {
		this.engine.addEvent(new EventRecord(s.trim(), pid));
	    }
	} else {
	    // just fire the one and only event
	    this.engine.addEvent(new EventRecord(this.postEventPattern, pid));
	}

    }

    /**
     * Return the message Ids that need to be processed, in order to perform the
     * task
     * 
     * @return
     */
    public String[] getSourceMessageIds() {
	return this.srcMsgs.split(",");
    }

    public String getInputMsgs() {
	return srcMsgs;
    }

    public void setInputMsgs(String inputMsgs) {
	this.srcMsgs = inputMsgs;
    }

    /**
     * Return tht task description
     * 
     * @return
     */
    public String getTaskDescr() {
	return this.taskDescr;
    }

    private void initLogger() {
	log.setLevel(Level.DEBUG);
    }

    @Override
    public String getId() {
	// TODO Auto-generated method stub
	return this.taskId;
    }

    public String getObligatedRoleId() {
	return obligatedRoleId;
    }

    public void setObligatedRoleId(String obligatedRoleId) {
	this.obligatedRoleId = obligatedRoleId;
    }

    public boolean isOverride() {
	return override;
    }

    public void setOverride(boolean override) {
	this.override = override;
    }

    public status getCurrentStatus() {
	return currentStatus;
    }

    public void setCurrentStatus(status currentStatus) {
	this.currentStatus = currentStatus;
    }

    public String getPostEventPattern() {
	return postEventPattern;
    }

    public void setPostEventPattern(String postEventPattern) {
	this.postEventPattern = postEventPattern;
    }

    public PerformanceProperty getProperty() {
	return property;
    }

    public void setProperty(PerformanceProperty property) {
	this.property = property;
    }

    public String getOutMessageId() {
	// At the moment we use the taskId_Msg
	return this.endMsg;

    }

    public BehaviorTerm getBehaviorTerm() {
	return behaviorTerm;
    }

    public String getTaskDetailedId() {
	return taskDetailedId;
    }

    public void setTaskDetailedId(String taskDetailedId) {
	this.taskDetailedId = taskDetailedId;
    }

    // "\\[[^]]*]"
    public static ArrayList<String> getAllEventsAsAnArray(String eventPattern) {
	ArrayList<String> eventList = new ArrayList<String>();
	/*
	 * \\ -- one backslash, meaning "match next character" [ -- the quoted
	 * character: a [ without special meaning [ -- start of a character
	 * class ^] -- ... containing all characters except ] ] -- end of the
	 * character class -- match zero or more non-] characters ] -- match a
	 * single ] character
	 */
	String regex = "\\[[^]]*]";// Find anything that match [somestring]
	Pattern pattern = Pattern.compile(regex);
	Matcher matcher = pattern.matcher(eventPattern);
	// Find all the matches.
	while (matcher.find()) {
	    String s = matcher.group();
	    String eventId = s.substring(1, s.indexOf("]"));
	    eventList.add(eventId);
	}

	return eventList;
    }

    public String toText() {
	StringBuffer buf = new StringBuffer();
	buf.append("Task " + this.taskId + "{\n");
	buf.append("EPpre " + this.eventPattern + "\n");
	buf.append("EPPost " + this.postEventPattern + "\n");
	buf.append("PerfProb " + this.property.getValue() + "\n");
	buf.append("ROblig " + this.obligatedRoleId + "\n");

	buf.append("};" + "\n");
	return buf.toString();
    }

    public String toString() {
	return this.eventPattern + "->" + this.obligatedRoleId + "."
		+ this.taskId + "->" + this.postEventPattern;
    }

    public void setProcessInstance(ProcessInstance pi) {
	this.pId = pi.getPId();
	this.pi = pi;
    }

}
