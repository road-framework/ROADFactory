package au.edu.swin.ict.road.composite.transform;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.MessageWrapper.SyncType;
import au.edu.swin.ict.road.composite.message.analyzer.MessageAnalyzer;
import au.edu.swin.ict.road.composite.message.analyzer.TransformLogic;
import au.edu.swin.ict.road.composite.message.analyzer.XSLTAnalyzer;
import au.edu.swin.ict.road.composite.message.containers.QueueListener;
import au.edu.swin.ict.road.xml.bindings.InMsgType;
import au.edu.swin.ict.road.xml.bindings.OutMsgType;
import au.edu.swin.ict.road.xml.bindings.RoleType;
import au.edu.swin.ict.road.xml.bindings.SrcMsgType;
import au.edu.swin.ict.road.xml.bindings.SrcMsgsType;
import au.edu.swin.ict.road.xml.bindings.TaskType;
import au.edu.swin.ict.road.xml.bindings.TasksType;

/**
 * This class is creates the TransformLogic object and invokes the conjunct
 * method of MessageAnalyzer. The transform method of this class is invoked by
 * serendip when all source messages in the task Out are available in the
 * pending Out buffer.
 * 
 * @author Malinda Kapuruge
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */
public class OutTransform implements QueueListener {

    private static Logger log = Logger.getLogger(OutTransform.class.getName());
    private Role role;

    public final static String MSG_ID_SEPERATOR = ".";
    public final static String MSG_ID_REQUEST = "Req";
    public final static String MSG_ID_RESPONSE = "Res";

    public OutTransform(Role role) {
	this.role = role;
    }

    /**
     * This method is invoked by serendip when all source messages in the task
     * Out are available in the pending Out buffer.
     * 
     * @param role
     * @param taskId
     * @param correlationId
     */
    public static void transform(Role role, String taskId, String correlationId) {

	// if(role == null || taskId == null || correlationId == null)
	// {
	// log.info("Required Details missing");
	// }

	// this is the resultant message reference
	MessageWrapper conjunctMessage = null;
	// get all the tasks in this role
	RoleType roleType = role.getRoleType();
	TasksType tasks = roleType.getTasks();
	// if there are no tasks then return
	if (tasks == null) {
	    // log.info("Role " + role.getName() + " has no tasks.");
	    return;
	}

	// iterate over the list of tasks
	for (TaskType task : tasks.getTask()) {
	    // get the MessageAnalyzer declared in the task and instantiate it.
	    MessageAnalyzer analyzer;
	    try {
		String analyzerClass = task.getMsgAnalyser();
		if (analyzerClass != null)
		    analyzer = (MessageAnalyzer) Class.forName(analyzerClass)
			    .newInstance();
	    } catch (InstantiationException e) {
		e.printStackTrace();
	    } catch (IllegalAccessException e) {
		e.printStackTrace();
	    } catch (ClassNotFoundException e) {
		e.printStackTrace();
	    } finally {
		// if MessageAnalyzer is not declared, create the default
		// MessageAnalyzer
		// log.info("Default MessageAnalyzer is used");
		analyzer = new XSLTAnalyzer();
	    }
	    // this is the transform logic object which holds all the details
	    // required for transformation
	    TransformLogic tLogic = new TransformLogic();

	    // if the task if matches with the task id passed by serendip, then
	    // proceed
	    if (task.getId().equalsIgnoreCase(taskId)) {
		// get the task Out and if it is null continue to the next task
		OutMsgType outMsgType = task.getOut();
		if (outMsgType == null)
		    continue;
		// get all the source messages which need to be merged
		SrcMsgsType srcMsgsType = task.getSrcMsgs();
		if (srcMsgsType == null)
		    break;
		List<SrcMsgType> srcMsgs = srcMsgsType.getSrcMsg();
		// get the xslt used for transformation
		String xsltFilePath = srcMsgsType.getTransformation();
		// get the message delivery type
		String deliveryType = outMsgType.getDeliveryType();
		boolean isResponse = outMsgType.isIsResponse();

		if (!isResponse) {
		    InMsgType inMsgType = task.getIn();
		    if (inMsgType != null && inMsgType.isIsResponse()) {
			tLogic.setSyncType(SyncType.OUTIN);
		    } else {
			tLogic.setSyncType(SyncType.OUT);
		    }
		}
		// the list which holds all the source messages
		List<MessageWrapper> messages = new ArrayList<MessageWrapper>();
		// iterate over the source messages and pick the source message
		// objects
		// from the pending out buffer and add it to the messages list
		for (SrcMsgType srcMsg : srcMsgs) {
		    String msgId = srcMsg.getContractId() + MSG_ID_SEPERATOR
			    + srcMsg.getTermId() + MSG_ID_SEPERATOR;

		    msgId += (srcMsg.isIsResponse()) ? MSG_ID_RESPONSE
			    : MSG_ID_REQUEST;
		    // get the message from the pending out buffer using the
		    // message
		    // id and correlation id
		    MessageWrapper message = role.pollPendingOutBufMessage(
			    msgId, correlationId);

		    if (null != message) {
			messages.add(message);
		    } else {
			log.error("Message retrieval failed (" + msgId + ","
				+ correlationId + ") from PendingOutBuf of "
				+ role.getId());
		    }

		}

		// if xslt is not provided and there is only 1 source message,
		// then no transformation is required. This only message becomes
		// the result conjunct message
		if (xsltFilePath == null && messages.size() == 1) {
		    conjunctMessage = messages.get(0);
		    conjunctMessage.setDestinationPlayerBinding(role
			    .getPlayerBinding());
		} else {
		    // set all the transformation details in the tLogic
		    tLogic.setTaskId(task.getId());
		    tLogic.addXsltFilePath(xsltFilePath);
		    tLogic.setOperationName(outMsgType.getOperation().getName());
		    tLogic.setDeliveryType(deliveryType);
		    tLogic.setResponse(isResponse);
		    tLogic.setRole(role);

		    // invoke conjunct methods on the MessageAnalyzer
		    try {
			role.getComposite().getBenchUtil()
				.addBenchRecord("OUTTRANS BEGIN", task.getId());
			conjunctMessage = analyzer.conjunct(messages, tLogic);
			role.getComposite().getBenchUtil()
				.addBenchRecord("OUTTRANS END", task.getId());
			conjunctMessage.setCorrelationId(correlationId);
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		}

		log.info("Transformed to OutMessage "
			+ conjunctMessage.getMessageId());

		// based on the delivery type of the conjunct message,
		// place it in the approprate out queue
		if (conjunctMessage.getMessageType().equalsIgnoreCase("pull"))
		    role.delivererPutOutgoingMessage(conjunctMessage);
		else if (conjunctMessage.isResponse())
		    role.delivererPutOutgoingSyncMessage(conjunctMessage);
		else
		    role.workerPutOutgoingPushMessage(conjunctMessage);// Common
								       // push
								       // delivery

	    }
	}
    }

    /**
     * This method is invoked whenever a message is placed in the pendingOutBuf
     * of the associated role
     * 
     * @see au.edu.swin.ict.road.composite.message.containers.QueueListener#
     *      messageReceived()
     */
    @Override
    public void messageReceived(MessageWrapper message) {
	// log.info("start of message received");
	// get the rolType and the tasks
	RoleType roleType = role.getRoleType();
	TasksType tasksType = roleType.getTasks();
	// if there are no tasks for this role, then move the message to
	// respective outQueue.
	// if destination contract is null, it is a facy message
	if (tasksType == null || message.getDestinationContract() == null) {
	    // log.info("no tasks for role >>> " + role.getName());
	    role.removePendingOutBufMessage(message.getMessageId(), null);
	    message.setDestinationPlayerBinding(role.getPlayerBinding());
	    // keep the message in the respetive outQueue
	    if (message.getMessageType().equalsIgnoreCase("pull")) {
		role.delivererPutOutgoingMessage(message);
	    } else if (message.isResponse()) {
		role.delivererPutOutgoingSyncMessage(message);
	    } else {
		role.workerPutOutgoingPushMessage(message);
	    }
	    return;
	}
	// get the operation name
	String operationName = message.getOperationName();
	// term corresponding to this message
	Term messageTerm = null;
	// get the contract
	Contract destContract = message.getDestinationContract();
	// get the list of terms in the contract
	List<Term> terms = destContract.getTermList();
	// loop over list of terms in the contract to find the term
	// corresponding to this message
	for (Term term : terms) {
	    // if the operation name of the pending out buffer msg
	    // equals to the operation name of the term, then this is
	    // the term corresponsing to the message.
	    if (term.getOperation().getName().equalsIgnoreCase(operationName)) {
		messageTerm = term;
		break;
	    }
	}
	String messageIdentifier = destContract.getId() + messageTerm.getId();
	// get all the tasks in the role
	List<TaskType> tasks = tasksType.getTask();
	TaskType messageTask = null;
	// loop over the list of tasks
	for (TaskType task : tasks) {
	    SrcMsgsType srcMsgsType = task.getSrcMsgs();
	    // if the task has no src msgs, then continue to the next task
	    if (srcMsgsType == null) {
		continue;
	    }
	    // get the list of src msgs
	    List<SrcMsgType> srcMsgs = srcMsgsType.getSrcMsg();
	    // loop over the list of src msgs and add the src msg
	    // identifiers to the set
	    for (SrcMsgType srcMsg : srcMsgs) {
		if ((srcMsg.getContractId() + srcMsg.getTermId())
			.equalsIgnoreCase(messageIdentifier)) {
		    messageTask = task;
		    break;
		}
	    }
	}
	// the list which holds all the source messages
	List<MessageWrapper> messages = new ArrayList<MessageWrapper>();

	if (messageTask != null) {
	    if (messageTask.isIsMsgDriven()) {
		boolean doConjunct = true;
		SrcMsgsType srcMsgsType = messageTask.getSrcMsgs();
		List<SrcMsgType> srcMsgs = srcMsgsType.getSrcMsg();
		for (SrcMsgType srcMsg : srcMsgs) {
		    String msgId = getSrcMsgId(srcMsg);
		    if (msgId == null) {
			doConjunct = false;
			break;
		    }

		    MessageWrapper mw = role.peekPendingOutBufMessage(msgId,
			    null);
		    if (mw == null) {
			doConjunct = false;
			log.info("Failed to retrieve message with msgId = "
				+ msgId + " from PendingOutBuf ");
			break;
		    }
		    messages.add(mw);
		}
		if (doConjunct) {
		    // remove messages from pending out buffer before doing
		    // the conjunct
		    for (MessageWrapper aMessage : messages) {
			role.removePendingOutBufMessage(
				aMessage.getMessageId(), null);
		    }

		    MessageAnalyzer analyzer;
		    try {
			String analyzerClass = messageTask.getMsgAnalyser();
			if (analyzerClass != null)
			    analyzer = (MessageAnalyzer) Class.forName(
				    analyzerClass).newInstance();
		    } catch (InstantiationException e) {
			e.printStackTrace();
		    } catch (IllegalAccessException e) {
			e.printStackTrace();
		    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		    } finally {
			// if MessageAnalyzer is not declared, create the
			// default
			// MessageAnalyzer
			// log.info("Default MessageAnalyzer is used");
			analyzer = new XSLTAnalyzer();
		    }

		    OutMsgType outMsgType = messageTask.getOut();
		    if (outMsgType == null) {
			// log.info("Missing out tag in Task with id : "
			// + messageTask.getId() + " in role : "
			// + role.getName());
			return;
		    }

		    MessageWrapper conjunctMessage = null;
		    String xsltFilePath = messageTask.getSrcMsgs()
			    .getTransformation();
		    if (xsltFilePath == null && messages.size() == 1) {
			conjunctMessage = messages.get(0);
			conjunctMessage.setDestinationPlayerBinding(role
				.getPlayerBinding());
		    } else {
			// this is the transform logic object which holds all
			// the details
			// required for transformation
			TransformLogic tLogic = new TransformLogic();

			boolean isResponse = outMsgType.isIsResponse();
			if (!isResponse) {
			    InMsgType inMsgType = messageTask.getIn();
			    if (inMsgType != null && inMsgType.isIsResponse()) {
				tLogic.setSyncType(SyncType.OUTIN);
			    } else {
				tLogic.setSyncType(SyncType.OUT);
			    }
			}

			// set all the transformation details in the tLogic
			tLogic.setTaskId(messageTask.getId());
			tLogic.addXsltFilePath(xsltFilePath);
			tLogic.setDeliveryType(outMsgType.getDeliveryType());
			tLogic.setOperationName(outMsgType.getOperation()
				.getName());
			tLogic.setResponse(isResponse);
			tLogic.setRole(role);
			// invoke conjunct methods on the MessageAnalyzer
			try {
			    conjunctMessage = analyzer.conjunct(messages,
				    tLogic);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		    }

		    if (conjunctMessage.getMessageType().equalsIgnoreCase(
			    "pull"))
			role.delivererPutOutgoingMessage(conjunctMessage);
		    else if (conjunctMessage.isResponse())
			role.delivererPutOutgoingSyncMessage(conjunctMessage);
		    else
			role.workerPutOutgoingPushMessage(conjunctMessage);
		}
	    }
	} else {
	    role.removePendingOutBufMessage(message.getMessageId(), null);
	    message.setDestinationPlayerBinding(role.getPlayerBinding());
	    // keep the message in the respetive outQueue
	    if (message.getMessageType().equalsIgnoreCase("pull"))
		role.delivererPutOutgoingMessage(message);
	    else if (message.isResponse())
		role.delivererPutOutgoingSyncMessage(message);
	    else
		role.workerPutOutgoingPushMessage(message);
	}
	// log.info("end of message received");
    }

    private String getSrcMsgId(SrcMsgType srcMsg) {
	String msgId = null;
	String contractId = srcMsg.getContractId();
	String reqOrRes = srcMsg.isIsResponse() ? "Res" : "Req";
	String opName = null;
	Contract[] contracts = role.getAllContracts();
	for (Contract contract : contracts) {
	    if (contract.getId().equalsIgnoreCase(contractId)) {
		Term term = contract.getTermById(srcMsg.getTermId());
		if (term != null) {
		    opName = term.getOperation().getName();
		}
		break;
	    }
	}
	msgId = contractId + "." + opName + "." + reqOrRes;
	return msgId;
    }
}
