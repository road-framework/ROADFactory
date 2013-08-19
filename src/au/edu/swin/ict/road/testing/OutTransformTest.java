package au.edu.swin.ict.road.testing;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.transform.OutTransform;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;

public class OutTransformTest {
    private static final Log log = LogFactory.getLog(OutTransformTest.class);

    public static void main(String[] args) {
	Composite composite = null;
	File file = new File(
		"C:\\Chandu\\Semester 4\\Internship\\ROAD\\workspace\\SerendipMerge\\sample\\Scenario\\TestScenario2.xml");
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

	Role tcRole = (Role) composite.getRoleByID("TC");
	Role coRole = (Role) composite.getRoleByID("CO");
	MessageWrapper coMessage = new MessageWrapper();
	coRole.putMessage(coMessage);
	Role grRole = (Role) composite.getRoleByID("GR");
	MessageWrapper grMessage = new MessageWrapper();
	grRole.putMessage(grMessage);
	OutTransform.transform(tcRole, "Tow", "");
	tcRole.printOutQ();
    }
}
