package au.edu.swin.ict.road.composite.message.analyzer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.axiom.om.util.StAXUtils;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.impl.builder.StAXSOAPModelBuilder;
import org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.xml.bindings.ResultMsgType;
import au.edu.swin.ict.road.xml.bindings.TaskType;

/**
 * This is the default message analyzer implementation which makes use of XSLT
 * for conjunct and disjunct transformations.
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */

public class XSLTAnalyzer implements MessageAnalyzer {

    // get the logger
    private static Logger log = Logger.getLogger(XSLTAnalyzer.class.getName());
    public final static String MSG_ID_SEPERATOR = ".";
    public final static String MSG_ID_REQUEST = "Req";
    public final static String MSG_ID_RESPONSE = "Res";

    /**
     * This method is used to merge multiple messages using the xslt
     * transformation provided by the TransformLogic object.
     * 
     * @see au.edu.swin.ict.road.composite.message.analyzer.MessageAnalyzer#conjunct
     *      (java.util.List,
     *      au.edu.swin.ict.road.composite.message.analyzer.TransformLogic)
     */
    @Override
    public MessageWrapper conjunct(List<MessageWrapper> messages,
	    TransformLogic tLogic) throws Exception {
	MessageWrapper conjunctMessagewrapper = new MessageWrapper();
	// get the xslt file from TransformLogic logic and retrieve it from the
	// folder transformation in AXIS2_HOME
	// String xsltPath = System.getenv("AXIS2_HOME") + "/transformation/"
	// + tLogic.getXsltFilePath().get(0);
	String transDir = tLogic.getRole().getComposite().getTransDir();
	String xsltPath = transDir + tLogic.getXsltFilePath().get(0);
	// create TransformerFactory and then the Transfomer to perform xslt
	// transformation
	TransformerFactory tFactory = TransformerFactory.newInstance();
	try {
	    Transformer transformer = tFactory.newTransformer(new StreamSource(
		    xsltPath));
	    // log.info("XSLT analyzer for " + tLogic.getRole().getId() +
	    // ". No of messages ::: " + messages.size());
	    // iterate over the list of messages passed to this method
	    for (MessageWrapper message : messages) {
		// get the SOAP message, transform it to a DOM document and set
		// it as an attribute to the transformer.
		// This attribute will be used to reference this message in the
		// xslt.
		String xsltParamName = message.getMessageId();
		SOAPEnvelopeImpl env = (SOAPEnvelopeImpl) message.getMessage();
		// log.info("XSLT param name: " + xsltParamName);
		// log.info(">>>>>>>> SOAPEnvelope ::: " + env.toString());
		// env.setNamespace(new
		// OMNamespaceImpl("http://schemas.xmlsoap.org/soap/envelope/",
		// "soapenv"));
		// Document document =
		// SAAJUtil.getDocumentFromSOAPEnvelope(env);
		// Document document =
		// getSOAPMsgDocument(env.getBody().getFirstElement().toString());
		Document document = XMLUtils.newDocument(new InputSource(
			new StringReader(env.toString())));
		// DocumentBuilderFactory factory = DocumentBuilderFactory
		// .newInstance();
		// InputSource source = new InputSource(new
		// StringReader(env.toString()));
		// Document document =
		// factory.newDocumentBuilder().parse(source);
		transformer.setParameter(xsltParamName, document);
	    }
	    Writer sWriter = new StringWriter();
	    // once all messages are set as attributes to the transormer
	    // perform the xslt transformation
	    transformer
		    .transform(new StreamSource(), new StreamResult(sWriter));

	    XMLStreamReader reader = StAXUtils
		    .createXMLStreamReader(new ByteArrayInputStream(sWriter
			    .toString().getBytes()));
	    StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader,
		    null);
	    SOAPEnvelope conjunctSoapEnv = builder.getSOAPEnvelope();
	    // conjunctSoapEnv.setNamespace(new
	    // OMNamespaceImpl("http://www.w3.org/2003/05/soap-envelope",
	    // "soapenv"));
	    // log.info(">>>>>>>>>>> conjunct soap envelope ::: " +
	    // conjunctSoapEnv);
	    // set the SOAP message and other required details to the final
	    // message wrapper
	    conjunctMessagewrapper.setMessage(conjunctSoapEnv);
	    conjunctMessagewrapper.setTaskId(tLogic.getTaskId());
	    conjunctMessagewrapper.setOperationName(tLogic.getOperationName());
	    conjunctMessagewrapper.setDestinationPlayerBinding(tLogic.getRole()
		    .getPlayerBinding());
	    conjunctMessagewrapper.setMessageType(tLogic.getDeliveryType());
	    conjunctMessagewrapper.setMessageId(tLogic.getTaskId() + ".OutMsg");
	    conjunctMessagewrapper.setResponse(tLogic.isResponse());
	    conjunctMessagewrapper.setSyncType(tLogic.getSyncType());

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw e;
	}

	return conjunctMessagewrapper;
    }

    /**
     * This method is used to split a single message passed to it into multiple
     * messages using the xslt provided by TransformLogic object.
     * 
     * @see au.edu.swin.ict.road.composite.message.analyzer.MessageAnalyzer#disjunct
     *      (au.edu.swin.ict.road.composite.message.MessageWrapper,
     *      au.edu.swin.ict.road.composite.message.analyzer.TransformLogic)
     */
    @Override
    public List<MessageWrapper> disjunct(MessageWrapper msgToDisjunct,
	    TransformLogic tLogic) throws Exception {
	// get the conposite, role and the list of xslts used for the
	// transformations
	Composite smc = tLogic.getRole().getComposite();
	List<String> xsltPaths = tLogic.getXsltFilePath();
	Role role = tLogic.getRole();
	String taskId = tLogic.getTaskId();
	// get the list of tasks in this role
	List<TaskType> tasks = role.getRoleType().getTasks().getTask();
	TaskType currentTask = null;
	// iterate over the task list and get the contractId, termId and the
	// response flag
	for (TaskType task : tasks) {
	    if (task.getId().equalsIgnoreCase(taskId)) {
		currentTask = task;
		break;
	    }
	}

	// the list which will hold all the messages generated from the
	// transformations
	List<MessageWrapper> messages = new ArrayList<MessageWrapper>();
	// get the SOAP message from the message passed to this method
	SOAPEnvelopeImpl env = (SOAPEnvelopeImpl) msgToDisjunct.getMessage();
	// log.info("In XSLTAnalyzer msgToDisjunct SOAPEnvelope >>>>>>> " +
	// env.toString());
	// create the TransformerFactory which is used in performing the
	// transformations
	TransformerFactory tFactory = TransformerFactory.newInstance();
	try {
	    Document document = XMLUtils.newDocument(new InputSource(
		    new StringReader(env.toString())));
	    // get the soap message source which will then be transformed as per
	    // each of the xslts provided by the TransformLogic
	    // Source soapResMessageSource = new StreamSource(new
	    // StringReader(env.toString()));
	    // iterate over the list of xslts provided
	    for (int i = 0; i < xsltPaths.size(); i++) {
		// the transformation result would be set to this message object
		MessageWrapper aMessageWrapper = new MessageWrapper();
		// create the transformer using the one of the xslts. It
		// retrives the
		// xslt from the transformation folder in AXIS2_HOME
		String transDir = tLogic.getRole().getComposite().getTransDir();
		Transformer transformer = tFactory
			.newTransformer(new StreamSource(transDir
				+ xsltPaths.get(i)));
		transformer.setParameter(taskId + ".doneMsg", document);
		Writer sWriter = new StringWriter();
		// perform the transformation, create the soap message from the
		// transformation result and set all the details for this
		// message
		transformer.transform(new StreamSource(), new StreamResult(
			sWriter));

		XMLStreamReader reader = StAXUtils
			.createXMLStreamReader(new ByteArrayInputStream(sWriter
				.toString().getBytes()));
		StAXSOAPModelBuilder builder = new StAXSOAPModelBuilder(reader,
			null);
		SOAPEnvelope disjunctSoapEnv = builder.getSOAPEnvelope();

		// log.info("Disjunct soap envelope: " + disjunctSoapEnv);
		aMessageWrapper.setMessage(disjunctSoapEnv);
		aMessageWrapper.setOriginRole(role);

		ResultMsgType resultMsg = currentTask.getResultMsgs()
			.getResultMsg().get(i);
		String contractId = resultMsg.getContractId();
		String interactiveTermId = resultMsg.getTermId();
		boolean isResponse = resultMsg.isIsResponse();

		// get the destination contract details using the contract id
		// and set it to the result message wrapper
		Contract contract = smc.getContractMap().get(contractId);
		aMessageWrapper.setDestinationContract(contract);
		// get the operation name using the termId and set it to the
		// result message wrapper
		Term term = contract.getTermById(interactiveTermId);
		if (null == term) {
		    throw new Exception("Cannot find term " + interactiveTermId
			    + " in cotract " + contract.getId());
		}
		aMessageWrapper.setOperationName(term.getOperation().getName());
		// set the response flag
		aMessageWrapper.setResponse(isResponse);
		// create the message id and set it to the result message
		// wrapper
		String msgId = contractId + MSG_ID_SEPERATOR
			+ interactiveTermId + MSG_ID_SEPERATOR;
		msgId += isResponse ? MSG_ID_RESPONSE : MSG_ID_REQUEST;
		aMessageWrapper.setMessageId(msgId);

		// Set the correlation id that of the message subjected to
		// disjunct
		if (msgToDisjunct.getCorrelationId() != null) {
		    aMessageWrapper.setCorrelationId(msgToDisjunct
			    .getCorrelationId());
		}
		// add it to the list of result messages
		messages.add(aMessageWrapper);
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	    throw e;
	}

	return messages;
    }

}
