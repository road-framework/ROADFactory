package au.edu.swin.ict.road.tester;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;

public class OrgRemoveOperationTester {
    private static Logger log = Logger.getLogger(OrgRemoveOperationTester.class
	    .getName());

    public static void main(String[] args) {
	OrgRemoveOperationTester tester = new OrgRemoveOperationTester();
	tester.runTest();
    }

    public void runTest() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	IRole waiter = smc.getRoleByID("wt");

	OrganiserOperationResult or;

	or = org.removeContractRule("wt-cf", "Order Food");
	log.debug(or.getMessage());

	// remove the order food operation
	// or = org.removeOperation("orderFood", "wt-cf-t1");

	// start our player threads
	Thread waiterThread = new Thread(new WaiterPlayer(waiter));
	waiterThread.start();
    }

    private Composite instantiateRunningSMC(String file) {
	Composite c = null;
	try {
	    CompositeDemarshaller dm = new CompositeDemarshaller();
	    c = dm.demarshalSMC(file);
	    Thread cthread = new Thread(c);
	    cthread.start();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// sleep to give composite time to start
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

	return c;
    }

    public class WaiterPlayer implements Runnable {

	private IRole role;

	public WaiterPlayer(IRole role) {
	    this.role = role;
	}

	@Override
	public void run() {
	    MessageWrapper req = new MessageWrapper("rice", "orderFood", false);
	    MessageWrapper resp = role.putSyncMessage(req);
	    log.debug("Waiter: recieved response");
	}
    }
}
