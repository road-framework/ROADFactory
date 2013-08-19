package au.edu.swin.ict.serendip.core.mgmt;

import java.util.List;

import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.road.xml.bindings.SMC;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.mgmt.action.InstanceAdaptAction;
import au.edu.swin.ict.serendip.verficiation.SerendipVerificationException;

/**
 * Handles all the process instance specific adaptations
 * 
 * @author Malinda
 * 
 */
public class ProcessInstanceAdaptationEngine {
    private SerendipEngine engine = null;
    private Composition compo = null;
    private SMC smc = null;
    private ProcessInstance bkupInstance = null;;

    /**
     * Handles all the process instance specifica adaptations
     * 
     * @param engine
     */
    public ProcessInstanceAdaptationEngine(SerendipEngine engine) {
	this.engine = engine;
	this.compo = engine.getComposition();
	this.smc = this.compo.getComposite().getSmcBinding();
    }

    /**
     * This method should be called to perform a single or an array of
     * adaptation
     * 
     * @param pId
     *            the process instance id
     * @param adaptationList
     *            contains a number of atomic adaptation actions.
     * @throws AdaptationException
     */
    public void executeAdaptation(String pId,
	    List<InstanceAdaptAction> adaptationList)
	    throws AdaptationException {
	// Get the process instance
	ProcessInstance pi = this.engine.getProcessInstance(pId);
	// Get the task
	if (null == pi) {
	    throw new AdaptationException("Cannot find the process isntance"
		    + pId);
	}
	// Backup it first
	this.backup(pi);
	// Perform all the adaptations to the instance
	for (InstanceAdaptAction aa : adaptationList) {
	    try {
		boolean res = aa.adapt(pi);// call the adapt method
		if (!res) {
		    this.restore();
		    throw new AdaptationException(
			    "Problem in adapting process instance " + pId);
		}
	    } catch (AdaptationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		this.restore();
		throw new AdaptationException(
			"Adaptation aborted due to following reason.\n"
				+ e.getMessage());
	    }
	}
	// Validate against the constraints
	try {
	    boolean isValid = this.engine.getModelFactory()
		    .verifyProcessInstance(pi);
	    if (!isValid) {
		this.restore();
		throw new AdaptationException(
			"Verification Failed. Adaptation aborted.\n");
	    }
	} catch (SerendipVerificationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    this.restore();
	    throw new AdaptationException(
		    "Adaptation invalid due to following reason.\n"
			    + e.getMessage());
	}

	// TODO: We need to check the state of the

    }

    private ProcessInstance backup(ProcessInstance pi) {
	// Always the fist argument of any command is the process
	// instance(Assumption)
	pi.setCurrentStatus(ProcessInstance.status.paused);
	this.bkupInstance = (ProcessInstance) pi.clone();
	// Clone
	return this.bkupInstance;
    }

    /**
     * Restore a process instance when there is a need for a rollback
     */
    private void restore() {
	this.engine.replaceProcessInstance(this.bkupInstance,
		this.bkupInstance.getPId());// Both have the same id

    }
}
