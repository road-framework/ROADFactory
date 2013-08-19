package au.edu.swin.ict.serendip.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.processmining.converting.epc2transitionsystem.EpcToTransitionSystem;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.composition.view.ProcessView;

import au.edu.swin.ict.serendip.epc.EPMLWriter;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.event.SerendipEventListener;
import au.edu.swin.ict.serendip.message.Message;
import au.edu.swin.ict.serendip.message.MessageInterpreter;

import au.edu.swin.ict.road.xml.bindings.*;
import au.edu.swin.ict.serendip.util.CompositionUtil;

public class ProcessInstance extends SerendipEventListener implements Cloneable {
    // We specifically use lower case letters for enums to support the
    // scripting. Change with care.
    public enum status {
	active, completed, abort, paused
    }

    public enum propertyAttribute {
	cot, cos
    }

    static Logger logger = Logger.getLogger(ProcessInstance.class);

    private SerendipEngine engine = null;
    // private ProcessDefinitionType pDef = null;
    private static int instanceIdNum = 11;
    private Vector<BehaviorTerm> btVec = new Vector<BehaviorTerm>();
    private Vector<ConstraintType> constraintsVec = new Vector<ConstraintType>();
    public status currentStatus = status.active;
    private Hashtable<String, Task> currentTasks = new Hashtable<String, Task>();
    private Hashtable<String, Task> completedTasks = new Hashtable<String, Task>();

    private String defId = null;

    /**
     * A process instance that maintains the state of a running business process
     * 
     * @param engine
     * @param defId
     */
    public ProcessInstance(SerendipEngine engine, String defId) {
	this.engine = engine;
	this.defId = defId;
	this.pId = defId + (instanceIdNum++);// we increment the process ids
    }

    public Object clone() {// Not complete.
	try {
	    ProcessInstance cloned = (ProcessInstance) super.clone();

	    //
	    return cloned;
	} catch (CloneNotSupportedException e) {
	    logger.error(e);
	    return null;
	}
    }

    /**
     * Subscribe to the event cloud so that when the CoT is met the process
     * instance can release its resources.
     * 
     * @throws SerendipException
     * @see eventPatternMatched
     */
    public String initializeSubscriptions() throws SerendipException {
	// Listen for the condition of termination
	this.eventPattern = this.engine.getComposition()
		.getProcessDefinition(this.defId).getCoT().trim();
	this.engine.getEventCloud().subscribe(this);
	return this.pId;
    }

    /**
     * To retrieve the current view of the process instance.
     * 
     * @return A process view
     * @throws SerendipException
     */
    public ProcessView getCurrentProcessView() throws SerendipException {
	String cot = this.engine.getComposition()
		.getProcessDefinition(this.defId).getCoT().trim();
	Vector<BehaviorTerm> tempBtVec = new Vector<BehaviorTerm>();

	Task finalTask = new Task(cot, this.defId, "tTerminate", "e"
		+ this.defId + "END");
	Vector<Task> finalTaskVec = new Vector<Task>();
	finalTaskVec.add(finalTask);
	BehaviorTerm finalBT = new BehaviorTerm("final", finalTaskVec, null,
		false);

	tempBtVec.addAll(this.btVec);
	tempBtVec.add(finalBT);
	ProcessView pv = new ProcessView(
		this.defId + "(" + this.getPId() + ")", tempBtVec);

	return pv;

    }

    /**
     * Get all the constraints defined in the process definition
     * 
     * @return
     */
    public ConstraintType[] getAllConstriants() {
	Collection<ConstraintType> tempCollec = new ArrayList<ConstraintType>();

	ConstraintType[] pdCons = CompositionUtil.getAllConstriantsForPD(
		this.defId, true, this.engine.getComposition());
	tempCollec.addAll(this.constraintsVec);
	tempCollec.addAll((Collection<ConstraintType>) Arrays.asList(pdCons));
	return (ConstraintType[]) tempCollec.toArray(new ConstraintType[0]);
    }

    /**
     * Add a constraint only for this instance
     * 
     * @param pid
     * @param cid
     * @param expression
     * @param enabled
     */
    public void addConstraint(String cid, String expression, boolean enabled) {
	ConstraintType cons = new ConstraintType();
	cons.setId(cid);
	cons.setExpression(expression);
	cons.setEnabled(enabled);
	this.constraintsVec.add(cons);

    }

    /**
     * When the CoT is met this will terminate the process instance.
     * Self-destruction?
     */
    public void eventPatternMatched(String ep, String id) {
	// Terminate the process instance
	this.setAlive(false);// Unsubscribe from the even listener set
	this.currentStatus = ProcessInstance.status.completed;
	// this.removeAllBehaviorTerms();//We temporarily disable this as we
	// need to bakup the process instances
	this.engine.removeProcessInstance(this);
	logger.debug(ep + " -> Successfully terminated the process instance "
		+ this.pId);

	try {
	    // Just to mark all done
	    this.engine.addEvent(new EventRecord("e" + this.getDefinitionId()
		    + "END", this.pId));
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	this.engine.writeLogMessage("[PI-END]",
		"Successfully terminated process instance " + this.pId);
    }

    public void delete() {
	this.engine.writeLogMessage("[PI-DELETE]",
		"Successfully deleted process instance " + this.pId);
	this.removeAllBehaviorTerms();
	// System.gc();
    }

    /**
     * To get the definition id
     * 
     * @return
     */
    public String getDefinitionId() {
	return this.defId;
    }

    public SerendipEngine getEngine() {
	return engine;
    }

    public Vector<BehaviorTerm> getBtVec() {
	return btVec;
    }

    public BehaviorTerm getBehaviorTerm(String behaviorTermId) {
	for (int i = 0; i < this.btVec.size(); i++) {
	    BehaviorTerm bt = this.btVec.get(i);
	    if (bt.getId().equals(behaviorTermId)) {
		return bt;
	    }
	}
	return null;
    }

    /**
     * Iterate thru current tasks that are active. A message should be recieved
     * when the task is active
     * 
     * @param m
     * @return
     */
    public Task getCurrentTaskForMsg(Message m) {
	String mId = m.getId();

	// Scan thru current tasks. If the task has an out message for the above
	// name return the task
	Set<String> set = this.currentTasks.keySet();

	Iterator<String> itr = set.iterator();
	while (itr.hasNext()) {
	    String taskId = itr.next();
	    Task task = this.currentTasks.get(taskId);

	    if (task.getOutMessageId().equals(m.getId())) {
		return task;
	    }
	}
	logger.error("Cannot find the task for the message" + m.getId());
	return null;
    }

    /**
     * Find a task by its id
     * 
     * @param taskId
     * @return
     */
    public Task findTaskById(String taskId) {

	for (BehaviorTerm b : this.getBtVec()) {
	    for (Task t : b.getAllTasks()) {
		if (t.getId().equals(taskId)) {
		    return t;

		}
	    }
	}
	return null;
    }

    public Vector<Task> getAllTheTasks() {
	Vector<Task> allTasks = new Vector<Task>();

	for (BehaviorTerm b : this.getBtVec()) {
	    allTasks.addAll(b.getAllTasks());
	}
	return allTasks;
    }

    public Hashtable<String, Task> getCurrentTasks() {
	return currentTasks;
    }

    public boolean isTaskInProgress(String taskId) {
	return currentTasks.containsKey(taskId);
    }

    public String getCurrentTasksAsString() {
	Set<String> set = this.currentTasks.keySet();
	String text = null;
	Iterator<String> itr = set.iterator();
	while (itr.hasNext()) {
	    text += itr.next() + " ";
	}
	return text;
    }

    public void removeFromCurrentTasks(String taskId) {
	this.currentTasks.remove(taskId);
    }

    public void addToCompletedTasks(Task t) {
	this.completedTasks.put(t.getId(), t);
    }

    public Hashtable<String, Task> getCompletedTasks() {

	return this.completedTasks;
    }

    // //Adaptation methods
    public boolean changeEventPattern(String behaviorTermId, String taskId,
	    String newEventPattern) {

	BehaviorTerm bt = this.getBehaviorTerm(behaviorTermId);
	return bt.changeEventPattern(taskId, newEventPattern);
    }

    public boolean removeTask(String behaviorTermId, String taskId) {
	BehaviorTerm bt = this.getBehaviorTerm(behaviorTermId);
	return bt.removeTask(taskId);
    }

    public boolean addTask(String behaviorTermId, Task task) {
	BehaviorTerm bt = this.getBehaviorTerm(behaviorTermId);
	return bt.addTask(task);
    }

    public void addToCurrentTasks(Task task) {
	this.currentTasks.put(task.getId(), task);
    }

    public status getCurrentStatus() {
	return currentStatus;
    }

    public void setCurrentStatus(status currentStatus) {
	this.currentStatus = currentStatus;
    }

    public boolean removeBehaviorTerm(String behaviorTermId) {
	for (int i = 0; i < this.btVec.size(); i++) {
	    if (this.btVec.get(i).getId().equals(behaviorTermId)) {
		this.btVec.remove(i);
		return true;
	    }
	}
	return false;
    }

    public boolean removeAllBehaviorTerms() {
	for (int i = 0; i < this.btVec.size(); i++) {
	    this.btVec.remove(i);
	}
	return true;
    }

    public boolean addBehaviorTerm(BehaviorTerm bt) {
	this.btVec.add(bt);
	return true;
    }

    @Override
    public String getId() {
	// Unlike other EventListeners, process instance's pid=id
	return this.pId;
    }

    public boolean containsEvent(String eventId) {

	for (int i = 0; i < this.btVec.size(); i++) {
	    if (this.btVec.get(i).containsEvent(eventId)) {
		return true;
	    }
	}
	return false;
    }

    public BehaviorTerm getContainedBehavior(String taskId) {
	for (int i = 0; i < this.btVec.size(); i++) {
	    if (this.btVec.get(i).containsTask(taskId)) {
		return this.btVec.get(i);
	    }
	}

	return null;
    }

    /**
     * Export EPC vie as an EPML description
     * 
     * @param fileName
     * @throws SerendipException
     * @throws IOException
     */
    public void exportToEPML(String fileName) throws SerendipException,
	    IOException {
	EPMLWriter epmlWriter = new EPMLWriter(this.getCurrentProcessView()
		.getViewAsEPC(), true);
	epmlWriter.writeToFile(fileName);
    }

}
