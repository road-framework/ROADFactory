package au.edu.swin.ict.serendip.event;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;

/**
 * This class is responsible of observing problems in event triggering in post
 * event patterns of tasks. The reason why we went for this type of coding
 * pattern is due to the complexity of the data routing mechanism in underlying
 * ROAD fraemwork. A completion of a SINGLE task may possibly result in MULTIPLE
 * result messages. See disjunct operation in InTransform. These result messages
 * may route through different contracts. Also the Message Deliverer is
 * processing messages one by one. So it is diffuclt to determine whether the
 * postEP of a task is correctly triggered or not during the runtime.
 * 
 * An EventObserver will act as follows. For each result message of a SINGLE
 * task (disjunct operation) will refer to the same EventObserver instance. As
 * soon as a message get interpreted, the observer will be notified. When
 * observer identifies that all the messages under its observation are
 * interpreted, the observer checks the event cloud for the triggered events. If
 * events are triggered successfully without violating the postEP, then observer
 * will keep quit. Otherwise obsever will throw an exception.
 * 
 * @author Malinda
 * 
 */
public class EventObserver {
    static Logger logger = Logger.getLogger(EventObserver.class);
    private String postEventPattern = null;
    private Composite composite = null;
    private String pid = null;
    private String taskId = null;
    private EventCloud eCloud = null;
    private List<MessageWrapper> disjunctMsgs = null;

    public EventObserver(Composite composite, String pid, String taskId,
	    List<MessageWrapper> disjunctMsgs) throws SerendipException {
	super();
	this.composite = composite;
	this.pid = pid;
	this.taskId = taskId;
	this.disjunctMsgs = disjunctMsgs;
	// sets himself as the observer
	for (MessageWrapper mw : this.disjunctMsgs) {
	    mw.setEventObserver(this);
	}
	// finds the postEventPattern for <taskId, pid> from the composte.
	if (composite != null) {
	    // First we will set the post event pattern to observe
	    ProcessInstance pi = composite.getSerendipEngine()
		    .getProcessInstance(pid);
	    if (pi != null) {
		Task t = pi.findTaskById(taskId);
		if (t != null) {
		    this.postEventPattern = t.getPostEventPattern();
		} else {
		    throw new SerendipException(
			    "Cannot find the task to observe by the observer for task <"
				    + pid + "," + taskId + ">");
		}
	    } else {
		throw new SerendipException(
			"Cannot find the process instance in the EventObserver observing task <"
				+ pid + "," + taskId + ">");
	    }

	    // Now set the eventCloud
	    SerendipEngine serEngine = composite.getSerendipEngine();
	    if (serEngine != null) {
		this.eCloud = serEngine.getEventCloud();
		if (this.eCloud == null) {
		    throw new SerendipException(
			    "Event cloud is null. Cannot observe");
		}
	    } else {
		throw new SerendipException(
			"Serendip Engine is null. Cannot observe");
	    }
	} else {
	    throw new SerendipException(
		    "Composite instance is null in the EventObserver observing task <"
			    + pid + "," + taskId + ">");
	}
    }

    public void msgInterpreted() throws SerendipException {
	boolean allInterpreted = true;
	for (MessageWrapper mw : this.disjunctMsgs) {
	    if (!mw.isInterpretedByRule()) {
		allInterpreted = false;
	    }
	}

	if (allInterpreted) {
	    // time to check the post event pattern has been triggered or not
	    boolean isPostEPOK = this.verifyNow();
	    if (isPostEPOK) {
		// success
		logger.info("Poset EP is OK for " + this.taskId);
	    } else {
		// problem. All messages are done and dusted. There is nothing
		// else left to trigger events.
		String msgStr = null;
		for (MessageWrapper mw : this.disjunctMsgs) {
		    msgStr += mw.getMessageId() + ",";
		}
		throw new SerendipException(
			"Triggering of post event pattern  "
				+ this.postEventPattern
				+ "  "
				+ "of task  <"
				+ pid
				+ ","
				+ taskId
				+ "> is problematic. "
				+ "None of the following messages triggerd the event pattern. "
				+ "The responsible messages are " + msgStr);
	    }
	} else {
	    // Ignore. we have few more message left. Lets see what they'll do.
	}
    }

    /**
     * This should be called when all the messages of the same disjunct are
     * interpreted if all the events added so far triggered according to the
     * event pattern return true Otherwise false.
     */
    public boolean verifyNow() {
	// we check whether the events are recorded in the event cloud according
	// to the pattern
	// TODO: Need to check if this is the correct way to verify.
	boolean postPatternMatched = eCloud.isPatternMatched(postEventPattern,
		this.pid);
	return postPatternMatched;
    }
}
