package au.edu.swin.ict.serendip.core.mgmt;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.serendip.core.mgmt.action.*;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;

import au.edu.swin.ict.serendip.core.mgmt.action.custom.CustomDefAdaptScaleIn;
import au.edu.swin.ict.serendip.core.mgmt.action.custom.CustomDefAdaptScaleOut;
import au.edu.swin.ict.serendip.core.mgmt.scripting.Block;
import au.edu.swin.ict.serendip.core.mgmt.scripting.Command;
import au.edu.swin.ict.serendip.core.mgmt.scripting.Script;
import au.edu.swin.ict.serendip.core.mgmt.scripting.ScriptParser;

/**
 * This engine will act as the script processor to modify a process instance
 * 
 * Assumptions: 1. A script can contain a number of commands seperated by ';' 2.
 * Each command can contain a finite number of arguments 2. Always the first
 * argument of a command is the pid
 * 
 * Example Script: PI:PDgold11{ updatePropertyOfTask tid="Tow" property="postep"
 * value="eTowSuccess * eTXReqd";
 * 
 * }
 * 
 * @author Malinda
 * 
 */
public class AdaptationScriptEngine implements Constants {
    static Logger logger = Logger.getLogger(AdaptationScriptEngine.class);

    private SerendipEngine engine = null;

    public AdaptationScriptEngine(SerendipEngine engine) {
	this.engine = engine;
    }

    public boolean validateScriptSyntax(String scriptText) throws Exception {
	Script script = null;
	try {
	    script = ScriptParser.parseScript(scriptText);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    throw new Exception(e.getMessage());
	}
	return true;
    }

    public void execute(String scriptText) throws AdaptationException {
	Script script = null;
	try {
	    script = ScriptParser.parseScript(scriptText);
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw new AdaptationException("Script error " + e.getMessage());
	}
	// Lets execute a block by block
	List<Block> blocks = script.getBlocks();

	if (blocks.size() < 1) {
	    throw new AdaptationException(
		    "Script error. No Scope? No parsable blocks? Script="
			    + scriptText);
	}
	for (Block b : blocks) {
	    executeBlock(b);
	}

    }

    /**
     * Executes a block. A block must have a scope and an id to identify the
     * scope e.g., PI:p001 a process instance e.g., DEF:smc is an identification
     * of the smc
     * 
     * @param b
     * @throws AdaptationException
     */
    private void executeBlock(Block b) throws AdaptationException {
	String blockName = b.getName();
	// Lets execute a command by command
	String[] blockNameSplit = blockName.split(":");
	if (blockNameSplit.length != 2) {
	    throw new AdaptationException("Invalid block scope specification "
		    + blockName);
	}
	String blockScope = blockNameSplit[0];
	// Get the commands
	List<Command> cmds = b.getCommands();
	if (blockScope.equals(scope.PI.toString())) {

	    // Check if multiple process instances needs to be adapted. A comma
	    // separated string.
	    String[] pids = null;
	    if (blockNameSplit[1].contains(",")) {
		pids = blockNameSplit[1].split(",");
	    } else {
		pids = new String[] { blockNameSplit[1] };
	    }
	    Vector<String> errorMsgs = new Vector<String>();
	    // For all the process instances perform the same change
	    // If there is any problem adapting a single instance, throw an
	    // exception
	    for (String pid : pids) {
		// Collect all adaptation actions
		List<InstanceAdaptAction> adaptationActionsList = new ArrayList<InstanceAdaptAction>();
		for (Command c : cmds) {
		    InstanceAdaptAction act = this.getInstanceAdaptationAction(
			    c, pid);
		    adaptationActionsList.add(act);
		}
		// execute the collected adaptation actions on the process
		// instance pid
		OrganiserOperationResult result = engine.getSerendipOrgenizer()
			.adaptProcessInstance(pid, adaptationActionsList);
		// If there is an error record it
		if (!result.getResult()) {
		    throw new AdaptationException("Adaptation failed for "
			    + pid + "Reason: " + result.getMessage());

		}
	    }

	} else if (blockScope.equals(scope.DEF.toString())) {
	    // TODO: We need to support this.
	    List<DefAdaptAction> adaptationActionsList = new ArrayList<DefAdaptAction>();
	    for (Command c : cmds) {
		DefAdaptAction act = this.getDefAdaptationAction(c);
		adaptationActionsList.add(act);
	    }
	    // execute the collected adaptation actions
	    OrganiserOperationResult result = engine.getSerendipOrgenizer()
		    .adaptDefinition(adaptationActionsList);
	    if (!result.getResult()) {
		throw new AdaptationException(result.getMessage());
	    }
	} else {
	    throw new AdaptationException("Invalid block scope  " + blockScope);
	}

    }

    /**
     * This is where a command is interpreted and executed
     * 
     * @param c
     * @throws AdaptationException
     */
    private InstanceAdaptAction getInstanceAdaptationAction(Command c,
	    String pid) throws AdaptationException {
	InstanceAdaptAction action = null;

	String commandName = c.getName();
	// Select which command
	if (commandName.equals(commands.updatePropertyOfTask.name())) {
	    action = new InstanceTaskPropertyAdaptationAction(
		    c.getProperty(KEY_TID), c.getProperty(KEY_PROPERTY),
		    c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.updatePropertyOfProcessInstance
		.name())) {
	    action = new InstanceProcessPropertyAdaptationAction(
		    c.getProperty(KEY_PROPERTY), c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.addNewTaskToInstance.name())) { // add
									       // a
									       // new
									       // task
									       // to
									       // a
									       // process
									       // instance
	    action = new InstanceTaskAddAction(c.getProperty(KEY_BTID),
		    c.getProperty(KEY_TID), c.getProperty(KEY_PREEP_VAL),
		    c.getProperty(KEY_POSTEP_VAL),
		    c.getProperty(KEY_OBLIGROLE_VAL), c.getProperty(KEY_PP_VAL));
	    // /String btid, String taskId, String preep, String postep, String
	    // obligrole, String pp
	} else if (commandName.equals(commands.updateProcessInstanceState
		.name())) {
	    action = new InstanceProcessStateAdaptationAction(
		    c.getProperty(KEY_STATUS));
	} else if (commandName.equals(commands.pause.name())) {
	    action = new InstanceProcessStateAdaptationAction(
		    ProcessInstance.status.paused.toString());
	} else if (commandName.equals(commands.resume.name())) {
	    action = new InstanceProcessStateAdaptationAction(
		    ProcessInstance.status.active.toString());
	} else {
	    throw new AdaptationException("Unsupported command " + commandName
		    + " for process instance " + pid);
	}

	return action;
    }

    /**
     * Get the definition adaptation action
     * 
     * @param c
     * @return
     * @throws AdaptationException
     */
    private DefAdaptAction getDefAdaptationAction(Command c)
	    throws AdaptationException {
	DefAdaptAction action = null;

	String commandName = c.getName();

	// Select which command
	if (commandName.equals(commands.applyPatch.name())) {
	    action = new DefApplyPatchAction(c.getProperty(KEY_PATCH_FILE));
	} else if (commandName.equals(commands.addRole.name())) {
	    action = new DefRoleAddAction(c.getProperty(KEY_ROLE_ID),
		    c.getProperty(KEY_ROLE_NAME), c.getProperty(KEY_ROLE_DESCR));
	} else if (commandName.equals(commands.removeRole.name())) {
	    action = new DefRoleRemoveAction(c.getProperty(KEY_ROLE_ID));
	} else if (commandName.equals(commands.addContract.name())) {
	    boolean isAbstrat = false;// default
	    if (null != c.getProperty(KEY_CONTRACT_ISABSTRACT)) {
		isAbstrat = (c.getProperty(KEY_CONTRACT_ISABSTRACT)
			.equals(VAL_TRUE));
	    }
	    action = new DefContractAddAction(c.getProperty(KEY_CONTRACT_ID),
		    c.getProperty(KEY_CONTRACT_NAME),
		    c.getProperty(KEY_CONTRACT_DESCR),
		    c.getProperty(KEY_CONTRACT_STATE),
		    c.getProperty(KEY_CONTRACT_TYPE),
		    c.getProperty(KEY_CONTRACT_RULEFILE), isAbstrat,
		    c.getProperty(KEY_CONTRACT_ROLEAID),
		    c.getProperty(KEY_CONTRACT_ROLEBID));
	} else if (commandName.equals(commands.removeContract.name())) {
	    action = new DefContractRemoveAction(c.getProperty(KEY_CONTRACT_ID));
	} else if (commandName.equals(commands.addTaskDef.name())) {
	    action = new DefTaskDefAddAction(c.getProperty(KEY_R_ID),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_SRC_MGS),
		    c.getProperty(KEY_RESULT_MGS),
		    c.getProperty(KEY_TRANS_FILE));
	} else if (commandName.equals(commands.updateTaskDef.name())) {
	    action = new DefTaskDefUpdateAction(c.getProperty(KEY_R_ID),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_PROPERTY),
		    c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.removeTaskDef.name())) {
	    action = new DefTaskDefRemoveAction(c.getProperty(KEY_R_ID),
		    c.getProperty(KEY_TASK_ID));
	} else if (commandName.equals(commands.addTerm.name())) {
	    action = new DefAddTermAction(c.getProperty(KEY_TERM_ID),
		    c.getProperty(KEY_TERM_CTID), c.getProperty(KEY_TERM_NAME),
		    c.getProperty(KEY_TERM_MESSAGE_TYPE),
		    c.getProperty(KEY_TERM_DEONTIC_TYPE),
		    c.getProperty(KEY_TERM_DESCR),
		    c.getProperty(KEY_TERM_DIRECTION));
	} else if (commandName.equals(commands.removeTerm.name())) {
	    action = new DefRemoveTermAction(c.getProperty(KEY_TERM_ID),
		    c.getProperty(KEY_TERM_CTID));
	} else if (commandName.equals(commands.updateTerm.name())) {
	    action = new DefUpdateTermAction(c.getProperty(KEY_TERM_ID),
		    c.getProperty(KEY_TERM_CTID), c.getProperty(KEY_PROPERTY),
		    c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.addOperationToTerm.name())) {
	    action = new DefAddOperationToTermAction(
		    c.getProperty(KEY_TERM_OP_NAME),
		    c.getProperty(KEY_TERM_RETURN_TYPE),
		    createParameters(c.getProperty(KEY_TERM_PARAMETERS)),
		    c.getProperty(KEY_TERM_ID), c.getProperty(KEY_TERM_CTID));
	} else if (commandName.equals(commands.setOutMsgOnTask.name())) {
	    action = new DefSetOutMsgOnTaskAction(
		    c.getProperty(KEY_TASK_OP_NAME),
		    c.getProperty(KEY_TASK_RETURN_TYPE),
		    c.getProperty(KEY_TASK_DELIVERY_TYPE), false,
		    createParameters(c.getProperty(KEY_TASK_PARAMETERS)),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_ROLE_ID));
	} else if (commandName.equals(commands.setInMsgOnTask.name())) {
	    action = new DefSetInMsgOnTaskAction(
		    c.getProperty(KEY_TASK_OP_NAME),
		    c.getProperty(KEY_TASK_RETURN_TYPE), true,
		    createParameters(c.getProperty(KEY_TASK_PARAMETERS)),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_ROLE_ID));
	} else if (commandName.equals(commands.addPlayerBinding.name())) {
	    action = new DefAddPlayerBindingAction(c.getProperty(KEY_PB_ID),
		    c.getProperty(KEY_PB_RID), c.getProperty(KEY_PB_ENDPOINT));
	} else if (commandName.equals(commands.removePlayerBinding.name())) {
	    action = new DefRemovePlayerBindingAction(c.getProperty(KEY_PB_ID));
	} else if (commandName.equals(commands.updatePlayerBinding.name())) {
	    action = new DefUpdatePlayerBindingAction(c.getProperty(KEY_PB_ID),
		    c.getProperty(KEY_PROPERTY), c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.addBehavior.name())) {
	    action = new DefBehaviorAddAction(c.getProperty(KEY_BU_ID),
		    c.getProperty(KEY_BU_EXTENDSFROM));
	} else if (commandName.equals(commands.removeBehavior.name())) {
	    action = new DefBehaviorRemoveAction(c.getProperty(KEY_BU_ID));
	} else if (commandName.equals(commands.updateBehavior.name())) {
	    action = new DefBehaviorUpdateAction(c.getProperty(KEY_BU_ID),
		    c.getProperty(KEY_PROPERTY), c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.addTaskRef.name())) {
	    action = new DefTaskRefAddAction(c.getProperty(KEY_BU_ID),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_BU_PREEP),
		    c.getProperty(KEY_BU_POSTEP), c.getProperty(KEY_BU_PP));
	} else if (commandName.equals(commands.removeTaskRef.name())) {
	    action = new DefTaskRefRemoveAction(c.getProperty(KEY_TASK_ID),
		    c.getProperty(KEY_BU_ID));
	} else if (commandName.equals(commands.updateTaskRef.name())) {
	    action = new DefTaskRefUpdateAction(c.getProperty(KEY_BU_ID),
		    c.getProperty(KEY_TASK_ID), c.getProperty(KEY_PROPERTY),
		    c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.addConstraintToBehavior.name())) {
	    action = new DefAddConstraintToBehavior(c.getProperty(KEY_BU_ID),
		    c.getProperty(KEY_BU_CID), c.getProperty(KEY_BU_CEXPR),
		    true);
	} else if (commandName.equals(commands.removeConstraintToBehavior
		.name())) {
	    action = new DefRemoveConstraintToBehavior(
		    c.getProperty(KEY_BU_ID), c.getProperty(KEY_BU_CID));
	} else if (commandName.equals(commands.updateConstraintToBehavior
		.name())) {
	    action = new DefUpdateConstraintToBehavior(
		    c.getProperty(KEY_BU_ID), c.getProperty(KEY_BU_CID),
		    c.getProperty(KEY_PROPERTY), c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.updateProcessDef.name())) {
	    action = new DefUpdateProcessDef(c.getProperty(KEY_PDID),
		    c.getProperty(KEY_PROPERTY), c.getProperty(KEY_VALUE));
	} else if (commandName.equals(commands.removeBehaviorRefFromProcessDef
		.name())) {
	    action = new DefRemoveBehaviorRefFromProcessDef(
		    c.getProperty(KEY_PDID), c.getProperty(KEY_BU_ID));
	} else if (commandName.equals(commands.addBehaviorRefToProcessDef
		.name())) {
	    action = new DefAddBehaviorRefToProcessDef(c.getProperty(KEY_PDID),
		    c.getProperty(KEY_BU_ID));
	} else if (commandName.equals(commands.expandTheRole.name())) {
	    action = new CustomDefAdaptScaleOut(commands.expandTheRole.name(),
		    c.getProperties());
	} else if (commandName.equals(commands.scaleInTheComposite.name())) {
	    action = new CustomDefAdaptScaleIn(
		    commands.scaleInTheComposite.name(), c.getProperties());
	} else {
	    // TODO: Here we might need to support custom adaptation actions.
	    action = loadCustomDefAdaptationAction(c);
	    // throw new AdaptationException("Unspported command "+commandName
	    // );
	}

	return action;
    }

    /**
     * This method loads an object of a custom adaptation action class. Such a
     * class should
     * <ul>
     * <li>Extend class interface
     * <code> au.edu.swin.ict.serendip.core.mgmt.action.CustomDefAdaptationAction</code>
     * </ul>
     * The command should be like [anyUserDefinedCommand]
     * class=[fullQlfdClassName] (key=val)*
     * 
     * @param c
     * @return
     * @throws AdaptationException
     */
    private DefAdaptAction loadCustomDefAdaptationAction(Command c)
	    throws AdaptationException {
	CustomDefAdaptationAction action = null;
	String className = c.getProperty(KEY_CUSTOM_CLASS);
	if (null == className) {
	    throw new AdaptationException("Unspported command OR the key  "
		    + KEY_CUSTOM_CLASS
		    + " is not specified for custom command " + c.getName());
	}
	try {
	    Class<CustomDefAdaptationAction> clazz = (Class<CustomDefAdaptationAction>) Class
		    .forName(className);

	    Constructor<CustomDefAdaptationAction> ct = clazz.getConstructor(
		    String.class, Properties.class);
	    action = (CustomDefAdaptationAction) ct.newInstance(c.getName(),
		    c.getProperties());// Here we set the command name and
				       // properties

	} catch (Exception e) {
	    throw new AdaptationException("Cannot load class " + className
		    + " due to error " + e.getMessage());
	}
	return action;
    }

    private Parameter[] createParameters(String paramStr) {
	List<Parameter> parameters = new ArrayList<Parameter>();
	String[] paraStrs = paramStr.split(",");
	for (String resultMsgStr : paraStrs) {
	    // e.g., String.Name,....
	    String[] split = resultMsgStr.split("\\.");
	    parameters.add(new Parameter(split[0], split[1]));
	}
	return parameters.toArray(new Parameter[parameters.size()]);
    }
}
