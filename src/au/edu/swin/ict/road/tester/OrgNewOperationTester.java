package au.edu.swin.ict.road.tester;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;

public class OrgNewOperationTester {
    private static Logger log = Logger.getLogger(OrgNewOperationTester.class
	    .getName());

    public static void main(String[] args) {
	OrgNewOperationTester tester = new OrgNewOperationTester();
	tester.runTest();
    }

    public void runTest() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// add a new term to wt-cf contract
	String termId = "wt-cf-t3";
	String name = "term 3";
	String messageType = "push";
	String deonticType = "permission";
	String description = "a test term";
	String direction = "AtoB";
	String contractId = "wt-cf";

	// add new term wt-cf-t3
	OrganiserOperationResult or = org.addNewTerm(termId, name, messageType,
		deonticType, description, direction, contractId);

	// add a new operation to term wt-cf-t3
	String operationName = "test";
	String operationReturnType = "int";
	Parameter[] parameters = { new Parameter("String", "test") };

	or = org.addNewOperation(operationName, operationReturnType,
		parameters, termId, contractId);

	/**
	 * make sure we can send a message correctly using the new term and
	 * operation
	 **/
	IRole waiter = smc.getRoleByID("wt");
	IRole chef = smc.getRoleByID("cf");

	// need to add a new permission rule to allow
	or = org.addNewContractRule(
		"rule \"test\" when $event : MessageRecievedEvent(operationName == \"test\") then $event.setBlocked(false); System.out.println(\"test message from waiter to chef\");end",
		contractId);

	// start our player threads
	Thread waiterThread = new Thread(new WaiterPlayer(waiter));
	waiterThread.start();

	Thread chefThread = new Thread(new ChefPlayer(chef));
	chefThread.start();
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
	    MessageWrapper req = new MessageWrapper("test message", "test",
		    false);
	    log.debug("Waiter: sending test message from waiter");
	    MessageWrapper resp = role.putSyncMessage(req);
	    log.debug("Waiter: recieved response");
	}
    }

    public class ChefPlayer implements Runnable {

	private IRole role;

	public ChefPlayer(IRole role) {
	    this.role = role;
	}

	@Override
	public void run() {
	    MessageWrapper req = role.getNextPushMessage();
	    log.debug("Chef: recieved message, op name: "
		    + req.getOperationName());
	    MessageWrapper resp = new MessageWrapper("test message response",
		    "test", true);
	    log.debug("Chef: ending the response from chef");
	    role.putMessage(resp);
	}
    }
}
