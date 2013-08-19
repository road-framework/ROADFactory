package au.edu.swin.ict.road.composite.contract;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.ICompositeRules;
import au.edu.swin.ict.road.composite.rules.IContractRules;
import au.edu.swin.ict.road.composite.rules.MessageProcessResult;
import au.edu.swin.ict.road.composite.rules.drools.DroolsContractRulesImpl;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;
import au.edu.swin.ict.road.regulator.FactObject;
import au.edu.swin.ict.road.regulator.FactSynchroniser;
import au.edu.swin.ict.road.regulator.FactTupleSpace;
import au.edu.swin.ict.road.regulator.FactTupleSpaceRow;
import au.edu.swin.ict.road.xml.bindings.ContractLinkedFactType;
import au.edu.swin.ict.road.xml.bindings.ContractType;
import au.edu.swin.ict.road.xml.bindings.ContractType.LinkedFacts;
import au.edu.swin.ict.road.xml.bindings.ContractType.Terms;
import au.edu.swin.ict.road.xml.bindings.DirectionType;
import au.edu.swin.ict.road.xml.bindings.OperationType;
import au.edu.swin.ict.road.xml.bindings.ParamsType;
import au.edu.swin.ict.road.xml.bindings.TermType;

/**
 * A ROAD Contract defines the permissible interactions and obligations on those
 * interactions between two (and only two) Roles. Contracts are composed of
 * terms and rules.
 * <p/>
 * Each Term defines an allowable interaction in the format of a one way or two
 * way operation (request-response). The business logic for permissions,
 * obligations and general clauses is contained within the rule file for this
 * contract.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class Contract {
    private static Logger log = Logger.getLogger(Contract.class.getName());
    private String id;
    private String name;
    private String state;
    private String type;
    private Role roleA;
    private Role roleB;
    private String description;
    private boolean abstractContract; // if marked as abstract, no messages can

    public String getRulesDir() {
	return rulesDir;
    }

    public void setRulesDir(String rulesDir) {
	this.rulesDir = rulesDir;
    }

    public String getRuleFile() {
	return ruleFile;
    }

    public void setRuleFile(String ruleFile) {
	this.ruleFile = ruleFile;
    }

    // be routed via this contract
    private List<Term> termList;
    private ContractType contractBinding;
    private String rulesDir;
    private IContractRules contractRules;
    private ICompositeRules compositeRules;
    private Composite composite;
    private StatefulKnowledgeSession session;

    private String ruleFile;

    private List<FactSynchroniser> regulatorList;

    /**
     * Populates a Contract using a JAXB binding which contains data from an XML
     * file.
     * 
     * @param contractBinding
     *            the JAXB binding used to populate the Contract.
     * @throws CompositeInstantiationException
     *             if an error occurs populating information from the JAXB
     *             binding, or if the rule file for this Contract is invalid or
     *             not present.
     */
    public Contract(ContractType contractBinding, String rulesDir)
	    throws CompositeInstantiationException {
	this.rulesDir = rulesDir;
	this.contractBinding = contractBinding;
	this.termList = new ArrayList<Term>();
	this.roleA = null;
	this.roleB = null;
	this.compositeRules = null;
	this.regulatorList = new ArrayList<FactSynchroniser>();

	extractData(contractBinding);
    }

    public ICompositeRules getCompositeRules() {
	return compositeRules;
    }

    public StatefulKnowledgeSession getSession() {
	return session;
    }

    public List<FactSynchroniser> getRegulatorList() {
	return regulatorList;
    }

    /**
     * Populates a Contract without the need for a JAXB binding class.
     * 
     * @param id
     *            the unique id of this Contract.
     * @param name
     *            the pretty name of this Contract.
     * @param description
     *            the Contract description.
     * @param state
     *            the initial state of the Contract.
     * @param type
     *            the type of the Contract.
     * @param ruleFile
     *            the rule file where this contracts rules are stored.
     * @param isAbstract
     *            true if the Contract is abstract.
     * @param rulesDir
     *            the directory of the rules file.
     * @throws CompositeInstantiationException
     *             if an error occurs populating information in the contract or
     *             the rule file is invalid or not present.
     */
    public Contract(String id, String name, String description, String state,
	    String type, String ruleFile, boolean isAbstract, String rulesDir)
	    throws CompositeInstantiationException {
	this.id = id;
	this.name = name;
	this.state = state;
	this.description = description;
	this.type = type;
	this.rulesDir = rulesDir;
	this.termList = new ArrayList<Term>();
	this.roleA = null;
	this.roleB = null;
	this.compositeRules = null;
	this.contractRules = loadContractRules(ruleFile);
	this.regulatorList = new ArrayList<FactSynchroniser>();
    }

    /**
     * Returns the contracts opposite Role to the one passed in or null if the
     * passed Role is not one one of this Contracts two parties. I.e. if passed
     * Role is roleA then the method returns roleB.
     * 
     * @param role
     *            the Role for which the contracts opposite is required.
     * @return the opposite Role to the one passed as a parameter or null if the
     *         passed Role is not one of this contracts parties.
     */
    public Role getOppositeRole(Role role) {
	if (role.getId().equals(roleA.getId())) {
	    return roleB;
	} else if (role.getId().equals(roleB.getId())) {
	    return roleA;
	} else {
	    return null;
	}
    }

    /**
     * Passes a message (in a MessageWrapper) to this Contract for processing.
     * The steps in processing the message are: 1, Match the message wrappers
     * operation name with a Term. 2, Execute the rules for this contract. The
     * result is whether this message was blocked by the rules or not.
     * 
     * @param msg
     *            the MessageWrapper to process / evaluate.
     * @return true if message blocked at the permission rule level or false if
     *         allowed.
     */

    public MessageProcessResult processMessage(MessageWrapper msg) {

	if (compositeRules != null) {
	    compositeRules.insertMessageRecievedAtContractEvent(msg, id); // new
	    // event
	}

	MessageProcessResult mpr = null;
	// Iterate thru terms and process message
	for (Term t : termList) {
	    if (msg.getOperationName().equalsIgnoreCase(
		    t.getOperation().getName())) {
		log.info("Contract 3 . processing" + t.getId());
		mpr = t.processMessage(msg);
	    }
	}
	// Set the contract
	mpr.setContract(this);
	return mpr;
    }

    /**
     * Returns the list of Term for this Contract.
     * 
     * @return the list of Term.
     */
    public List<Term> getTermList() {
	return termList;
    }

    /**
     * Returns the term in this Contract with the specified Id.
     * 
     * @param termId
     *            the Id of the required Term.
     * @return the Term with the required termId, or null if no Term with that
     *         Id exists.
     */
    public Term getTermById(String termId) {
	for (Term t : termList) {
	    if (t.getId().equals(termId))
		return t;
	}

	return null;
    }

    /**
     * Adds a new term to this Contracts list of terms. Also updates the
     * Contracts associated roles routing tables if the Term contains an
     * Operation.
     * 
     * @param term
     *            The new Term to add.
     */
    public void addTerm(Term term) {
	updateRoutingTable(term.getOperation(), term.getDirection());
	this.termList.add(term);
    }

    public void updateRoutingTable(Operation operation, String direction) {
	if (operation != null) {
	    if (direction.equalsIgnoreCase("atob")) {
		roleA.getRoutingTable().putRequestSignature(
			operation.getName(), this);
		// if the return type is not void it will be in the response
		// table of role b
		if (!operation.getReturnType().equalsIgnoreCase("void")) {
		    roleB.getRoutingTable().putResponseSignature(
			    operation.getName(), this);
		}
	    } else {
		roleB.getRoutingTable().putRequestSignature(
			operation.getName(), this);
		// if the return type is not void it will be in the response
		// table of role A (because it is BtoA
		if (!operation.getReturnType().equalsIgnoreCase("void")) {
		    roleA.getRoutingTable().putResponseSignature(
			    operation.getName(), this);
		}
	    }
	}
    }

    /**
     * Remove a <code>Term</code> from this Contract
     * 
     * @param term
     *            The <code>Term</code> which needs to be removed from this
     *            contract
     */
    public void removeTerm(Term term) {
	this.termList.remove(term);

	// If the contract does not have any more terms
	if (this.termList.isEmpty()) {
	    if (term.getDirection().equalsIgnoreCase("atob"))
		roleA.removeContract(this);
	    else
		roleB.removeContract(this);
	}
    }

    /**
     * Remove a <code>Term</code> from this Contract according to the termId
     * 
     * @param termId
     *            The <code>Term</code> which needs to be removed from this
     *            contract
     */
    public void removeTerm(String termId) {
	for (Term term : this.termList)
	    if (term.getId().equalsIgnoreCase(termId))
		removeTerm(term);
    }

    /**
     * Checks this contracts terms to see if the passed message signature (or
     * operation signature) is used.
     * 
     * @param msgSignature
     *            the message signature to check for
     * @return true if found, false if not
     */
    public boolean isMessageSignatureFoundInTerms(String msgSignature) {
	List<Term> termList = this.getTermList();
	for (int i = 0; i < termList.size(); i++) {
	    Term term = termList.get(i);
	    String opName = term.getOperation().getName();

	    if (msgSignature.equals(opName)) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Gets this contracts type e.g. push or pull.
     * 
     * @return the contracts type.
     */
    public String getType() {
	return type;
    }

    /**
     * Set this contracts message interaction type e.g. push or pull.
     * 
     * @param type
     *            the new contracts message interaction type.
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return <code>DroolsRulesImpl</code>
     */
    public IContractRules getContractRules() {
	return contractRules;
    }

    /**
     * @param rules
     */
    public void setContractRules(IContractRules rules) {
	this.contractRules = rules;
    }

    /**
     * Gets this contracts unique id.
     * 
     * @return the unique id for this <code>Contract</code>.
     */
    public String getId() {
	return id;
    }

    /**
     * Sets this contracts unique id.
     * 
     * @param id
     *            the new unique id for this <code>Contract</code>.
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Gets this contracts name.
     * 
     * @return this contracts name.
     */
    public String getName() {
	return name;
    }

    /**
     * Sets this contracts name.
     * 
     * @param name
     *            the new name for this contract.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * Gets this contracts current state, which reflects how well the contract
     * between the two parties (<code>Roles</code>) is being fulfilled.
     * 
     * @return this contracts current state.
     */
    public String getState() {
	return state;
    }

    /**
     * Sets this contracts current state.
     * 
     * @param state
     *            the new state for this <code>Contract</code>.
     */
    public void setState(String state) {
	this.state = state;
    }

    /**
     * Gets the <code>Role</code> playing party A in this <code>Contract</code>.
     * 
     * @return the <code>Role</code> playing party A.
     */
    public Role getRoleA() {
	return roleA;
    }

    /**
     * Sets the <code>Role</code> playing party A in this <code>Contract</code>.
     * 
     * @param roleA
     *            the <code>Role</code> playing party A in this
     *            <code>Contract</code>.
     */
    public void setRoleA(Role roleA) {
	this.roleA = roleA;
    }

    /**
     * Gets the <code>Role</code> playing party B in this <code>Contract</code>.
     * 
     * @return the <code>Role</code> playing party B.
     */
    public Role getRoleB() {
	return roleB;
    }

    /**
     * Sets the <code>Role</code> playing party B in this <code>Contract</code>.
     * 
     * @param roleB
     *            the <code>Role</code> playing party B in this
     *            <code>Contract</code>.
     */
    public void setRoleB(Role roleB) {
	this.roleB = roleB;
    }

    /**
     * Gets this contracts description.
     * 
     * @return the description of this <code>Contract</code>
     */
    public String getDescription() {
	return description;
    }

    /**
     * Sets this contracts description.
     * 
     * @param description
     *            the new description for this <code>Contract</code>
     */
    public void setDescription(String description) {
	this.description = description;
    }

    /**
     * Gets a boolean representing whether this contract is abstract.
     * 
     * @return true is abstract, false is not abstract
     */
    public boolean isAbstractContract() {
	return abstractContract;
    }

    /**
     * Sets the boolean representing whether this <code>Contract</code> is
     * abstract.
     * 
     * @param abstractContract
     *            a boolean value representing whether or not this
     *            <code>Contract</code> is abstract
     */
    public void setAbstractContract(boolean abstractContract) {
	this.abstractContract = abstractContract;
    }

    /**
     * Returns a list containing all the operation names of all the terms in
     * this <code>Contract</code>.
     * 
     * @return the list of operation names
     */
    public List<String> getAllOperationNames() {
	List<String> msList = new ArrayList<String>();

	for (Term t : termList) {
	    msList.add(t.getOperation().getName());
	}
	return msList;
    }

    /**
     * Returns the JAXB binding that was used to instantiate this contract from
     * an XML file.
     * 
     * @return the JAXB binding
     */
    public ContractType getContractBinding() {
	return contractBinding;
    }

    /**
     * Returns the <code>Role</code> who is the 'requester' in the
     * <code>Term</code> matching the passed operation signature. This is
     * determined by the terms direction property.
     * 
     * @param operationName
     *            the operation signature
     * @return the <code>Role</code> who is the requester, or null if the
     *         msgSignature is not found in this contracts terms.
     */
    public Role getRequesterRole(String operationName) {
	for (Term t : termList) {
	    if (t.getOperation().getName().equals(operationName)) {
		if (t.getDirection().equals("AtoB")) {
		    return roleA;
		} else {
		    return roleB;
		}
	    }
	}

	return null;
    }

    /**
     * Returns the <code>Role</code> who is the 'responder' in the
     * <code>Term</code> matching the passed operation signature. This is
     * determined by the terms direction property.
     * 
     * @param operationName
     *            the operation signature
     * @return the <code>Role</code> who is the responder, or null if the
     *         msgSignature is not found in this contracts terms.
     */
    public Role getResponderRole(String operationName) {
	for (Term t : termList) {
	    if (t.getOperation().getName().equals(operationName)) {
		if (t.getDirection().equals("AtoB")) {
		    return roleA;
		} else {
		    return roleB;
		}
	    }
	}

	return null;
    }

    /**
     * Add a new rule to <code>this</code> Contract.
     * 
     * @param newRule
     *            the new rule in drools syntax which has to be added as
     *            <code>String</code>.
     * @return <code>true</code> if inserting was successful otherwise
     *         <code>false</code>.
     */
    public boolean addRule(String newRule) {
	return contractRules.addRule(newRule);
    }

    /**
     * Remove a rule in <code>this</code> Contract.
     * 
     * @param ruleName
     *            The name of the rule which needs to be removed.
     */
    public boolean removeRule(String ruleName) {
	return contractRules.removeRule(ruleName);
    }

    public String toString() {
	return "Contract: '" + this.name + "' (" + this.id + ") - RoleA: '"
		+ roleA.getId() + "'; RoleB: '" + roleB.getId() + "'";
    }

    public void setCompositeRules(ICompositeRules compositeRules) {
	this.compositeRules = compositeRules;
    }

    /**
     * Loads up the Drools rules associated with this <code>Contract</code> from
     * the disk. Rules are assumed to be in the location specifed by the
     * properties file in the format 'contract id''RULE_FILE_EXTENSION'.
     * Assuming RULE_FILE_EXTENSION is the default '.drl', a rule file for a
     * <code>Contract</code> might be 'contract1.drl'.
     * 
     * @return the <code>DroolsRulesImpl</code> containing the loaded drools
     *         rules.
     * @throws CompositeInstantiationException
     *             if the sought after rules file does not exist or the rules
     *             are invalid.
     */
    private IContractRules loadContractRules(String ruleFile)
	    throws CompositeInstantiationException {
	DroolsContractRulesImpl rules = null;
	try {
	    rules = new DroolsContractRulesImpl(rulesDir + ruleFile, this);
	} catch (RulesException e) {
	    throw new CompositeInstantiationException(e.getMessage());
	}

	return rules;
    }

    /**
     * Extracts data from the <code>ContractType</code> (a JAXB binding) and
     * populates the <code>Contract</code> using it.
     * 
     * @param conBinding
     *            the JAXB binding which is used to populate the
     *            <code>Contract</code>.
     * @throws CompositeInstantiationException
     *             if an error occurs attempting to instantiate the
     *             <code>Contract</code> using the supplied JAXB binding or when
     *             attempting to read the rules files.
     */
    private void extractData(ContractType conBinding)
	    throws CompositeInstantiationException {
	if (conBinding.getId() != null) {
	    id = conBinding.getId();
	}
	if (conBinding.getName() != null) {
	    name = conBinding.getName();
	}
	if (conBinding.getDescription() != null) {
	    description = conBinding.getDescription();
	}
	if (conBinding.getState() != null) {
	    state = conBinding.getState();
	}
	if (conBinding.getType() != null) {
	    type = conBinding.getType();
	}
	// if (conBinding.isAbstractContract() != null) {
	// this.abstractContract = conBinding.isAbstractContract();
	// }
	/*
	 * if (conBinding.getLinkedFacts() != null) { LinkedFacts linkedFacts =
	 * conBinding.getLinkedFacts(); for (ContractLinkedFactType factType :
	 * linkedFacts.getFact()) { for (FactTupleSpaceRow factRow :
	 * this.roleA.getComposite() .getFTS().getFTSMemory()) { if
	 * (factRow.getFactType().equalsIgnoreCase( factType.getName())) {
	 * 
	 * FactSynchroniser synchroniser = new FactSynchroniser(
	 * factType.getName());
	 * synchroniser.setAcceptableFact(factRow.getMasterFact());
	 * registerRegulator(synchroniser); } } } }
	 */

	contractRules = loadContractRules(conBinding.getRuleFile());
	session = contractRules.getSession();
	extractTerms(conBinding.getTerms(), contractRules);

    }

    /**
     * Extracts the facts linked to this contract
     * 
     * @param conBinding
     */
    public void extractFacts(ContractType conBinding) {
	// get the composite FTS, set as a global in Drools working memory
	FactTupleSpace fts = this.roleA.getComposite().getFTS();
	session.setGlobal("fts", fts);
	// if the contracts is linked to any facts
	if (conBinding.getLinkedFacts() != null) {
	    // get the linked facts
	    LinkedFacts linkedFacts = conBinding.getLinkedFacts();
	    // iterate over the list of facts linked to the contract
	    for (ContractLinkedFactType factType : linkedFacts.getFact()) {
		// get the list of facts in the composite FTS and iterate over
		// it
		for (FactTupleSpaceRow factRow : this.roleA.getComposite()
			.getFTS().getFTSMemory()) {
		    // if this fact type exists in the FTS
		    if (factRow.getFactType().equalsIgnoreCase(
			    factType.getName())) {

			// create a fact synchroniser
			FactSynchroniser synchroniser = new FactSynchroniser(
				factType.getName(), this.roleA);
			synchroniser.setAcceptableFact(factRow.getMasterFact());
			// register this synchroniser as a regulator
			registerRegulator(synchroniser);
		    }
		}
	    }
	}
    }

    /**
     * Creates and returns the contract binding object from the composite's
     * contract object to use in the JAXB transformation of the XML
     * representation of the composite.
     * 
     * @return the contract binding object used in JAXB transformation
     */
    public ContractType createContractBinding() {

	/*
	 * Creating a new contract type - JAXB binding object and setting the
	 * instance variables using the contract object's instance values
	 */
	ContractType ct = new ContractType();

	ct.setId(this.getId());
	ct.setName(this.getName());
	ct.setRuleFile(this.getContractRules().getRuleFile());
	ct.setType(this.getType());
	// ct.setAbstractContract(this.isAbstractContract());
	ct.setState(this.getState());

	/* Creating the terms type object */
	ContractType.Terms terms = new ContractType.Terms();
	List<TermType> tTypeList = new ArrayList<TermType>();
	for (Term t : this.getTermList()) {
	    TermType tt = new TermType();
	    tt.setId(t.getId());
	    // CREATE NAME ATTRIBUTE IN TERMS CLASS TO STORE THE NAME
	    tt.setName(t.getName());

	    tt.setMessageType(t.getMessageType());
	    tt.setDeonticType(t.getDeonticType());

	    /* Creating the operation type object */
	    OperationType ot = new OperationType();
	    ot.setName(t.getOperation().getName());
	    ParamsType pType = new ParamsType();
	    List<ParamsType.Parameter> parameterList = new ArrayList<ParamsType.Parameter>();

	    /* Creating the parameter type object */
	    for (Parameter o_p : t.getOperation().getParameters()) {

		ParamsType.Parameter pt_p = new ParamsType.Parameter();
		pt_p.setName(o_p.getName());
		pt_p.setType(o_p.getType());
		parameterList.add(pt_p);
		pType.getParameter().add(pt_p);

	    }

	    /* Adding operation and parameter to the terms object */
	    // pType.setParameterList(parameterList);
	    ot.setParameters(pType);
	    ot.setReturn(t.getOperation().getReturnType());
	    tt.setOperation(ot);
	    tt.setDirection(DirectionType.fromValue(t.getDirection()));
	    tt.setDescription(t.getDescription());

	    tTypeList.add(tt);
	    terms.getTerm().add(tt);

	}
	/* Adding terms object to the contract object */
	// terms.setTerm(tTypeList);
	ct.setTerms(terms);
	ct.setRoleAID(this.getRoleA().getId());
	ct.setRoleBID(this.getRoleB().getId());
	ct.setDescription(this.getDescription());

	return ct;
    }

    /**
     * Loops over the list of <code>TermType<code> and
     * instantiates actual ROAD <code>Term<code> objects.
     * 
     * @param terms
     *            a JAXB binding which contains the list of
     *            <code>TermType<code>.
     * @param rules
     *            the reference to the drools rules for this <code>Contract
     *            </code>
     */
    private void extractTerms(Terms terms, IContractRules rules) {
	List<TermType> termsTypeList = terms.getTerm();

	for (TermType t : termsTypeList) {
	    Term term = new Term(t, rules);
	    this.termList.add(term);
	}
    }

    /**
     * Add a FactSynchroniser as a regulator
     * 
     * @param reg
     */
    public void registerRegulator(FactSynchroniser reg) {
	this.regulatorList.add(reg);
    }

    /**
     * remove a regulator from the list
     * 
     * @param regId
     * @return
     */
    public boolean removeRegulator(String regId) {
	for (FactSynchroniser reg : regulatorList)
	    if (reg.getId().equals(regId))
		return regulatorList.remove(reg);

	return false;
    }

    /**
     * Get the parent composite
     * 
     * @return the parent composite
     */
    public Composite getComposite() {
	return composite;
    }

    /**
     * Set the parent composite. Note: Use with care.
     * 
     * @param composite
     */
    public void setComposite(Composite composite) {
	this.composite = composite;
    }

    /**
     * Function to inject (or <em>insert</em>) the internal facts to the Drools
     * rules engine. In the rules engine, the facts will be modified and then
     * returned back to the composite. It iterates through the list of
     * <code>FactSynchroniser</code>s bound to this contract, and then inject
     * the internal facts one by one to the Drools rules engine. After the
     * insertion, it refreshes the facts to synchronise between internal and
     * external facts. The internal facts <em>took priority</em> over the
     * external facts.
     * <p/>
     * Because Java by default acts on object reference, it is easier to refer
     * to the internal fact from the regulator, hence there is no
     * <code>return</code> statement.
     * <p/>
     * The fact insertion still uses a hack to wait for 100 milliseconds (0.1
     * seconds) after inserting the fact for it to be updated.
     */
    public void injectFact() {
	// For each regulator and internal facts

	for (FactSynchroniser reg : regulatorList) {

	    // Refreshing the facts before the facts are inserted so as to
	    // ensure
	    // that the facts in the working memory are the latest ones
	    // reg.loadFacts();

	    reg.refreshFacts();

	    for (FactObject fact : reg.getInternalFacts()) {

		// checking if the fact already exists in the drools working
		// memory
		if (session.getFactHandle(fact) != null) {// fact exist
		    session.update(session.getFactHandle(fact), fact);
		} else {// This is a new fact
		    session.insert(fact);
		}

	    }

	    // Major hack to wait until the inserted fact gets updated. :( This
	    // is a performance hit - Kau
	    try {
		Thread.sleep(100);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }

	    // Refreshes the external facts
	    // reg.loadFacts();
	}
    }

    public void terminate() {

	// TODO: Need to terminate all the sessions when the composite is going
	// down
	this.contractRules.terminateSession();
    }

}
