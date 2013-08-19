package au.edu.swin.ict.road.composite;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.print.attribute.HashAttributeSet;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.exceptions.MessageDelivererException;
import au.edu.swin.ict.road.composite.message.DroolsMsgUtil;
import au.edu.swin.ict.road.composite.message.FactMsgUtil;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.routing.RoutingTable;
import au.edu.swin.ict.road.composite.routing.exceptions.MessageRoutingException;
import au.edu.swin.ict.road.composite.routing.exceptions.MessageRoutingUndeterminedException;
import au.edu.swin.ict.road.composite.routing.exceptions.NoRequestException;
import au.edu.swin.ict.road.composite.rules.ICompositeRules;
import au.edu.swin.ict.road.composite.rules.MessageProcessResult;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;
import au.edu.swin.ict.road.regulator.FactObject;
import au.edu.swin.ict.road.regulator.FactSynchroniser;
import au.edu.swin.ict.road.regulator.bindings.Facts;
import au.edu.swin.ict.road.regulator.bindings.JFactAttributeType;
import au.edu.swin.ict.road.regulator.bindings.JFactAttributesType;
import au.edu.swin.ict.road.regulator.bindings.JFactType;
import au.edu.swin.ict.road.roadtest.events.Event;
import au.edu.swin.ict.road.xml.bindings.SMC;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventRecord;

/**
 * MessageDeliverer is an implementation of the worker thread pattern.
 * MessageDeliverers are assigned to functional roles and move messages from
 * source roles to destination roles via a contract.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class MessageDeliverer extends Thread {

    // get the logger
    private static Logger log = Logger.getLogger(MessageDeliverer.class
	    .getName());

    private Role role;
    private boolean terminated;
    private Object mutex;
    private RoutingTable routingTable;
    private ICompositeRules compositeRules;

    /**
     * Default constructor which initialises all values to null.
     */
    public MessageDeliverer() {
	terminated = false;
	role = null;
	compositeRules = null;
	mutex = new Object();
    }

    /**
     * Constructor that also accepts a <code>Role</code>(the role this
     * MessageDeliverer is assigned too) and <code>RoutingTable</code> (routing
     * table is required for a MessageDeliverer to pass messages around the
     * composite).
     * 
     * @param role
     *            the role for which the worker will route messages
     */
    public MessageDeliverer(Role role) {
	terminated = false;
	this.role = role;
	compositeRules = null;
	mutex = new Object();
	this.routingTable = role.getRoutingTable();
    }

    public MessageDeliverer(Role role, ICompositeRules compositeRules) {
	terminated = false;
	this.role = role;
	this.compositeRules = compositeRules;
	mutex = new Object();
	this.routingTable = role.getRoutingTable();
	role.getComposite();
    }

    /**
     * Starts the MessageDeliverer processing messages. This starts the message
     * processing loop.
     */
    public void run() {
	super.run();

	if (role != null) {
	    startMessageProcessingLoop();
	    log.info("MessageDeliverer for " + role.getId() + " has terminated");
	} else {
	    this.stop(new MessageDelivererException(
		    "Role has not yet been assigned to MessageDeliverer"));
	}
    }

    /**
     * Sets this MessageDeliverers assigned <code>Role</code>. Once the
     * MessageDeliverer is started it will process messages originating from
     * this <code>Role</code>.
     * 
     * @param role
     *            this MessageDeliverers assigned <code>Role</code>
     */
    public void setRole(Role role) {
	this.role = role;
    }

    /**
     * Gets the MessageDeliverers assigned <code>Role</code>.
     * 
     * @return the assigned role for this MessageDeliverer
     */
    public Role getRole() {
	return role;
    }

    /**
     * Terminate this MessageDeliverer. Note that this is a request to stop the
     * MessageDeliverer, It will still complete any work already started before
     * terminating.
     * 
     * @param terminate
     *            a boolean to indicate terminate or keep running
     */
    public void setTerminate(boolean terminate) {
	synchronized (mutex) {
	    this.terminated = terminate;
	}
	log.info("MessageDeliverer for " + role.getId()
		+ " has been set to terminate");
    }

    public ICompositeRules getCompositeRules() {
	return compositeRules;
    }

    public void setCompositeRules(ICompositeRules compositeRules) {
	this.compositeRules = compositeRules;
    }

    /**
     * Check if the message id is in any of the process definitions. If yes,
     * start a new process instance. Then set the correlation id of the message
     * as same as the process instance id
     * 
     * @param message
     */
    public void createNewInstanceIfRequired(MessageWrapper message) {
	String msgId = message.getMessageId();
	SerendipEngine engine = this.getRole().getComposite()
		.getSerendipEngine();

    }

    /**
     * The main processing loop. Starts a MessageDeliverer retrieving messages
     * from its assigned <code>Role</code> and routing to the destination
     * <code>Role</code> via the appropriate contract. This method should not be
     * invoked directly, starting the MessageDeliverer thread will result in
     * this method being called.
     */
    private void startMessageProcessingLoop() {
	log.info("MessageDeliverer for " + this.getRole().getId()
		+ " is ready to" + " accept incoming messages");

	while (!terminated) {
	    MessageWrapper message = role.delivererGetIncomingMessage(5,
		    TimeUnit.SECONDS);

	    if (message != null) {
		boolean isfactMethodFound = false;
		String msgOpName = message.getOperationName();

		for (FactSynchroniser roleFS : role.getFactSynchronisers()) {
		    if (msgOpName.equals("add" + roleFS.getId() + "Facts")
			    || msgOpName.equals("update" + roleFS.getId()
				    + "Facts")) {
			SOAPEnvelope soapEnv = (SOAPEnvelope) message
				.getMessage();
			// log.info("SOAP msg in Msg Deliverer :: " +
			// soapEnv.toString());
			Iterator soapBodyIterator = soapEnv.getBody()
				.getChildElements();
			OMElement OperationElement = (OMElement) soapBodyIterator
				.next();
			Iterator OpBodyIteraror = OperationElement
				.getChildElements();
			OMElement factsElement = (OMElement) OpBodyIteraror
				.next();
			String facts = factsElement.toString();
			// log.info("facts in msg deliverer : " + facts);

			String xmlFacts = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			xmlFacts += "<tns:facts xmlns:tns=\"http://www.swin.edu.au/ict/road/regulator/Facts\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.swin.edu.au/ict/road/regulator/Facts JFacts.xsd \">";
			Iterator factsIter = factsElement.getChildElements();
			while (factsIter.hasNext()) {
			    xmlFacts += ((OMElement) factsIter.next())
				    .toString();
			}
			xmlFacts += "</tns:facts>";
			// log.info("xml facts ::: " + xmlFacts);
			JAXBContext jc;
			try {
			    jc = JAXBContext
				    .newInstance("au.edu.swin.ict.road.regulator.bindings");
			    Unmarshaller unmarshaller = jc.createUnmarshaller();
			    Facts factsObj = (Facts) unmarshaller
				    .unmarshal(new StringReader(xmlFacts));
			    List<FactObject> regulatorFactList = new ArrayList<FactObject>();

			    // log.info("Number of facts :: " +
			    // factsObj.getFact().size());
			    for (JFactType fact : factsObj.getFact()) {

				FactObject regulatorFact = new FactObject(
					fact.getName(), fact.getIdentifier()
						.getIdentifierKey(), fact
						.getIdentifier()
						.getIdentifierValue());

				// log.info(fact.getName()
				// + ":"
				// + fact.getSource()
				// + ":"
				// + fact.getIdentifier()
				// .getIdentifierKey()
				// + ":"
				// + fact.getIdentifier()
				// .getIdentifierValue());

				JFactAttributesType attributesType = fact
					.getAttributes();
				if (attributesType != null) {
				    List<JFactAttributeType> attrs = attributesType
					    .getAttribute();
				    for (JFactAttributeType attr : attrs) {
					regulatorFact.setAttribute(
						attr.getAttributeKey(),
						attr.getAttributeValue());
					// log.info(attr.getAttributeKey() + ":"
					// + attr.getAttributeValue());

				    }
				}

				regulatorFactList.add(regulatorFact);

			    }

			    // log.info("Number of REGULATOR FACTS created" +
			    // regulatorFactList.size());
			    roleFS.manageSetOperations(msgOpName,
				    regulatorFactList);

			} catch (JAXBException e) {
			    e.printStackTrace();
			    // throw new
			    // CompositeDemarshallingException(e.getMessage());
			}

			// Iterator soapBodyIterator = soapEnv.getBody()
			// .getChildElements();
			// OMElement OperationElement = (OMElement)
			// soapBodyIterator
			// .next();
			// Iterator OpBodyIteraror = OperationElement
			// .getChildElements();
			// OMElement paramElement = (OMElement) OpBodyIteraror
			// .next();
			// String facts = paramElement.getText();
			//
			// log.info("Sent facts are: " + facts);

			isfactMethodFound = true;
			break;
		    } else if (msgOpName
			    .equals("get" + roleFS.getId() + "Fact")
			    || msgOpName.equals("getAll" + roleFS.getId()
				    + "Facts")) {
			// log.info("start of else get ");
			String factId = FactMsgUtil.getFactId(message);
			roleFS.manageGetOperations(msgOpName, factId);
			isfactMethodFound = true;
			// log.info("end of else get ");
			break;

		    } else if (msgOpName.equals("remove" + roleFS.getId()
			    + "Fact")) {
			// log.info("start of else get ");
			String factId = FactMsgUtil.getFactId(message);
			roleFS.manageRemoveOperations(msgOpName, factId);
			isfactMethodFound = true;
			// log.info("end of else get ");
			break;

		    }
		}
		if (isfactMethodFound) {
		    isfactMethodFound = false;
		    continue;
		}
	    }// end of facts impl

	    // possible timeout
	    if (message != null) {
		message.setOriginRole(role);

		this.role.getComposite().getBenchUtil()
			.addBenchRecord("DROOLS BEGIN", message.getMessageId());
		log.info("MessageDeliverer for " + role.getId()
			+ " has recieved a message of type "
			+ message.getOperationName());

		compositeRules.insertMessageRecievedAtSourceEvent(message,
			role.getId()); // new event

		try {
		    // results in message containing destination contract.
		    Contract destContract = routingTable
			    .getDestinationContract(message, true);

		    if (compositeRules != null) {
			compositeRules.insertRoutingSuccessEvent(message,
				role.getId(), destContract.getId()); // new
		    }

		    // Process the message and get the MessageProcessResult
		    MessageProcessResult mpr = destContract
			    .processMessage(message);
		    log.info("Message " + message.getMessageId()
			    + " got interpreted via contract  "
			    + message.getDestinationContract().getId());
		    // try {
		    // Thread.sleep(3000);
		    // } catch (InterruptedException e1) {
		    // // TODO Auto-generated catch block
		    // e1.printStackTrace();
		    // }

		    this.role
			    .getComposite()
			    .getBenchUtil()
			    .addBenchRecord("DROOLS END",
				    message.getMessageId());

		    boolean isBlocked = mpr.isBlocked();
		    if (isBlocked) {
			log.info("MessageDeliverer for " + role.getId()
				+ " is routing the message"
				+ message.getMessageId() + " back to its "
				+ "source=" + message.getOriginRoleId()
				+ ", due to it being blocked by the contract "
				+ message.getDestinationContract());
			message.setAnErrorMessage(true);
			message.setErrorMessage("Message blocked by contract "
				+ message.getDestinationContract().getId()
				+ " and routed back to origin role "
				+ message.getOriginRoleId());
			role.delivererPutOutgoingSyncMessage(message);
		    } else {

			// /////Serendip 1 Start////////////////////
			// In here we perform few pre-processing before the
			// message is palced in the destination role
			List<EventRecord> interpretedEvents = mpr
				.getAllInterprettedEvents();
			if (interpretedEvents.size() > 0) {
			    EventRecord firstEvent = interpretedEvents.get(0);
			    if ((null == firstEvent.getPid())
				    || (firstEvent.getPid().equals(""))
				    || (firstEvent.getPid().equals("null"))) {
				String pid = null;
				try {
				    pid = this.role.getComposite()
					    .getSerendipEngine()
					    .startProcessForEvent(firstEvent);
				} catch (SerendipException e1) {
				    // TODO Auto-generated catch block
				    e1.printStackTrace();
				}
				// allocate same pid for all other events
				for (EventRecord e : interpretedEvents) {
				    e.setPid(pid);
				}
				// allocate the pid for the message as well.
				// This is important as the out transform picks
				// up the message via <msg_id, pid>
				message.setCorrelationId(pid);
				log.info("Correlation id "
					+ message.getCorrelationId()
					+ " is set for message ="
					+ message.getMessageId());
				// This is important to check whether the post
				// ep is
				// Triggered. Check
				// au.edu.swin.ict.serendip.event.EventObserver
				try {
				    message.setInterpretedByRule(true);
				} catch (Exception e) {
				    log.fatal(e.getMessage());
				    e.printStackTrace();
				}
			    }
			}
			// //Serendip 1 End////////////////////

			// find the destination role
			Role destRole = destContract.getOppositeRole(role);
			log.info("MessageDeliverer for " + role.getId()
				+ " is routing the message "
				+ message.getMessageId() + " to "
				+ destRole.getId() + " via the contract"
				+ message.getDestinationContract().getId());

			if (compositeRules != null)
			    compositeRules
				    .insertMessageRecievedAtDestinationEvent(
					    message, destRole.getId()); // new

			if (!message.isResponse()) {
			    if (destRole.getRoutingTable().isResponse(
				    message.getOperationName()))
				destRole.getRoutingTable()
					.putResponseSignature(
						message.getOperationName(),
						destContract);
			}

			// place the message in the pending out buffer of the
			// destination role

			destRole.putPendingOutBufMessage(message);
			log.info("MsgDeliverer placed the message in the pendingout of destination role "
				+ destRole.getId());
			this.role
				.getComposite()
				.getBenchUtil()
				.addBenchRecord("MSG OUT",
					message.getMessageId());
			// Serendip 2 start
			// Now the message is safely in the destination role
			// In here we have to let the Serendip engine know about
			// what happened after processing the message.
			// We send our events to Serendip via the role.
			// this.role.fireEvents(interpretedEvents);
			// NOTE: Due to event-message correlation requirements,
			// the Event triggering need to be done after all the
			// messages are safely placed in the destRole buffer
			if (interpretedEvents.size() > 0) {
			    try {
				String pid = this.role
					.fireEvents(interpretedEvents);
			    } catch (SerendipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			    }
			} else {
			    log.info("Message " + message.getMessageId()
				    + " does not interpret any events");
			}
		    }

		} catch (MessageRoutingUndeterminedException e) {
		    log.fatal(e.getMessage());

		    if (compositeRules != null)
			compositeRules.insertRoutingFailureEvent(
				e.getMessageWrapper(), role.getId());

		    role.delivererPutOutgoingMessage(message);
		} catch (MessageRoutingException e) {
		    log.fatal(e.getMessage());
		    role.delivererPutOutgoingMessage(message);
		} catch (NoRequestException e) {
		    log.info(e.getMessage());
		    role.delivererPutOutgoingMessage(message);
		}
	    }
	}// eof while loop

    }
}