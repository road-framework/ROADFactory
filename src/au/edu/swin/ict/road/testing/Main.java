package au.edu.swin.ict.road.testing;

import java.io.File;

import org.apache.axis2.deployment.DeploymentException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.tool.gui.AdminView;

public class Main {
    private static final Log log = LogFactory.getLog(Main.class);

    public static void main(String[] args) throws SerendipException {

	Composite composite = null;
	String filePath = "E:\\ROAD\\workspace\\SerendipMerge\\sample\\Scenario\\RoSaS.xml";
	File file = new File(filePath);
	if (file == null) {
	    log.error("File Not found" + filePath);
	    System.exit(0);
	}

	CompositeDemarshaller demarsheller = new CompositeDemarshaller();
	try {
	    log.debug("Loading SMC from " + file.getAbsolutePath());
	    composite = demarsheller.demarshalSMC(file.getAbsolutePath());
	    if (null == composite) {
		log.error("Cannot instantiate the composite from file "
			+ file.getAbsoluteFile());
	    }

	} catch (CompositeDemarshallingException e) {
	    e.printStackTrace();
	} catch (ConsistencyViolationException e) {
	    e.printStackTrace();
	} catch (CompositeInstantiationException e) {
	    e.printStackTrace();
	}

	Thread compo = new Thread(composite);
	compo.start();

	// Add a new message
	// composite.getRoleByID("MM").putMessage(new MessageWrapper(null, "",
	// false));

	// Test starting a new instance
	SerendipEngine engine = composite.getSerendipEngine();

	// Lets create the GUI
	AdminView adminView = new AdminView(composite.getSerendipEngine());

	// Test adding events
	try {
	    Thread.sleep(10000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	for (int i = 0; i < 100; i++) {// We fire three initial events

	    composite.getSerendipEngine().addEvent(
		    new EventRecord("[ComplainRcvd]", null));// This will
							     // instantiate a
							     // process instance
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

    }
}
