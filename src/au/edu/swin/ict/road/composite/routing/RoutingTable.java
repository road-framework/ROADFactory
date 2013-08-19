package au.edu.swin.ict.road.composite.routing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.routing.exceptions.MessageRoutingException;
import au.edu.swin.ict.road.composite.routing.exceptions.MessageRoutingUndeterminedException;
import au.edu.swin.ict.road.composite.routing.exceptions.NoRequestException;
import au.edu.swin.ict.road.composite.rules.IRoutingRules;
import au.edu.swin.ict.road.composite.rules.drools.DroolsRoutingRulesImpl;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

/**
 * Class that acts as a routing table for each role. Each role has a routing
 * table associated with it, and the routing table consists of two separate
 * tables for request and response. The request table would always lists all the
 * message signatures available for the role to invoke, as well as the
 * corresponding contract. The response table contains all the message
 * signatures for the role to respond. In the case of multiple messages with the
 * same signatures, the role would respond to a list of contract in the response
 * table in a first-come-first-served basis.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class RoutingTable {
    private static Logger log = Logger.getLogger(RoutingTable.class.getName());
    private Map<String, List<Contract>> requestTable;
    private Map<String, Queue<Contract>> responseTable;
    private IRoutingRules routingRules;
    private Role role;
    // Used to sequentially select a contract from adjoining contracts. @see
    // mthod getAlternativeContractForRole. Strictly NO other use.
    private static int sequenceIndex = 0;

    /**
     * Default constructor for the routing table.
     * 
     * @param tableOwner
     *            the Role which this routing table is associated with.
     */
    public RoutingTable(Role tableOwner) {
	role = tableOwner;
	requestTable = new HashMap<String, List<Contract>>();
	responseTable = new HashMap<String, Queue<Contract>>();
	routingRules = null;
    }

    /**
     * Procedure to insert the destination contract to the routing tables. This
     * procedure will insert the message signature and the associated contract
     * to the <b>request</b> table only. If the message signature exists, then
     * add the contract to the list of contract with the same signature.
     * 
     * @param msgSig
     *            the message signature.
     * @param contract
     *            the new contract associated with the signature.
     */
    public void putRequestSignature(String msgSig, Contract contract) {
	List<Contract> contractList = null;

	// Message signature already in the request table
	// Retrieve the list of contract with this message signature
	if (requestTable.containsKey(msgSig))
	    contractList = requestTable.get(msgSig);

	// If no contract with the message signature, create a new list
	if (contractList == null)
	    contractList = new ArrayList<Contract>();

	// Add the contract to the list and put inside request table
	contractList.add(contract);
	requestTable.put(msgSig, contractList);
    }

    /**
     * Procedure to put the response signature into the routing tables. This
     * procedure will insert the message signature and the associated contract
     * to the <b>response</b> table only. If the message signature exists, then
     * the new contract will be added to the list of contract with the same
     * signature. <br/>
     * At this moment, multiple request messages would overwrite the response
     * messages with the latest one.
     * 
     * @param msgSig
     *            the message signature.
     * @param contract
     *            the new contract associated with the signature.
     */
    public void putResponseSignature(String msgSig, Contract contract) {
	Queue<Contract> contractQueue = null;

	if (responseTable.containsKey(msgSig))
	    contractQueue = responseTable.get(msgSig);

	if (contractQueue == null)
	    contractQueue = new LinkedList<Contract>();

	if (contract != null)
	    contractQueue.add(contract);

	responseTable.put(msgSig, contractQueue);
    }

    /**
     * Function to check whether a message signature is on the response table or
     * not. If it is not on the table, then the operation is a void type.
     * 
     * @param msgSig
     *            the message signature to check.
     * @return whether the message signature is on the response table or not.
     */
    public boolean isResponse(String msgSig) {
	return responseTable.containsKey(msgSig);
    }

    /**
     * Function to retrieve the destination contract from the routing table
     * based on the message wrapper. It will retrieve the contract from the
     * request table, and if it is a response message, it will retrieve it from
     * the response table. In the case if the contract is still not found in
     * both tables, or if there is more than one contract with the same
     * signature, it will execute the routing rules. <br/>
     * In the event when the role tried to send a response message without an
     * earlier request, it would throw an exception and the message would be
     * blocked.
     * 
     * @param wrapper
     *            the message wrapper.
     * @param executeRules
     *            boolean value whether to execute the rules or not.
     * @return the contract associated with the message.
     * @throws MessageRoutingException
     *             when the routing rules failed to fire.
     * @throws MessageRoutingUndeterminedException
     *             when the contract is not found.
     * @throws NoRequestException
     *             when the role tried to response without a request.
     */
    public Contract getDestinationContract(MessageWrapper wrapper,
	    boolean executeRules) throws MessageRoutingException,
	    MessageRoutingUndeterminedException, NoRequestException {
	Contract contract = null;
	boolean isRandomSelectionOK = true;// TEST
	try {
	    // Retrieve the destination contract from the tables
	    contract = this.getDestinationContract(wrapper.getOperationName(),
		    wrapper.isResponse());
	    // TEST:get a random contract if Not found
	    if (isRandomSelectionOK && contract == null) {
		log.info("Allocating a rand contract");
		// contract =
		// this.getRandomContractForRole(wrapper.getOriginRole().getComposite(),
		// wrapper.getOriginRole().getId());
		contract = this.getAlternativeContractForRole(wrapper
			.getOriginRole().getComposite(), wrapper
			.getOriginRole().getId());
	    }

	    // If no contract found and role still has contracts, execute the
	    // routing rules to determine the contract
	    if (contract == null && executeRules
		    && role.getAllContracts() != null) {
		contract = routingRules.executeRoutingRules(wrapper,
			Arrays.asList(role.getAllContracts()));
	    }
	    // If found then put into the wrapper, if not throw an exception
	    if (contract != null) {
		wrapper.setDestinationContract(contract);
	    } else if (contract == null && wrapper.isResponse()) { // If
								   // responding
		// without a
		// request
		throw new NoRequestException(
			"Cannot find request message to respond from role "
				+ role.getId());
	    } else {
		throw new MessageRoutingUndeterminedException(wrapper,
			"Cannot find contract for operation "
				+ wrapper.getOperationName() + " from role "
				+ wrapper.getOriginRoleId());
	    }
	} catch (RulesException e) {
	    throw new MessageRoutingException(e.getMessage());
	}
	return contract;
    }

    /**
     * Procedure to reload the routing rules from a file.
     * 
     * @param ruleFile
     *            the file location of the routing rules.
     * @throws RulesException
     */
    public void reloadRules(String ruleFile) throws RulesException {
	this.loadRules(ruleFile);
    }

    /**
     * Procedure to load the routing rules from a file.
     * 
     * @param ruleFile
     *            the file location of the routing rules.
     * @throws RulesException
     */
    public void loadRules(String ruleFile) throws RulesException {
	log.info("Start loading routing rules " + ruleFile + " for role "
		+ role.getId());
	routingRules = new DroolsRoutingRulesImpl(ruleFile);
	log.info("Finished loading routing rules from disk");
    }

    /**
     * Function to remove the contract based on message signature given. The
     * contract will be permanently removed from the routing table of the role.
     * It retrieves the contract list based on the message signature and then
     * proceeds to remove the contract. If the contract list is then empty, then
     * all instances of the message signature is then removed.
     * 
     * @param msgSignature
     *            the message signature of the Contract.
     * @param contract
     *            the Contract to be removed.
     * @return boolean condition whether the removal successful or not.
     */
    public boolean removeContract(String msgSignature, Contract contract) {
	List<Contract> contractList = requestTable.get(msgSignature);
	boolean removed = false;
	removed = contractList.remove(contract);

	if (contractList.isEmpty())
	    requestTable.remove(msgSignature);
	else
	    requestTable.put(msgSignature, contractList);

	return removed;
    }

    /**
     * Function to remove the contract from the routing table based on the
     * contract id. This function calls the remove contract function by the
     * contract object.
     * 
     * @param contractId
     *            the contract id to be removed.
     * @return boolean condition whether the removal successful or not.
     */
    public boolean removeContract(String contractId) {
	for (Contract c : role.getAllContracts()) {
	    if (c.getId().equals(contractId)) {
		return this.removeContract(c);
	    }
	}
	return false;
    }

    /**
     * Function to remove the contract from the routing table based on the
     * contract object. It retrieves the list of contracts based on every
     * message signature and then remove the corresponding contract. If there
     * are no other contract remaining, then the message signature is also
     * removed from the contract. This function only removes from the request
     * table, as the removal from the response table is automatic.
     * 
     * @param contract
     *            the contract object to be removed.
     * @return boolean condition whether the removal successful or not.
     */
    public boolean removeContract(Contract contract) {
	for (String opName : contract.getAllOperationNames()) {
	    List<Contract> listContract = requestTable.get(opName);
	    if (listContract != null) {
		listContract.remove(contract);
		if (listContract.isEmpty())
		    requestTable.remove(opName);
	    }
	}
	return true;
    }

    public boolean removeOperationName(String operationName, Term term,
	    Contract contract) {
	boolean result = false;
	if (isRequestor(term, contract)) {
	    if (requestTable.containsKey(operationName)) {
		List<Contract> requestContractList = requestTable
			.get(operationName);
		requestContractList.remove(contract);
		if (requestContractList.isEmpty())
		    requestTable.remove(operationName);

		result = true;
	    }
	}

	return result;
    }

    public void addOperationName(Operation newOperation, Term term,
	    Contract contract) {
	// delete the old operation if required
	if (term.getOperation() != null) {
	    removeOperationName(term.getOperation().getName(), term, contract);
	    term.setOperation(null);
	}

	/** do the request table **/
	if (isRequestor(term, contract)) {
	    // get list of contracts if an operation of this name already exists
	    List<Contract> requestContracts = new ArrayList<Contract>();
	    if (requestTable.containsKey(newOperation.getName())) {
		requestContracts
			.addAll(requestTable.get(newOperation.getName()));
	    }

	    requestContracts.add(contract);
	    requestTable.put(newOperation.getName(), requestContracts);
	} else if (isResponder(newOperation, term, contract)) {
	    /** do the response table **/
	    responseTable.put(newOperation.getName(),
		    new LinkedList<Contract>());
	}
    }

    /**
     * Print the current request table
     */
    public void print() {
	log.debug("Request table:");
	for (String msgSig : requestTable.keySet()) {
	    log.debug(msgSig + " [");
	    for (Contract c : requestTable.get(msgSig)) {
		log.debug(c.getId() + ",");
	    }
	    log.debug("]");
	}
    }

    private boolean isRequestor(Term term, Contract contract) {
	boolean result = false;
	if (term.getDirection().equalsIgnoreCase("AtoB")) {
	    if (contract.getRoleA() == role)
		result = true;
	} else if (term.getDirection().equalsIgnoreCase("BtoA")) {
	    if (contract.getRoleB() == role)
		result = true;
	}

	return result;
    }

    private boolean isResponder(Operation newOperation, Term term,
	    Contract contract) {
	boolean result = false;
	if (!newOperation.getReturnType().equals("void")) {
	    if (term.getDirection().equalsIgnoreCase("AtoB")) {
		if (contract.getRoleB() == role)
		    result = true;
	    } else if (term.getDirection().equalsIgnoreCase("BtoA")) {
		if (contract.getRoleA() == role)
		    result = true;
	    }
	}

	return result;
    }

    /**
     * Function that retrieves the destination contract from both the tables. It
     * will check on request table if it is a request, and will check on
     * response table if it is a response. If there is no contract, or if there
     * are more than one contract, it would return null.
     * 
     * @param msgSig
     *            the message signature.
     * @param isResponse
     *            boolean whether the message is a response or not.
     * @return the contract associated with the message signature.
     */
    private Contract getDestinationContract(String msgSig, boolean isResponse) {
	Contract contract = null;

	// If not a response message then retrieve from request table
	if (!isResponse) {
	    List<Contract> contractList;

	    // Check if the message signature exists in the request table
	    // If it doesn't then the role is trying to invoke an operation
	    // improperly (flagging a response message as a request)
	    if (requestTable.containsKey(msgSig)) {
		contractList = requestTable.get(msgSig);

		// If there is only one contract then return that contract
		// Otherwise fire the routing rules in the caller function
		if (contractList.size() == 1)
		    contract = contractList.get(0);
	    }
	} else {
	    Queue<Contract> contractQueue;

	    // Check if the message signature exists in the response table
	    // If it doesn't then the role is trying to invoke an operation
	    // with a void response type
	    if (responseTable.containsKey(msgSig)) {
		contractQueue = responseTable.get(msgSig);

		// Retrieve the first contract in the queue
		contract = contractQueue.poll();

		// Replacing the queue in the response table
		responseTable.put(msgSig, contractQueue);
	    }
	}

	return contract;
    }

    // The function to select a random contract
    public Contract getRandomContractForRole(Composite comp, String roleId) {
	IRole role = comp.getRoleByID(roleId);
	Contract[] allBoundContracts = role.getAllContracts();
	Random generator = new Random();
	Contract randContract = allBoundContracts[generator
		.nextInt(allBoundContracts.length)];
	return randContract;
    }

    public Contract getAlternativeContractForRole(Composite comp, String roleId) {
	IRole role = comp.getRoleByID(roleId);
	Contract[] allBoundContracts = role.getAllContracts();
	sequenceIndex++;// Increment sequence index
	int index = sequenceIndex % allBoundContracts.length;
	Contract randContract = allBoundContracts[index];
	return randContract;
    }
}
