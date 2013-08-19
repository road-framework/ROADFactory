package au.edu.swin.ict.serendip.test.wsclient;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.Constants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.log4j.Logger;

public class Client {
    private static Logger log = Logger.getLogger(Client.class.getName());
    static String eprStr = "http://localhost:8080/axis2/services/RoSAS_CL/";

    public static void main(String[] args1) {

	log.debug("Invoking service " + eprStr);
	try {
	    OMElement payload = getPayLoad();
	    EndpointReference targetEPR = new EndpointReference(eprStr);
	    Options options = new Options();

	    options.setTo(targetEPR); // this sets the location of MyService
	    // service
	    options.setAction("report");
	    options.setProperty(
		    Constants.Configuration.DISABLE_ADDRESSING_FOR_OUT_MESSAGES,
		    true);
	    ServiceClient serviceClient = new ServiceClient();
	    serviceClient.setOptions(options);

	    log.debug("Sending message " + payload.toString());
	    serviceClient.fireAndForget(payload);
	    Thread.sleep(10000);

	} catch (AxisFault axisFault) {
	    axisFault.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private static OMElement getPayLoad() {

	OMFactory fac = OMAbstractFactory.getOMFactory();
	OMNamespace omNs = fac.createOMNamespace(
		"http://quickstart.samples/xsd", "tns");

	OMElement method = fac.createOMElement("report", omNs);
	OMElement value = fac.createOMElement("Param", omNs);
	value.addChild(fac.createOMText(value, "Relative"));
	method.addChild(value);
	return method;

    }
}
