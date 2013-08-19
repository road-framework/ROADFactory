package au.edu.swin.ict.road.composite.testSuite;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IInternalOrganiserView;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.contract.Term;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;

/**
 * @deprecated TODO need to update
 * @author Jutin King, Malinda Kapuruge
 * 
 */
public class CompositeTest {
    public static String sampleFile = "sample/Scenario1/RoSaS.xml";

    @Test
    public void testComposite() {
	fail("Not yet implemented");
    }

    @Test
    public void testRun() {
	fail("Not yet implemented");
    }

    @Test
    public void testStop() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddCompositeAddRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveCompositeAddRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddCompositerRemoveRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveCompositerRemoveRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testAddCompositeUpdateRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testRemoveCompositeUpdateRoleListner() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetCompositeRoles() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetOrganiserRole() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetDescription() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetName() {
	fail("Not yet implemented");
    }

    @Test
    public void testGetRoleByID() {
	fail("Not yet implemented");
    }

    @Test
    public void testToString() {
	fail("Not yet implemented");
    }

    /**
     * Test cases for organiser inner class
     */
    @Test
    public void testOrganiserAddRole() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();

	// should be null
	IRole wt2 = smc.getRoleByID("CO");
	assertNull(wt2);

	String roleId = "CO2";
	String roleName = "CaseOfficer 2";
	String roleDesc = "a test role";

	// should now exist
	OrganiserOperationResult or = org
		.addNewRole(roleId, roleName, roleDesc);
	assertTrue(or.getResult());
	wt2 = smc.getRoleByID("CO2");
	assertNotNull(wt2);

	// check values are correct
	assertTrue(wt2.getId().equals(roleId));
	assertTrue(wt2.getName().equals(roleName));
	assertTrue(wt2.getDescription().equals(roleDesc));
    }

    @Test
    public void testOrganiserRemoveRole() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();

	String roleId = "CO2";
	String roleName = "Case Officer 2";
	String roleDesc = "a test role";

	// add waiter 2 and check if exists
	OrganiserOperationResult or = org
		.addNewRole(roleId, roleName, roleDesc);
	assertTrue(or.getResult());
	IRole wt2 = smc.getRoleByID(roleId);
	assertNotNull(wt2);

	// perform the remove of wt2
	or = org.removeRole(roleId);
	assertTrue(or.getResult());

	// wt2 should be null
	wt2 = smc.getRoleByID(roleId);
	assertNull(wt2);

	/** waiter 1 (pre existing) **/
	// check wt exists
	roleId = "CO";
	IRole wt = smc.getRoleByID(roleId);
	assertNotNull(wt);

	// perform the remove of wt2
	or = org.removeRole(roleId);
	assertTrue(or.getResult());

	// wt should be null
	wt = smc.getRoleByID(roleId);
	assertNull(wt);

	/** remove a non existing role **/
	or = org.removeRole("not a role");
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserGetContractById() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();

	/** get contract waiter to chef (existing) **/
	String contractId = "CO-GR";
	Contract c = org.getContractById(contractId);
	assertNotNull(c);
	assertTrue(c.getId().equals(contractId));

	/** get a non existing contract **/
	c = org.getContractById("not a contract");
	assertNull(c);
    }

    @Test
    public void testOrganiserGetNextManagementMessage() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();
	String testTxt = "test message";

	// drop in a message
	IInternalOrganiserView iov = (IInternalOrganiserView) org;
	MessageWrapper msg = new MessageWrapper(testTxt);
	iov.sendToOrganiser(msg);

	/** test get next message with no timeout **/
	MessageWrapper msg1 = org.getNextManagementMessage();
	assertTrue(((String) msg1.getMessage()).equals(testTxt));

	// make sure the queue is empty again
	MessageWrapper msg2 = org.getNextManagementMessage(1, TimeUnit.SECONDS);
	assertNull(msg2);

	// drop in another message
	MessageWrapper msg3 = new MessageWrapper(testTxt);
	iov.sendToOrganiser(msg3);

	/** test get next message with timeout **/
	MessageWrapper msg4 = org.getNextManagementMessage(1, TimeUnit.SECONDS);
	assertTrue(((String) msg4.getMessage()).equals(testTxt));

	// make sure the queue is empty again
	MessageWrapper msg5 = org.getNextManagementMessage(1, TimeUnit.SECONDS);
	assertNull(msg5);
    }

    @Test
    public void testOrganiserSendManagementMessage() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();

	// drop in a new message to a role
	String roleId = "CO";
	String testTxt = "test message";
	MessageWrapper msg = new MessageWrapper(testTxt);
	OrganiserOperationResult or = org.sendManagementMessage(msg, roleId);
	assertTrue(or.getResult());

	// drop in a message to a non existing role
	or = org.sendManagementMessage(msg, "not a role");
	assertFalse(or.getResult());

	// make sure the message is present at the destination role
	IRole role = smc.getRoleByID(roleId);
	MessageWrapper manMsg = role.getNextManagementMessage();
	assertTrue(((String) manMsg.getMessage()).equals(testTxt));

	// make sure no other roles received the message
	role = smc.getRoleByID("GR");
	manMsg = role.getNextManagementMessage(1, TimeUnit.SECONDS);
	assertNull(manMsg);
    }

    @Test
    public void testOrganiserAddNewContract() {
	Composite smc = this.instantiateRunningSMC(sampleFile);
	IOrganiserRole org = smc.getOrganiserRole();

	// add a new waiter role
	OrganiserOperationResult or = org.addNewRole("CO2", "Case Officer 2",
		" a Case Officer");
	assertTrue(or.getResult());

	// show contract does not exist beforehand
	Contract c = org.getContractById("CO2-GR");
	assertNull(c);

	// add the contract
	or = org.addNewContract("CO2-GR", "GR to CO2 Contract 2",
		" a description", "Incipient", "permissive", "CO-GR.drl",
		false, "CO2", "GR");
	assertTrue(or.getResult());

	// check message flow
	MessageWrapper req = new MessageWrapper("pizza", "orderFood", false);
	smc.getRoleByID("CO2").putMessage(req);

	// response should be null as no terms added to this contract
	MessageWrapper resp = smc.getRoleByID("cf").getNextPushMessage(2,
		TimeUnit.SECONDS);
	assertNull(resp);
    }

    @Test
    public void testOrganiserRemoveContract() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	String contractId = "wt-cf2";
	// show contract does not exist beforehand
	Contract c = org.getContractById("wt-cf2");
	assertNull(c);

	// add a new waiter role
	OrganiserOperationResult or = org.addNewRole("wt2", "waiter 2",
		" a waiter");
	assertTrue(or.getResult());

	// add the contract and verify
	or = org.addNewContract(contractId, "Waiter to Chef Contract 2",
		" a description", "Incipient", "permissive", "waiter-chef.drl",
		false, "wt2", "cf");
	assertTrue(or.getResult());

	c = org.getContractById(contractId);
	assertTrue(c.getId().equals(contractId));

	// remove contract
	or = org.removeContract(contractId);
	assertTrue(or.getResult());

	// should now return null
	c = org.getContractById(contractId);
	assertNull(c);

	// remove a contract that does not exist
	or = org.removeContract("not a contract");
	assertFalse(or.getResult());

    }

    @Test
    public void testOrganiserAddNewTerm() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// add a new term to wt-cf contract
	String termId = "wt-cf-t3";
	String name = "";
	String messageType = "push";
	String deonticType = "permission";
	String description = "a test term";
	String direction = "BtoA";
	String contractId = "wt-cf";

	// add new term wt-cf-t3
	OrganiserOperationResult or = org.addNewTerm(termId, name, messageType,
		deonticType, description, direction, contractId);

	// check was successful
	assertTrue(or.getResult());

	// get the contract we added too
	Contract c = org.getContractById(contractId);

	// c should exist
	assertNotNull(c);

	// check that the new term exists in the contract as expected
	Term addedTerm = c.getTermById(termId);
	assertNotNull(addedTerm);

	// check that all the values are populated
	assertTrue(addedTerm.getId().equals(termId));
	assertTrue(addedTerm.getName().equals(name));
	assertTrue(addedTerm.getMessageType().equals(messageType));
	assertTrue(addedTerm.getDeonticType().equals(deonticType));
	assertTrue(addedTerm.getDescription().equals(description));
	assertTrue(addedTerm.getDirection().equals(direction));
	assertNotNull(addedTerm.getRules());

    }

    @Test
    public void testOrganiserRemoveTerm() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// add a new term to wt-cf contract
	String termId = "wt-cf-t3";
	String name = "";
	String messageType = "push";
	String deonticType = "permission";
	String description = "a test term";
	String direction = "BtoA";
	String contractId = "wt-cf";

	// add new term wt-cf-t3
	OrganiserOperationResult or = org.addNewTerm(termId, name, messageType,
		deonticType, description, direction, contractId);

	// check was successful
	assertTrue(or.getResult());

	// get the contract we added too
	Contract c = org.getContractById(contractId);

	// c should exist
	assertNotNull(c);

	// check that the new term exists in the contract as expected
	Term addedTerm = c.getTermById(termId);
	assertNotNull(addedTerm);

	// remove the term and check was successful
	or = org.removeTerm(contractId, termId);
	assertTrue(or.getResult());

	// confirm the term no longer exists
	Term removedTerm = c.getTermById(termId);
	assertNull(removedTerm);
    }

    @Test
    public void testOrganiserAddNewOperation() {
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

	// check was successful
	assertTrue(or.getResult());

	// try and add an operation to a term that does not exist
	or = org.addNewOperation(operationName, operationReturnType,
		parameters, "no term", contractId);

	// check was not successful
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserRemoveOperation() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// add remove an operation from term 2
	OrganiserOperationResult or = org.removeOperation("deliverFood",
		"wt-cf-t2");

	// check was successful
	assertTrue(or.getResult());

	// try to remove an operation from a term that does not exist
	or = org.removeOperation("deliverFood", "wt-cf-t3");

	// check was not successful
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserAddNewContractRule() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	String contractId = "wt-cf";

	// add a valid rule to an existing contract
	OrganiserOperationResult or = org
		.addNewContractRule(
			"rule \"test\" when $event : MessageRecievedEvent(operationName == \"test\") then $event.setBlocked(false); System.out.println(\"test message from waiter to chef\");end",
			contractId);

	// check was successful
	assertTrue(or.getResult());

	// add a valid rule to a non existing contract
	or = org.addNewContractRule(
		"rule \"test\" when $event : MessageRecievedEvent(operationName == \"test\") then $event.setBlocked(false); System.out.println(\"test message from waiter to chef\");end",
		"not a contract");

	// check was not successful
	assertFalse(or.getResult());

	// add an invalid rule to an existing contract
	or = org.addNewContractRule("not a rule", contractId);

	// check was not successful
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserRemoveContractRule() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	String contractId = "wt-cf";

	// remove an existing rule
	OrganiserOperationResult or = org.removeContractRule(contractId,
		"Order Food");

	// check was successful
	assertTrue(or.getResult());

	// remove a non existing rule (already removed)
	or = org.removeContractRule(contractId, "Order Food");

	// check was not successful
	assertFalse(or.getResult());

	// remove a rule from a non existing contract
	or = org.removeContractRule("not a contract", "Order Food");

	// check was not successful
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserAddCompositeRule() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// add a valid rule to the composite
	OrganiserOperationResult or = org
		.addNewCompositeRule("rule \"TestRule\"when $event : MessageRecievedAtSourceEvent()then organiser.sendToOrganiser(new MessageWrapper($event));end");

	// check was successful
	assertTrue(or.getResult());

	// add an invalid rule
	or = org.addNewCompositeRule("not a rule");

	// check was not successful
	assertFalse(or.getResult());
    }

    @Test
    public void testOrganiserRemoveCompositeRule() {
	Composite smc = this.instantiateRunningSMC("data/restaurant-smc.xml");
	IOrganiserRole org = smc.getOrganiserRole();

	// remove an existing rule
	OrganiserOperationResult or = org
		.removeCompositeRule("MessageRecievedAtSource");

	// check was successful
	assertTrue(or.getResult());

	// remove a non existing rule
	or = org.removeCompositeRule("not a rule");

	// check was not successful
	assertFalse(or.getResult());
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
}
