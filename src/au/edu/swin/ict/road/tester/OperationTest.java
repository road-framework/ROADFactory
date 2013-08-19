package au.edu.swin.ict.road.tester;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.exceptions.RoleDescriptionGenerationException;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;

public class OperationTest {
    private static Logger log = Logger.getLogger(OperationTest.class.getName());

    public static void main(String[] args) {
	CompositeDemarshaller dm = new CompositeDemarshaller();
	try {
	    // Composite c = dm.demarshalSMC("data/restaurant-smc.xml");
	    Composite c = dm.demarshalSMC("sample/Scenario2/USDL.xml");
	    List<IRole> roleList = c.getCompositeRoles();
	    for (IRole r : roleList) {

		Object desc = r.getProvidedOperationObject();
		Class descC = desc.getClass();
		log.debug(descC.getName());
		log.debug("provided:");
		for (Method m : descC.getDeclaredMethods()) {
		    log.debug(m.getName());
		}

		Object reqc = r.getRequiredOperationObject();
		Class reqC = reqc.getClass();
		log.debug("required:");
		for (Method m : reqC.getDeclaredMethods()) {
		    log.debug(m.getName());
		}
	    }
	} catch (CompositeDemarshallingException e) {
	    e.printStackTrace();
	} catch (ConsistencyViolationException e) {
	    e.printStackTrace();
	} catch (CompositeInstantiationException e) {
	    e.printStackTrace();
	} catch (RoleDescriptionGenerationException e) {
	    e.printStackTrace();
	}

    }

}
