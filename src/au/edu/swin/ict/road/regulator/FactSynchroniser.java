package au.edu.swin.ict.road.regulator;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axiom.soap.SOAPHeaderBlock;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.MessageWrapper.SyncType;

/**
 * Class that represents the <code>FactSynchroniser</code> within the composite.
 * <code>FactSynchroniser</code> is a sub-component of the
 * <code>Regulator</code> and contains the core functionality of a
 * <code>Regulator</code>. <code>FactSynchroniser</code> is responsible for
 * synchronising between external facts and internal facts, specifically when
 * the internal facts are updated within the working memory. The
 * <code>FactSynchroniser</code> keeps a collection of both internal and
 * external facts, and is registered to only a single contract.
 * <p/>
 * The <code>FactSynchroniser</code> class is registered to only one
 * <code>Contract</code>, where the <code>Contract</code> acts as an observer of
 * the regulator. The <code>Contract</code>, however, can be bound to multiple
 * <code>FactSynchroniser</code>s, thus creating a one-to-many relationship
 * between <code>Contract</code> and <code>FactSynchroniser</code> This
 * reference is kept inside the <code>Contract</code> class.
 * <p/>
 * The <code>FactSynchroniser</code> is also bound to only one
 * <code>IContextProvider</code> implementation. The
 * <code>IContextProvider</code> is the interface that represents the external
 * entity within the composite, and thus the implementation depends on the
 * external entity. If the <code>FactSynchroniser</code> is set to
 * <em>active</em> mode, then the external entity would have to implement some
 * methods from the <code>IContextProvider</code> interface within their own
 * service composition. If the <code>FactSynchroniser</code> is set to
 * <em>passive</em> mode, then by default the composite will use the default
 * implementation of <code>IContextProvider</code>.
 * <p/>
 * When set to <em>active</em> mode, the <code>FactSynchroniser</code> is set to
 * be the active party in the interaction. This means that the it would have to
 * invoke the facts from the external entities as defined from the
 * <code>&lt;SyncInterval&gt;</code> tag. In this mode, the regulator
 * automatically update the facts and notifies the external entities. Active
 * mode requires an external implementation of the context provider (done by the
 * external entities), as it is only an empty method.
 * <p/>
 * When set to <em>passive</em> mode, the <code>FactSynchroniser</code> is set
 * to be a passive party in the interaction. This means that the external
 * entities would have to put the facts into the regulator, and the regulator
 * would ignore the sync interval and external implementation of the context
 * provider. In this mode, the regulator does <strong>NOT</strong> automatically
 * update the facts, and does <strong>NOT</strong> automatically notify the
 * external entities. Passive mode is implemented inside the Regulator
 * component.
 * <p/>
 * The <code>FactSynchroniser</code> comprises of two lists of facts, external
 * and internal facts. Upon synchronisation with external entities, these facts
 * will be populated to reflect the relevant facts in the external entities. The
 * internal facts, however, will be manipulated in the composite and then
 * resynchronised with the external entities.
 * 
 * @author Jovino Margathe (jmargathe@gmail.com)
 */
public class FactSynchroniser {
    private Logger log = Logger.getLogger(FactSynchroniser.class.getName());

    // FactSynchroniser-related variables
    private String factType;
    private List<FactObject> externalFacts;
    private List<FactObject> internalFacts;
    private FactObject factStructure;
    private Role associatedRole;
    private FactRegime factRegime;
    // START HERE

    private IContextProvider ctxProvider;
    private ContextProvider defaultCtxProvider = new ContextProvider(this);

    private int regulatorMode;
    private int factSource;
    private long syncInterval;

    public final static int REGULATOR_ACTIVE_MODE = 0;
    public final static int REGULATOR_PASSIVE_MODE = 1;

    public final static String synchroniserOperation = "set";
    public final static String requestFactOperation = "get";
    public final static String requestAllFactsOperation = "getAll";

    public FactSynchroniser(String factType, Role associatedRole) {
	this.factType = factType;
	this.externalFacts = new CopyOnWriteArrayList<FactObject>();
	this.internalFacts = new CopyOnWriteArrayList<FactObject>();
	this.factStructure = null;
	this.associatedRole = associatedRole;
	this.factRegime = new FactRegime(false, false, 0, 0, false);
    }

    /**
     * Default constructor for the regulator class. It accepts the regulator id
     * as the parameter and instantiate the empty facts.
     * 
     * @param factType
     *            the id of the <code>FactSynchroniser</code>
     */
    public FactSynchroniser(String factType, Role associatedRole,
	    boolean isContextProvider, boolean isMonitor,
	    int acquisitionSyncInterval, int provisionSyncInterval,
	    boolean onChange) {
	this.factType = factType;
	this.externalFacts = new CopyOnWriteArrayList<FactObject>();
	this.internalFacts = new CopyOnWriteArrayList<FactObject>();
	this.factStructure = null;
	this.associatedRole = associatedRole;
	this.factRegime = new FactRegime(isContextProvider, isMonitor,
		acquisitionSyncInterval, provisionSyncInterval, onChange);
    }

    /**
     * Procedure to set the implementation of the <code>IContextProvider</code>.
     * The context provider implementation needs to be provided by the developer
     * and heavily dependent and specific to the external entity in which the
     * context provider is bound to.
     * <p/>
     * The implementation of <code>IContextProvider</code> correlates to the
     * <code><em>impl</em></code> attribute in the
     * <code>&lt;Regulator&gt;</code> tag within the composite XML. During the
     * composite boot time, the class specified in the said attribute will be
     * set as the default implementation.
     * <p/>
     * Note that there are <strong>NO</strong> default implementation of the
     * <code>IContextProvider</code> that exists within the ROADfactory.
     * 
     * @param ctxClass
     *            the class name of the <code>IContextProvider</code>
     *            implementation.
     */
    public void setContextProvider(String ctxClass) {
	try {
	    if (this.regulatorMode == FactSynchroniser.REGULATOR_ACTIVE_MODE) {
		this.ctxProvider = (IContextProvider) Class.forName(ctxClass)
			.newInstance();
		this.ctxProvider = defaultCtxProvider;
	    } else if (this.regulatorMode == FactSynchroniser.REGULATOR_PASSIVE_MODE)
		this.ctxProvider = defaultCtxProvider;
	} catch (InstantiationException e) {
	    log.fatal("Unable to instantiate context provider for regulator ["
		    + this.getId() + "]");
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    log.fatal("Unable to access context provider for regulator ["
		    + this.getId() + "]");
	    e.printStackTrace();
	} catch (ClassNotFoundException e) {
	    log.fatal("Unable to find context provider class for regulator ["
		    + this.getId() + "]");
	    e.printStackTrace();
	}
    }

    /**
     * Procedure to set the acceptable fact structure to be regulated by this
     * <code>FactSynchroniser</code>. During the synchronisation with external
     * entities, if the fact received does not match the acceptable fact
     * structure, it will be rejected by the regulator.
     * 
     * @param fact
     *            the <code>FactObject</code> with the desired structure.
     */
    public void setAcceptableFact(FactObject fact) {
	factStructure = fact;
    }

    /**
     * Function to retrieve the regulator id.
     * 
     * @return the id of this <code>FactSynchroniser</code>.
     */
    public String getId() {
	return this.factType;
    }

    /**
     * Procedure to load the facts from the external entities through the
     * <code>IContextProvider</code> implementation. This procedure removes the
     * internal facts if there are no external facts loaded from the external
     * entities. It also checks whether the external facts comply with the fact
     * structure as defined in the composite XML, and will reject the external
     * facts if they do not match with the structure. It automatically updates
     * and replace the facts after any changes in the Drools engine based on the
     * fact <code>source</code> priority.
     * <p/>
     * This procedure does <strong>NOT</strong> insert the facts to the Drools
     * rules engine. The insertion happens in the <code>Contract</code> and
     * <code>DroolsContractRulesImpl</code> classes.
     */
    public void loadFacts() {
	// If the context provider implementation exists
	if (ctxProvider != null) {
	    // Load external facts based on regulator mode
	    if (this.regulatorMode == FactSynchroniser.REGULATOR_ACTIVE_MODE)
		externalFacts = FactParser.parse(ctxProvider.pollFacts());
	    else if (this.regulatorMode == FactSynchroniser.REGULATOR_PASSIVE_MODE)
		externalFacts = ctxProvider.getExternalFacts();

	    // External facts loaded, but null or empty
	    if (externalFacts.isEmpty() || externalFacts == null) {
		// Remove internal facts
		internalFacts.clear();
	    }
	    // If internal facts list is empty
	    else if (internalFacts.isEmpty()) {
		// Iterate through external facts and check the structure
		for (FactObject factObj : externalFacts) {
		    if (factObj.equalsInStructure(factStructure))
			internalFacts.add(factObj.clone());
		    else
			log.warn("Fact ["
				+ factObj.getFactType()
				+ "] with unique id ["
				+ factObj.getUniqueId()
				+ "] not loaded since it does not follow the same structure");
		}

		// After the copy, check whether empty or not and send a message
		if (!internalFacts.isEmpty())
		    log.info("Facts loaded for regulator [" + this.getId()
			    + "]");
		else
		    log.warn("Facts not equal in structure. No facts loaded for regulator ["
			    + this.getId() + "]");
	    }
	    // If internal facts list is not empty
	    else if (!internalFacts.isEmpty()) {
		boolean factUpdated = false;

		// Iterate through the contents of internal facts
		for (FactObject intFact : internalFacts) {

		    // Check if the earlier synchronised facts are still present
		    // in the external entity
		    if (externalFacts.contains(intFact)) {
			// Retrieve the associated external facts from this
			// internal
			// facts
			FactObject extFact = externalFacts.get(externalFacts
				.indexOf(intFact));

			// Check whether the fact is updated from the previous
			// fact
			// based on the fact source priority
			if (this.getFactSource() == FactObject.INTERNAL_SOURCE) {
			    // If it is updated, then replace the external fact
			    // and toggle the updated flag
			    if (intFact.isUpdated(extFact)) {
				externalFacts.set(
					externalFacts.indexOf(intFact),
					intFact.clone());
				factUpdated = true;
			    }
			} else {
			    // Replacing internal fact and toggle the updated
			    // flag
			    if (extFact.isUpdated(intFact)) {
				internalFacts.set(
					internalFacts.indexOf(extFact),
					extFact.clone());
				factUpdated = true;
			    }
			}
		    } else {
			// If the external source has deleted the earlier
			// synchronised facts and the source is external then
			// the fact must be removed from the internal source
			if (this.getFactSource() == FactObject.EXTERNAL_SOURCE) {
			    internalFacts.remove(intFact);
			    factUpdated = true;

			    // The fact must be manually removed from the Drools
			    // engine. This functionality is yet to be
			    // implemented.
			}
		    }
		}

		// Check if new external facts are returned from the external
		// source. These new facts are added irrespective of the
		// regulator being active or passive
		for (FactObject newExtFact : externalFacts) {

		    if (!internalFacts.contains(newExtFact)) {
			internalFacts.add(newExtFact);
			factUpdated = true;
		    }
		}

		// If the fact list is updated (even if only one)
		if (factUpdated) {
		    // Invoke the notification if regulator is active
		    if (this.regulatorMode == FactSynchroniser.REGULATOR_ACTIVE_MODE) {

			// Parse the list of facts to XML string
			List<FactObject> fObjList = new ArrayList<FactObject>();
			if (this.getFactSource() == FactObject.EXTERNAL_SOURCE)
			    fObjList = this.getExternalFacts();
			else
			    fObjList = this.getInternalFacts();

			String facts = "<Facts>";
			for (FactObject fObj : fObjList)
			    facts += fObj.toXMLString();
			facts += "</Facts>";

			// Notify the context provider
			ctxProvider.notifyFactSource(facts);
		    }
		}
	    } else
		log.info("External facts are equal with internal facts");
	}
    }

    /**
     * Function to retrieve a list of internal facts within this regulator.
     * 
     * @return a <code>List</code> of internal <code>FactObject</code>s.
     */
    public List<FactObject> getInternalFacts() {
	return this.internalFacts;
    }

    /**
     * Function to retrieve a list of external facts within this regulator.
     * 
     * @return a <code>List</code> of external <code>FactObject</code>s.
     */
    public List<FactObject> getExternalFacts() {
	return this.externalFacts;
    }

    /**
     * Procedure to set the fact source of the <code>FactSynchroniser</code>.
     * This procedure is not meant to be used explicitly, but only as a
     * convenience when checking the fact source of the facts inside the
     * <code>FactSynchroniser</code>. The fact source of this
     * <code>FactSynchroniser</code> is set automatically during the composite
     * boot time.
     * 
     * @param source
     *            the fact source.
     */
    public void setFactSource(int source) {
	this.factSource = source;
    }

    /**
     * Function to get the fact source of the facts within the
     * <code>FactSynchroniser</code>. The fact source reflected in this
     * procedure is the fact source contained by the <em>fact structure</em>
     * contained within the <code>FactSynchroniser</code>. This procedure is
     * meant only for convenience when checking the fact source.
     * 
     * @return the fact source, 0 if <em>external</em> or 1 if <em>internal</em>
     *         .
     */
    public int getFactSource() {
	if (this.factSource == FactObject.EXTERNAL_SOURCE)
	    return FactObject.EXTERNAL_SOURCE;
	else
	    return FactObject.INTERNAL_SOURCE;
    }

    /**
     * Procedure to set the regulator mode in which this
     * <code>FactSynchroniser</code> is working. <em>Active</em> mode means this
     * <code>FactSynchroniser</code> would have to invoke the external entities
     * to retrieve facts, and automatically notify them if there is an update.
     * <em>Passive</em> mode behaves the exact opposite,
     * <code>FactSynchroniser</code> would only wait until the external entities
     * put the facts inside, and would <strong>NOT</strong> notify the external
     * entities automatically. The external entities would have to invoke a
     * method within the context provider to retrieve the updates.
     * <p/>
     * It is set statically using static call from <code>FactSynchroniser</code>
     * class with either <code>FactSynchroniser.REGULATOR_ACTIVE_MODE</code> or
     * <code>FactSynchroniser.REGULATOR_PASSIVE_MODE</code>.
     * 
     * @param interaction
     *            the interaction style between the regulator and the external
     *            entities.
     */
    public void setRegulatorMode(int interaction) {
	this.regulatorMode = interaction;
    }

    /**
     * Function to retrieve the regulator mode that is set to this
     * <code>FactSynchroniser</code>.
     * 
     * @return the regulator mode, 0 if <em>active</em> or 1 if <em>passive</em>
     *         .
     */
    public int getRegulatorMode() {
	if (this.regulatorMode == FactSynchroniser.REGULATOR_PASSIVE_MODE)
	    return FactSynchroniser.REGULATOR_PASSIVE_MODE;
	else
	    return FactSynchroniser.REGULATOR_ACTIVE_MODE;
    }

    /**
     * Procedure to set the synchronisation interval of this
     * <code>FactSynchroniser</code>. The sync interval would only work if the
     * regulator is set to <em>active</code> mode.
     * <p/>
     * Please note that although the sync interval can be stated in the composite XML, if
     * the regulator is set to <em>passive</em> mode, then the sync interval
     * will be ignored.
     * 
     * @param time
     *            the synchronisation interval in milliseconds.
     */
    public void setSyncInterval(long time) {
	this.syncInterval = time;
    }

    /**
     * Function to retrieve the synchronisation interval of this
     * <code>FactSynchroniser</code>.
     * <p/>
     * Please note that although the sync interval can be stated in the
     * composite XML, if the regulator is set to <em>passive</em> mode, then the
     * sync interval will be ignored.
     * 
     * @return the synchronisation interval in milliseconds.
     */
    public long getSyncInterval() {
	return this.syncInterval;
    }

    public String getFactType() {
	return factType;
    }

    public void setFactType(String factType) {
	this.factType = factType;
    }

    public FactObject getFactStructure() {
	return factStructure;
    }

    public void setFactStructure(FactObject factStructure) {
	this.factStructure = factStructure;
    }

    public Role getAssociatedRole() {
	return associatedRole;
    }

    public void setAssociatedRole(Role associatedRole) {
	this.associatedRole = associatedRole;
    }

    public FactRegime getFactRegime() {
	return factRegime;
    }

    public void setFactRegime(FactRegime factRegime) {
	this.factRegime = factRegime;
    }

    public void setExternalFacts(List<FactObject> externalFacts) {
	this.externalFacts = externalFacts;
    }

    public void setInternalFacts(List<FactObject> internalFacts) {
	this.internalFacts = internalFacts;
    }

    /**
     * Function to handle facts arriving from the player. If the facts sent by
     * the player are either new or changed (from existing), then they will be
     * synchronised with the fact tuple space.
     * 
     * @param xmlFacts
     *            XML String which contains the facts
     * @return boolean which indicates whether the facts were successfully
     *         synchronised or not
     */
    public synchronized boolean factsArrived(List<FactObject> facts) {
	boolean isSynchronizationDone = false;
	boolean isInternalListChanged = false;
	boolean isExternalListChanged = false;

	// externalFacts = FactParser.parse(xmlFacts);
	externalFacts = facts;

	// External facts loaded, but null or empty
	if (externalFacts.isEmpty() || externalFacts == null) {
	    // Remove internal facts
	    internalFacts.clear();
	}
	// If internal facts list is empty
	else if (internalFacts.isEmpty()) {
	    // Iterate through external facts and check the structure
	    for (FactObject factObj : externalFacts) {
		if (factObj.equalsInStructure(factStructure)) {
		    internalFacts.add(factObj.clone());
		    isInternalListChanged = true;
		} else {
		    log.warn("Fact ["
			    + factObj.getFactType()
			    + "] with unique id ["
			    + factObj.getUniqueId()
			    + "] not loaded since it does not follow the same structure");
		}
	    }
	}
	// If internal facts list is not empty
	else if (!internalFacts.isEmpty()) {
	    List<FactObject> updateInternalFactsList = new LinkedList<FactObject>();
	    List<FactObject> removeInternalFactsList = new LinkedList<FactObject>();
	    // Iterate through the contents of internal facts
	    Iterator<FactObject> iter = internalFacts.iterator();
	    while (iter.hasNext()) {
		FactObject intFact = iter.next();
		// Check if the earlier synchronised facts are still present
		// in the external entity
		if (externalFacts.contains(intFact)) {
		    // Retrieve the associated external facts from this
		    // internal
		    // facts
		    FactObject extFact = externalFacts.get(externalFacts
			    .indexOf(intFact));

		    // Check whether the fact is updated from the previous
		    // fact
		    // based on the fact source priority
		    if (this.getFactSource() == FactObject.INTERNAL_SOURCE) {
			// If it is updated, then replace the external fact
			// and toggle the updated flag
			if (intFact.isUpdated(extFact)) {
			    externalFacts.set(externalFacts.indexOf(intFact),
				    intFact.clone());
			    isExternalListChanged = true;
			}
		    } else {
			// Replacing internal fact and toggle the updated
			// flag
			if (extFact.isUpdated(intFact)) {
			    // internalFacts.set(internalFacts.indexOf(extFact),
			    // extFact.clone());
			    // iter = internalFacts.iterator();
			    updateInternalFactsList.add(extFact);
			    isInternalListChanged = true;
			}
		    }
		} else {
		    // If the external source has deleted the earlier
		    // synchronised facts and the source is external then
		    // the fact must be removed from the internal source

		    // Need to know if this is required
		    if (this.getFactSource() == FactObject.EXTERNAL_SOURCE) {
			// internalFacts.remove(intFact);
			// iter = internalFacts.iterator();
			removeInternalFactsList.add(intFact);
			isInternalListChanged = true;

			// The fact must be manually removed from the Drools
			// engine. This functionality is yet to be
			// implemented.
		    }
		}
	    }

	    Iterator<FactObject> iter1 = updateInternalFactsList.iterator();
	    while (iter1.hasNext()) {
		FactObject extFact = iter1.next();
		internalFacts.set(internalFacts.indexOf(extFact),
			extFact.clone());
	    }
	    Iterator<FactObject> iter2 = removeInternalFactsList.iterator();
	    while (iter2.hasNext()) {
		FactObject intFact = iter2.next();
		internalFacts.remove(intFact);
	    }
	    // Check if new external facts are returned from the external
	    // source. These new facts are added irrespective of the
	    // regulator being active or passive
	    for (FactObject newExtFact : externalFacts) {

		if (!internalFacts.contains(newExtFact)) {
		    internalFacts.add(newExtFact);
		    isInternalListChanged = true;
		}
	    }
	}

	if (isInternalListChanged) {
	    FactTupleSpace FTSRef = associatedRole.getComposite().getFTS();
	    // log.info("INTERNAL FACTS SIZE: " + internalFacts.size());

	    Iterator<FactObject> iter = internalFacts.iterator();
	    while (iter.hasNext()) {
		FactObject fact = iter.next();
		FTSRef.updateFact(fact);
	    }
	    // for (FactObject fact : internalFacts) {
	    //
	    // FTSRef.updateFact(fact);
	    // }
	}

	if (isExternalListChanged) {
	    notifyRole();
	}

	return isSynchronizationDone;
    }

    public boolean factsChanged(List<FactObject> FTSFacts) {
	boolean isSynchronizationDone = false;

	for (FactObject fact : FTSFacts) {
	    if (fact.getFactSource() == FactObject.EXTERNAL_SOURCE) {

		if (internalFacts.contains(fact)) {
		    internalFacts
			    .set(internalFacts.indexOf(fact), fact.clone());
		} else {
		    internalFacts.add(fact);
		}
	    }
	}
	updateExternalList();
	notifyRole();
	return isSynchronizationDone;
    }

    private void updateExternalList() {
	for (FactObject fact : internalFacts) {
	    if (fact.getFactSource() == FactObject.EXTERNAL_SOURCE) {
		if (externalFacts.contains(fact)) {

		    externalFacts
			    .set(externalFacts.indexOf(fact), fact.clone());

		} else {
		    externalFacts.add(fact);
		}
	    }
	}
    }

    public void notifyRole() {
	String xmlFacts = "<facts>";
	// log.info("NO OF EXTERNAL FACTS IN NOTIFY ROLE: " +
	// externalFacts.size());
	for (FactObject fact : externalFacts) {
	    xmlFacts += fact.toXMLString();
	}
	xmlFacts += "</facts>";
	// log.info("xml string in fact sync : " + xmlFacts);
	String factOp = "set" + this.getFactType() + "Facts";
	SOAPEnvelope factSOAPEnvelope = createSOAPEnvelope(factOp, xmlFacts);
	// log.info("soap envelope in fact syn: " + factSOAPEnvelope);
	MessageWrapper factMessage = new MessageWrapper(factSOAPEnvelope, "set"
		+ this.getFactType() + "Facts", false);
	factMessage.setResponse(false);
	factMessage.setSyncType(SyncType.OUT);
	if (analyseRegime()) {
	    this.putMessage(factMessage);
	    log.info("Fact notified for " + this.associatedRole.getName());
	}
    }

    public boolean analyseRegime() {
	return factRegime.isOnChange();
    }

    public void synchroniseFacts() {

	FactTupleSpace FTSRef = associatedRole.getComposite().getFTS();

	for (FactObject fact : internalFacts) {
	    FTSRef.synchronizeFacts(fact);
	}
    }

    public void manageSetOperations(String factOp, List<FactObject> facts) {
	log.info("Facts received for role " + this.associatedRole.getName());
	this.factsArrived(facts);
    }

    public void manageGetOperations(String factOp, String factId) {

	refreshFacts();
	String xmlFacts = "";
	if (factOp.contains(FactSynchroniser.requestAllFactsOperation)) {
	    xmlFacts = "<return>";
	    for (FactObject extFact : externalFacts) {
		xmlFacts += "<fact>";
		xmlFacts += extFact.toXMLString();
		xmlFacts += "</fact>";
	    }
	    xmlFacts += "</return>";
	} else if (factOp.contains(FactSynchroniser.requestFactOperation)) {
	    xmlFacts = "<return>";
	    for (FactObject extFact : externalFacts) {
		if (extFact.getUniqueId().equals(factId))
		    xmlFacts = xmlFacts + extFact.toXMLString();
	    }
	    xmlFacts += "</return>";
	}
	SOAPEnvelope factSOAPEnvelope = createSOAPEnvelope(factOp + "Response",
		xmlFacts);
	MessageWrapper factMessage = new MessageWrapper(factSOAPEnvelope,
		factOp, true);

	this.putMessage(factMessage);

    }

    public void manageRemoveOperations(String factOp, String factId) {
	refreshFacts();
	String response = "<return>";
	boolean result = false;
	for (FactObject extFact : externalFacts) {
	    if (extFact.getUniqueId().equals(factId)) {
		result = associatedRole.getComposite().getFTS()
			.getFactTupleSpaceRow(factType).deleteFact(extFact);
	    }
	}
	response += result;
	response += "</return>";
	SOAPEnvelope factSOAPEnvelope = createSOAPEnvelope(factOp + "Response",
		response);
	MessageWrapper factMessage = new MessageWrapper(factSOAPEnvelope,
		factOp, true);

	this.putMessage(factMessage);
    }

    public void refreshFacts() {
	internalFacts.clear();
	externalFacts.clear();
	for (FactObject fact : getLatestFactsFromFTS()) {
	    internalFacts.add(fact.clone());
	    externalFacts.add(fact.clone());
	}
    }

    public List<FactObject> getLatestFactsFromFTS() {
	FactTupleSpace FTSRef = associatedRole.getComposite().getFTS();
	return FTSRef.getFactTupleSpaceRow(factType).getFactList();
    }

    public SOAPEnvelope createSOAPEnvelope(String factOp, String soapBody) {
	// log.info("start pf createsoapenvelope method");
	SOAPFactory soap11Factory = OMAbstractFactory.getSOAP11Factory();
	SOAPEnvelope newEnvelope = soap11Factory.getDefaultEnvelope();

	try {
	    OMNode factsNode = XMLUtils.toOM(new StringReader(soapBody));
	    OMFactory omFactory = OMAbstractFactory.getOMFactory();
	    OMNamespace ns = omFactory.createOMNamespace(
		    "http://ws.apache.org/axis2", "ft");
	    // SOAPHeaderBlock newSOAPHeaderBlock = soap11Factory
	    // .createSOAPHeaderBlock("fact", ns);
	    OMElement factDetails = omFactory.createOMElement(factOp, ns);
	    factDetails.addChild(factsNode);
	    newEnvelope.getBody().addChild(factDetails);
	    newEnvelope.build();
	} catch (XMLStreamException e) {
	    // log.info("inside catch in createsoapenvelope method in FS");
	    e.printStackTrace();
	}
	// log.info("end pf createsoapenvelope method");
	return newEnvelope;
    }

    public void putMessage(MessageWrapper factMessage) {
	if (this.associatedRole.isBound() || factMessage.isResponse()) {
	    log.info("message type is set to push ");
	    factMessage.setMessageType("push");
	    this.associatedRole.putPendingOutBufMessage(factMessage);
	} else {
	    log.info("message type is set to pull ");
	    factMessage.setMessageType("pull");
	    this.associatedRole.putPendingOutBufMessage(factMessage);
	}

    }

}
