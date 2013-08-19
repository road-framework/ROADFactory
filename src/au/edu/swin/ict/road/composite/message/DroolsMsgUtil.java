package au.edu.swin.ict.road.composite.message;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import au.edu.swin.ict.road.composite.MessageDeliverer;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.events.contract.MessageRecievedEvent;
import au.edu.swin.ict.road.regulator.FactObject;
import au.edu.swin.ict.road.regulator.FactTupleSpace;
import au.edu.swin.ict.road.regulator.FactTupleSpaceRow;

/**
 * This is a utility class providing all the methods which need to be invoked
 * from the contract Drools files in USDL scenario
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */
public class DroolsMsgUtil {
    private static Logger log = Logger.getLogger(DroolsMsgUtil.class.getName());

    /**
     * Extracts and returns the user Id in the given MessageWrapper
     * 
     * @param mw
     * @return
     */
    private static String getUserId(MessageWrapper mw) {
	// log.info("start of getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	// log.info("start of getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	// log.info("start of getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	String userId = null;
	SOAPEnvelopeImpl env = (SOAPEnvelopeImpl) mw.getMessage();
	// log.info("SOAP env in  getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + env.toString());
	try {
	    Document document = XMLUtils.newDocument(new InputSource(
		    new StringReader(env.toString())));
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr = xpath.compile("//args0/text()");
	    // log.info("Inside getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	    Object result = expr.evaluate(document, XPathConstants.NODE);
	    // log.info("getUserId result >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	    // + result);
	    if (result != null) {
		Node node = (Node) result;
		userId = node.getNodeValue();
	    }
	} catch (ParserConfigurationException e) {
	    // log.info("Inside getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ParserConfigurationException");
	    e.printStackTrace();
	} catch (SAXException e) {
	    // log.info("Inside getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SAXException");
	    e.printStackTrace();
	} catch (IOException e) {
	    // log.info("Inside getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IOException");
	    e.printStackTrace();
	} catch (XPathExpressionException e) {
	    // log.info("Inside getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> XPathExpressionException");
	    e.printStackTrace();
	}
	// log.info("end of getUserId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + userId);
	return userId;
    }

    /**
     * Extracts and returns the servce id in the given MessageWrapper
     * 
     * @param mw
     * @return
     */
    private static String getServiceId(MessageWrapper mw) {
	// log.info("start of getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	// log.info("start of getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	// log.info("start of getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	String serviceId = null;
	SOAPEnvelopeImpl env = (SOAPEnvelopeImpl) mw.getMessage();
	// log.info("SOAP env in  getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + env.toString());
	try {
	    Document document = XMLUtils.newDocument(new InputSource(
		    new StringReader(env.toString())));
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr = xpath.compile("//args1/text()");
	    // log.info("Inside getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	    Object result = expr.evaluate(document, XPathConstants.NODE);
	    // log.info("getServiceId result >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	    // + result);
	    if (result != null) {
		Node node = (Node) result;
		serviceId = node.getNodeValue();
	    }
	} catch (ParserConfigurationException e) {
	    // log.info("Inside getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ParserConfigurationException");
	    e.printStackTrace();
	} catch (SAXException e) {
	    // log.info("Inside getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SAXException");
	    e.printStackTrace();
	} catch (IOException e) {
	    // log.info("Inside getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IOException");
	    e.printStackTrace();
	} catch (XPathExpressionException e) {
	    // log.info("Inside getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> XPathExpressionException");
	    e.printStackTrace();
	}
	// log.info("end of getServiceId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + serviceId);
	return serviceId;
    }

    /**
     * This method is used to increment the count value of the User Invocation
     * Stat in USDL Scenario
     * 
     * @param mw
     * @param fts
     */
    public static void updateUserInvStat(MessageWrapper mw, FactTupleSpace fts) {
	// if(mw.isResponse())
	// return;

	String userId = DroolsMsgUtil.getUserId(mw);
	boolean userInvFactExists = false;
	FactTupleSpaceRow Invrow = fts
		.getFactTupleSpaceRow("UserInvocationStat");

	if (Invrow != null) {
	    List<FactObject> invStatFacts = Invrow.getFactList();
	    for (FactObject fact : invStatFacts) {
		if (fact.getUniqueId().equals(userId)) {
		    int count = ((Integer) fact.getAttribute("Count"))
			    .intValue();
		    count = count + 1;
		    fact.setAttribute("Count", count);
		    fts.updateFact(fact);
		    userInvFactExists = true;
		    break;
		}
	    }
	    if (!userInvFactExists) {
		FactObject newFact = new FactObject("UserInvocationStat",
			"UserId", userId);
		newFact.setFactSource(FactObject.EXTERNAL_SOURCE);
		newFact.setAttribute("Count", 1);
		fts.updateFact(newFact);
	    }
	}
    }

    /**
     * This method is used to increment the count value of the Service
     * Invocation Stat in USDL Scenario
     * 
     * @param mw
     * @param fts
     */
    public static void updateServiceInvStat(MessageWrapper mw,
	    FactTupleSpace fts) {
	// if(mw.isResponse())
	// return;

	String serviceId = DroolsMsgUtil.getServiceId(mw);
	boolean serviceInvFactExists = false;
	FactTupleSpaceRow servInvrow = fts
		.getFactTupleSpaceRow("ServiceInvocationStat");

	if (servInvrow != null) {
	    List<FactObject> invStatFacts = servInvrow.getFactList();
	    for (FactObject fact : invStatFacts) {
		if (fact.getUniqueId().equals(serviceId)) {
		    int count = ((Integer) fact.getAttribute("Count"))
			    .intValue();
		    count = count + 1;
		    fact.setAttribute("Count", count);
		    fts.updateFact(fact);
		    serviceInvFactExists = true;
		    break;
		}
	    }
	    if (!serviceInvFactExists) {
		FactObject newFact = new FactObject("ServiceInvocationStat",
			"ServiceId", serviceId);
		newFact.setFactSource(FactObject.EXTERNAL_SOURCE);
		newFact.setAttribute("Count", 1);
		fts.updateFact(newFact);
	    }
	}
    }

    /**
     * This method is used to check if a message has to be blocked based on the
     * Block attribute of RegisterUsers fact in USDL scenario
     * 
     * @param mw
     * @param fts
     * @return
     */
    public static boolean isBlocked(MessageWrapper mw, FactTupleSpace fts) {
	if (mw.isResponse()) {
	    return false;
	}

	boolean blocked = true;

	String userId = DroolsMsgUtil.getUserId(mw);
	log.info("User id in isBlocked " + userId);
	FactTupleSpaceRow row = fts.getFactTupleSpaceRow("RegisteredUsers");

	if (row != null) {
	    List<FactObject> regUserFacts = row.getFactList();
	    for (FactObject fact : regUserFacts) {
		if (fact.getUniqueId().equals(userId)) {
		    String attributeValue = (String) fact.getAttribute("Block");
		    log.info("attributeValue in isBlocked " + attributeValue);
		    if (attributeValue != null
			    && attributeValue.equalsIgnoreCase("false"))
			blocked = false;

		    break;
		}
	    }
	}
	return blocked;

    }
}
