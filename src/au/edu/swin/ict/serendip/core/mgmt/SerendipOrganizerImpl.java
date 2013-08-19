package au.edu.swin.ict.serendip.core.mgmt;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.xml.bindings.*;
import au.edu.swin.ict.road.xml.bindings.SMC.Contracts;
import au.edu.swin.ict.road.xml.bindings.SMC.Facts;
import au.edu.swin.ict.road.xml.bindings.SMC.PlayerBindings;
import au.edu.swin.ict.road.xml.bindings.SMC.Roles;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.composition.PerformanceProperty;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.core.mgmt.action.*;
import au.edu.swin.ict.serendip.event.EventRecord;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;
import java.io.*;
import java.util.*;

/**
 * The implementation of the Serendip Organizer. This class will handle all the
 * process related adaptations.
 * 
 * @author Malinda
 */
public class SerendipOrganizerImpl implements SerendipOrganizer, Constants {
    private static Logger log = Logger.getLogger(SerendipOrganizerImpl.class
	    .getName());
    private SerendipEngine engine = null;
    private AdaptationScriptEngine scriptEngine = null;

    public SerendipOrganizerImpl(SerendipEngine engine) {
	this.engine = engine;
	this.scriptEngine = new AdaptationScriptEngine(engine);
    }

    @Override
    public OrganiserOperationResult adaptProcessInstance(
	    String processInstanceId,
	    List<InstanceAdaptAction> adaptationActionsList) {
	log.info("Got a list of commands for instance" + processInstanceId);
	for (InstanceAdaptAction aa : adaptationActionsList) {
	    log.info("Command found " + aa.toString());
	}
	// Create a new instance adaptation engine
	ProcessInstanceAdaptationEngine piae = new ProcessInstanceAdaptationEngine(
		this.engine);

	try {
	    piae.executeAdaptation(processInstanceId, adaptationActionsList);
	} catch (AdaptationException e) {

	    e.printStackTrace();
	    return new OrganiserOperationResult(false,
		    "Cannot perform adaptation. " + e.getMessage());
	}
	return new OrganiserOperationResult(true,
		"Process Instance Level Adaptation Complete");
    }

    @Override
    public OrganiserOperationResult adaptDefinition(
	    List<DefAdaptAction> adaptationActions) {
	// TODO Auto-generated method stub
	log.info("Got a list of commands for a definition level adaptation ");
	for (DefAdaptAction aa : adaptationActions) {
	    log.info("Command found " + aa.toString());
	}

	// Create a new instance of adaptaiton engine
	DefAdaptationEngine defe = new DefAdaptationEngine(this.engine);
	try {
	    defe.executeAdaptation(adaptationActions);
	} catch (AdaptationException e) {
	    e.printStackTrace();
	    return new OrganiserOperationResult(false,
		    "Cannot perform adaptation. " + e.getMessage());
	}
	return new OrganiserOperationResult(true,
		"Composite Definition Level Adaptation Complete.");
    }

    @Override
    public OrganiserOperationResult addNewBehaviorConstraint(String btid,
	    String cid, String expression, boolean enabled) {
	SMC smcCur = this.getSmcBinding();
	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(btid)) {
		// Found BT. Then add constrain
		ConstraintType ct = new ConstraintType();
		ct.setId(cid);
		ct.setExpression(expression);
		ct.setEnabled(enabled);
		btt.getConstraints().getConstraint().add(ct);
		return new OrganiserOperationResult(true, "New constraint  "
			+ expression + " has been added to Behaviour  " + btid);
	    }
	}
	return new OrganiserOperationResult(false, "Behaviour  " + btid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult addBehaviorRefToPD(String pdid, String btid) {
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// Found PD.
		// Check if reference already exist
		if (null != pdType.getBehaviorTermRefs()) {
		    for (String refId : pdType.getBehaviorTermRefs()
			    .getBehavirTermId()) {
			if (refId.equals(btid)) {
			    return new OrganiserOperationResult(
				    false,
				    "Behaviour  "
					    + btid
					    + " already referred by Process Def "
					    + pdid);
			}
		    }
		}
		// Now look for BT
		for (BehaviorTermType btt : smcCur.getBehaviorTerms()
			.getBehaviorTerm()) {
		    if (btt.getId().equals(btid)) {
			// Found BT. Then connect
			pdType.getBehaviorTermRefs().getBehavirTermId()
				.add(btid);
			return new OrganiserOperationResult(true, "Behaviour  "
				+ btid + " has been added to Process Def "
				+ pdid);
		    }
		}
		return new OrganiserOperationResult(false, "Behaviour  " + btid
			+ " cannot be found");
	    }
	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult addConstraintToProcessInstance(String pid,
	    String cid, String expression, boolean enabled) {
	// Get Instance
	ProcessInstance pi = this.engine.getProcessInstance(pid);
	if (null == pi) {
	    return new OrganiserOperationResult(false,
		    "Process Instances cannot be found.");
	}
	// adapt
	InstanceProcessConstraintAddAction action = new InstanceProcessConstraintAddAction(
		cid, expression, enabled);
	try {
	    action.adapt(pi);
	} catch (AdaptationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return new OrganiserOperationResult(false, "Process Instnace "
		    + pid + " cannot adapted"
		    + Arrays.toString(e.getStackTrace()));
	}
	return new OrganiserOperationResult(true, "Process Instnace " + pid
		+ " has been adapted");
    }

    @Override
    public OrganiserOperationResult addTaskToInstance(String pid, String btid,
	    String tid, String preEP, String postEP, String obligRole, String pp) {
	// Get Instance
	ProcessInstance pi = this.engine.getProcessInstance(pid);
	if (null == pi) {
	    return new OrganiserOperationResult(false,
		    "Process Instances cannot be found.");
	}

	InstanceTaskAddAction action = new InstanceTaskAddAction(btid, tid,
		preEP, postEP, obligRole, pp);
	try {
	    action.adapt(pi);
	} catch (AdaptationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return new OrganiserOperationResult(false,
		    "Process Instances cannot be adapted " + e.getMessage());
	}
	return new OrganiserOperationResult(false, "Process Instances adapted "
		+ pid);

    }

    @Override
    public OrganiserOperationResult addNewProcessConstraint(String pdid,
	    String cid, String expression, boolean enabled) {
	// TODO Auto-generated method stub
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		ConstraintType ct = new ConstraintType();
		ct.setId(cid);
		ct.setExpression(expression);
		ct.setEnabled(enabled);
		pdType.getConstraints().getConstraint().add(ct);
		return new OrganiserOperationResult(true, "New constraint  "
			+ expression + " has been added to Process Def " + pdid);
	    }
	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult addTaskToBehavior(String btid, String tid,
	    String preep, String postep, String pp) {
	SMC smcCur = this.getSmcBinding();
	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(btid)) {
		// Found
		TaskRefType tt = new TaskRefType();
		tt.setId(tid);
		tt.setPreEP(preep);
		tt.setPostEP(postep);
		tt.setPerformanceVal(pp);
		btt.getTaskRefs().getTaskRef().add(tt);
		return new OrganiserOperationResult(true, "Task ref  " + tid
			+ " added to " + btid);
	    }
	}
	return new OrganiserOperationResult(false, "Behaviour  " + btid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult updateProcessInstanceState(String pid,
	    String status) {

	for (ProcessInstance pi : this.engine.getLiveProcessInstances()
		.values()) {
	    if (pi.getPId().equals(pid)) {
		pi.currentStatus = ProcessInstance.status.valueOf(status);
		return new OrganiserOperationResult(true,
			"Change status of instance " + pid + " to "
				+ pi.currentStatus.toString());
	    }
	}
	return new OrganiserOperationResult(false, "Cannot find instance "
		+ pid);
    }

    @Override
    public OrganiserOperationResult updateStateofAllProcessInstances(
	    String state) {
	// TODO Auto-generated method stub
	for (ProcessInstance pi : this.engine.getLiveProcessInstances()
		.values()) {
	    pi.currentStatus = ProcessInstance.status.valueOf(state);
	}
	return new OrganiserOperationResult(true,
		"Change status of all process instances");
    }

    @Override
    public OrganiserOperationResult executeScript(String script) {
	log.info("Got a script to execute \n" + script);

	try {
	    // String file = readAFile(); // for testing
	    long start = System.nanoTime();
	    // this.scriptEngine.execute(file); // for testing
	    this.scriptEngine.execute(script);
	    long stop = System.nanoTime();
	    return new OrganiserOperationResult(true, "Script applied in ."
		    + (stop - start) + "nanoseconds");
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return new OrganiserOperationResult(false,
		    "Process Instances Adaptation failed." + e.getMessage());
	}

    }

    @Override
    public OrganiserOperationResult executeScheduledScript(String script,
	    String onEventPattern, String pid) {
	if ((null == onEventPattern) || (onEventPattern.equals(""))) {
	    return new OrganiserOperationResult(false,
		    "Must specify an event or event pattern");
	}
	if ((null == pid) || (pid.equals(""))) {
	    return new OrganiserOperationResult(false,
		    "Must specify a process instance id");
	}
	// First we need to validate the script
	try {
	    if (!this.scriptEngine.validateScriptSyntax(script)) {
		return new OrganiserOperationResult(false, "Script Error.");
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return new OrganiserOperationResult(false, "Script Error. "
		    + e.getMessage());
	}

	// Create new adaptation script schedular.
	AdaptationScriptSchedular scriptSchedular = new AdaptationScriptSchedular(
		script, onEventPattern, pid, this);
	this.engine.getEventCloud().subscribe(scriptSchedular);
	return new OrganiserOperationResult(true,
		"A schedular added for event pattern "
			+ scriptSchedular.getEventPattern() + " with id "
			+ scriptSchedular.getId());
    }

    @Override
    public OrganiserOperationResult updatePropertyOfProcessInstance(String pid,
	    String property, String value) {
	ProcessInstance pi = this.engine.getProcessInstance(pid);
	if (null == pi) {
	    return new OrganiserOperationResult(false,
		    "Process Instances cannot be found.");
	}

	if (ProcessInstance.propertyAttribute.cos.toString().equals(property)) {
	    return new OrganiserOperationResult(false,
		    "Process already enacted");
	} else if (ProcessInstance.propertyAttribute.cot.toString().equals(
		property)) {
	    pi.setEventPattern(value);
	} else {
	    return new OrganiserOperationResult(false, "Unknown property "
		    + property);
	}
	return new OrganiserOperationResult(true,
		"ProcessInstance property modified.");
    }

    @Override
    /**
     * Property = PreEP/PostEP/PP/Role
     e.g., Set pre event pattern of a task

     */
    public OrganiserOperationResult updatePropertyofTaskOfInstance(String pid,
	    String taskid, String property, String value) {
	// Get Instance
	ProcessInstance pi = this.engine.getProcessInstance(pid);
	if (null == pi) {
	    return new OrganiserOperationResult(false,
		    "Process Instances cannot be found.");
	}
	// Pause
	pi.setCurrentStatus(ProcessInstance.status.paused);

	BehaviorTerm bt = pi.getContainedBehavior(taskid);
	if (null == bt) {
	    return new OrganiserOperationResult(false, "Task cannot be found.");
	}

	Task t = bt.getTask(taskid);
	if (null == t) {
	    return new OrganiserOperationResult(false, "Task cannot be found.");
	}
	// Modify Task
	if (Task.propertyAttribute.preep.toString().equals(property)) {
	    t.setEventPattern(value);
	} else if (Task.propertyAttribute.postep.toString().equals(property)) {
	    t.setPostEventPattern(value);
	} else if (Task.propertyAttribute.pp.toString().equals(property)) {
	    t.setProperty(new PerformanceProperty(value));
	} else {
	    return new OrganiserOperationResult(false, "Unknown property "
		    + property);
	}
	// Resume
	pi.setCurrentStatus(ProcessInstance.status.active);
	return new OrganiserOperationResult(true, "Task property modified.");

    }

    @Override
    public OrganiserOperationResult updateTaskDef(String roleId, String taskId,
	    String property, String value) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getRoles()) {
	    return new OrganiserOperationResult(false, "Cannot find role "
		    + roleId);
	}

	for (RoleType rt : smcCur.getRoles().getRole()) {
	    if (rt.getId().equals(roleId)) {
		TasksType tasks = rt.getTasks();
		if (null == tasks) {// if no TasksType, we will create a new one
		    return new OrganiserOperationResult(false,
			    "Cannot find role " + roleId);
		}
		for (TaskType taskType : tasks.getTask()) {
		    if (taskId.equals(taskType.getId())) {
			if ("usingMsgs".equals(property)) {
			    updateSrcMsgsOfTask(taskType, value);
			} else if ("resultingMsgs".equals(property)) {
			    updateResultsMsgsOfTask(taskType, value);
			}
		    }
		}
	    }
	}
	return new OrganiserOperationResult(true, "Updated the task : "
		+ taskId + " of the role :" + roleId);
    }

    private void updateSrcMsgsOfTask(TaskType tt, String srcMsgsStr) {
	SrcMsgsType srcMsgsType = new SrcMsgsType();
	String[] srcMsgArr = srcMsgsStr.split(",");
	for (String srcMsgStr : srcMsgArr) {
	    // e.g., CO_TT.sendTowRequest.Req
	    SrcMsgType smt = new SrcMsgType();
	    String[] split = srcMsgStr.split("\\.");
	    smt.setContractId(split[0]);
	    smt.setTermId(split[1]);
	    smt.setIsResponse(!(split[2].equals("Req")));
	    srcMsgsType.getSrcMsg().add(smt);
	}
	tt.setSrcMsgs(srcMsgsType);
    }

    private void updateResultsMsgsOfTask(TaskType tt, String resultMsgsStr) {
	ResultMsgsType resultMsgsType = new ResultMsgsType();
	String[] resultMsgArr = resultMsgsStr.split(",");
	for (String resultMsgStr : resultMsgArr) {
	    // e.g., CO_TT.sendTowRequest.Res.exsltFile
	    ResultMsgType rmt = new ResultMsgType();
	    String[] split = resultMsgStr.split("\\.");
	    rmt.setContractId(split[0]);
	    rmt.setTermId(split[1]);
	    rmt.setIsResponse(!(split[2].equals("Req")));
	    if (split.length > 3 && split[3] != null) {
		rmt.setTransformation(split[3] + ".xsl");
	    }
	    resultMsgsType.getResultMsg().add(rmt);
	}
	tt.setResultMsgs(resultMsgsType);
    }

    @Override
    public OrganiserOperationResult addNewEvent(String eventId, String pid,
	    int expiration) {
	// TODO Auto-generated method stub
	try {
	    this.engine.addEvent(new EventRecord(eventId, pid));
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    return new OrganiserOperationResult(true, "Cannot add event."
		    + e.getMessage());
	}
	return new OrganiserOperationResult(true, "Event Added.");
    }

    @Override
    public OrganiserOperationResult removeEvent(String eventId, String pid) {
	// TODO Auto-generated method stub
	this.engine.getEventCloud().expireEvent(eventId, pid);
	return new OrganiserOperationResult(true, "Event Removed.");
    }

    @Override
    public OrganiserOperationResult setEventExpiration(String eventId,
	    String pid, int expiration) {
	EventRecord event = this.engine.getEventCloud().getEventReocrd(eventId,
		pid);
	if (null == event) {
	    return new OrganiserOperationResult(true, "Cannot find event.");
	}
	event.setExpiration(new Date(expiration));
	return new OrganiserOperationResult(true, "Set Event Expiration.");
    }

    @Override
    public OrganiserOperationResult subscribeToEventPattern(
	    String eventPattern, String pid) {
	IOrganiserRole organiserRole = this.getOrganiserRole();
	this.engine.getEventCloud().subscribe(
		new OrganizerEventListener(eventPattern, pid, organiserRole));
	return null;
    }

    @Override
    public OrganiserOperationResult addTaskDefToRole(String roleId,
	    String taskId, boolean isMsgDriven, String srcMsgsStr,
	    String resultMsgsStr, String transFile) {

	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getRoles()) {
	    return new OrganiserOperationResult(false, "Cannot find role "
		    + roleId);
	}

	for (RoleType rt : smcCur.getRoles().getRole()) {
	    if (rt.getId().equals(roleId)) {
		TasksType tasks = rt.getTasks();
		if (null == tasks) {// if no TasksType, we will create a new one
		    tasks = new TasksType();
		    rt.setTasks(tasks);
		}
		TaskType tt = new TaskType();
		tt.setId(taskId);
		tt.setIsMsgDriven(isMsgDriven);

		if (null != srcMsgsStr) {
		    SrcMsgsType srcMsgsType = new SrcMsgsType();
		    String[] srcMsgArr = srcMsgsStr.split(",");
		    for (String srcMsgStr : srcMsgArr) {
			// e.g., CO_TT.sendTowRequest.Req
			SrcMsgType smt = new SrcMsgType();
			String[] split = srcMsgStr.split("\\.");
			smt.setContractId(split[0]);
			smt.setTermId(split[1]);
			smt.setIsResponse(!(split[2].equals("Req")));
			srcMsgsType.getSrcMsg().add(smt);
		    }
		    srcMsgsType.setTransformation(transFile);
		    tt.setSrcMsgs(srcMsgsType);
		}
		if (null != resultMsgsStr) {
		    ResultMsgsType resultMsgsType = new ResultMsgsType();
		    String[] resultMsgArr = resultMsgsStr.split(",");
		    for (String resultMsgStr : resultMsgArr) {
			// e.g., CO_TT.sendTowRequest.Res
			ResultMsgType rmt = new ResultMsgType();
			String[] split = resultMsgStr.split("\\.");
			rmt.setContractId(split[0]);
			rmt.setTermId(split[1]);
			rmt.setIsResponse(!(split[2].equals("Req")));
			if (split.length > 3) {
			    rmt.setTransformation(split[3] + ".xsl");
			}
			resultMsgsType.getResultMsg().add(rmt);
		    }
		    tt.setResultMsgs(resultMsgsType);
		}

		tasks.getTask().add(tt);
	    }
	}

	return new OrganiserOperationResult(true, "Added a Task : " + taskId);

    }

    @Override
    /**
     * patchFileName  must be xml and need to be placed in AXIS2_HOME/patches/
     */
    public OrganiserOperationResult applyPatch(String patchFileName) {
	// TODO Auto-generated method stub
	String patchFile = System.getenv("AXIS2_HOME")
		+ System.getProperty("file.separator")
		+ Constants.PATCH_FILE_DIR
		+ System.getProperty("file.separator") + patchFileName;
	File file = new File(patchFile);
	if (!file.canRead()) {
	    return new OrganiserOperationResult(false, "Invalid file path");
	}
	JAXBContext jc;
	SMC smcNew = null;
	try {

	    jc = JAXBContext.newInstance("au.edu.swin.ict.road.xml.bindings");
	    Unmarshaller unmarshaller = jc.createUnmarshaller();

	    smcNew = (SMC) unmarshaller.unmarshal(new File(patchFile));
	} catch (Exception e) {
	    return new OrganiserOperationResult(false,
		    "Cannot load XML descriptor " + e.getMessage());
	}

	// TODO : Now go through all the properties and add them accordingly to
	// existing SMC in MPF
	Composition serComposition = this.engine.getComposition();
	Composite composite = serComposition.getComposite();// This is the ROAD
							    // composite
							    // maintained in MPF
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF

	// Process Defintiions
	ProcessDefinitionsType processDefinitions = smcNew
		.getProcessDefinitions();
	if (null != processDefinitions) {
	    smcCur.getProcessDefinitions().getProcessDefinition()
		    .addAll(processDefinitions.getProcessDefinition());
	}
	// Behavior units
	BehaviorTermsType behaviorTerms = smcNew.getBehaviorTerms();
	if (null != behaviorTerms) {
	    smcCur.getBehaviorTerms().getBehaviorTerm()
		    .addAll(behaviorTerms.getBehaviorTerm());
	}

	// Roles
	Roles roles = smcNew.getRoles();
	if (null != roles) {
	    smcCur.getRoles().getRole().addAll(roles.getRole());
	    try {
		composite.extractRoles(roles);
	    } catch (CompositeInstantiationException e) {
		return new OrganiserOperationResult(false,
			"Cannot xtract roles " + e.getMessage());
	    }
	}

	// Contracts
	Contracts contracts = smcNew.getContracts();
	if (null != contracts) {
	    smcCur.getContracts().getContract().addAll(contracts.getContract());
	    try {
		composite.extractContracts(contracts);
	    } catch (Exception e) {
		return new OrganiserOperationResult(false,
			"Cannot xtract contracts " + e.getMessage());
	    }

	}

	// Player bindings
	PlayerBindings playerBindings = smcNew.getPlayerBindings();
	if (null != playerBindings) {
	    smcCur.getPlayerBindings().getPlayerBinding()
		    .addAll(playerBindings.getPlayerBinding());
	    composite.extractPlayerBindings(playerBindings);
	}

	// Facts
	Facts facts = smcNew.getFacts();
	if (null != facts) {
	    smcCur.getFacts().getFact().addAll(facts.getFact());
	    composite.extractFacts(facts);
	}

	return new OrganiserOperationResult(true, "Successfully patched");
    }

    @Override
    public OrganiserOperationResult removePD(String id) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getProcessDefinitions()) {
	    return new OrganiserOperationResult(false,
		    "Cannot find process definition ");
	}

	for (ProcessDefinitionType pd : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pd.getId().equals(id)) {
		// remove
		smcCur.getProcessDefinitions().getProcessDefinition()
			.remove(pd);
		return new OrganiserOperationResult(true, "removed " + id);
	    }
	}
	return new OrganiserOperationResult(false,
		"Cannot find process definition ");
    }

    @Override
    public OrganiserOperationResult removeBehavior(String id) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getBehaviorTerms()) {
	    return new OrganiserOperationResult(false, "Cannot find Behavior ");
	}

	for (BehaviorTermType bt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (bt.getId().equals(id)) {
		// remove
		smcCur.getBehaviorTerms().getBehaviorTerm().remove(bt);

		return new OrganiserOperationResult(true, " Behavior removed "
			+ id);
	    }
	}
	return new OrganiserOperationResult(false, "Cannot find Behavior ");
    }

    @Override
    public OrganiserOperationResult removeTaskFromBehavior(String id,
	    String behaviorId) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getBehaviorTerms()) {
	    return new OrganiserOperationResult(false, "Cannot find Behavior "
		    + behaviorId);
	}

	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(behaviorId)) {
		TaskRefsType trefs = btt.getTaskRefs();
		if (null == trefs) {
		    return new OrganiserOperationResult(false,
			    "Cannot find Task " + id + " in behavior "
				    + behaviorId);
		}
		for (TaskRefType tt : trefs.getTaskRef()) {
		    if (tt.getId().equals(id)) {
			trefs.getTaskRef().remove(tt);
			return new OrganiserOperationResult(true,
				" Task removed from " + id + " behavior "
					+ behaviorId);
		    }
		}
	    }
	}
	return new OrganiserOperationResult(false, "Cannot find Task " + id
		+ " in behavior " + behaviorId);
    }

    @Override
    public OrganiserOperationResult updateTaskFromBehavior(String id,
	    String behaviorId, String property, String value) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getBehaviorTerms()) {
	    return new OrganiserOperationResult(false, "Cannot find Behavior "
		    + behaviorId);
	}

	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(behaviorId)) {
		TaskRefsType trefs = btt.getTaskRefs();
		if (null == trefs) {
		    return new OrganiserOperationResult(false,
			    "Cannot find Task " + id + " in behavior "
				    + behaviorId);
		}
		for (TaskRefType tt : trefs.getTaskRef()) {
		    if (tt.getId().equals(id)) {
			if ("preEP".equals(property)) {
			    tt.setPreEP(value);
			} else if ("postEP".equals(property)) {
			    tt.setPostEP(value);
			}
			return new OrganiserOperationResult(true,
				" Task updated from " + id + " behavior "
					+ behaviorId);
		    }
		}
	    }
	}
	return new OrganiserOperationResult(false, "Cannot find Task " + id
		+ " in behavior " + behaviorId);
    }

    @Override
    public OrganiserOperationResult removeTaskDefFromRole(String id,
	    String roleId) {
	SMC smcCur = this.getSmcBinding();// This is the SMC maintained in MPF
	if (null == smcCur.getRoles()) {
	    return new OrganiserOperationResult(false, "Cannot find role "
		    + roleId);
	}

	for (RoleType rt : smcCur.getRoles().getRole()) {
	    if (rt.getId().equals(roleId)) {
		TasksType tasks = rt.getTasks();
		if (null == tasks) {
		    return new OrganiserOperationResult(false,
			    "Cannot find Task " + id + " in role " + roleId);
		}
		for (TaskType tt : tasks.getTask()) {
		    if (tt.getId().equals(id)) {
			tasks.getTask().remove(tt);
			return new OrganiserOperationResult(true,
				" Task removed from " + id + " role " + roleId);
		    }
		}
	    }
	}
	return new OrganiserOperationResult(false, "Cannot find Task " + id
		+ " in role " + roleId);
    }

    @Override
    public OrganiserOperationResult addNewPD(String pdid) {
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// Found. ERROR
		return new OrganiserOperationResult(true, "ProcessDefinition "
			+ pdid + " already exist. Choose another id");
	    }
	}
	ProcessDefinitionType pdType = new ProcessDefinitionType();
	pdType.setId(pdid);
	smcCur.getProcessDefinitions().getProcessDefinition().add(pdType);
	return new OrganiserOperationResult(true, "ProcessDefinition " + pdid
		+ " has been added");
    }

    @Override
    public OrganiserOperationResult updatePD(String pdid, String prop,
	    String val) {

	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// Found
		if (KEY_PD_COS.equalsIgnoreCase(prop)) {
		    pdType.setCoS(val);
		} else if (KEY_PD_COT.equalsIgnoreCase(prop)) {
		    pdType.setCoT(val);
		} else {
		    return new OrganiserOperationResult(false,
			    "Unknown property " + prop
				    + " for ProcessDefinition");
		}

		return new OrganiserOperationResult(true, "Update property "
			+ prop + " of ProcessDefinition" + pdid + " is done");
	    }
	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult addNewBehavior(String bid, String extendfrom) {
	SMC smcCur = this.getSmcBinding();
	BehaviorTermType btt = new BehaviorTermType();
	btt.setId(bid);
	btt.setExtends(extendfrom);
	if (btt.getConstraints() == null) {
	    btt.setConstraints(new ConstraintsType());
	}
	if (btt.getTaskRefs() == null) {
	    btt.setTaskRefs(new TaskRefsType());
	}
	smcCur.getBehaviorTerms().getBehaviorTerm().add(btt);
	return new OrganiserOperationResult(true, "BehaviorUnit " + bid
		+ " has been added");
    }

    @Override
    public OrganiserOperationResult updateBehavior(String bid, String prop,
	    String val) {
	SMC smcCur = this.getSmcBinding();
	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(bid)) {
		// Found
		if (prop.equals(KEY_BU_EXTENDSFROM)) {
		    btt.setExtends(val);
		} else if (prop.equals(KEY_BU_ISABSTRACT)) {

		    btt.setIsAbstract(Boolean.valueOf(val));

		} else {
		    return new OrganiserOperationResult(false,
			    "Unknown property " + prop + " for BehaviorUnit");
		}

		return new OrganiserOperationResult(true, "Update property "
			+ prop + " of BehaviorUnit" + bid + " is done");
	    }
	}
	return new OrganiserOperationResult(false, "BehaviorUnit " + bid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult removeBehaviorConstraint(String bid,
	    String cid) {
	SMC smcCur = this.getSmcBinding();
	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(bid)) {
		for (ConstraintType ct : btt.getConstraints().getConstraint()) {
		    if (ct.getId().equals(cid)) {
			// Found
			btt.getConstraints().getConstraint().remove(ct);
			return new OrganiserOperationResult(true, "Constraint "
				+ cid + " of BehaviorUnit " + bid
				+ " has been removed");
		    }
		}
		return new OrganiserOperationResult(false, "Constraint " + cid
			+ " of BehaviorUnit " + bid + " cannot be found");
	    }
	}
	return new OrganiserOperationResult(false, "BehaviorUnit " + bid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult updateBehaviorConstraint(String bid,
	    String cid, String prop, String val) {
	SMC smcCur = this.getSmcBinding();
	for (BehaviorTermType btt : smcCur.getBehaviorTerms().getBehaviorTerm()) {
	    if (btt.getId().equals(bid)) {
		for (ConstraintType ct : btt.getConstraints().getConstraint()) {
		    if (ct.getId().equals(cid)) {
			// Found
			if (prop.equals(KEY_CT_ISENABLED)) {

			    ct.setEnabled(Boolean.valueOf(val));

			} else if (prop.equals(KEY_CT_EXPRESSION)) {
			    ct.setExpression(val);
			} else if (prop.equals(KEY_CT_LANG)) {
			    ct.setLanguage(val);
			} else {
			    return new OrganiserOperationResult(false,
				    "Property  " + prop + " cannot be found");
			}
			return new OrganiserOperationResult(true, "Property  "
				+ prop + " of Constraint " + cid
				+ "of BehaviorUnit " + bid
				+ " has been updated ");
		    }
		}
		return new OrganiserOperationResult(false, "Constraint " + cid
			+ " of BehaviorUnit " + bid + " cannot be found");
	    }
	}
	return new OrganiserOperationResult(false, "BehaviorUnit " + bid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult removeProcessConstraint(String pdid,
	    String cid) {
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// Found
		for (ConstraintType ct : pdType.getConstraints()
			.getConstraint()) {
		    if (ct.getId().equals(cid)) {

			pdType.getConstraints().getConstraint().remove(ct);
			return new OrganiserOperationResult(true, "Constraint "
				+ cid + "of ProcessDefinition " + pdid
				+ " has been removed ");
		    }
		}
		return new OrganiserOperationResult(false, "Constraint " + cid
			+ " of ProcessDefinition " + pdid + " cannot be found");
	    }

	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult updateProcessConstraint(String pdid,
	    String cid, String prop, String val) {
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// Found
		for (ConstraintType ct : pdType.getConstraints()
			.getConstraint()) {
		    if (ct.getId().equals(cid)) {

			if (prop.equals(KEY_CT_EXPRESSION)) {
			    ct.setExpression(val);
			} else if (prop.equals(KEY_CT_ISENABLED)) {
			    ct.setEnabled(Boolean.valueOf(val));
			} else if (prop.equals(KEY_CT_LANG)) {
			    ct.setLanguage(val);
			} else {
			    return new OrganiserOperationResult(false,
				    "Property  " + prop + " cannot be found");
			}
			return new OrganiserOperationResult(true, "Constraint "
				+ cid + "of ProcessDefinition " + pdid
				+ " has been updated ");
		    }
		}
		return new OrganiserOperationResult(false, "Constraint " + cid
			+ " of ProcessDefinition " + pdid + " cannot be found");
	    }
	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult removeBehaviorRefFromPD(String pdid,
	    String bid) {
	SMC smcCur = this.getSmcBinding();
	for (ProcessDefinitionType pdType : smcCur.getProcessDefinitions()
		.getProcessDefinition()) {
	    if (pdType.getId().equals(pdid)) {
		// found pd
		for (String btRef : pdType.getBehaviorTermRefs()
			.getBehavirTermId()) {
		    if (btRef.equals(bid)) {
			pdType.getBehaviorTermRefs().getBehavirTermId()
				.remove(btRef);
			return new OrganiserOperationResult(true,
				"Behaviour Ref " + bid
					+ "of ProcessDefinition " + pdid
					+ " has been removed ");
		    }
		}
		return new OrganiserOperationResult(false, "Behaviour Ref "
			+ bid + "of ProcessDefinition " + pdid
			+ " cannot be found ");
	    }
	}
	return new OrganiserOperationResult(false, "ProcessDefinition " + pdid
		+ " cannot be found");
    }

    @Override
    public OrganiserOperationResult removeTaskFromInstance(String pid,
	    String tid) {
	ProcessInstance processInstance = this.engine.getProcessInstance(pid);
	if (null == processInstance) {
	    return new OrganiserOperationResult(false, "Process Instance "
		    + pid + " cannot be found");
	}

	InstanceTaskDeleteAction action = new InstanceTaskDeleteAction(tid);
	try {
	    action.adapt(processInstance);
	} catch (AdaptationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    return new OrganiserOperationResult(false, "Process Instance "
		    + pid + " cannot be adapted " + e.getMessage());
	}

	return new OrganiserOperationResult(true, "Process Instance " + pid
		+ " adapted");

    }

    @Override
    public OrganiserOperationResult removeContraintFromProcessInstance(
	    String pid, String cid) {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public OrganiserOperationResult updateContraintOfProcessInstance(
	    String pid, String cid, String expression) {
	// TODO Auto-generated method stub
	return null;
    }

    private SMC getSmcBinding() {
	Composition serComposition = this.engine.getComposition();
	Composite composite = serComposition.getComposite();// This is the ROAD
							    // composite
							    // maintained in MPF
	return composite.getSmcBinding();// This is the SMC maintained in MPF
    }

    // ////////////////////////////ROAD
    // //////////////////////////////////////////////
    /*
     * @Override public MessageWrapper getNextManagementMessage() { // TODO
     * Auto-generated method stub return this
     * .getOrganiserRole().getNextManagementMessage(); }
     * 
     * @Override public MessageWrapper getNextManagementMessage(long timeout,
     * TimeUnit unit) { // TODO Auto-generated method stub return this
     * .getOrganiserRole().getNextManagementMessage(timeout, unit); }
     * 
     * @Override public OrganiserOperationResult
     * sendManagementMessage(MessageWrapper msg, String destinationRoleId) { //
     * TODO Auto-generated method stub return this
     * .getOrganiserRole().sendManagementMessage(msg, destinationRoleId); }
     * 
     * @Override public OrganiserOperationResult addNewRole(String id, String
     * name, String description) { // TODO Auto-generated method stub return
     * this .getOrganiserRole().addNewRole(id, name, description); }
     * 
     * @Override public OrganiserOperationResult removeRole(String roleId) { //
     * TODO Auto-generated method stub return this
     * .getOrganiserRole().removeRole(roleId); }
     * 
     * @Override public OrganiserOperationResult addNewContract(String id,
     * String name, String description, String state, String type, String
     * ruleFile, boolean isAbstract, String roleAId, String roleBId) { // TODO
     * Auto-generated method stub return this
     * .getOrganiserRole().addNewContract(id, name, description, state, type,
     * ruleFile, isAbstract, roleAId, roleBId); }
     * 
     * @Override public OrganiserOperationResult removeContract(String
     * contractId) { // TODO Auto-generated method stub return this
     * .getOrganiserRole().removeContract(contractId); }
     * 
     * @Override public OrganiserOperationResult addNewTerm(String id, String
     * name, String messageType, String deonticType, String description, String
     * direction, String contractId) { // TODO Auto-generated method stub return
     * this .getOrganiserRole().addNewTerm(id, name, messageType, deonticType,
     * description, direction, contractId); }
     * 
     * @Override public OrganiserOperationResult removeTerm(String termId) { //
     * TODO Auto-generated method stub return this
     * .getOrganiserRole().removeTerm(termId); }
     * 
     * @Override public OrganiserOperationResult addNewOperation(String
     * operationName, String operationReturnType, Parameter[] parameters, String
     * termId) { // TODO Auto-generated method stub return this
     * .getOrganiserRole().addNewOperation(operationName, operationReturnType,
     * parameters, termId); }
     * 
     * @Override public OrganiserOperationResult removeOperation(String
     * operationName, String termId) { // TODO Auto-generated method stub return
     * this .getOrganiserRole().removeOperation(operationName, termId); }
     * 
     * @Override public OrganiserOperationResult addNewContractRule(String
     * newRule, String contractId) { // TODO Auto-generated method stub return
     * this .getOrganiserRole().addNewContractRule(newRule, contractId); }
     * 
     * @Override public OrganiserOperationResult removeContractRule(String
     * contractId, String ruleName) { // TODO Auto-generated method stub return
     * this.getOrganiserRole().removeContractRule(contractId, ruleName); }
     * 
     * @Override public OrganiserOperationResult addNewCompositeRule(String
     * newRule) { // TODO Auto-generated method stub return
     * this.addNewCompositeRule(newRule); }
     * 
     * @Override public OrganiserOperationResult removeCompositeRule(String
     * ruleName) { // TODO Auto-generated method stub return
     * this.getOrganiserRole().removeCompositeRule(ruleName); }
     * 
     * @Override public Contract getContractById(String id) { // TODO
     * Auto-generated method stub return
     * this.getOrganiserRole().getContractById(id); }
     * 
     * @Override public OrganiserOperationResult takeSnapshot() { // TODO
     * Auto-generated method stub return this.getOrganiserRole().takeSnapshot();
     * }
     * 
     * @Override public OrganiserOperationResult takeSnapshot(String folder) {
     * // TODO Auto-generated method stub return
     * this.getOrganiserRole().takeSnapshot(folder); }
     * 
     * @Override public OrganiserOperationResult changePlayerBinding(String
     * roleId, String endpoint) { // TODO Auto-generated method stub return
     * this.getOrganiserRole().changePlayerBinding(roleId, endpoint); }
     * 
     * @Override public FactObject getFact(String factType, String
     * factIdentifierValue) { // TODO Auto-generated method stub return
     * this.getFact(factType, factIdentifierValue); }
     * 
     * @Override public OrganiserOperationResult updateFact(FactObject
     * factObject) { // TODO Auto-generated method stub return
     * this.updateFact(factObject); }
     * 
     * @Override public OrganiserOperationResult addFact(String factType,
     * FactObject factObject) { // TODO Auto-generated method stub return
     * this.addFact(factType, factObject); }
     * 
     * @Override public OrganiserOperationResult removeFact(String factType,
     * String factIdentifierValue) { // TODO Auto-generated method stub return
     * this.removeFact(factType, factIdentifierValue); }
     * 
     * @Override public String getName() { // TODO Auto-generated method stub
     * return this.getOrganiserRole().getName(); }
     */
    private IOrganiserRole getOrganiserRole() {
	return this.getOrganiserRole();
    }

    private ProcessDefinitionType createProcessDefinitionType(
	    OMElement omElement) {
	String nsURI = "http://www.ict.swin.edu.au/serendip/types";
	ProcessDefinitionType processDefinitionType = new ProcessDefinitionType();
	processDefinitionType.setCoS(omElement
		.getFirstChildWithName(new QName(nsURI, "CoS")).getText()
		.trim());
	processDefinitionType.setCoT(omElement
		.getFirstChildWithName(new QName(nsURI, "CoT")).getText()
		.trim());
	processDefinitionType.setId(omElement
		.getAttributeValue(new QName("id")));
	OMElement constraintEle = omElement.getFirstChildWithName(new QName(
		nsURI, "Constraints"));
	if (constraintEle != null) {
	    ConstraintsType constraintsType = new ConstraintsType();

	    Iterator iterator = constraintEle
		    .getChildrenWithLocalName("Constraint");

	    while (iterator.hasNext()) {
		OMElement childConstraint = (OMElement) iterator.next();
		ConstraintType constraintType = new ConstraintType();
		constraintType.setExpression(childConstraint.getText().trim());
	    }
	    processDefinitionType.setConstraints(constraintsType);
	}
	BehaviorTermRef behaviorTermRef = new BehaviorTermRef();
	Iterator iterator = omElement.getFirstChildWithName(
		new QName(nsURI, "BehaviorTermRefs")).getChildrenWithLocalName(
		"BehavirTermId");
	while (iterator.hasNext()) {
	    OMElement childBT = (OMElement) iterator.next();
	    behaviorTermRef.getBehavirTermId().add(childBT.getText().trim());
	}
	processDefinitionType.setBehaviorTermRefs(behaviorTermRef);
	return processDefinitionType;
    }

    public OrganiserOperationResult addProcessDefinition(OMElement element) {
	if (element == null) {
	    return new OrganiserOperationResult(false,
		    "Must give a non-empty process definition");
	}
	ProcessDefinitionType processDefinitionType = createProcessDefinitionType(element);
	if (processDefinitionType == null) {
	    return new OrganiserOperationResult(false,
		    "Must give a non-empty process definition");
	}
	this.engine.getComposition().getComposite().getSmcBinding()
		.getProcessDefinitions().getProcessDefinition()
		.add(processDefinitionType);
	return new OrganiserOperationResult(true,
		"A process definition was added  with id : "
			+ processDefinitionType.getId());
    }

    @Override
    public OrganiserOperationResult getProcessInstanceIds(boolean isLive) {
	Composition serComposition = this.engine.getComposition();
	SerendipEngine serendipEngine = serComposition.getComposite()
		.getSerendipEngine();
	Hashtable<String, ProcessInstance> processInstances = null;
	if (isLive == false) {
	    processInstances = serendipEngine.getCompletedProcessInstances();
	} else {
	    processInstances = serendipEngine.getLiveProcessInstances();
	}

	if (null == processInstances) {
	    return new OrganiserOperationResult(false, "No process instances");
	}

	Set<String> keySet = processInstances.keySet();
	Iterator<String> it = keySet.iterator();
	String commaSepString = "";
	while (it.hasNext()) {
	    commaSepString += "," + it.next();
	}
	return new OrganiserOperationResult(true, commaSepString);
    }

    @Override
    public OrganiserOperationResult exportEPMLViewOfProcessInstance(String pid,
	    String filePath) {
	// Save an EPML file for a specific process instance
	Composition serComposition = this.engine.getComposition();
	SerendipEngine serendipEngine = serComposition.getComposite()
		.getSerendipEngine();
	// Check if the given id is in the live process instances
	ProcessInstance processInstance = null;
	if (serendipEngine.getLiveProcessInstances().keySet().contains(pid)) {
	    // is a live process instance
	    processInstance = serendipEngine.getLiveProcessInstances().get(pid);
	} else if (serendipEngine.getCompletedProcessInstances().keySet()
		.contains(pid)) {
	    processInstance = serendipEngine.getCompletedProcessInstances()
		    .get(pid);
	} else {
	    return new OrganiserOperationResult(false,
		    "No such process instance " + pid);
	}

	try {
	    processInstance.exportToEPML(filePath);
	} catch (Exception e) {
	    return new OrganiserOperationResult(false, "Cannot export EPML  "
		    + e.getMessage());
	}

	return new OrganiserOperationResult(true,
		"Successfully exported EPML to " + filePath);
    }

    @Override
    public OrganiserOperationResult exportEPMLViewOfProcessDef(String pDefId,
	    String filePath) {
	Composition serComposition = this.engine.getComposition();
	SerendipEngine serendipEngine = serComposition.getComposite()
		.getSerendipEngine();

	ProcessInstance processInstance = null;
	try {
	    processInstance = serendipEngine.getModelFactory()
		    .getNewProcessInstance(pDefId);
	    processInstance.exportToEPML(filePath);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    return new OrganiserOperationResult(true, "Cannot export EPML "
		    + e.getMessage());
	}

	return new OrganiserOperationResult(true,
		"Successfully exported EPML to " + filePath);
    }
}
