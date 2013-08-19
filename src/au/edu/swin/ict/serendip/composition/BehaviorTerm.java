package au.edu.swin.ict.serendip.composition;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.epc.EPCModifier;
import au.edu.swin.ict.serendip.epc.EPCToSerendip;
import au.edu.swin.ict.serendip.epc.EPMLBehavior;
import au.edu.swin.ict.serendip.epc.EPMLReader;
import au.edu.swin.ict.serendip.epc.MergeBehavior;
import au.edu.swin.ict.serendip.epc.PatternToEPC;
import au.edu.swin.ict.serendip.event.EventCloud;
import au.edu.swin.ict.serendip.message.Message;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.ContractType;
import au.edu.swin.ict.road.xml.bindings.TaskRefType;
import au.edu.swin.ict.road.xml.bindings.TaskRefsType;
import au.edu.swin.ict.road.xml.bindings.TasksType;
import au.edu.swin.ict.road.xml.bindings.TermType;
import au.edu.swin.ict.road.xml.bindings.TaskType;
import au.edu.swin.ict.serendip.util.BTScope;
import au.edu.swin.ict.serendip.util.CompositionUtil;

/**
 * Represents a behavior term in the process tier
 * 
 * @author Malinda
 * 
 */
public class BehaviorTerm {

    private enum status {
	BLOCKED, UNBLOCKED
    };// Blocked= cannot be modified. All the modification methods should
      // NOT continue if BLOCKED

    private Vector<Task> tasksVec = new Vector<Task>();
    private String id = null;
    private ConfigurableEPC epc = null;// Each behaviour term got its EPC
    private Composite composite = null;
    private SerendipEngine engine = null;
    private status currentStatus = status.UNBLOCKED;
    private ProcessInstance pi = null;
    private String extendsFrom = null;
    private boolean isAbstract = false;

    public enum propertyAttribute {
	isAbstract, extendsFrom
    };

    /**
     * Simplest form of the constructor. Cannot be enacted
     * 
     * @param id
     * @param tasksVec
     * @param extendsFrom
     */
    public BehaviorTerm(String id, Vector<Task> tasksVec, String extendsFrom,
	    boolean isBastract) {
	this.id = id;
	this.tasksVec = tasksVec;
	this.extendsFrom = extendsFrom;
	this.isAbstract = isBastract;
    }

    /**
     * The construtor to enact the behavior terms
     * 
     * @param btType
     * @param pi
     * @param engine
     * @throws SerendipException
     */
    public BehaviorTerm(BehaviorTermType btType, ProcessInstance pi,
	    SerendipEngine engine) throws SerendipException {

	// this(btType, engine.getComposition().getComposite());
	this.pi = pi;
	this.engine = engine;
	this.init(btType, engine.getComposition().getComposite());

    }

    public BehaviorTerm(BehaviorTermType btType, Composite composite)
	    throws SerendipException {
	super();
	// if(null == btType){
	// throw new SerendipException("Invalid behavior term "+btType);
	// }
	// this.id = btType.getId();
	// this.composite = composite;
	// if (null != btType.getExtension()) {
	// this.fromEPML();// load from EPML //No support for inheritance
	// } else {
	// this.fromDescriptor(btType);// load from the <Task> elements in the
	// // descriptor
	// }
	//
	// //Then we need to add the tasks from the parent behavior(s) if any
	// if(null!=this.extendsFrom){
	// this.supportInheritance();
	// }
	this.init(btType, composite);

    }

    private void init(BehaviorTermType btType, Composite composite)
	    throws SerendipException {
	if (null == btType) {
	    throw new SerendipException("Invalid behavior term " + btType);
	}
	this.id = btType.getId();
	this.composite = composite;
	if (null != btType.getExtension()) {
	    this.fromEPML();// load from EPML //No support for inheritance
	} else {
	    this.fromDescriptor(btType);// load from the <Task> elements in the
	    // descriptor
	}

	// Then we need to add the tasks from the parent behavior(s) if any
	if (null != this.extendsFrom) {
	    this.supportInheritance();
	}
    }

    /**
     * When a process definition PD refer to a behavior term B, which
     * specializes behavior term B'. B -> B'(parent) Rule 1. All tasks specified
     * in B' but not specified in B are inherited from B'. All the tasks
     * specified in B but not specified in B' are recognised as newly specified
     * tasks of B.
     * 
     * Rule 2. If there are tasks common to both B and B', the tasks of B
     * overrides tasks of B'. (See next rule)
     * 
     * Rule 3. When a task T of B (B.T) overrides a task T of B' (B'.T) 3.a. All
     * attributes specified in Task B'.T but not specified in B.T are inherited
     * by T. 3.b. If there are attributes common to both B.T and B'.T, the
     * attributes of B.T are considered. Rule 4. All constraints specified in B'
     * are inherited by B. Rule 5. Constraints cannot be overridden, i.e. a
     * constraints in B cannot use an identifier of a constraint of B' to
     * specify a new constraint.
     * 
     * Other rules: Rule 6. All the attributes of tasks of a concrete behavior
     * term need to be fully specified. Rule 7. If attributes of tasks are not
     * fully specified (due to lack of knowledge), the behavior term should be
     * explicitly declared abstract.
     * 
     * @throws SerendipException
     */
    private void supportInheritance() throws SerendipException {
	if (null != this.extendsFrom) {
	    // Check for the parent behavior term
	    BehaviorTermType parentType = CompositionUtil.getBehaviorTermType(
		    this.extendsFrom, this.composite);
	    // If invalid behavior term, throw an exception
	    if (null == parentType) {
		throw new SerendipException(
			"No such parent behavior term for id="
				+ this.extendsFrom);
	    }
	    // Model the parent behavior term
	    BehaviorTerm parentBt = new BehaviorTerm(parentType, this.composite);
	    // Get the tasks of parents
	    Vector<Task> parentTasks = parentBt.getAllTasks();
	    for (Task t : parentTasks) {
		// Check if there are any overrides
		// If so ignore those from the parent and preserve the child's
		String taskId = t.getId();
		if (!this.containsTask(taskId)) {// If child does not have the
						 // task t
		    if (null != this.pi) {
			// t.setPId(this.pi.getId());//We need to set the pid
			t.setProcessInstance(this.pi);
		    }
		    this.tasksVec.add(t);// We add the inherited task from the
					 // parent
		} else {// We do have such a task
			// And, we might need to check whether the child task
			// needs to inherit some properties form parent's
			// counterpart
		    Task childTask = this.getTask(taskId);// Child task
		    // if(childTask.isOverride()){
		    // Now we know the child task is expecting to override the
		    // parent
		    // So what are the values in child that are empty?
		    // Get those from the parent
		    if ((null == childTask.getEventPattern())
			    || (childTask.getEventPattern().equals(""))) {
			childTask.setEventPattern(t.getEventPattern());
		    }
		    if ((null == childTask.getPostEventPattern())
			    || (childTask.getPostEventPattern().equals(""))) {
			childTask.setPostEventPattern(t.getPostEventPattern());
		    }
		    if ((null == childTask.getProperty())
			    || (null == childTask.getProperty().getValue())
			    || childTask.getProperty().getValue().equals("")) {
			childTask.setProperty(new PerformanceProperty(t
				.getProperty().getValue()));
		    }
		    // NOTE: If there are any other attributes of Task please
		    // override them here
		}
	    }
	}
    }

    public void subscribeTasksTo(EventCloud ec) {
	if (null != ec) {
	    for (Task t : this.tasksVec) {
		t.setPId(this.pi.getPId());
		t.subscribeTo(ec);
	    }
	}
    }

    private void fromDescriptor(BehaviorTermType btType)
	    throws SerendipException {
	// First we add the tasks defined in the behavior term itself
	List<TaskRefType> taskRefList = btType.getTaskRefs().getTaskRef();
	if (taskRefList.size() < 1) {
	    System.err.println("WARNING: " + btType.getId() + " contains "
		    + taskRefList.size() + " tasks");
	}
	for (TaskRefType tt : taskRefList) {// Iterate thru Task types and

	    String taskId = null, roleId = null;
	    if (tt.getId().contains(".")) {
		String[] taskSplit = tt.getId().split("\\.");// Seperate the
							     // role from the
							     // task id
		roleId = taskSplit[0];
		taskId = taskSplit[1];
	    } else {
		taskId = tt.getId();
		roleId = "Unknown";
	    }
	    // create tasks
	    Task task = new Task(this.composite.getSerendipEngine(), this.pi,
		    taskId, tt.getPreEP(), tt.getPostEP(), roleId, null,
		    new PerformanceProperty(tt.getPerformanceVal()), this);

	    String functionName = task.getObligatedRoleId() + "."
		    + task.getId() + "(" + task.getProperty().getValue() + ")";

	    this.tasksVec.add(task);
	}
	// This need to be set if inheritence need to be suported.
	this.extendsFrom = btType.getExtends();
	this.isAbstract = btType.isIsAbstract();
    }

    private void fromEPML() throws SerendipException {
	File file = new File(engine.getModelFactory().getFileLoadingDirectory()
		+ "/" + this.id + ".epml");
	try {
	    this.epc = EPMLReader.getEPCFromFile(file);
	} catch (Exception e) {
	    throw new SerendipException(
		    "No file is available to load behavior term " + this.id);
	}

	if (null == this.epc) {
	    throw new SerendipException("Unable to load a valid behavor from "
		    + file.getAbsolutePath());
	}

	// This will populate the behavior term
	if (null != pi) {
	    EPCToSerendip.convrtEPCToSerendip(engine, pi, this.epc, this);
	}

    }

    public Vector<Task> getTasksVec() {
	return tasksVec;
    }

    public String getId() {
	return this.id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public status getCurrentStatus() {
	return currentStatus;
    }

    public void setCurrentStatus(status currentStatus) {
	this.currentStatus = currentStatus;
    }

    public ConfigurableEPC constructEPC() {
	// If there are no tasks at all
	if (this.tasksVec.size() < 1) {
	    this.epc = null;
	    return null;
	}
	// If there is only one task we use the task's EPC
	if (this.tasksVec.size() == 1) {
	    this.epc = this.tasksVec.get(0).constructEPC();
	    return this.epc;
	}

	// If there are more than one task we need ti iteratively build the EPC
	ConfigurableEPC result = this.tasksVec.get(0).constructEPC();
	// Now iteratively merge all the tasks. Note; start from i=1
	for (int i = 1; i < this.tasksVec.size(); i++) {

	    Task t = this.tasksVec.get(i);
	    ConfigurableEPC newEpc = t.constructEPC();
	    result = MergeBehavior.mergeEPC(result, newEpc);
	}
	// We got our behaviour EPC
	this.epc = result;

	return this.epc;

    }

    // Adaptation methods////////////////////////////
    /*
     * Change an event pattern of a particular behavior term
     */
    public boolean changeEventPattern(String taskId, String newEventPattern) {

	for (int j = 0; j < this.getTasksVec().size(); j++) {
	    Task task = this.getTasksVec().get(j);
	    if (task.getId().equals(taskId)) {
		// Task found
		task.setEventPattern(newEventPattern);
		return true;
	    }
	}
	// TODO: Also change the epc of the behavior term accordingly
	return false;
    }

    // TODO This method should be replaced with a proper implementation. For the
    // demo only
    public boolean addEvent(String taskId, String eventId, String preOrPost,
	    String connectorStr) throws SerendipException {
	if (status.BLOCKED == this.getCurrentStatus()) {
	    return false;
	}

	Task t = this.getTask(taskId);
	if (null == t) {
	    System.err.println("Error in adding new event no such task "
		    + taskId);
	    return false;
	}

	String functionId = t.getTaskDetailedId();

	ConfigurableEPC result = null;
	if (preOrPost.equals("pre")) {
	    result = EPCModifier.addPreEvent(this.epc, functionId, eventId,
		    connectorStr);
	} else if (preOrPost.equals("post")) {
	    result = EPCModifier.addPostEvent(this.epc, functionId, eventId,
		    connectorStr);
	} else {

	}
	if (null == result) {
	    System.err.println("Error in adding pre-event");
	    throw new SerendipException("Cannot add the event");

	}

	this.epc = result;// We set the new epc

	// Then we take care of rest of the details
	EPCFunction function = this.epc.getAllFunctions(functionId).get(0);
	Task taskNew = EPCToSerendip.parseFunction(function, engine, pi,
		this.epc, this);

	this.tasksVec.remove(t);// We remove the current task
	this.tasksVec.add(taskNew);// Then replace with the new one

	return true;
    }

    public boolean setPP(String taskId, String val) {
	if (status.BLOCKED == this.getCurrentStatus()) {
	    return false;
	}

	Task t = this.getTask(taskId);
	t.getProperty().setValue(val);

	// Now we need to change the epc too
	String funcId = t.getTaskDetailedId();
	ConfigurableEPC result = EPCModifier.setPP(this.epc, funcId, val);
	this.epc = result;

	return true;
    }

    public Vector<Task> getAllTasks() {
	return this.tasksVec;
    }

    public boolean removeTask(String taskId) {
	if (status.BLOCKED == this.getCurrentStatus()) {
	    return false;
	}

	for (int i = 0; i < this.tasksVec.size(); i++) {
	    if (this.tasksVec.get(i).getId().equals(taskId)) {
		this.tasksVec.remove(i);
		return true;
	    }
	}
	return false;
    }

    public Task getTask(String taskId) {

	for (int i = 0; i < this.tasksVec.size(); i++) {
	    if (this.tasksVec.get(i).getId().equals(taskId)) {
		return this.tasksVec.get(i);
	    }
	}
	return null;
    }

    public boolean addTask(Task task) {
	this.tasksVec.add(task);
	return true;
    }

    public ConfigurableEPC getEpc() {
	return this.constructEPC();
    }

    public void setEpc(ConfigurableEPC epc) {
	this.epc = epc;
	EPCToSerendip.convrtEPCToSerendip(engine, this.pi, this.epc, this);
    }

    public boolean containsEvent(String eventId) {
	ArrayList<EPCEvent> eventList = this.epc.getEvents();

	for (int i = 0; i < eventList.size(); i++) {
	    if (eventList.get(i).getID().equals(eventId)) {
		return true;
	    }
	}
	return false;
    }

    public boolean containsTask(String taskId) {
	for (int i = 0; i < this.tasksVec.size(); i++) {
	    String s = this.tasksVec.get(i).getId();
	    if (s.equals(taskId)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Returns the tasks obligated by the role given by roleId
     * 
     * @param roleId
     * @return
     */
    public Vector<Task> getTasksOfRole(String roleId) {
	Vector<Task> tasksOfRoleVec = new Vector<Task>();
	for (int i = 0; i < this.tasksVec.size(); i++) {
	    Task t = this.tasksVec.get(i);
	    if (t.getObligatedRoleId().equals(roleId)) {
		tasksOfRoleVec.add(t);
	    }
	}

	return tasksOfRoleVec;
    }

    public String getExtendsFrom() {
	return extendsFrom;
    }

    public void setExtendsFrom(String extendsFrom) {
	this.extendsFrom = extendsFrom;
    }

    public boolean isAbstract() {
	return isAbstract;
    }

    public void setAbstract(boolean isAbstract) {
	this.isAbstract = isAbstract;
    }

    public String toText() {
	StringBuffer buf = new StringBuffer();

	buf.append("BehaviorTerm " + this.id + "{\n");

	for (Task t : this.tasksVec) {
	    buf.append(t.toText());
	}
	buf.append("};\n");

	return buf.toString();

    }

    public static void getAllParents(String btId, List<String> parentList,
	    Composition comp) {
	BehaviorTermType childBT = comp.getBehaviorTermTypeById(btId);
	if (null == childBT) {
	    return;
	}
	String parentId = childBT.getExtends();
	if ((null == parentId) || (parentId.equals(""))) {
	    // No parent
	    return;
	} else {
	    // We have a parent
	    // Add to cart
	    parentList.add(parentId);
	    // recursive
	    getAllParents(parentId, parentList, comp);
	}

    }

    public static boolean isChildOf(Composition comp, String childId,
	    String parentId) {

	List<String> parentIds = new ArrayList<String>();
	getAllParents(childId, parentIds, comp);
	for (String s : parentIds) {
	    if (s.equals(parentId)) {
		return true;
	    }
	}

	return false;

    }
}
