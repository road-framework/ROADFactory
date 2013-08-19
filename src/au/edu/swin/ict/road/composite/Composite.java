package au.edu.swin.ict.road.composite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import au.edu.swin.ict.road.xml.bindings.*;
import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.listeners.CompositeAddRoleListener;
import au.edu.swin.ict.road.composite.listeners.CompositeRemoveRoleListener;
import au.edu.swin.ict.road.composite.listeners.CompositeUpdateRoleListener;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.composite.player.PlayerBinding;
import au.edu.swin.ict.road.composite.rules.ICompositeRules;
import au.edu.swin.ict.road.composite.rules.drools.DroolsCompositeRulesImpl;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;
import au.edu.swin.ict.road.demarshalling.CompositeMarshalling;
import au.edu.swin.ict.road.regulator.FactObject;
import au.edu.swin.ict.road.regulator.FactTupleSpace;
import au.edu.swin.ict.road.regulator.FactTupleSpaceRow;
import au.edu.swin.ict.road.xml.bindings.FactType.Attributes;
//import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.core.ProcessInstance.status;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationScriptEngine;
import au.edu.swin.ict.serendip.core.mgmt.ProcessInstanceAdaptationEngine;
import au.edu.swin.ict.serendip.core.mgmt.action.DefAdaptAction;
import au.edu.swin.ict.serendip.core.mgmt.action.InstanceAdaptAction;
import au.edu.swin.ict.serendip.util.BenchUtil;
import au.edu.swin.ict.serendip.verficiation.SerendipVerificationException;

/**
 * Composite is an implementation of a ROAD self-managed composite or SMC. A
 * ROAD Composite is a collection of roles bound by contracts. Roles are
 * intended to be interacted with by players. Each composite has an organiser
 * role which allows reconfiguration of the structure. Composite implements
 * Runnable and is intended to be run in its own thread. The run() method starts
 * a series of threads called MessageDeliverer, each of which run in their own
 * thread and shuttle messages between roles, execute evaluation of contracts,
 * and so on. Composite requires a JAXB binding class to be instantiated, which
 * represents the XML design time representation of the composite.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class Composite implements Runnable {

    private static Logger log = Logger.getLogger(Composite.class.getName());
    private SMC smcBinding;
    private BenchUtil benchUtil = null;

    public SMC getSmcBinding() {
	return smcBinding;
    }

    private String description;
    private String name;
    private Map<String, Role> roleMap; // all this composites roles
    private Map<String, Contract> contractMap; // all this composites contracts
    private Map<String, PlayerBinding> playerBindingMap; // all the player
							 // bindings

    // player bindings
    private List<MessageDeliverer> workerList; // the current road workers
    // created for each role
    private IOrganiserRole organiserRole;
    private Object shutdownMutex;
    private String rulesDir; // general directory for rules
    private String transDir; // general directory for transformations
    private String routingRulesFileName; // routing rule file
    private String compositeRulesFileName; // composite rule file
    private ICompositeRules compositeRules; // composite level rule engine
    private List<CompositeAddRoleListener> addRoleListenerList;
    private List<CompositeRemoveRoleListener> removeRoleListenerList;
    private List<CompositeUpdateRoleListener> updateRoleListenerList;
    private List<RuleChangeTracker> ruleChangeTrackerList = new ArrayList<RuleChangeTracker>(); // Attribute
    private Map<String, String> compositeProperties = new HashMap<String, String>();

    private ROADDeploymentEnv roadDepEnv = null;
    // Serendip
    private SerendipEngine serendipEngine = null;

    // Fact Tuple Space
    private FactTupleSpace FTS;

    /**
     * Initializes a Composite using the supplied SMC, which is a JAXB binding
     * class containing properties from an XML document. Calling this
     * constructor does not *start* the Composite i.e. messages will not be
     * routed and contracts will not be evaluated. This is done using the run()
     * method which is intended to be called using a Thread class.
     * 
     * @param smcBinding
     *            the JAXB binding class whose contents will be used to
     *            instantiate the Composite.
     * @throws ConsistencyViolationException
     *             if composite contains invalid or inconsistent values, e.g. a
     *             contract without two roles attached, which is not allowed in
     *             the ROAD meta-model.
     * @throws CompositeInstantiationException
     *             if an error occurs attempting to instantiate the Composite
     *             using the supplied JAXB binding or when attempting to read
     *             the rules files.
     */
    public Composite(SMC smcBinding) throws ConsistencyViolationException,
	    CompositeInstantiationException {
	this.smcBinding = smcBinding;
	description = null;
	roleMap = new HashMap<String, Role>();
	contractMap = new HashMap<String, Contract>();
	playerBindingMap = new HashMap<String, PlayerBinding>();
	workerList = new ArrayList<MessageDeliverer>();
	shutdownMutex = new Object();
	organiserRole = new OrganiserRole();
	rulesDir = null;
	routingRulesFileName = null;
	compositeRulesFileName = null;
	compositeRules = null;
	addRoleListenerList = new ArrayList<CompositeAddRoleListener>();
	removeRoleListenerList = new ArrayList<CompositeRemoveRoleListener>();
	updateRoleListenerList = new ArrayList<CompositeUpdateRoleListener>();

	this.benchUtil = new BenchUtil(this.smcBinding.getName());
	// Fact Tuple Space
	FTS = new FactTupleSpace();

	extractFileFolderPaths(smcBinding); // need to extract folder and file
	// paths first for composite rules
	// to be loaded
	try {
	    compositeRules = this.loadCompositeRules(compositeRulesFileName); // load
	} catch (CompositeInstantiationException e) {
	    e.printStackTrace();
	    throw new CompositeInstantiationException(
		    "Problem loading composite rules " + compositeRulesFileName);
	}
	// composite
	// rules

	extractData(smcBinding); // extract all other data, may throw composite

	// Here we create the process engine instance
	try {
	    this.serendipEngine = new SerendipEngine(this);
	} catch (SerendipException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// Then we subscribe all the roles of the composite to the engine.
	// In this way the role gets notified when a task needs to be performed
	for (Role r : this.roleMap.values()) {
	    this.serendipEngine.subscribe(r);
	}

	log.info("Deployment complete");
    }

    /**
     * Starts a Composite by starting all MessageDeliverer's (worker threads)
     * which are responsible for routing messages and executing evaluations of
     * contracts.
     */
    public void run() {
	for (MessageDeliverer rw : workerList) {
	    rw.start();
	}
	log.info("Composite is now running...");
	this.benchUtil.addBenchRecord("COMP_START", this.name);
	try {
	    synchronized (shutdownMutex) {
		shutdownMutex.wait();
	    }
	} catch (InterruptedException e) {
	    log.fatal(e.getMessage());
	}

	for (MessageDeliverer rw : workerList) {
	    try {
		rw.join();
	    } catch (InterruptedException e) {
		log.fatal(e.getMessage());
	    }
	}
	this.benchUtil.addBenchRecord("COMP_STOP", this.name);
	this.benchUtil.finalize();
	log.info("All MessageDeliverers now terminated");
	log.info("Composite shutdown complete, Adios!");
    }

    /**
     * Shutdowns the <code>Composite</code> safely by allowing any RoadWorkers
     * to finish routing any messages they have already started. Note that any
     * messages still in queues waiting to be routed will remain in those queues
     * until the <code>Composite</code> is started again.
     */
    public void stop() {
	synchronized (shutdownMutex) {
	    log.info("Beginning composite shutdown");
	    log.info("Requesting all MessageDeliverers finish current jobs and terminate");

	    // ask all workers to terminate
	    for (MessageDeliverer rw : workerList) {

		rw.setTerminate(true);
	    }
	    log.info("Requesting all Contracts to release resources");
	    for (String cId : this.contractMap.keySet()) {
		this.contractMap.get(cId).terminate();
	    }

	    shutdownMutex.notify();
	}
    }

    public ROADDeploymentEnv getRoadDepEnv() {
	return roadDepEnv;
    }

    public void setRoadDepEnv(ROADDeploymentEnv roadDepEnv) {
	this.roadDepEnv = roadDepEnv;
    }

    /**
     * Registers a new CompositeAddRoleListener to receive events when new roles
     * are added to the Composite by the organiser.
     * <p>
     * This is intended to be used by deployment environments or other things
     * that need to act when something changes, e.g. updating a WSDL document in
     * the case of ROAD4WS.
     * 
     * @param listener
     *            the new CompositeAddRoleListener wishing to receive events.
     */
    public void addCompositeAddRoleListner(CompositeAddRoleListener listener) {
	this.addRoleListenerList.add(listener);
    }

    /**
     * Removes a CompositeAddRoleListener from the registered event listeners.
     * 
     * @param listener
     *            the CompositeAddRoleListener to be removed.
     */
    public void removeCompositeAddRoleListner(CompositeAddRoleListener listener) {
	this.addRoleListenerList.remove(listener);
    }

    /**
     * Registers a new CompositeRemoveRoleListener to receive events when roles
     * are removed from the Composite by the organiser.
     * <p>
     * This is intended to be used by deployment environments or other things
     * that need to act when something changes, e.g. updating a WSDL document in
     * the case of ROAD4WS.
     * 
     * @param listener
     *            the new CompositeRemoveRoleListener wishing to receive events.
     */
    public void addCompositerRemoveRoleListner(
	    CompositeRemoveRoleListener listener) {
	this.removeRoleListenerList.add(listener);
    }

    /**
     * Removes a CompositeRemoveRoleListener from the registered event
     * listeners.
     * 
     * @param listener
     *            the CompositeRemoveRoleListener to be removed.
     */
    public void removeCompositerRemoveRoleListner(
	    CompositeRemoveRoleListener listener) {
	this.removeRoleListenerList.remove(listener);
    }

    /**
     * Registers a new CompositeUpdateRoleListener to receive events when roles
     * are changed in Composite by the organiser as a result of change in one of
     * its binded contracts, e.g. a term being added or removed.
     * <p>
     * This is intended to be used by deployment environments or other things
     * that need to act when something changes, e.g. updating a WSDL document in
     * the case of ROAD4WS.
     * 
     * @param listener
     *            the new CompositeUpdateRoleListener wishing to receive events.
     */
    public void addCompositeUpdateRoleListner(
	    CompositeUpdateRoleListener listener) {
	this.updateRoleListenerList.add(listener);
    }

    /**
     * Removes a CompositeUpdateRoleListener from the registered event
     * listeners.
     * 
     * @param listener
     *            the CompositeUpdateRoleListener to be removed.
     */
    public void removeCompositeUpdateRoleListner(
	    CompositeUpdateRoleListener listener) {
	this.updateRoleListenerList.remove(listener);
    }

    /**
     * Returns a list containing all the Composites roles (as IRole interfaces).
     * 
     * @return the list of roles.
     */
    public List<IRole> getCompositeRoles() {
	ArrayList<IRole> list = new ArrayList<IRole>();
	list.addAll(this.roleMap.values());

	return list;
    }

    /**
     * Returns the organiser role for this Composite as an IOrganiser interface.
     * 
     * @return the organiser role for this Composite.
     */
    public IOrganiserRole getOrganiserRole() {
	return organiserRole;
    }

    /**
     * Returns the description of the Composite.
     * 
     * @return the description of the Composite.
     */
    public String getDescription() {
	return description;
    }

    /**
     * Returns the name of the Composite.
     * 
     * @return the name of the Composite.
     */
    public String getName() {
	return name;
    }

    /**
     * Returns the rulesDir of the Composite.
     * 
     * @return the rulesDir of the Composite.
     */
    public String getRulesDir() {
	return rulesDir;
    }

    /**
     * Returns the directory which holds the transformations of this Composite.
     * 
     * @return the transDir of the Composite.
     */
    public String getTransDir() {
	return transDir;
    }

    /**
     * Returns the routingRulesFileName of the Composite.
     * 
     * @return the routingRulesFileName of the Composite.
     */
    public String getRoutingRulesFileName() {
	return routingRulesFileName;
    }

    /**
     * Returns the compositeRulesFileName of the Composite.
     * 
     * @return the compositeRulesFileName of the Composite.
     */
    public String getCompositeRulesFileName() {
	return compositeRulesFileName;
    }

    /**
     * Returns a role (as IRole) that contains the specified unique id.
     * 
     * @return the role or null if no role exists with the specified id.
     */
    public IRole getRoleByID(String roleId) {
	return roleMap.get(roleId);
    }

    /**
     * Returns a string containing the <code>Composites</code> <code>name</code>
     * and <code>description</code> in the format 'Composite name: blah.
     * Composite description: blah'.
     * 
     * @return the description string.
     */
    public String toString() {
	return "Composite name: " + this.getName()
		+ ". Composite description: " + this.getDescription();
    }

    /**
     * Sends notifications to all CompositeAddRoleListeners.
     * 
     * @param newRole
     *            the new role added.
     */
    private void notifyAddRoleListeners(IRole newRole) {
	for (CompositeAddRoleListener l : addRoleListenerList) {
	    l.roleAdded(newRole);
	}
    }

    /**
     * Sends notifications to all CompositeRemoveRoleListeners.
     * 
     * @param removedRole
     *            the id of the role that was removed.
     */
    private void notifyRemoveRoleListeners(IRole removedRole) {
	for (CompositeRemoveRoleListener l : removeRoleListenerList) {
	    l.roleRemoved(removedRole);
	}
    }

    /**
     * Sends notifications to all CompositeUpdateRoleListeners.
     * 
     * @param updatedRole
     *            the role which was updated.
     */
    private void notifyUpdateRoleListeners(IRole updatedRole) {
	for (CompositeUpdateRoleListener l : updateRoleListenerList) {
	    l.roleUpdated(updatedRole);
	}
    }

    /**
     * Loads up the Drools rules associated with this <code>Composite</code>
     * from the disk. Rules are assumed to be in the location specifed by the
     * properties file in the format 'composite''RULE_FILE_EXTENSION'. Generally
     * RULE_FILE_EXTENSION is the default '.drl'.
     * 
     * @return the <code>DroolsCompositeRulesImpl</code> containing the loaded
     *         drools rules.
     * @throws CompositeInstantiationException
     *             if the sought after rules file does not exist or the rules
     *             are invalid.
     */
    private DroolsCompositeRulesImpl loadCompositeRules(String ruleFile)
	    throws CompositeInstantiationException {
	DroolsCompositeRulesImpl rules = null;
	try {
	    rules = new DroolsCompositeRulesImpl(rulesDir + ruleFile,
		    (IInternalOrganiserView) organiserRole);
	} catch (RulesException e) {
	    throw new CompositeInstantiationException(e.getMessage());
	}

	return rules;
    }

    /**
     * Procedure to add the contract to the routing table of each roles.
     * 
     * @param a
     *            role A.
     * @param b
     *            role B.
     * @param c
     *            the contract to be added.
     */
    private void addToRoutingTable(Role a, Role b, Contract c) {
	// Retrieves a list of terms from the contract
	List<Term> listTerms = c.getTermList();
	for (Term t : listTerms) {
	    // Putting the message signature dependent on the message
	    // destination
	    if (t.getDirection().equalsIgnoreCase("atob")) {
		// Put the request signature
		a.getRoutingTable().putRequestSignature(
			t.getOperation().getName(), c);

		// If not void then put response signature
		if (!(t.getOperation().getReturnType().equalsIgnoreCase("void")))
		    b.getRoutingTable().putResponseSignature(
			    t.getOperation().getName(), null);
	    } else {
		// Put the request signature
		b.getRoutingTable().putRequestSignature(
			t.getOperation().getName(), c);

		// If not void then put response signature
		if (!(t.getOperation().getReturnType().equalsIgnoreCase("void")))
		    a.getRoutingTable().putResponseSignature(
			    t.getOperation().getName(), null);
	    }
	}
    }

    /**
     * Extracts the folder path and file names for rule files.
     * 
     * @param smcBinding
     */
    private void extractFileFolderPaths(SMC smcBinding) {
	String pathSep = System.getProperty("file.separator");

	if (smcBinding.getDataDir() != null) {
	    this.rulesDir = smcBinding.getDataDir() + pathSep + "rules"
		    + pathSep;
	    this.transDir = smcBinding.getDataDir() + pathSep + "trans"
		    + pathSep;
	}

	if (smcBinding.getRoutingRuleFile() != null) {
	    routingRulesFileName = smcBinding.getRoutingRuleFile();
	}

	if (smcBinding.getCompositeRuleFile() != null) {
	    compositeRulesFileName = smcBinding.getCompositeRuleFile();
	}
    }

    /**
     * Extracts data from the <code>SMC</code> (a JAXB binding) and populates
     * the <code>Composite</code> using it.
     * 
     * @param smcBinding
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code>.
     * @throws ConsistencyViolationException
     *             if composite contains invalid or inconsistent values, e.g. a
     *             contract without two roles attached, which is not allowed in
     *             the ROAD meta-model.
     * @throws CompositeInstantiationException
     *             if an error occurs attempting to instantiate the
     *             <code>Composite</code> using the supplied JAXB binding or
     *             when attempting to read the rules files.
     */
    private void extractData(SMC smcBinding)
	    throws ConsistencyViolationException,
	    CompositeInstantiationException {

	if (smcBinding.getDescription() != null) {
	    description = smcBinding.getDescription();
	}

	if (smcBinding.getName() != null) {
	    name = smcBinding.getName();
	}
	SMC.Facts facts = smcBinding.getFacts();
	if (facts != null) {
	    extractFacts(facts);
	}
	extractRoles(smcBinding.getRoles());
	extractContracts(smcBinding.getContracts());
	extractPlayerBindings(smcBinding.getPlayerBindings());
	// extractProcessDefs(smcBinding.getProcessDefinitions());
	// TODO: Extract Serendip descriptions. No we dont do that here.
    }

    /**
     * Extracts data from the <code>PlayerBindings</code> (a JAXB binding) and
     * populates the <code>Composite</code> player bindings using it.
     * 
     * @param pBindings
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code> player bindings.
     */
    public void extractPlayerBindings(SMC.PlayerBindings pBindings) {
	if (null == pBindings) {
	    log.warn("No player bindings in the descriptor");
	    return;
	}
	List<PlayerBindingType> pBindingsTypeList = pBindings
		.getPlayerBinding();
	for (PlayerBindingType pbt : pBindingsTypeList) {
	    PlayerBinding pb = new PlayerBinding(pbt);
	    this.playerBindingMap.put(pb.getId(), pb);
	}

	populateRolesWithPlayerBindings();
    }

    /**
     * Populates the <code>Role</code> objects in this <code>Composite</code>
     * with the binding information present in the <code>PlayerBinding</code>
     * objects located inside the playerBindingMap.
     */
    private void populateRolesWithPlayerBindings() {
	Collection<PlayerBinding> pbCollection = playerBindingMap.values();
	for (PlayerBinding pb : pbCollection) {
	    updateRoleBindings(pb, true);
	}
    }

    private void updateRoleBindings(PlayerBinding pb, boolean bind) {
	Collection<Role> roleCollection = roleMap.values();
	for (Role role : roleCollection) {
	    if (pb.isBoundToRole(role.getId())) {
		if (!bind) {
		    role.unBind();
		    continue;
		}
		if (pb.isEndpointBinding()) {
		    role.bind(pb.getEndpoint());
		} else {
		    role.bind(pb.getImplementation());
		}
	    }
	}
    }

    private boolean updateTermPrivate(String id, String contractId,
	    String property, String value) {
	Contract contract = contractMap.get(contractId);
	if (contract != null) {
	    Term term = contract.getTermById(id);
	    if ("messageType".equals(property)) {
		term.setMessageType(value);
	    }
	    this.notifyUpdateRoleListeners(contract.getRoleA());
	    this.notifyUpdateRoleListeners(contract.getRoleB());

	    log.info("New term with id '" + id
		    + "' has been added to the contract with the id '"
		    + contractId + "'");
	    return true;
	} else
	    log.fatal("New term with id '" + id
		    + "' has NOT been added. Contract with the id '"
		    + contractId + "' can not be found!");

	return false;
    }

    /**
     * Extracts data from the <code>ProcessDefinitions</code> (a JAXB binding)
     * and populates the <code>Composite</code> process definitions using it.
     * 
     * @param processDefinitions
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code> process definitions.
     * @throws CompositeInstantiationException
     */
    /*
     * private void extractProcessDefs(ProcessDefinitionsType
     * processDefinitions) { // TODO Auto-generated method stub if (null ==
     * processDefinitions) {
     * log.warn("No Process Definitions in the descriptor"); return; } }
     */

    /**
     * Extracts data from the <code>Roles</code> (a JAXB binding) and populates
     * the <code>Composite</code> roles using it.
     * 
     * @param roles
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code> roles.
     * @throws CompositeInstantiationException
     */
    public void extractRoles(SMC.Roles roles)
	    throws CompositeInstantiationException {
	if (null == roles) {
	    log.warn("No roles in the descriptor");
	    return;
	}
	List<RoleType> roleTypeList = roles.getRole();
	for (RoleType r : roleTypeList) {
	    Role aRole = new Role(r);
	    this.addRole(aRole, false);
	    aRole.extractFacts(r);
	}
    }

    /**
     * Extracts data from the regulator JAXB binding and then populates the map
     * containing all the regulators for the <code>Composite</code>.
     * 
     * @param facts
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code> regulators.
     * @throws ConsistencyViolationException
     * @throws CompositeInstantiationException
     */

    public void extractFacts(SMC.Facts facts) {
	if (null == facts) {
	    return;
	}
	List<FactType> factList = facts.getFact();
	for (FactType factType : factList) {
	    FactObject factObj = new FactObject(factType.getName(),
		    factType.getIdentifier(), "unidentified");

	    if (factType.getSource().equalsIgnoreCase("external")) {
		factObj.setFactSource(FactObject.EXTERNAL_SOURCE);
	    } else {
		factObj.setFactSource(FactObject.INTERNAL_SOURCE);
	    }

	    Attributes attributes = factType.getAttributes();
	    if (attributes != null) {
		for (String attr : attributes.getAttribute())
		    factObj.setAttribute(attr, null);
	    }
	    FTS.createFactRow(factObj);
	}
	// log.info("FTS: \t" + FTS.toString());
    }

    /**
     * Extracts data from the <code>Contracts</code> (a JAXB binding) and
     * populates the <code>Composite</code> contracts using it.
     * 
     * @param contracts
     *            the JAXB binding which is used to populate the
     *            <code>Composite</code> contracts.
     */
    public void extractContracts(SMC.Contracts contracts)
	    throws ConsistencyViolationException,
	    CompositeInstantiationException {
	if (null == contracts) {
	    log.warn("No contracts in the descriptor");
	    return;
	}
	List<ContractType> contractTypeList = contracts.getContract();
	for (ContractType c : contractTypeList) {
	    Contract aCon = new Contract(c, rulesDir);
	    this.addContract(aCon, c.getRoleAID(), c.getRoleBID());
	    aCon.extractFacts(c);
	}
    }

    /**
     * Returns a boolean indicating whether a role exists in the
     * <code>Composite</code> with the specified unique id.
     * 
     * @param roleId
     *            the unique id of a role.
     * @return <code>true</code> if the role is found or <code>false</code> if
     *         not.
     */
    private boolean containsRole(String roleId) {
	return this.roleMap.containsKey(roleId);
    }

    /**
     * Adds a new role to the <code>Composite</code>. This will also create an
     * new <code>RoadWorker</code> to route messages for this role. Furthermore
     * it will load the rules for routing rules. The file is the file which was
     * retrieved from the SMC on createion of this composite.
     * 
     * @param newRole
     *            the new role to add to the <code>Composite</code>.
     * @param alreadyStarted
     *            a boolean to determine if this <code>Role</code> is being
     *            added on <code>Composite</code> startup, or at a later time by
     *            the organiser (meaning its worker thread needs to be started).
     * @throws CompositeInstantiationException
     */
    public void addRole(Role newRole, boolean alreadyStarted)
	    throws CompositeInstantiationException {
	newRole.setComposite(this);
	roleMap.put(newRole.getId(), newRole);
	log.info("Adding role with id '" + newRole.getId() + "' to composite "
		+ name);

	MessageDeliverer newWorker = new MessageDeliverer(newRole,
		compositeRules);
	workerList.add(newWorker);
	if (alreadyStarted) {
	    newWorker.start();
	}
	try {
	    newRole.getRoutingTable()
		    .loadRules(rulesDir + routingRulesFileName);
	} catch (RulesException e) {
	    log.fatal("Exception during loading rules ('" + rulesDir
		    + routingRulesFileName + "') for newly added role ("
		    + newRole.getId() + ")");
	    // throw new CompositeInstantiationException(e.getMessage());
	}
    }

    /**
     * Function to delete <code>Role</code> from the composite. It removes the
     * <code>Role</code> from the role map. Additionally it will remove all
     * contracts which were connected to this <code>Role</code>.
     * 
     * @param roleId
     *            the <code>Role</code> to be removed.
     * @return <code>true</code> if removed, <code>false</code> otherwise.
     */
    private IRole deleteRole(String roleId) {
	Role role = roleMap.get(roleId);
	if (role != null) {
	    // TODO Auto-generated method stub
	    Contract[] cArr = role.getAllContracts();
	    if (cArr != null) {
		for (Contract c : cArr) {
		    this.deleteContract(c.getId());
		}
	    }

	    roleMap.remove(role.getId());
	    log.info("Role with id '" + roleId
		    + "' has been removed from this composite.");

	    // Remove the worker
	    for (MessageDeliverer rw : this.workerList) {
		if (rw.getRole() != null) {
		    if (rw.getRole().getId().equals(role.getId())) {
			rw.setTerminate(true);
		    }
		}
	    }

	    return role;
	}
	log.fatal("Role with id '" + roleId
		+ "' can not be found in this composite.");
	return null;
    }

    /**
     * Adds a new contract to the <code>Composite</code>.
     * 
     * @param newContract
     *            the new contract to add to the <code>Composite</code>.
     * @param roleAId
     *            the unique id of role A which must already exist in the
     *            <code>Composite</code>.
     * @param roleBId
     *            the unique id of role B which must already exist in the
     *            <code>Composite</code>.
     * @throws ConsistencyViolationException
     *             if the role unique id's supplied do not belong to any role in
     *             the <code>Composite</code>.
     */
    public void addContract(Contract newContract, String roleAId, String roleBId)
	    throws ConsistencyViolationException {

	if (!(containsRole(roleAId))) {
	    throw new ConsistencyViolationException(roleAId + " in contract "
		    + newContract.getId() + " does not exist");
	}
	if (!(containsRole(roleBId))) {
	    throw new ConsistencyViolationException(roleBId + " in contract "
		    + newContract.getId() + " does not exist");
	}

	// get roles and add them as party a and b
	Role roleA = this.roleMap.get(roleAId);
	Role roleB = this.roleMap.get(roleBId);

	newContract.setRoleA(roleA);
	newContract.setRoleB(roleB);

	roleA.addContract(newContract);
	roleB.addContract(newContract);

	// adds the newContract to each role to whom it belongs
	this.addToRoutingTable(roleA, roleB, newContract);

	contractMap.put(newContract.getId(), newContract);
	newContract.setCompositeRules(this.compositeRules);

	newContract.setComposite(this);
	this.notifyUpdateRoleListeners(roleA);
	this.notifyUpdateRoleListeners(roleB);

	log.info("Adding contract " + newContract.getId() + " to composite "
		+ name);
    }

    /**
     * Function that removes a <code>Contract</code> from the composite. This
     * function by default will remove all instances of the
     * <code>Contract</code> from both <code>Role</code>s in the composite, as
     * well from the contract map.
     * 
     * @param contractId
     *            the id of the <code>Contract</code> to be removed as
     *            <code>String</code>.
     * @return <code>true</code> if removal is successful, <code>false</code>
     *         otherwise.
     */
    private boolean deleteContract(String contractId) {
	Contract contract = contractMap.get(contractId);
	if (contract != null) {
	    contract.getRoleA().removeContract(contract);
	    contract.getRoleB().removeContract(contract);
	    contract.terminate();// Terminate the contract

	    this.contractMap.remove(contractId);

	    this.notifyUpdateRoleListeners(contract.getRoleA());
	    this.notifyUpdateRoleListeners(contract.getRoleB());

	    log.info("Contract with the id '" + contractId
		    + "' has been removed from the composite");
	    return true;
	}
	log.info("Contract with the id '" + contractId
		+ "' was not removed as it could not be found");
	return false;
    }

    /**
     * Add a term to the contract with the given contract id.
     * 
     * @param id
     *            the new <code>Term</code> which will be added to the contract
     *            with the provided contract id.
     * @param contractId
     *            the contract id where the new term as to be added as
     *            <code>String</code>.
     */
    private boolean addTerm(String id, String name, String messageType,
	    String deonticType, String description, String direction,
	    String contractId) {
	Contract contract = contractMap.get(contractId);
	if (contract != null) {
	    Term newTerm = new Term(id, name, messageType, deonticType,
		    description, contract.getContractRules(), direction);
	    contract.addTerm(newTerm);

	    this.notifyUpdateRoleListeners(contract.getRoleA());
	    this.notifyUpdateRoleListeners(contract.getRoleB());

	    log.info("New term with id '" + id
		    + "' has been added to the contract with the id '"
		    + contractId + "'");
	    return true;
	} else
	    log.fatal("New term with id '" + id
		    + "' has NOT been added. Contract with the id '"
		    + contractId + "' can not be found!");

	return false;
    }

    /**
     * Function to delete <code>Term</code> from the composite. It removes the
     * <code>Term</code> from the <code>Contract</code> containing the
     * <code>Term</code>. It iterates through the <code>Contract</code>s in the
     * composite, and matching the <code>Term</code>s inside the term list to be
     * removed.
     * 
     * @param termId
     *            the id of the <code>Term</code> to be removed as
     *            <code>String</code>.
     * @return <code>true</code> if removed, <code>false</code> otherwise.
     */
    private boolean deleteTerm(String ctId, String termId) {
	Contract c = contractMap.get(ctId);
	for (Term t : c.getTermList()) {
	    if (t.getId().equals(termId)) {
		c.removeTerm(t);

		this.notifyUpdateRoleListeners(c.getRoleA());
		this.notifyUpdateRoleListeners(c.getRoleB());

		log.info("The term with the id '" + termId
			+ "' has been removed from the contract with the id '"
			+ c.getId() + "'.");
		return true;
	    }
	}
	log.info("The term with the id '" + termId
		+ "' has NOT been removed. The term id can not be found!");
	return false;
    }

    /**
     * Change the operation in a term where the id and the new operation to set
     * is provided
     * 
     * @param operationName
     *            New <code>operation</code> to set in the term
     * @param termId
     *            The id of the <code>Term</code> to which the
     *            <code>Operation</code> shoule be set
     */
    private boolean addOperation(String operationName,
	    String operationReturnType, Parameter[] parameters, String termId,
	    String contractID) {

	// convert param array to list
	List<Parameter> paramList = Arrays.asList(parameters);

	// create the new operation
	Operation newOperation = new Operation(operationName, paramList,
		operationReturnType);

	boolean found = false;
	Term term = null;
	Contract contract = this.contractMap.get(contractID);

	// search for the contract where the term is located and save them
	// for (Contract c : this.contractMap.values()) {
	for (Term t : contract.getTermList()) {
	    if (t.getId().equals(termId)) {
		term = t;
		found = true;
	    }
	}
	// }

	// if no term with that id found get out
	if (!found) {
	    log.fatal("Could not change operation. Term with id: '" + termId
		    + "' not found.");
	    return false;
	}

	contract.updateRoutingTable(newOperation, term.getDirection());
	term.setOperation(newOperation);

	this.notifyUpdateRoleListeners(contract.getRoleA());
	this.notifyUpdateRoleListeners(contract.getRoleB());

	return true;
    }

    private boolean deleteOperation(String operationName, String termId) {
	boolean found = false;
	Term term = null;
	Contract contract = null;

	// search for the contract where the term is located and save them
	for (Contract c : this.contractMap.values()) {
	    for (Term t : c.getTermList()) {
		if (t.getId().equals(termId)) {
		    term = t;
		    contract = c;
		    found = true;
		}
	    }
	}

	// if no term with that id found get out
	if (!found) {
	    log.fatal("Could not remove operation. Term with id: '" + termId
		    + "' not found.");
	    return false;
	}

	contract.getRoleA().getRoutingTable()
		.removeOperationName(operationName, term, contract);
	contract.getRoleB().getRoutingTable()
		.removeOperationName(operationName, term, contract);

	term.setOperation(null);

	this.notifyUpdateRoleListeners(contract.getRoleA());
	this.notifyUpdateRoleListeners(contract.getRoleB());

	return true;
    }

    /**
     * Inserts a new rule which is written in the drools syntax to a given
     * contract
     * 
     * @param newRule
     *            the new rule which is written in drools syntax as
     *            <code>String</code>.
     * @param contractId
     *            the id for the contract where the new rule should be inserted.
     * @return <code>true</code> if inserting the new rules was successful,
     *         otherwise <code>false</code>.
     */
    private boolean insertNewRule(String newRule, String contractId) {
	Contract contract = contractMap.get(contractId);
	if (contract != null) {
	    if (contract.addRule(newRule)) {

		// Creating the RuleChangeTracker object and adding it to the
		// ruleChangeTrackerList for snapshot generation
		ruleChangeTrackerList.add(new RuleChangeTracker("insert",
			contract.getContractRules().getRuleFile(), newRule,
			contractId));
		log.info("A new rule has been inserted in the conract with the id '"
			+ contractId + "'.");

		this.notifyUpdateRoleListeners(contract.getRoleA());
		this.notifyUpdateRoleListeners(contract.getRoleB());
		return true;
	    }
	}
	log.fatal("New rule couldn't be inserted in the contract with the id  '"
		+ contractId + "'. Contract does not exist");
	return false;
    }

    /**
     * Remove a rule which is existent in a contracts drool rules
     * 
     * @param contractId
     *            the id for the contract where the new rule should be inserted.
     * @param ruleName
     *            The name of the rule which needs to be removed.
     */
    private boolean deleteRule(String contractId, String ruleName) {
	boolean result = false;
	if (contractMap.containsKey(contractId)) {
	    Contract contract = contractMap.get(contractId);

	    // Creating the RuleChangeTracker object and adding it to the
	    // ruleChangeTrackerList for snapshot generation
	    ruleChangeTrackerList.add(new RuleChangeTracker("remove", contract
		    .getContractRules().getRuleFile(), ruleName, contractId));

	    result = contract.removeRule(ruleName);
	    this.notifyUpdateRoleListeners(contract.getRoleA());
	    this.notifyUpdateRoleListeners(contract.getRoleB());
	}

	return result;
    }

    /**
     * <code>OrganiserRole<code> is an implementation of the <code>IOrganiserRole</code>
     * interface. Because its an inner class to <code>Composite</code> it can
     * access all of its private instance variables and methods to make
     * reconfiguring the contracts and structure easier.
     * 
     * @author The ROAD team, Swinburne University of Technology
     */
    public class OrganiserRole implements IOrganiserRole,
	    IInternalOrganiserView {

	private LinkedBlockingQueue<MessageWrapper> outQueue;

	/**
	 * Creates a new instance of <code>OrganiserRole</code>.
	 */
	public OrganiserRole() {
	    outQueue = new LinkedBlockingQueue<MessageWrapper>();
	}

	/**
	 * Puts a <code>MessageWrapper</code> containing a management related
	 * message into the organisers message queue for the organiser to
	 * retrieve. This method is for internal <code>Composite</code> use and
	 * is not intended to be exposed publicly to any role.
	 * 
	 * @param message
	 *            the MessageWrapper intended for the organiser.
	 */
	@Override
	public void sendToOrganiser(MessageWrapper message) {
	    outQueue.add(message);
	    if (log.isDebugEnabled()) {
		log.debug("Added a management message the organiser roles outQueue");
	    }
	}

	/**
	 * Returns the next MessageWrapper in the organisers message queue. The
	 * MessageWrapper should contain a management related message sent from
	 * one of the roles inside the <code>Composite</code>.
	 * 
	 * @return the next <code>MessageWrapper</code> in the organisers queue.
	 */
	@Override
	public MessageWrapper getNextManagementMessage() {
	    try {
		log.info("retrieving next message for organiser");
		return outQueue.take();
	    } catch (InterruptedException e) {
		return null;
	    }
	}

	@Override
	public MessageWrapper getNextManagementMessage(long timeout,
		TimeUnit unit) {
	    try {
		if (log.isInfoEnabled()) {
		    log.info("retrieving next message for organiser with a timeout of "
			    + timeout + " " + unit);
		}
		return outQueue.poll(timeout, unit);
	    } catch (InterruptedException e) {
		return null;
	    }
	}

	/**
	 * Allows the organiser to send management related messages to specific
	 * roles inside a standard <code>MessageWrapper</code>. If the specified
	 * recipient role does not exist the method returns <code>false</code>,
	 * or <code>true</code> otherwise.
	 * 
	 * @param msg
	 *            the <code>MessageWrapper</code> to send.
	 * @param destinationRoleId
	 *            the recipient roles unique id.
	 * @return <code>true</code> if recipient role exists,
	 *         <code>false</code> if not.
	 */
	@Override
	public OrganiserOperationResult sendManagementMessage(
		MessageWrapper msg, String destinationRoleId) {

	    log.info("Sending a management message to Role:"
		    + destinationRoleId + " from the" + "organiser role");

	    if (roleMap.containsKey(destinationRoleId)) {
		Role destRole = roleMap.get(destinationRoleId);
		destRole.organiserPutOutgoingManagement(msg);
		return new OrganiserOperationResult(true,
			"Management message sent");
	    } else
		return new OrganiserOperationResult(false,
			"Management message not sent. Destination role not found");
	}

	@Override
	public OrganiserOperationResult addNewRole(String id, String name,
		String description) {
	    log.info("Organiser: addNewRole, roleId: " + id);

	    RoleType roleType = new RoleType();
	    roleType.setId(id);
	    roleType.setName(name);
	    roleType.setDescription(description);
	    Role newRole = new Role(roleType);
	    smcBinding.getRoles().getRole().add(roleType);
	    serendipEngine.subscribe(newRole);
	    try {
		addRole(newRole, true);
		notifyAddRoleListeners(newRole);
		return new OrganiserOperationResult(true, "New role "
			+ newRole.getId() + " added successfully");
	    } catch (CompositeInstantiationException e) {
		return new OrganiserOperationResult(false, e.getMessage());
	    }
	}

	@Override
	public OrganiserOperationResult removeRole(String roleId) {
	    log.info("Organiser: remove, roleId: " + roleId);
	    boolean result = false;
	    IRole removedRole = deleteRole(roleId);
	    if (removedRole != null) {
		notifyRemoveRoleListeners(removedRole);
		result = true;
		return new OrganiserOperationResult(result, "Role" + roleId
			+ " has been sucessfully removed");
	    } else
		return new OrganiserOperationResult(result, "Role" + roleId
			+ " was not removed. Role not found");
	}

	@Override
	public OrganiserOperationResult addNewContract(String id, String name,
		String description, String state, String type, String ruleFile,
		boolean isAbstract, String roleAId, String roleBId) {
	    log.info("Organiser: addNewContract, contractId: " + id);

	    Contract newContract = null;
	    try {
		newContract = new Contract(id, name, description, state, type,
			ruleFile, isAbstract, rulesDir);

		addContract(newContract, roleAId, roleBId);
		return new OrganiserOperationResult(true, "Contract "
			+ newContract.getId() + " was successfully added");
	    } catch (CompositeInstantiationException e) {
		return new OrganiserOperationResult(false, e.getMessage());
	    } catch (ConsistencyViolationException e) {
		return new OrganiserOperationResult(false, e.getMessage());
	    }
	}

	@Override
	public OrganiserOperationResult removeContract(String contractId) {
	    log.info("Organiser: removeContract, contractId: " + contractId);
	    boolean result = deleteContract(contractId);
	    if (result)
		return new OrganiserOperationResult(result,
			"Contract with the id '" + contractId
				+ "' has been removed from thcomposite");
	    else
		return new OrganiserOperationResult(result,
			"Contract with the id '" + contractId
				+ "' was not removed as it could not be found");
	}

	@Override
	public OrganiserOperationResult addNewTerm(String id, String name,
		String messageType, String deonticType, String description,
		String direction, String contractId) {
	    log.info("Organiser: addNewTerm, contractId: " + contractId);
	    boolean result = addTerm(id, name, messageType, deonticType,
		    description, direction, contractId);
	    if (result)
		return new OrganiserOperationResult(result, "Term " + id
			+ " was successfully added");
	    else
		return new OrganiserOperationResult(
			result,
			"Term "
				+ id
				+ " could not be added as the specified contract does not exist");
	}

	public OrganiserOperationResult updateTerm(String id,
		String contractId, String property, String value) {
	    log.info("Organiser: updateTerm, contractId: " + contractId);
	    boolean result = updateTermPrivate(id, contractId, property, value);
	    if (result) {
		return new OrganiserOperationResult(result, "Term " + id
			+ " was successfully added");
	    } else {
		return new OrganiserOperationResult(
			result,
			"Term "
				+ id
				+ " could not be added as the specified contract does not exist");
	    }
	}

	@Override
	public OrganiserOperationResult removeTerm(String ctId, String termId) {
	    log.info("Organiser: removeTerm, termId: " + termId);
	    boolean result = deleteTerm(ctId, termId);
	    if (result)
		return new OrganiserOperationResult(result, "Term " + termId
			+ " was successfully removed");
	    else
		return new OrganiserOperationResult(result, "Term " + termId
			+ " was not found");
	}

	@Override
	public OrganiserOperationResult addNewOperation(String operationName,
		String operationReturnType, Parameter[] parameters,
		String termId, String contractName) {
	    log.info("Organiser: addNewOperation");
	    boolean result = addOperation(operationName, operationReturnType,
		    parameters, termId, contractName);
	    if (result)
		return new OrganiserOperationResult(result,
			"Operation successfully added");
	    else
		return new OrganiserOperationResult(result,
			"Could not change operation. " + "Term with id: '"
				+ termId + "' not found.");
	}

	@Override
	public OrganiserOperationResult setOutMessageType(String deliveryType,
		boolean isResponse, String operationName,
		String operationReturnType, Parameter[] parameters, String tId,
		String rId) {
	    log.info("Organiser: setOutMessageType");
	    Role role = roleMap.get(rId);
	    if (role == null) {
		return new OrganiserOperationResult(false,
			"Could not set the out message.A role cannot be found : "
				+ rId);
	    }

	    TasksType tasksType = role.getRoleType().getTasks();
	    TaskType taskType = null;
	    for (TaskType t : tasksType.getTask()) {
		if (tId.equals(t.getId())) {
		    taskType = t;
		    break;
		}
	    }
	    if (taskType == null) {
		return new OrganiserOperationResult(false,
			"Could not set the out message. A task cannot be found : "
				+ tId);
	    }
	    OutMsgType outMsgType = new OutMsgType();
	    outMsgType.setDeliveryType(deliveryType);
	    outMsgType.setIsResponse(isResponse);
	    OperationType operationType = new OperationType();
	    operationType.setName(operationName);
	    operationType.setReturn(operationReturnType);
	    ParamsType paramsType = new ParamsType();
	    for (Parameter parameter : parameters) {
		ParamsType.Parameter r = new ParamsType.Parameter();
		r.setName(parameter.getName());
		r.setType(parameter.getType());
		paramsType.getParameter().add(r);
	    }
	    operationType.setParameters(paramsType);
	    outMsgType.setOperation(operationType);
	    taskType.setOut(outMsgType);
	    return new OrganiserOperationResult(true,
		    "Updated the out message : " + tId);
	}

	@Override
	public OrganiserOperationResult setInMessageType(boolean isResponse,
		String operationName, String operationReturnType,
		Parameter[] parameters, String tId, String rId) {
	    log.info("Organiser: setInMessageType");
	    Role role = roleMap.get(rId);
	    if (role == null) {
		return new OrganiserOperationResult(false,
			"Could not set the in message.A role cannot be found : "
				+ rId);
	    }

	    TasksType tasksType = role.getRoleType().getTasks();
	    TaskType taskType = null;
	    for (TaskType t : tasksType.getTask()) {
		if (tId.equals(t.getId())) {
		    taskType = t;
		    break;
		}
	    }
	    if (taskType == null) {
		return new OrganiserOperationResult(false,
			"Could not set the in message. A task cannot be found : "
				+ tId);
	    }
	    InMsgType inMsgType = new InMsgType();
	    inMsgType.setIsResponse(isResponse);
	    OperationType operationType = new OperationType();
	    operationType.setName(operationName);
	    operationType.setReturn(operationReturnType);
	    ParamsType paramsType = new ParamsType();
	    for (Parameter parameter : parameters) {
		ParamsType.Parameter r = new ParamsType.Parameter();
		r.setName(parameter.getName());
		r.setType(parameter.getType());
		paramsType.getParameter().add(r);
	    }
	    operationType.setParameters(paramsType);
	    inMsgType.setOperation(operationType);
	    taskType.setIn(inMsgType);
	    return new OrganiserOperationResult(true,
		    "Updated the in message : " + tId);
	}

	@Override
	public OrganiserOperationResult removeOperation(String operationName,
		String termId) {
	    log.info("Organiser: removeOperation");
	    boolean result = deleteOperation(operationName, termId);
	    if (result)
		return new OrganiserOperationResult(result,
			"Operation successfully removed");
	    else
		return new OrganiserOperationResult(result,
			"Could not remove operation. " + "Term with id: '"
				+ termId + "' not found.");
	}

	@Override
	public OrganiserOperationResult addNewContractRule(String newRule,
		String contractId) {
	    log.info("Organiser: addNewContractRule");
	    boolean result = insertNewRule(newRule, contractId);
	    if (result)
		return new OrganiserOperationResult(result,
			"New rule successfully inserted");
	    else
		return new OrganiserOperationResult(result,
			"New rule couldn't be "
				+ "inserted in the contract with the id  '"
				+ contractId + "'. Contract does not exist");
	}

	@Override
	public OrganiserOperationResult removeContractRule(String contractId,
		String ruleName) {
	    log.info("Organiser: removeContractRule");
	    boolean result = deleteRule(contractId, ruleName);
	    if (result)
		return new OrganiserOperationResult(result, "Rule " + ruleName
			+ " removed from contract " + contractId
			+ " successfully");
	    else
		return new OrganiserOperationResult(result, "Rule " + ruleName
			+ " could not be removed from contract " + contractId);

	}

	/**
	 * Returns a reference to a <code>Contract</code> based on its unique
	 * id.
	 * 
	 * @param id
	 *            the id of the desired contract.
	 * @return the contract or false if a Contract with the specified unique
	 *         id does not exist.
	 */
	@Override
	public Contract getContractById(String id) {
	    return contractMap.get(id);
	}

	@Override
	public OrganiserOperationResult addNewCompositeRule(String newRule) {
	    log.info("Organiser: addNewCompositeRule");
	    boolean result = compositeRules.insertRule(newRule);
	    if (result)
		return new OrganiserOperationResult(result,
			"New rule successfully inserted");
	    else
		return new OrganiserOperationResult(result,
			"New rule couldn't be " + "inserted");
	}

	@Override
	public OrganiserOperationResult removeCompositeRule(String ruleName) {
	    log.info("Organiser: removeCompositeRule");
	    boolean result = compositeRules.removeRule(ruleName);
	    if (result)
		return new OrganiserOperationResult(result, "Rule " + ruleName
			+ " removed successfully");
	    else
		return new OrganiserOperationResult(result, "Rule " + ruleName
			+ " was not removed sucessfully");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see au.edu.swin.ict.road.composite.IOrganiserRole#takeSnapshot()
	 */
	@Override
	public OrganiserOperationResult takeSnapshot() {
	    log.info("Organiser: takeSnapshot");

	    Composite currentComposite = Composite.this;
	    CompositeMarshalling cm = new CompositeMarshalling();

	    boolean result = cm.marshalSMC(currentComposite);
	    if (result) {
		return new OrganiserOperationResult(result,
			"Snapshot generated on: " + cm.getFoldername());
	    } else {
		return new OrganiserOperationResult(result,
			"Snapshot generation failed. Contact System Administrator.");
	    }

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * au.edu.swin.ict.road.composite.IOrganiserRole#takeSnapshot(java.lang
	 * .String)
	 */
	@Override
	public OrganiserOperationResult takeSnapshotAtDir(String folder) {
	    log.info("Organiser: takeSnapshot");

	    Composite currentComposite = Composite.this;
	    CompositeMarshalling cm = new CompositeMarshalling();

	    boolean result = cm.marshalSMC(currentComposite, folder);
	    if (result) {
		return new OrganiserOperationResult(result,
			"Snapshot generated on: " + cm.getFoldername());
	    } else {
		return new OrganiserOperationResult(result,
			"Snapshot generation failed. Contact System Administrator.");
	    }
	}

	@Override
	public OrganiserOperationResult changePlayerBinding(String roleId,
		String endpoint) {

	    PlayerBinding pb = playerBindingMap.get(roleId);
	    if (null == pb) {
		return new OrganiserOperationResult(false,
			"No such player binding");
	    } else {
		pb.setEndpoint(endpoint);
		return new OrganiserOperationResult(true,
			"Successfully changed the player binding of " + roleId
				+ " to " + endpoint);
	    }
	}

	// /////////////////////////////////////////////////////////////

	/**
	 * Fact related organizer methods
	 */

	public FactObject getFact(String factType, String factIdentifierValue) {
	    FactTupleSpaceRow factTupleSpaceRow = getFTS()
		    .getFactTupleSpaceRow(factType);
	    if (null != factTupleSpaceRow) {
		if (null == factIdentifierValue) {
		    return factTupleSpaceRow.getMasterFact();
		} else {
		    return factTupleSpaceRow
			    .getFactObjectByIdValue(factIdentifierValue);
		}
	    }
	    return null;
	}

	public OrganiserOperationResult updateFact(FactObject factObject) {
	    getFTS().updateFact(factObject);
	    return new OrganiserOperationResult(true,
		    "update fact successfully");
	}

	public OrganiserOperationResult addFact(String factType,
		FactObject factObject) {
	    FactTupleSpaceRow factTupleSpaceRow = getFTS()
		    .getFactTupleSpaceRow(factType);
	    if (null != factTupleSpaceRow) {

		factTupleSpaceRow.addFact(factObject);
		return new OrganiserOperationResult(true,
			"fact added successfully");
	    } else {
		return new OrganiserOperationResult(false, "No such fact type "
			+ factType);
	    }
	}

	public OrganiserOperationResult removeFact(String factType,
		String factIdentifierValue) {
	    FactTupleSpaceRow factTupleSpaceRow = getFTS()
		    .getFactTupleSpaceRow(factType);
	    if (null != factTupleSpaceRow) {
		FactObject factObject = factTupleSpaceRow
			.getFactObjectByIdValue(factIdentifierValue);
		boolean deleteFact = factTupleSpaceRow.deleteFact(factObject);
		if (deleteFact) {
		    return new OrganiserOperationResult(true,
			    "fact deleted successfully");
		} else {
		    return new OrganiserOperationResult(true,
			    "fact deletion unsuccessful");
		}

	    }
	    return new OrganiserOperationResult(false, "No such fact type "
		    + factType);
	}

	@Override
	public String getName() {

	    return Composite.this.getName();
	}

	// /////////////////////////////////////////////////////////////

	/**
	 * To adapt the serendip processes
	 */

	@Override
	public OrganiserOperationResult addNewBehaviorConstraint(String btid,
		String cid, String expression, boolean enabled) {

	    return serendipEngine.getSerendipOrgenizer()
		    .addNewBehaviorConstraint(btid, cid, expression, enabled);
	}

	@Override
	public OrganiserOperationResult addBehaviorRefToPD(String pdid,
		String btid) {

	    return serendipEngine.getSerendipOrgenizer().addBehaviorRefToPD(
		    pdid, btid);
	}

	@Override
	public OrganiserOperationResult addConstraintToProcessInstance(
		String pid, String cid, String expression, boolean enabled) {

	    return serendipEngine.getSerendipOrgenizer()
		    .addConstraintToProcessInstance(pid, cid, expression,
			    enabled);
	}

	@Override
	public OrganiserOperationResult addTaskToInstance(String pid,
		String btid, String tid, String preEP, String postEP,
		String obligRole, String pp) {

	    return serendipEngine.getSerendipOrgenizer().addTaskToInstance(pid,
		    btid, tid, preEP, postEP, obligRole, pp);
	}

	@Override
	public OrganiserOperationResult addNewProcessConstraint(String pdid,
		String cid, String expression, boolean enabled) {

	    return serendipEngine.getSerendipOrgenizer()
		    .addNewProcessConstraint(pdid, cid, expression, enabled);
	}

	@Override
	public OrganiserOperationResult addTaskToBehavior(String btid,
		String tid, String preep, String postep, String pp) {

	    return serendipEngine.getSerendipOrgenizer().addTaskToBehavior(
		    btid, tid, preep, postep, pp);
	}

	@Override
	public OrganiserOperationResult updateProcessInstanceState(String pid,
		String status) {

	    return serendipEngine.getSerendipOrgenizer()
		    .updateProcessInstanceState(pid, status);
	}

	@Override
	public OrganiserOperationResult updateStateofAllProcessInstances(
		String state) {

	    return serendipEngine.getSerendipOrgenizer()
		    .updateStateofAllProcessInstances(state);
	}

	@Override
	public OrganiserOperationResult updatePropertyOfProcessInstance(
		String pid, String property, String value) {

	    return serendipEngine.getSerendipOrgenizer()
		    .updatePropertyOfProcessInstance(pid, property, value);
	}

	@Override
	public OrganiserOperationResult updatePropertyofTaskOfInstance(
		String pid, String taskid, String property, String value) {

	    return serendipEngine.getSerendipOrgenizer()
		    .updatePropertyofTaskOfInstance(pid, taskid, property,
			    value);
	}

	@Override
	public OrganiserOperationResult updateTaskDef(String roleId,
		String taskId, String property, String value) {
	    return serendipEngine.getSerendipOrgenizer().updateTaskDef(roleId,
		    taskId, property, value);
	}

	@Override
	public OrganiserOperationResult adaptProcessInstance(
		String processInstanceId,
		List<InstanceAdaptAction> adaptationActions) {
	    return serendipEngine.getSerendipOrgenizer().adaptProcessInstance(
		    processInstanceId, adaptationActions);
	}

	@Override
	public OrganiserOperationResult adaptDefinition(
		List<DefAdaptAction> adaptationActions) {
	    return serendipEngine.getSerendipOrgenizer().adaptDefinition(
		    adaptationActions);
	}

	@Override
	public OrganiserOperationResult addProcessDefinition(OMElement element) {
	    return serendipEngine.getSerendipOrgenizer().addProcessDefinition(
		    element);
	}

	@Override
	public OrganiserOperationResult addPlayerBinding(String pbId,
		String rid, String endpoint) {
	    if (log.isDebugEnabled()) {
		log.info("Organiser: addPlayerBinding, pbID: " + pbId);
	    }

	    PlayerBindingType playerBindingType = new PlayerBindingType();
	    playerBindingType.setId(pbId);
	    PlayerBindingType.Roles roles = new PlayerBindingType.Roles();
	    roles.getRoleID().add(rid);
	    playerBindingType.setRoles(roles);
	    playerBindingType.setEndpoint(endpoint);

	    PlayerBinding playerBinding = new PlayerBinding(playerBindingType);
	    playerBindingMap.put(pbId, playerBinding);
	    updateRoleBindings(playerBinding, true);
	    return new OrganiserOperationResult(true, "New PlayerBinding "
		    + playerBinding.getId() + " added successfully");
	}

	@Override
	public OrganiserOperationResult removePlayerBinding(String pbId) {
	    if (log.isDebugEnabled()) {
		log.info("Organiser: removePlayerBinding, pbID: " + pbId);
	    }
	    PlayerBinding playerBinding = playerBindingMap.remove(pbId);
	    updateRoleBindings(playerBinding, false);
	    return new OrganiserOperationResult(true, "New PlayerBinding "
		    + playerBinding.getId() + " removed successfully");
	}

	@Override
	public OrganiserOperationResult updatePlayerBinding(String pbId,
		String property, String value) {
	    if (log.isDebugEnabled()) {
		log.info("Organiser: updatePlayerBinding, pbID: " + pbId);
	    }
	    PlayerBinding playerBinding = playerBindingMap.remove(pbId);
	    if ("endpoint".equals(property)) {
		playerBinding.setEndpoint(value);
	    }
	    playerBindingMap.put(pbId, playerBinding);
	    updateRoleBindings(playerBinding, true);
	    return new OrganiserOperationResult(true, "New PlayerBinding "
		    + playerBinding.getId() + " updated successfully");
	}

	@Override
	public OrganiserOperationResult executeScript(String script) {

	    return serendipEngine.getSerendipOrgenizer().executeScript(script);
	}

	@Override
	public OrganiserOperationResult executeScheduledScript(String script,
		String onEventPattern, String pid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .executeScheduledScript(script, onEventPattern, pid);
	}

	@Override
	public OrganiserOperationResult addNewEvent(String eventId, String pid,
		int expiration) {
	    return serendipEngine.getSerendipOrgenizer().addNewEvent(eventId,
		    pid, expiration);
	}

	@Override
	public OrganiserOperationResult removeEvent(String eventId, String pid) {
	    return serendipEngine.getSerendipOrgenizer().removeEvent(eventId,
		    pid);
	}

	@Override
	public OrganiserOperationResult setEventExpiration(String eventId,
		String pid, int expiration) {
	    return serendipEngine.getSerendipOrgenizer().setEventExpiration(
		    eventId, pid, expiration);
	}

	@Override
	public OrganiserOperationResult subscribeToEventPattern(
		String eventPattern, String pid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .subscribeToEventPattern(eventPattern, pid);

	}

	@Override
	public OrganiserOperationResult addTaskDefToRole(String roleId,
		String taskId, boolean isMsgDriven, String srcMsgs,
		String resutMsgs, String transFile) {
	    // TODO Auto-generated method stub
	    return serendipEngine.getSerendipOrgenizer().addTaskDefToRole(
		    roleId, taskId, isMsgDriven, srcMsgs, resutMsgs, transFile);
	}

	@Override
	public OrganiserOperationResult applyPatch(String patchFile) {
	    // TODO Auto-generated method stub
	    return serendipEngine.getSerendipOrgenizer().applyPatch(patchFile);
	}

	@Override
	public OrganiserOperationResult removePD(String id) {
	    return serendipEngine.getSerendipOrgenizer().removePD(id);
	}

	@Override
	public OrganiserOperationResult removeBehavior(String id) {
	    return serendipEngine.getSerendipOrgenizer().removeBehavior(id);
	}

	@Override
	public OrganiserOperationResult removeTaskFromBehavior(String id,
		String behaviorId) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeTaskFromBehavior(id, behaviorId);
	}

	@Override
	public OrganiserOperationResult updateTaskFromBehavior(String id,
		String behaviorId, String property, String value) {
	    return serendipEngine.getSerendipOrgenizer()
		    .updateTaskFromBehavior(id, behaviorId, property, value);
	}

	@Override
	public OrganiserOperationResult removeTaskDefFromRole(String id,
		String roleId) {
	    return serendipEngine.getSerendipOrgenizer().removeTaskDefFromRole(
		    id, roleId);
	}

	@Override
	public OrganiserOperationResult addNewPD(String pdid) {

	    return serendipEngine.getSerendipOrgenizer().addNewPD(pdid);
	}

	@Override
	public OrganiserOperationResult updatePD(String pdid, String prop,
		String val) {

	    return serendipEngine.getSerendipOrgenizer().updatePD(pdid, prop,
		    val);
	}

	@Override
	public OrganiserOperationResult addNewBehavior(String bid,
		String extendfrom) {
	    return serendipEngine.getSerendipOrgenizer().addNewBehavior(bid,
		    extendfrom);
	}

	@Override
	public OrganiserOperationResult updateBehavior(String bid, String prop,
		String val) {
	    return serendipEngine.getSerendipOrgenizer().updateBehavior(bid,
		    prop, val);
	}

	@Override
	public OrganiserOperationResult removeBehaviorConstraint(String btid,
		String cid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeBehaviorConstraint(btid, cid);
	}

	@Override
	public OrganiserOperationResult updateBehaviorConstraint(String btid,
		String cid, String prop, String val) {
	    return serendipEngine.getSerendipOrgenizer()
		    .updateBehaviorConstraint(btid, cid, prop, val);
	}

	@Override
	public OrganiserOperationResult removeProcessConstraint(String pdid,
		String cid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeProcessConstraint(pdid, cid);
	}

	@Override
	public OrganiserOperationResult updateProcessConstraint(String pdif,
		String cid, String prop, String val) {
	    return serendipEngine.getSerendipOrgenizer()
		    .updateProcessConstraint(pdif, cid, prop, val);
	}

	@Override
	public OrganiserOperationResult removeBehaviorRefFromPD(String pdid,
		String bid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeBehaviorRefFromPD(pdid, bid);
	}

	@Override
	public OrganiserOperationResult removeTaskFromInstance(String pid,
		String tid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeTaskFromInstance(pid, tid);
	}

	@Override
	public OrganiserOperationResult removeContraintFromProcessInstance(
		String pid, String cid) {
	    return serendipEngine.getSerendipOrgenizer()
		    .removeContraintFromProcessInstance(pid, cid);
	}

	@Override
	public OrganiserOperationResult updateContraintOfProcessInstance(
		String pid, String cid, String expression) {
	    return serendipEngine.getSerendipOrgenizer()
		    .updateContraintOfProcessInstance(pid, cid, expression);
	}

	@Override
	public OrganiserOperationResult getProcessInstanceIds(boolean isLive) {
	    return serendipEngine.getSerendipOrgenizer().getProcessInstanceIds(
		    isLive);
	}

	@Override
	public OrganiserOperationResult exportEPMLViewOfProcessInstance(
		String pid, String filePath) {
	    return serendipEngine.getSerendipOrgenizer()
		    .exportEPMLViewOfProcessInstance(pid, filePath);
	}

	@Override
	public OrganiserOperationResult exportEPMLViewOfProcessDef(
		String pDefId, String filePath) {
	    return serendipEngine.getSerendipOrgenizer()
		    .exportEPMLViewOfProcessDef(pDefId, filePath);
	}

    }// END OF ORGANIZER IMPL

    /**
     * Returns the role map which contains all the roles for this Composite
     * 
     * @return the role map which contains all the roles for this Composite
     */
    public Map<String, Role> getRoleMap() {
	return roleMap;
    }

    /**
     * Sets the role map to the desired role map
     * 
     * @param roleMap
     *            which contains all the roles for this Composite
     */
    public void setRoleMap(Map<String, Role> roleMap) {
	this.roleMap = roleMap;
    }

    /**
     * Returns the contract map which contains all the contracts for this
     * Composite
     * 
     * @return contract map which contains all the contracts for this Composite
     */
    public Map<String, Contract> getContractMap() {
	return contractMap;
    }

    public Contract getContract(String id) {
	return this.contractMap.get(id);
    }

    /**
     * Sets the contract map to the desired contract map
     * 
     * @param contractMap
     *            which contains all the contracts for this Composite
     */
    public void setContractMap(Map<String, Contract> contractMap) {
	this.contractMap = contractMap;
    }

    /**
     * Returns the player binding map which contains all the player bindings for
     * this Composite
     * 
     * @return playerBindingMap which contains all the player bindings for this
     *         Composite
     */
    public Map<String, PlayerBinding> getPlayerBindingMap() {
	return playerBindingMap;
    }

    /**
     * Sets the player binding map to the desired role map
     * 
     * @param playerBindingMap
     *            map which contains all the player bindings for this Composite
     */
    public void setPlayerBindingMap(Map<String, PlayerBinding> playerBindingMap) {
	this.playerBindingMap = playerBindingMap;
    }

    /**
     * Returns the rule change tracker list which contains all the rule change
     * tracker objects for keeping track of all the rule changes for this
     * Composite
     * 
     * @return rule change tracker list which contains all the rule change
     *         tracker objects for keeping track of all the rule changes for
     *         this Composite
     */
    public List<RuleChangeTracker> getRuleChangeTrackerList() {
	return ruleChangeTrackerList;
    }

    /**
     * Sets the rule change tracker list to the desired rule change tracker list
     * 
     * @param ruleChangeTrackerList
     *            list which contains all the rule change tracker objects for
     *            keeping track of all the rule changes for this Composite
     */
    public void setRuleChangeTrackerList(
	    List<RuleChangeTracker> ruleChangeTrackerList) {
	this.ruleChangeTrackerList = ruleChangeTrackerList;
    }

    // Serendip
    /**
     * Returns the instance of the process engine in the composite.
     */
    public SerendipEngine getSerendipEngine() {
	return this.serendipEngine;
    }

    public FactTupleSpace getFTS() {
	return FTS;
    }

    public void setFTS(FactTupleSpace fts) {
	FTS = fts;
    }

    public BenchUtil getBenchUtil() {
	return this.benchUtil;
    }

    public Map<String, String> getCompositeProperties() {
	return compositeProperties;
    }

}