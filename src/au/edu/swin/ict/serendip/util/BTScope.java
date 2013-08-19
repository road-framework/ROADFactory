package au.edu.swin.ict.serendip.util;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.ContractType;

/**
 * Defines the scope of a Behavior Term
 * 
 * @author Malinda Kapuruge
 * 
 */
public class BTScope {
    private BehaviorTermType btType = null;
    private ContractType definedInContractType = null;
    private ContractType applicableContractType = null;

    /**
     * 
     * @param btType
     *            the behavior term type
     * @param definedInContractType
     *            the SR that the @bt is defined in
     * @param applicableSrType
     *            the SR that the @bt is referenced
     */
    public BTScope(BehaviorTermType btType, ContractType definedInContractType,
	    ContractType applicableContractType) {
	super();
	this.btType = btType;
	this.definedInContractType = definedInContractType;
	this.applicableContractType = applicableContractType;
    }

    public BehaviorTermType getBtType() {
	return btType;
    }

    public void setBtType(BehaviorTermType btType) {
	this.btType = btType;
    }

    public ContractType getDefinedInContractType() {
	return definedInContractType;
    }

    public void setDefinedInContractType(ContractType definedInSrType) {
	this.definedInContractType = definedInSrType;
    }

    public ContractType getApplicableContractType() {
	return applicableContractType;
    }

    public void setApplicableContractType(ContractType applicableSrType) {
	this.applicableContractType = applicableSrType;
    }

}
