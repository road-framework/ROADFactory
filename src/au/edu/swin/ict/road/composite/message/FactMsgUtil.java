package au.edu.swin.ict.road.composite.message;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.soap.impl.llom.SOAPEnvelopeImpl;
import org.apache.axis2.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * This is a uutility class which provides methods to get details from the fact
 * messages
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */
public class FactMsgUtil {

    /**
     * This method extract the fact Id from the given MessageWrapper and returns
     * it
     * 
     * @param mw
     * @return
     */
    public static String getFactId(MessageWrapper mw) {
	String factId = null;
	SOAPEnvelopeImpl env = (SOAPEnvelopeImpl) mw.getMessage();
	// log.info("SOAP env in  getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + env.toString());
	try {
	    Document document = XMLUtils.newDocument(new InputSource(
		    new StringReader(env.toString())));
	    XPath xpath = XPathFactory.newInstance().newXPath();
	    XPathExpression expr = xpath.compile("//args0/text()");
	    // log.info("Inside getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ");
	    Object result = expr.evaluate(document, XPathConstants.NODE);
	    // log.info("getFactId result >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	    // + result);
	    if (result != null) {
		Node node = (Node) result;
		factId = node.getNodeValue();
	    }
	} catch (ParserConfigurationException e) {
	    // log.info("Inside getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ParserConfigurationException");
	    e.printStackTrace();
	} catch (SAXException e) {
	    // log.info("Inside getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> SAXException");
	    e.printStackTrace();
	} catch (IOException e) {
	    // log.info("Inside getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> IOException");
	    e.printStackTrace();
	} catch (XPathExpressionException e) {
	    // log.info("Inside getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> XPathExpressionException");
	    e.printStackTrace();
	}
	// log.info("end of getFactId >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
	// + factId);
	return factId;
    }

}
