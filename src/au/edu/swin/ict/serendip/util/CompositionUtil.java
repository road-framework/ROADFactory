package au.edu.swin.ict.serendip.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.xml.bindings.ContractType;
import au.edu.swin.ict.road.xml.bindings.SMC.Contracts;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.composition.ProcessDefinition;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.epc.PatternToEPC;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermRef;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermsType;
import au.edu.swin.ict.road.xml.bindings.ConstraintType;
import au.edu.swin.ict.road.xml.bindings.ConstraintsType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ResultMsgType;
import au.edu.swin.ict.road.xml.bindings.ResultMsgsType;
import au.edu.swin.ict.road.xml.bindings.RoleType;
import au.edu.swin.ict.road.xml.bindings.SMC.Roles;
import au.edu.swin.ict.road.xml.bindings.SrcMsgType;
import au.edu.swin.ict.road.xml.bindings.SrcMsgsType;
import au.edu.swin.ict.road.xml.bindings.TaskType;
import au.edu.swin.ict.road.xml.bindings.TasksType;

public class CompositionUtil {
    static Logger logger = Logger.getLogger(CompositionUtil.class);

    // Get all
    // methods/////////////////////////////////////////////////////////////////////
    // Get all process definition ids
    // DONE
    public static String[] getAllProcessDefinitionIds(Composition comp) {
	List<String> idList = comp.getAllProcessDefIds();
	String[] idArr = new String[idList.size()];
	idList.toArray(idArr);
	return idArr;
    }

    // Get all SR ids
    // TODO rename to SR=Contract
    public static String[] getAllSRIds(Composition comp) {

	Map<String, Contract> contracts = comp.getComposite().getContractMap();

	contracts.keySet().toArray();

	String[] ids = new String[contracts.size()];
	contracts.keySet().toArray(ids);

	return ids;
    }

    // Get all BT ids
    // DONE
    public static String[] getAllBTIDOfComposition(Composition comp) {
	/*
	 * TODO Vector<String> btVec = new Vector<String>(); List<ContractType>
	 * contractList =
	 * comp.getComposite().getSmcBinding().getContracts().getContract();
	 * for(ContractType ct: contractList){ List<BehaviorTermType> btTypeList
	 * = ct.getBehaviorTerms().getBehaviorTerm(); for(BehaviorTermType bt :
	 * btTypeList){ btVec.add( bt.getId()); } } String[] idArr =
	 * (String[])btVec.toArray(new String[btVec.size()]); return idArr;
	 */
	return null;
    }

    // Get all constraints
    // TODO: Not being used. Remove if not neccessary
    // public static ConstraintType[]
    // getAllConstriantsOfComposition(CompositionType compType){
    // Vector<ConstraintType> constriantsVec = new Vector<ConstraintType>();
    // //Get constrinats defined in behavior terms
    // String [] btIds = CompositionUtil.getAllBTIDOfComposition(compType);
    // BehaviorTermType[] bTerms =
    // CompositionUtil.getBehaviorTermsForBTIDs(btIds, compType);
    // ConstraintType[] bConstriantsFromBT =
    // CompositionUtil.getAllConstriantsForBTs(bTerms);
    // constriantsVec.addAll(Arrays.asList(bConstriantsFromBT));//add to the
    // vector
    //
    // //Get constraints defined in each process definition
    // String[] pDefIds = CompositionUtil.getAllProcessDefinitionIds(compType);
    // for(int i=0 ; i< pDefIds.length; i++){
    // ConstraintType[] bConstriantsFromPDTemp=
    // CompositionUtil.getAllConstriantsForPD(pDefIds[i], false,
    // compType);//NOTE: We avoid behavior terms by setting
    // includeBTConstraints=false
    // constriantsVec.addAll(Arrays.asList(bConstriantsFromPDTemp));//add to the
    // vector
    // }
    //
    // //Now we have gone thru all the behavior terms and process deifnitions.
    // return the vector as an array
    // ConstraintType[] constraintsArr = new
    // ConstraintType[constriantsVec.size()];
    // return (ConstraintType[]) constriantsVec.toArray(constraintsArr);
    // }
    //

    // Given X return y
    // methods/////////////////////////////////////////////////////////////////////

    /**
     * For a given pid return all the BT ids
     */
    // DONE
    public static String[] getAllBTIDsForProcess(String defId, Composition comp) {

	List<String> btIdList = comp.getAllBehaviorTermIdsForPD(defId);

	String[] idArr = (String[]) btIdList
		.toArray(new String[btIdList.size()]);
	btIdList.toArray();

	return idArr;
    }

    /**
     * For a given definition return all the BT ids
     * 
     * @param pDef
     * @param compType
     * @return
     */
    // DONE
    public static String[] getAllBTIDsForProcess(ProcessDefinitionType pDef,
	    Composition comp) {
	// Get list
	List<String> btIdRefList = pDef.getBehaviorTermRefs()
		.getBehavirTermId();
	// To array
	return (String[]) btIdRefList.toArray(new String[btIdRefList.size()]);
    }

    /**
     * Given the id return the SR
     * 
     * @param contractId
     * @param compType
     * @return
     */
    // TODO: Need to be renamed to get Contract
    public static ContractType getContractType(String contractId,
	    Composition comp) {

	List<ContractType> contractList = comp.getComposite().getSmcBinding()
		.getContracts().getContract();
	for (ContractType ct : contractList) {
	    if (ct.getId().equals(contractId)) {
		return ct;
	    }
	}

	return null;
    }

    /**
     * For a given SR id return all the BT ids
     */
    // Done
    public static String[] getAllBTIDsForSR(String srId, Composition comp) {
	/*
	 * TODO Vector<String> btIdVec = new Vector<String>();
	 * List<ContractType> contractList =
	 * comp.getComposite().getSmcBinding().getContracts().getContract();
	 * for(ContractType ct: contractList ){ List<BehaviorTermType>
	 * behaviorTermTypeList= ct.getBehaviorTerms().getBehaviorTerm();
	 * for(BehaviorTermType btt : behaviorTermTypeList){
	 * btIdVec.add(btt.getId()); } }
	 * 
	 * String[] btIds = new String[btIdVec.size()]; return
	 * btIdVec.toArray(btIds);
	 */
	return null;
    }

    /**
     * For a given definition return all the BT ids. (Recursive if inherited
     * from other service relationships)
     * 
     * @param pDef
     * @param compType
     * @param localitySR
     *            the SR in which the BT is applicable. e.g. Child SR
     * @return
     */
    // TODO: Need to support inheritance in ROAD
    public static String[] getAllBTIDsForSR(ContractType contract/* sr */,
	    Composition comp/* compType */, ContractType localityContract/* localitySR */) {
	// TODO: Support inheritance. Check the parent service relationship
	/*
	 * TODO Vector<String> btIdVec = new Vector<String>();
	 * 
	 * List<BehaviorTermType> behaviorTermTypeList =
	 * contract.getBehaviorTerms().getBehaviorTerm(); for(BehaviorTermType
	 * btt : behaviorTermTypeList){ btIdVec.add(btt.getId()); }
	 * 
	 * 
	 * //If extended, use the behavior terms of the parent too String
	 * extendsFromID = contract.getExtends(); if(null != extendsFromID){
	 * ContractType conractParent = getContractType(extendsFromID, comp);
	 * 
	 * String[] extended = getAllBTIDsForSR(conractParent, comp,
	 * localityContract); //Recursive call for(int i=0; i< extended.length;
	 * i++){ btIdVec.add(extended[i]); } }
	 * 
	 * String[] btIds = new String[btIdVec.size()];
	 * 
	 * return btIdVec.toArray(btIds);
	 */
	return null;
    }

    /**
     * 
     * @param pdId
     * @param includeBTConstraints
     *            set true if the constraints of the encapsulated behavior terms
     *            too
     * @param compType
     * @return
     */
    // DONE
    public static ConstraintType[] getAllConstriantsForPD(String pdId,
	    boolean includeBTConstraints, Composition comp) {
	Vector<ConstraintType> constriantsVec = new Vector<ConstraintType>();

	List<ProcessDefinitionType> processDefinitionTypeList = comp
		.getComposite().getSmcBinding().getProcessDefinitions()
		.getProcessDefinition();
	for (ProcessDefinitionType pdt : processDefinitionTypeList) {
	    if (pdt.getId().equals(pdId)) {// Matching PD found
		if (null != pdt.getConstraints()) {
		    List<ConstraintType> constraintTypeList = pdt
			    .getConstraints().getConstraint();
		    constriantsVec.addAll(constraintTypeList);// Add to the
							      // vector
		}
	    }
	}

	if (includeBTConstraints) {
	    String[] btIds = getAllBTIDsForProcess(pdId, comp);
	    BehaviorTermType[] bts = getBehaviorTermsForBTIDs(btIds, comp);
	    ConstraintType[] bConstriants = getAllConstriantsForBTs(bts);
	    constriantsVec.addAll(Arrays.asList(bConstriants));
	}
	ConstraintType[] constraintsArr = new ConstraintType[constriantsVec
		.size()];
	return (ConstraintType[]) constriantsVec.toArray(constraintsArr);

    }

    /**
     * Get all the constraints for a given contract id
     */
    // DONE
    public static ConstraintType[] getAllConstriantsForSR(String contractId,
	    Composition comp) {
	/*
	 * TODO Vector<ConstraintType> constriantsVec = new
	 * Vector<ConstraintType>();
	 * 
	 * List<ContractType> contractList =
	 * comp.getComposite().getSmcBinding().getContracts().getContract();
	 * for(ContractType ct: contractList){ List<BehaviorTermType>
	 * behaviorTermTypeList = ct.getBehaviorTerms().getBehaviorTerm();
	 * for(BehaviorTermType bt: behaviorTermTypeList){
	 * constriantsVec.addAll(bt.getConstraints().getConstraint()); } }
	 * ConstraintType[] constraintsArr = new
	 * ConstraintType[constriantsVec.size()]; return (ConstraintType[])
	 * constriantsVec.toArray(constraintsArr);
	 */
	return null;
    }

    // Get collection for a given collection
    // methods///////////////////////////////////

    /**
     * Gets the parent hierarchy (recursive)
     */
    // DONE
    public static void getParents(ContractType contractType, Composition comp,
	    Vector<ContractType> contractVec) {

	// List<ContractType> contractList =
	// comp.getComposite().getSmcBinding().getContracts().getContract();
	// for(ContractType ct: contractList){
	// if(ct.getId().equals(parentId)){
	// contractVec.add(ct);
	// //recursive call
	// getParents(ct, comp, contractVec);
	// }
	// }

    }

    /**
     * 
     * @param btId
     *            non-qualified id of the behavior term
     * @param sr
     * @param compType
     * @return
     */
    // DONE
    public static BehaviorTermType getBTFromSR(String btnqId,
	    ContractType contractType, Composition comp) {
	/*
	 * TODO List<BehaviorTermType> behaviorTermTypeList=
	 * contractType.getBehaviorTerms().getBehaviorTerm();
	 * for(BehaviorTermType btt: behaviorTermTypeList){
	 * if(btt.getId().equals(btnqId)){ return btt; } }
	 * 
	 * return null;
	 */
	return null;
    }

    /**
     * 
     * @param fqBTids
     *            full qualifieds BT ids
     * @param comp
     * @return
     */
    // DONE
    public static BehaviorTermType[] getBehaviorTermsForBTIDs(String[] btids,
	    Composition comp) {
	Vector<BehaviorTermType> btVec = new Vector<BehaviorTermType>();

	//
	BehaviorTermType[] btArray = new BehaviorTermType[btVec.size()];
	return (BehaviorTermType[]) btVec.toArray(btArray);
    }

    /**
     * Given a fully qualified BT id, get the behavior term with the defined one
     * 
     * @param fqBtID
     * @param compType
     * @return
     */
    // DONE
    public static BTScope getBTWithSRForBTId(String fqBtID, Composition comp) {

	String[] split = fqBtID.split(Constants.SERENDIP_PATH_SEP);
	String srLocalityId = split[0];
	String btnqId = split[1];

	// First check the given btId is in the child SR.
	ContractType applicableSR = getContractType(srLocalityId, comp);
	BehaviorTermType btType = getBTFromSR(btnqId, applicableSR, comp);

	if (null != btType) {
	    // If the btid is in the child SR we add the behavior term
	    return new BTScope(btType, applicableSR, applicableSR);
	} else {
	    // If not, we need to look thru the parent SR hierarchy.
	    Vector<ContractType> contractVec = new Vector<ContractType>();
	    getParents(applicableSR, comp, contractVec);
	    for (int j = 0; j < contractVec.size(); j++) {
		ContractType ancestorSR = contractVec.get(j);
		btType = getBTFromSR(btnqId, ancestorSR, comp);

		if (null == btType) {
		    // Error
		    logger.error(fqBtID + " cannot be found");
		} else {
		    return new BTScope(btType, ancestorSR, applicableSR);
		}
	    }

	}

	return null;
    }

    /**
     * return all the constriant strings for the given behavior terms
     * 
     * @param bts
     * @return
     */
    public static ConstraintType[] getAllConstriantsForBTs(
	    BehaviorTermType[] bts) {
	Vector<ConstraintType> constriantsVec = new Vector<ConstraintType>();
	for (int i = 0; i < bts.length; i++) {
	    ConstraintsType constraintsType = bts[i].getConstraints();
	    if (null != constraintsType) {
		constriantsVec.addAll(constraintsType.getConstraint());
	    }
	}

	ConstraintType[] constraintsArr = new ConstraintType[constriantsVec
		.size()];
	return (ConstraintType[]) constriantsVec.toArray(constraintsArr);
    }

    /**
     * Return a BehaviorTermType for a given id
     * 
     * @param id
     * @param composition
     * @return
     */
    public static BehaviorTermType getBehaviorTermType(String id,
	    Composite composite) {
	BehaviorTermsType behaviorTermsTye = composite.getSmcBinding()
		.getBehaviorTerms();
	if (null != behaviorTermsTye) {
	    List<BehaviorTermType> bts = behaviorTermsTye.getBehaviorTerm();
	    for (BehaviorTermType bt : bts) {
		if (bt.getId().equals(id)) {
		    return bt;
		}
	    }
	}

	return null;
    }

    public static Map<String, String> getTaskRuleMapping(Composite composite) {
	Map<String, String> trMap = new HashMap<String, String>();

	Roles rolesType = composite.getSmcBinding().getRoles();
	if (null != rolesType) {
	    for (RoleType roleType : rolesType.getRole()) {
		String roleId = roleType.getId();
		TasksType tasksType = roleType.getTasks();
		if (null != tasksType) {
		    for (TaskType taskType : tasksType.getTask()) {
			String taskId = taskType.getId();
			// Construct src msg
			String srcMsgStr = "";
			int index = 0;// to avoid comma at the beginning
			SrcMsgsType srcMsgsType = taskType.getSrcMsgs();
			if (null != srcMsgsType) {

			    for (SrcMsgType srcMsgType : srcMsgsType
				    .getSrcMsg()) {
				String contractId = srcMsgType.getContractId();
				String termId = srcMsgType.getTermId();
				String isReponse = (srcMsgType.isIsResponse()) ? "res"
					: "req";
				if (index > 0) {
				    srcMsgStr += ", ";
				}
				index++;
				srcMsgStr += contractId + "_" + termId + "_"
					+ isReponse;
			    }
			}

			index = 0;// reset for next iteration
			// Construct result msg
			ResultMsgsType resultMsgsType = taskType
				.getResultMsgs();
			String resultMsgStr = "";
			if (null != resultMsgsType) {
			    for (ResultMsgType resultMsgType : resultMsgsType
				    .getResultMsg()) {
				String contractId = resultMsgType
					.getContractId();
				String termId = resultMsgType.getTermId();
				String isReponse = (resultMsgType
					.isIsResponse()) ? "res" : "req";
				if (index > 0) {
				    resultMsgStr += ", ";

				}
				index++;
				resultMsgStr += contractId + "_" + termId + "_"
					+ isReponse;
			    }

			}

			trMap.put(roleId + "." + taskId, "(" + srcMsgStr
				+ ")->(" + resultMsgStr + ")");
		    }
		}
	    }
	}
	return trMap;
    }
}
