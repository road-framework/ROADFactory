package au.edu.swin.ict.serendip.core.mgmt;

import java.util.List;

import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.action.DefAdaptAction;
import au.edu.swin.ict.serendip.core.mgmt.action.InstanceAdaptAction;
import org.apache.axiom.om.OMElement;

public interface SerendipOrganizer {

    // Advanced Adaptation methods. Can be used to do batch mode executions
    public OrganiserOperationResult executeScript(String script);

    public OrganiserOperationResult executeScheduledScript(String script,
	    String onEventPattern, String pid);

    public OrganiserOperationResult applyPatch(String patchFile);// used to
								 // patch a
								 // partial SMC
								 // file. Can be
								 // also called
								 // via
								 // executeScript

    // ////////////////Evolutionary phase 2 changes/////////////
    public OrganiserOperationResult addNewPD(String pdid);

    public OrganiserOperationResult removePD(String pdid);

    public OrganiserOperationResult updatePD(String pdid, String prop,
	    String val);

    public OrganiserOperationResult addNewBehavior(String bid, String extendfrom);

    public OrganiserOperationResult removeBehavior(String bid);

    public OrganiserOperationResult updateBehavior(String bid, String prop,
	    String val);

    public OrganiserOperationResult addNewBehaviorConstraint(String btid,
	    String cid, String expression, boolean enabled);

    public OrganiserOperationResult removeBehaviorConstraint(String btid,
	    String cid);

    public OrganiserOperationResult updateBehaviorConstraint(String btid,
	    String cid, String prop, String val);

    public OrganiserOperationResult addNewProcessConstraint(String pdid,
	    String cid, String expression, boolean enabled);

    public OrganiserOperationResult removeProcessConstraint(String pdid,
	    String cid);

    public OrganiserOperationResult updateProcessConstraint(String pdif,
	    String cid, String prop, String val);

    public OrganiserOperationResult addTaskToBehavior(String btid, String tid,
	    String preep, String postep, String pp);

    public OrganiserOperationResult removeTaskFromBehavior(String id,
	    String behaviorId);

    public OrganiserOperationResult updateTaskFromBehavior(String id,
	    String behaviorId, String property, String value);

    public OrganiserOperationResult addBehaviorRefToPD(String pdid, String btid);

    public OrganiserOperationResult removeBehaviorRefFromPD(String pdid,
	    String bid);

    public OrganiserOperationResult addTaskDefToRole(String roleId,
	    String taskId, boolean isMsgDriven, String srcMsgs,
	    String resutMsgs, String transFile);

    public OrganiserOperationResult removeTaskDefFromRole(String id,
	    String roleId);

    public OrganiserOperationResult updateTaskDef(String roleID, String taskId,
	    String property, String value);

    // ////////////////Ad-hoc runtime process instance level (phase 3)
    // operations /////////////

    public OrganiserOperationResult addTaskToInstance(String pid, String btid,
	    String tid, String preEP, String postEP, String obligRole, String pp);

    public OrganiserOperationResult removeTaskFromInstance(String pid,
	    String tid);

    public OrganiserOperationResult updatePropertyofTaskOfInstance(String pid,
	    String taskid, String property, String value);

    public OrganiserOperationResult addConstraintToProcessInstance(String pid,
	    String cid, String expression, boolean enabled);

    public OrganiserOperationResult removeContraintFromProcessInstance(
	    String pid, String cid);

    public OrganiserOperationResult updateContraintOfProcessInstance(
	    String pid, String cid, String expression);

    public OrganiserOperationResult updatePropertyOfProcessInstance(String pid,
	    String property, String value);

    public OrganiserOperationResult updateProcessInstanceState(String pid,
	    String status);

    public OrganiserOperationResult updateStateofAllProcessInstances(
	    String state);

    // Events
    public OrganiserOperationResult addNewEvent(String eventId, String pid,
	    int expiration);

    public OrganiserOperationResult removeEvent(String eventId, String pid);

    public OrganiserOperationResult setEventExpiration(String eventId,
	    String pid, int expiration);

    public OrganiserOperationResult subscribeToEventPattern(
	    String eventPattern, String pid);

    // Methods to be used by local organizers
    public OrganiserOperationResult adaptProcessInstance(
	    String processInstanceId,
	    List<InstanceAdaptAction> adaptationActions);

    public OrganiserOperationResult adaptDefinition(
	    List<DefAdaptAction> adaptationActions);

    public OrganiserOperationResult addProcessDefinition(OMElement element);

    // Monitoring
    public OrganiserOperationResult getProcessInstanceIds(boolean isLive);

    public OrganiserOperationResult exportEPMLViewOfProcessInstance(String pid,
	    String filePath);

    public OrganiserOperationResult exportEPMLViewOfProcessDef(String id,
	    String filePath);
}
