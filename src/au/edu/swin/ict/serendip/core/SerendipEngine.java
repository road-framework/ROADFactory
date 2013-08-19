package au.edu.swin.ict.serendip.core;

/**
 * TODO:
 * 1. Commandline interface for the Process Instance view
 */
import java.util.Hashtable;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.log.LogWriter;
import au.edu.swin.ict.serendip.core.mgmt.SerendipOrganizer;
import au.edu.swin.ict.serendip.core.mgmt.SerendipOrganizerImpl;
import au.edu.swin.ict.serendip.event.EventCloud;
import au.edu.swin.ict.serendip.event.EventEngineSubscriber;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.parser.XMLCompositionParser;
import au.edu.swin.ict.serendip.verficiation.SerendipVerificationException;

public class SerendipEngine {
    static Logger logger = Logger.getLogger(SerendipEngine.class);
    private EventCloud eventCloud = null;
    private XMLCompositionParser parser = null;
    // Engine maintains its own list for living process instances
    private Hashtable<String, ProcessInstance> processInstanceCollection = new Hashtable<String, ProcessInstance>();
    private Hashtable<String, ProcessInstance> completedInstanceCollection = new Hashtable<String, ProcessInstance>();
    // Engine maintain a list of roles that need to be notified when the tasks
    // become doable
    private Hashtable<String, EventEngineSubscriber> subscribers = new Hashtable<String, EventEngineSubscriber>();
    private ModelProviderFactory modelFactory = null;
    // TODO not in use?

    private Composition composition = null;
    private LogWriter logwriter = null;
    private SerendipOrganizer serOrg = null;

    /**
     * 
     * @param composite
     *            the road composite
     * @throws SerendipVerificationException
     */
    public SerendipEngine(Composite composite) throws SerendipException {
	logger.info("Creating the synchronization engine");
	this.composition = new Composition(composite);
	// Create the event cloud
	this.eventCloud = new EventCloud(this);
	// Create ModelProviderFactory
	this.modelFactory = new ModelProviderFactory(this);
	this.serOrg = new SerendipOrganizerImpl(this);

	// Then we verify the models
	// try {
	// this.modelFactory.verifyModels();
	// } catch (SerendipVerificationException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// throw new SerendipException(e.getMessage());
	// }
    }

    public Composition getComposition() {
	return composition;
    }

    public void setComposition(Composition composition) {
	this.composition = composition;
    }

    /**
     * Start a process instance of a given process definition Id
     * 
     * @param defId
     * @throws SerendipException
     */
    public ProcessInstance startProcessInstance(String defId)
	    throws SerendipException {

	ProcessInstance pi = this.modelFactory.getNewProcessInstance(defId);
	pi.initializeSubscriptions();
	pi.setCurrentStatus(ProcessInstance.status.active);
	this.processInstanceCollection.put(pi.getId(), pi);

	this.writeLogMessage("PI",
		"Starting a new process instance, pid:" + pi.getPId());
	return pi;
    }

    public void replaceProcessInstance(ProcessInstance newPi, String oldPiId) {
	ProcessInstance oldPi = this.getProcessInstance(oldPiId);
	this.removeProcessInstance(oldPi);
	newPi.setPId(oldPiId);
	this.processInstanceCollection.put(newPi.getId(), newPi);
	newPi.setCurrentStatus(ProcessInstance.status.active);// May be
							      // redundant but
							      // no harm
    }

    public void pauseAllProcessInstances() {

	// for(ProcessInstance pi: this.processInstanceCollection.values()){
	// pi.currentStatus = ProcessInstance.status.PAUSED;
	// }
	this.serOrg
		.updateStateofAllProcessInstances(ProcessInstance.status.paused
			.toString());
	// this.writeLogMessage("PI", "Paused all instances");
    }

    public void resumeAllProcessInstances() {
	this.writeLogMessage("PI", "Resume all instances");
	// for(ProcessInstance pi: this.processInstanceCollection.values()){
	// pi.currentStatus = ProcessInstance.status.ACTIVE;
	// }
	this.serOrg
		.updateStateofAllProcessInstances(ProcessInstance.status.active
			.toString());
    }

    public String startProcessForEvent(EventRecord e) throws SerendipException {
	if ((null == e.getPid()) || (e.getPid().equals(""))
		|| (e.getPid().equals("null"))) {

	    // This is an event without a process instance.
	    // We need to check if this is a process pre-condition event for PD
	    String defId = this.composition.getPDforCoS(e.getEventId());
	    if (null == defId) {
		throw new SerendipException(
			"Event "
				+ e.getEventId()
				+ " is not associated with a pid. Event is not a pre-condition for a new instance too");
	    }
	    // If it is then we create a new process instance
	    ProcessInstance pi = this.startProcessInstance(defId);
	    // Allocate the pid of the instance with the event. So that this
	    // event belong to the created process instance
	    e.setPid(pi.getId());

	    return pi.getPId();

	} else {
	    // event has a pid. So use the same pid.
	    return e.getPid();
	}
    }

    /**
     * This is the interface for external components to add new events to the
     * engine. The events added will be subjected to following operations. 1. If
     * the event DOES NOT contain a pid, the event can 'potentially' be an
     * initial event. 2. The engine check if any of the process definitions have
     * e as the CoS (Condition of Start) 3. If NO, an exception is thrown 4. If
     * YES, then a new process is enacted based on the event. And the event get
     * associated with the new pid. 5. If the event DOES contain a pid, the
     * event is simply passed to the event cloud
     * 
     * @param e
     * @return the pid (process instance id)
     * @throws SerendipException
     */
    public String addEvent(EventRecord e) throws SerendipException {
	logger.info("Event fired (" + e.getEventId() + "," + e.getPid() + ")");
	this.writeLogMessage("EVENT",
		"Fired " + e.getEventId() + "," + e.getPid());
	// If there is no pid associated with the event record
	// if((null == e.getPid()) || (e.getPid().equals(""))||
	// (e.getPid().equals("null")) ){
	//
	// //This is an event without a process instance.
	// //We need to check if this is a process pre-condition event for PD
	// String defId = this.composition.getPDforCoS(e.getEventId());
	// if(null == defId){
	// throw new
	// SerendipException("Event "+e.getEventId()+" is not associated with a pid. Event is not a pre-condition for a new instance too");
	// }
	// //If it is then we create a new process instance
	// ProcessInstance pi = this.startProcessInstance(defId);
	// logger.info("Created a new process instance "+pi.getPId());
	// //Allocate the pid of the instance with the event. So that this event
	// belong to the created process instance
	// e.setPid(pi.getId());
	// //Drop the event to the event cloud
	// this.eventCloud.addEvent(e);
	// //TESTING
	// //this.eventCloud.printEventListerners();
	// }else{//the pid is present -> An event targeting an existing process
	// instance.
	// //Simply drop the event to the event cloud
	//
	// }
	this.eventCloud.addEvent(e);
	return e.getPid();
    }

    public void ExpireEvent(String eventId, String pId) {
	this.eventCloud.expireEvent(eventId, pId);
    }

    // Getters and Setters
    public XMLCompositionParser getParser() {
	return parser;
    }

    public EventCloud getEventCloud() {
	return eventCloud;
    }

    /**
     * Initialize the logger for logging
     */
    private void initLogger() {
	logger.setLevel(Level.DEBUG);
    }

    public void printEventCloud() {
	this.eventCloud.printEventListerners();
    }

    /**
     * @depreicated
     * @param m
     * @throws SerendipException
     */
    // public void dropMessage(Message m) throws SerendipException {
    // // logger.debug("Dropping message "+m.getId() +" "+m.getPid());
    // String msgPid = m.getPid();// Need a more sophisticated method to
    // // identify the process id
    // ProcessInstance pInstance = this.processInstanceCollection.get(msgPid);
    // //pInstance.interpretMessage(m);
    // }

    public Hashtable<String, EventEngineSubscriber> getSubscribers() {
	return subscribers;
    }

    public void subscribe(EventEngineSubscriber subscriber) {
	// this.writeLogMessage("[INIT]",
	// "Role "+subscriber.getId()+" is subscribed to engine");
	logger.info(subscriber.getId() + " subscribed to the engine");
	this.subscribers.put(subscriber.getId(), subscriber);
    }

    /**
     * 
     * Checkout existing instance model. This model can be altered at runtime to
     * deviate it to accommodate the runtime requirements. See the API for more
     * details on how to change. Events may be added accordingly.
     * 
     * @param pId
     * @return
     */
    public ProcessInstance getProcessInstance(String pId) {
	ProcessInstance pi = this.processInstanceCollection.get(pId);
	if (null == pi) {
	    pi = this.completedInstanceCollection.get(pId);
	}
	if (null == pi) {
	    logger.error("No such process instance with pId=" + pId);
	}
	return pi;
    }

    public void removeProcessInstance(ProcessInstance pi) {
	ProcessInstance piBkup = this.processInstanceCollection.remove(pi
		.getPId());
	if (null != piBkup) {
	    this.completedInstanceCollection.put(piBkup.getPId(), piBkup);
	} else {
	    // error
	    logger.error("No such Live process instance. May be already aborted="
		    + pi.getPId());
	}
    }

    public Hashtable<String, ProcessInstance> getLiveProcessInstances() {
	return this.processInstanceCollection;
    }

    public Hashtable<String, ProcessInstance> getCompletedProcessInstances() {
	return this.completedInstanceCollection;
    }

    public String getCompositionName() {
	return this.composition.getComposite().getName();
    }

    public ModelProviderFactory getModelFactory() {
	return modelFactory;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////////////
    // PI level adaptation API

    public boolean removeTask(String pId, String behaviorTermId, String taskId) {
	ProcessInstance pi = getProcessInstance(pId);
	if (null == pi) {
	    return false;
	}
	return pi.removeTask(behaviorTermId, taskId);

    }

    public boolean addTask(String pId, String behaviorTermId, Task task) {
	ProcessInstance pi = getProcessInstance(pId);
	if (null == pi) {
	    return false;
	}
	return pi.addTask(behaviorTermId, task);
    }

    public boolean removeBehaviorTerm(String pId, String behaviorTermId) {
	ProcessInstance pi = getProcessInstance(pId);
	if (null == pi) {
	    return false;
	}
	return pi.removeBehaviorTerm(behaviorTermId);
    }

    public boolean addBehaviorTerm(String pId, BehaviorTerm bt) {
	ProcessInstance pi = getProcessInstance(pId);
	if (null == pi) {
	    return false;
	}
	return pi.addBehaviorTerm(bt);
    }

    // logging
    public void subscribeLogWriter(LogWriter writer) {
	this.logwriter = writer;
    }

    public void writeLogMessage(String context, String message) {
	// TODO: We might need to write to multiple logwriters in the future
	if (null != this.logwriter) {
	    this.logwriter.writeLog(context, message);
	} else {
	    // logger.error("LogWriter is null");
	    logger.info("[" + context + "]" + message);
	}
    }

    public SerendipOrganizer getSerendipOrgenizer() {
	return this.serOrg;
    }
}
