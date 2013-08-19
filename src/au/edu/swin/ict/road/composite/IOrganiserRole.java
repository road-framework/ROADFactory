package au.edu.swin.ict.road.composite;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.regulator.FactObject;
import au.edu.swin.ict.serendip.core.mgmt.SerendipOrganizer;

import java.util.concurrent.TimeUnit;

/**
 * <code>IOrganiser</code>. is an interface to be used by players of the
 * <code>Composite</code> organiser role. IOrganiser contains (or will contain
 * as development continues) all the methods required to configure a composites
 * contracts and structure.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public interface IOrganiserRole extends SerendipOrganizer {

    /**
     * Returns the next MessageWrapper in the organisers message queue. The
     * MessageWrapper should contain a management related message sent from one
     * of the roles inside the <code>Composite</code>.
     * 
     * @return the next <code>MessageWrapper</code> in the organisers queue.
     */
    public MessageWrapper getNextManagementMessage();

    public MessageWrapper getNextManagementMessage(long timeout, TimeUnit unit);

    /**
     * Allows the organiser to send management related messages to specific
     * roles inside a standard <code>MessageWrapper</code>. If the specified
     * recipient role does not exist the method return <code>false</code>, or
     * <code>true</code> otherwise.
     * 
     * @param msg
     *            the <code>MessageWrapper</code> to send.
     * @param destinationRoleId
     *            the recipient roles unique id.
     * @return <code>true</code> if recipient role exists, <code>false</code> if
     *         not.
     */
    public OrganiserOperationResult sendManagementMessage(MessageWrapper msg,
	    String destinationRoleId);

    /**
     * Adds a new <code>Role</code> to the <code>Composite</code>.
     * 
     * @param newRole
     *            the new role to add.
     */
    public OrganiserOperationResult addNewRole(String id, String name,
	    String description);

    /**
     * Function to remove a <code>Role</code> from the composite.
     * 
     * @param roleId
     *            the id of the <code>Role</code> to be removed as
     *            <code>String</code>.
     * @return <code>true</code> if removed, <code>false</code> otherwise.
     */
    public OrganiserOperationResult removeRole(String roleId);

    /**
     * Adds a new <code>Contract</code> to the <code>Composite</code>. The two
     * roles to bind to the new <code>Contract</code> must already exist inside
     * the <code>Composite</code>.
     * 
     * @param newContract
     *            the new contract to be added.
     * @param roleAId
     *            the unique id of role A.
     * @param roleBId
     *            the unique id of role B.
     * @throws ConsistencyViolationException
     *             if role unique id's do not exist for any roles currently in
     *             the <code>Composite</code>.
     */
    OrganiserOperationResult addNewContract(String id, String name,
	    String description, String state, String type, String ruleFile,
	    boolean isAbstract, String roleAId, String roleBId);

    /**
     * Function to remove a <code>Contract</code> from the composite.
     * 
     * @param contractId
     *            contract id for the <code>Contract</code> to be removed.
     * @return <code>true</code> if removed, <code>false</code> otherwise.
     */
    public OrganiserOperationResult removeContract(String contractId);

    /**
     * Procedure to add a new <code>Term</code> in a <code>Contract</code>.
     * Hierarchically, a <code>Term</code> should be contained in a
     * <code>Contract</code>.
     * 
     * @param newTerm
     *            the new <code>Term</code> to be added.
     * @param contractId
     *            the contract id as <code>String</code> in which the
     *            <code>Term</code> is added to.
     */
    public OrganiserOperationResult addNewTerm(String id, String name,
	    String messageType, String deonticType, String description,
	    String direction, String contractId);

    public OrganiserOperationResult updateTerm(String id, String contractId,
	    String property, String value);

    /**
     * Function to remove a <code>Term</code> from the composite.
     * 
     * @param termId
     *            the id of the <code>Term</code> to be removed as
     *            <code>String</code>.
     * @return <code>true</code> if removed, <code>false</code> otherwise.
     */
    public OrganiserOperationResult removeTerm(String contractID, String termId);

    /**
     * Procedure to add a new <code>Operation</code> in a <code>Term</code> with
     * the term id. An <code>Operation</code> should be contained in a
     * <code>Term</code>.
     * 
     * @param newOperation
     *            the new <code>Operation</code> to be added.
     * @param termId
     *            the term id as <code>String</code> in which the
     *            <code>Operation</code> is to be added
     */
    public OrganiserOperationResult addNewOperation(String operationName,
	    String operationReturnType, Parameter[] parameters, String termId,
	    String contractId);

    public OrganiserOperationResult setOutMessageType(String deliveryType,
	    boolean isResponse, String operationName,
	    String operationReturnType, Parameter[] parameters, String tId,
	    String rId);

    public OrganiserOperationResult setInMessageType(boolean isResponse,
	    String operationName, String operationReturnType,
	    Parameter[] parameters, String tId, String rId);

    public OrganiserOperationResult removeOperation(String operationName,
	    String termId);

    /**
     * Inject a new rule which is written in Drools syntax to an existing
     * contract which has already a *.drl file specified.
     * 
     * @param newRule
     *            the rule in drools syntax as <code>String</code>
     * @param contractId
     *            the id for the <code>Contract</code> in which this rule has to
     *            be injected.
     * @return <code>true</code> if injecting the new rule was syntax error free
     *         and successful, otherwise <code>false</code>
     */
    public OrganiserOperationResult addNewContractRule(String newRule,
	    String contractId);

    /**
     * Remove a rule which is existent in a contracts drool rules
     * 
     * @param contractId
     *            the id for the contract where the new rule should be inserted.
     * @param ruleName
     *            The name of the rule which needs to be removed.
     */
    public OrganiserOperationResult removeContractRule(String contractId,
	    String ruleName);

    public OrganiserOperationResult addNewCompositeRule(String newRule);

    public OrganiserOperationResult removeCompositeRule(String ruleName);

    /**
     * Returns a reference to a <code>Contract</code> based on its unique id.
     * 
     * @param id
     *            the id of the desired contract.
     * @return the contract or false if a Contract with the specified unique id
     *         does not exist.
     */
    public Contract getContractById(String id);

    /**
     * Generates a snapshot of the current runtime composite. The snapshot
     * includes the SMC file and all the drool files related to this composite.
     * The latest rule modifications done are also included in the snapshot. The
     * snapshot is stored at a default location in the 'data' folder.
     * 
     * @return true if the snapshot generation was successful
     */
    public OrganiserOperationResult takeSnapshot();

    public OrganiserOperationResult takeSnapshotAtDir(String folder);

    // Newly added methods
    public OrganiserOperationResult changePlayerBinding(String roleId,
	    String endpoint);

    public FactObject getFact(String factType, String factIdentifierValue);

    public OrganiserOperationResult updateFact(FactObject factObject);

    public OrganiserOperationResult addFact(String factType,
	    FactObject factObject);

    public OrganiserOperationResult removeFact(String factType,
	    String factIdentifierValue);

    public String getName();

    // Player binding changes
    public OrganiserOperationResult addPlayerBinding(String pbId, String rid,
	    String endpoint);

    public OrganiserOperationResult removePlayerBinding(String pbId);

    public OrganiserOperationResult updatePlayerBinding(String pbId,
	    String property, String value);
}
