package au.edu.swin.ict.serendip.composition;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.processmining.framework.models.petrinet.pattern.log.Log;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermsType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.road.xml.bindings.SMC;
import au.edu.swin.ict.serendip.core.SerendipEngine;

/**
 * This is the composite representation at the Serendip level. The additional
 * information about a composite that we need to keep, we will keep in here.
 * 
 * Note that we do not create models, e.g. process definitions at the time of
 * enactment. Rather we keep the types and dynamically generate the models on
 * demand. This will be the design principle for the rest of the sections such
 * as MPF In this way the code uses the latest version of SMC
 * 
 * @author Malinda Kapuruge
 * 
 */
public class Composition {
    static Logger logger = Logger.getLogger(Composition.class);
    private String id = null;

    // private SMC smc = null;
    private Composite composite = null;

    /**
     * Create a new composition from the type loaded via the ROAD
     * 
     * @param composite
     */
    public Composition(Composite composite) {

	this.composite = composite;
	if ((null == this.composite)
		|| (null == this.composite.getSmcBinding())) {
	    logger.error("Cannot instantiate the Serendip composite ");
	}

    }

    /**
     * Iterate through all the process definitions. Returns the matching process
     * definition for CoS.
     * 
     * @param cos
     *            condition of start (event)
     * @return the process definition
     */
    public String getPDforCoS(String cos) {
	if (this.composite.getSmcBinding().getProcessDefinitions() == null) {
	    return null;
	}
	if (this.composite.getSmcBinding().getProcessDefinitions() == null) {
	    return null;
	}
	List<ProcessDefinitionType> pdTypeList = this.composite.getSmcBinding()
		.getProcessDefinitions().getProcessDefinition();
	for (ProcessDefinitionType pdt : pdTypeList) {
	    if (pdt.getCoS().equals(cos)) {
		return pdt.getId();
	    }
	}

	return null;

    }

    public List<String> getAllProcessDefIds() {
	ArrayList<String> idList = new ArrayList<String>();

	if (null != this.composite.getSmcBinding().getProcessDefinitions()) {
	    List<ProcessDefinitionType> pdTypeList = this.composite
		    .getSmcBinding().getProcessDefinitions()
		    .getProcessDefinition();

	    for (ProcessDefinitionType pdt : pdTypeList) {
		idList.add(pdt.getId());
	    }
	}

	return idList;
    }

    /**
     * Returns a specific process definition type on demand
     * 
     * @param defId
     * @return
     */
    public ProcessDefinitionType getProcessDefinition(String defId) {
	List<ProcessDefinitionType> pdTypeList = this.composite.getSmcBinding()
		.getProcessDefinitions().getProcessDefinition();
	for (ProcessDefinitionType pdType : pdTypeList) {
	    if (pdType.getId().equals(defId)) {
		return pdType;
	    }
	}
	return null;
    }

    /**
     * Gets the composite
     * 
     * @return
     */
    public Composite getComposite() {
	return this.composite;
    }

    /**
     * Get all the behavior term types in the composition
     * 
     * @return
     */
    public List<BehaviorTermType> getAllBehaviorTermTypes() {
	BehaviorTermsType bts = this.composite.getSmcBinding()
		.getBehaviorTerms();
	return bts.getBehaviorTerm();
    }

    /**
     * Get all the behavior term types for a given process definitions id
     * 
     * @param defId
     * @return
     */
    public List<BehaviorTermType> getAllBehaviorTermTypesForPD(String defId) {

	List<String> btIdList = getAllBehaviorTermIdsForPD(defId);
	ArrayList<BehaviorTermType> btList = new ArrayList<BehaviorTermType>();
	for (String s : btIdList) {
	    BehaviorTermType btt = this.getBehaviorTermTypeById(s);
	    btList.add(btt);
	}

	return btList;
    }

    /**
     * Get al the behavior term ids referenced given process definition
     * 
     * @param defId
     * @return
     */
    public List<String> getAllBehaviorTermIdsForPD(String defId) {
	ProcessDefinitionType pdef = this.getProcessDefinition(defId);
	return pdef.getBehaviorTermRefs().getBehavirTermId();

    }

    /**
     * Gets a behavior term for a given id
     * 
     * @param btId
     * @return
     */
    public BehaviorTermType getBehaviorTermTypeById(String btId) {
	List<BehaviorTermType> btList = getAllBehaviorTermTypes();
	for (BehaviorTermType btt : btList) {
	    if (btt.getId().equals(btId)) {
		return btt;
	    }
	}

	return null;
    }

}
