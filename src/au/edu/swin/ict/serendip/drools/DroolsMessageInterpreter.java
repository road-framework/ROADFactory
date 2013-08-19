package au.edu.swin.ict.serendip.drools;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.message.Message;
import au.edu.swin.ict.serendip.message.MessageInterpreter;

/**
 * @deprecated
 * @author Malinda
 * 
 */
public class DroolsMessageInterpreter extends MessageInterpreter {
    static Logger logger = Logger.getLogger(DroolsMessageInterpreter.class);

    public DroolsMessageInterpreter(SerendipEngine engine) {
	super(engine);
	// TODO Auto-generated constructor stub
    }

    @Override
    public EventRecord[] interpretMessage(Message m) throws SerendipException {
	// TODO Auto-generated method stub

	ArrayList<String> postEventIds = new ArrayList<String>();

	// Get which behavior term should interpret this message.
	String mId = m.getId();
	String pId = m.getPid();

	// Get process instance
	ProcessInstance pi = this.engine.getProcessInstance(pId);
	if (null == pi) {
	    throw new SerendipException("Invalid process instance");
	}

	Task task = pi.getCurrentTaskForMsg(m);

	if (null == task) {
	    throw new SerendipException("Cannot find the task for message "
		    + m.getId());
	}
	String postEP = task.getPostEventPattern();

	// Performance improvement: If there is no ambiguity trigger all events
	// in the post EP.
	if (!this.isAmbiguous(postEP)) {
	    postEventIds = Task.getAllEventsAsAnArray(postEP);

	} else {// There are some decisions we need to read from the rules
	    BehaviorTerm bt = task.getBehaviorTerm();
	    String ruleFile = null;// TODO we ned to get the rule file name
	    StatefulKnowledgeSession session = this.readRule(ruleFile);

	    // Insert the eventsList as a fact so that the rule writer can
	    // insert a new event
	    session.insert(postEventIds);
	    session.insert(m);

	    session.fireAllRules();
	    if (postEventIds.size() <= 0) {
		logger.debug("No events got fired by the rule " + ruleFile);
	    }
	    // TODO: Check the postEventIds that got fired. confirm to the event
	    // pattern[Some work]

	    // DUMMY: Remove
	    postEventIds.add("TowReqd");
	    postEventIds.add("RepairReqd");
	}

	// Construct proper events from event ids
	ArrayList<EventRecord> eventsList = new ArrayList<EventRecord>();
	for (int i = 0; i < postEventIds.size(); i++) {
	    eventsList.add(new EventRecord(postEventIds.get(i), pi.getId()));
	}

	// Now we need to remove the completed task from the process instance.
	pi.removeFromCurrentTasks(task.getId());

	EventRecord[] events = new EventRecord[eventsList.size()];
	return eventsList.toArray(events);
    }

    private StatefulKnowledgeSession readRule(String ruleFile)
	    throws SerendipException {
	try {
	    KnowledgeBaseConfiguration kbConfig = KnowledgeBaseFactory
		    .newKnowledgeBaseConfiguration();
	    kbConfig.setOption(EventProcessingOption.STREAM);

	    KnowledgeBase kbase = KnowledgeBaseFactory
		    .newKnowledgeBase(kbConfig);

	    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		    .newKnowledgeBuilder();

	    Resource res = ResourceFactory.newFileResource(ruleFile);
	    kbuilder.add(res, ResourceType.DRL);

	    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	    StatefulKnowledgeSession session = kbase
		    .newStatefulKnowledgeSession();
	    return session;
	} catch (Exception e) {
	    throw new SerendipException("Cannot read rules from " + ruleFile);
	}
    }
}
