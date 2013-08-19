package au.edu.swin.ict.road.tester;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;

public class SyncTester {
    private static Logger log = Logger.getLogger(SyncTester.class.getName());

    public static void main(String[] args) {
	SyncTester tester = new SyncTester();
	tester.test();
    }

    public void test() {
	CompositeDemarshaller dm = new CompositeDemarshaller();

	Composite c;
	try {
	    c = dm.demarshalSMC("data/restaurant-smc.xml");

	    Thread worker = new Thread(c);
	    worker.start();

	    IRole waiterRole = c.getRoleByID("wt");
	    IRole chefRole = c.getRoleByID("cf");

	    Thread chefThread = new Thread(new ChefThread(chefRole));
	    Thread waiterThread = new Thread(new WaiterThread(waiterRole));

	    chefThread.start();
	    waiterThread.start();
	} catch (CompositeDemarshallingException e) {
	    e.printStackTrace();
	} catch (ConsistencyViolationException e) {
	    e.printStackTrace();
	} catch (CompositeInstantiationException e) {
	    e.printStackTrace();
	}
    }

    public class ChefThread implements Runnable {

	private IRole role;

	public ChefThread(IRole role) {
	    this.role = role;
	}

	@Override
	public void run() {
	    log.debug("chef is waiting for a message");
	    MessageWrapper msg = role.getNextPushMessage();
	    MessageWrapper response = new MessageWrapper("Heres your rice!",
		    "orderFood", true);
	    role.putMessage(response);
	    log.debug("chef responded");
	}
    }

    public class WaiterThread implements Runnable {

	private IRole role;

	public WaiterThread(IRole role) {
	    this.role = role;
	}

	@Override
	public void run() {
	    MessageWrapper msg = new MessageWrapper("Fried Rice!", "orderFood",
		    false);
	    log.debug("waiter is sending a message");
	    MessageWrapper response = role.putSyncMessage(msg);
	    log.debug(response.getMessage());
	}

    }
}
