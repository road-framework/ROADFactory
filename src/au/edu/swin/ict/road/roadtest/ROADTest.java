package au.edu.swin.ict.road.roadtest;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.contract.Parameter;
import au.edu.swin.ict.road.composite.exceptions.CompositeInstantiationException;
import au.edu.swin.ict.road.composite.exceptions.ConsistencyViolationException;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.road.demarshalling.CompositeDemarshaller;
import au.edu.swin.ict.road.demarshalling.exceptions.CompositeDemarshallingException;
import au.edu.swin.ict.road.roadtest.UI.Organiser.OrganiserEvent;
import au.edu.swin.ict.road.roadtest.UI.Organiser.OrganiserListener;
import au.edu.swin.ict.road.roadtest.connect.IServerRMI;
import au.edu.swin.ict.road.roadtest.connect.RoadTestServer;
import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;

/**
 * ROADTest is the main class to which it has to be interacted to test a
 * composition. A XML file needs to be passed to the constructor which contains
 * the SMC. From this Players for each role will be generated with which the
 * generated composite can be controlled (sending and receiving messages).
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public class ROADTest {
    private static Logger log = Logger.getLogger(ROADTest.class.getName());
    private Composite composite;
    private List<IRole> roleList;
    // private Map<String, Contract> contractList;
    private Map<String, Player> playerMap;
    private int newPort;
    // private FileHandler fHandler;

    private IOrganiserRole organiserRole;

    private Set<OrganiserListener> listeners;

    /**
     * This is the log for the ROADTest which gives information of ROADTest
     */
    public static Logger logROADTest = Logger.getLogger(ROADTest.class
	    .getName());

    /**
     * This will create and incite all players! Will not start the composite. It
     * needs to be started with the method <code>startComposite()</code>
     * 
     * @param smcFile
     *            The path to a xml-smc file as a <code>String</code>
     * @throws CompositeDemarshallingException
     * @throws ConsistencyViolationException
     * @throws CompositeInstantiationException
     */
    public ROADTest(String smcFile) throws CompositeDemarshallingException,
	    ConsistencyViolationException, CompositeInstantiationException {
	logROADTest.setLevel(Level.ALL);

	// String logFileName = "ROADTestLog-"
	// + Calendar.getInstance().getTime().getTime() + ".xml";
	// try {
	// //fHandler = new FileHandler(logFileName);
	// //logROADTest.addHandler(fHandler);
	// } catch (SecurityException e) {
	// logROADTest
	// .log(Level.FATAL,
	// "ROADTest: Following exception occured while trying to start logging: "
	// + e.getClass().getName()
	// + "; with the following Message: "
	// + e.getMessage());
	// e.printStackTrace();
	// } catch (IOException e) {
	// logROADTest
	// .log(Level.FATAL,
	// "ROADTest: Following exception occured while trying to start logging: "
	// + e.getClass().getName()
	// + "; with the following Message: "
	// + e.getMessage());
	// e.printStackTrace();
	// }

	logROADTest.log(Level.INFO,
		"ROADTest started with the following composite:" + smcFile);
	playerMap = new HashMap<String, Player>();

	listeners = new HashSet<OrganiserListener>();

	// contractList = new HashMap<String, Contract>();
	CompositeDemarshaller dm = new CompositeDemarshaller();
	try {
	    composite = dm.demarshalSMC(smcFile);
	} catch (CompositeDemarshallingException e) {
	    logROADTest
		    .log(Level.FATAL, "ROADTest: Following exception occured: "
			    + e.getClass().getName()
			    + "; with the following Message: " + e.getMessage());
	    throw e;
	} catch (ConsistencyViolationException e) {
	    logROADTest
		    .log(Level.FATAL, "ROADTest: Following exception occured: "
			    + e.getClass().getName()
			    + "; with the following Message: " + e.getMessage());
	    throw e;
	} catch (CompositeInstantiationException e) {
	    logROADTest
		    .log(Level.FATAL, "ROADTest: Following exception occured: "
			    + e.getClass().getName()
			    + "; with the following Message: " + e.getMessage());
	    throw e;
	}

	logROADTest.log(Level.INFO, "ROADTest composite " + composite.getName()
		+ " created (not started)");

	this.roleList = composite.getCompositeRoles();
	String roles = "";
	for (IRole r : roleList) {
	    roles = roles + r.getId() + ", ";
	    playerMap.put(r.getId(), new Player(r));
	}
	roles = roles.substring(0, roles.length() - 2);
	// Iterator it = playerMap.values().iterator();
	// while (it.hasNext()) {
	// Player p = (Player) it.next();
	// Contract contractArray[] = p.getRole().getAllContracts();
	// for (Contract contract : contractArray) {
	// contractList.put(contract.getId(), contract);
	// }
	// }

	// this.organiser = new RoadTestOrganiser(composite.getOrganiserRole());
	this.organiserRole = composite.getOrganiserRole();

	logROADTest.log(Level.INFO, "ROADTest created organiser role");
	logROADTest.log(Level.INFO,
		"ROADTest player created for following roles: " + roles);

    }

    public void addPlayer(IRole r) {
	if (!playerMap.containsKey(r.getId())) {
	    playerMap.put(r.getId(), new Player(r));
	} else {
	    log.debug("Duplicate ID: " + r.getId());
	}
    }

    public void removePlayer(IRole r) {
	if (playerMap.containsKey(r.getId())) {
	    playerMap.remove(r.getId());
	} else {
	    log.debug("Player Not found: " + r.getId());
	}

    }

    /**
     * Returns the available players who are playing a role in the composition
     * as a list
     * 
     * @return All available players as a list object
     */
    public List<Player> getPlayer() {
	ArrayList<Player> list = new ArrayList<Player>();
	list.addAll(this.playerMap.values());
	return list;
    }

    /**
     * Gets the player of the provided id.
     * 
     * @param id
     *            The id which player is needed.
     * @return A <code>Player</code> object.
     * @throws PlayerNotFoundException
     *             Throws PlayerNotFound exception if the id is not in player
     *             map.
     */
    public Player getPlayerById(String id) throws PlayerNotFoundException {
	Player p = this.playerMap.get(id);
	if (p == null) {
	    throw new PlayerNotFoundException("The Player with the id " + id
		    + " is not available");
	}
	return p;
    }

    /**
     * This will start the composite
     */
    public void startComposite() {
	Thread compThread = new Thread(this.composite);
	compThread.start();
	logROADTest.log(Level.INFO, "ROADTest composite starting!");
    }

    /**
     * Sends a message to the composite as a given player, with a given message
     * signature, and a given message content.
     * 
     * @param playerId
     *            The playerId as <code>String</code> which indicates where the
     *            message should be injected.
     * @param msgSignature
     *            The message signature as a <code>String</code>.
     * @param msgContent
     *            The content of the message as a <code>String</code>.
     * @throws PlayerNotFoundException
     *             This exception is thrown if the player not existing
     */
    public void sendMessage(String playerId, String msgSignature,
	    String msgContent, boolean response) throws PlayerNotFoundException {
	logROADTest.log(Level.INFO,
		"ROADTest: Message sent from ROADTest for player: " + playerId
			+ " with the signature: " + msgSignature);
	Player p = playerMap.get(playerId);
	if (p == null) {
	    logROADTest.log(Level.FATAL, "ROADTest: The player with the id: "
		    + playerId + " can not be found. Message not sent!");
	    throw new PlayerNotFoundException("The player with the id: "
		    + playerId + " can not be found");
	}
	p.sendMessage(msgSignature, msgContent, response);
    }

    /**
     * Starts this ROADTest as a server on standard port 1099 or less
     * 
     * @throws RemoteException
     * @throws MalformedURLException
     */
    public void startAsServer() throws RemoteException, MalformedURLException {
	try {
	    setUpRegistry();
	    IServerRMI rts = new RoadTestServer(this.roleList, this);
	    Naming.rebind("server.ROADTest." + this.composite.getName(), rts);
	} catch (RemoteException e) {
	    logROADTest
		    .log(Level.FATAL,
			    "ROADTest: Following exception occured while trying to start server: "
				    + e.getClass().getName()
				    + "; with the following Message: "
				    + e.getMessage());
	    throw e;
	} catch (MalformedURLException e) {
	    logROADTest
		    .log(Level.FATAL,
			    "ROADTest: Following exception occured while trying to register the composite:"
				    + this.composite.getName()
				    + " on the server: "
				    + e.getClass().getName()
				    + "; with the following Message: "
				    + e.getMessage());
	    throw e;
	}
	logROADTest.log(Level.INFO,
		"ROADTest this ROADTest " + this.composite.getName()
			+ " has been started as a server");
    }

    /**
     * Unbinds the composite from the registry
     */
    public void stopAsServer() {
	try {
	    Naming.unbind("server.ROADTest." + this.composite.getName());
	} catch (RemoteException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (NotBoundException e) {
	    e.printStackTrace();
	}
    }

    private void setUpRegistry() throws RemoteException {
	boolean exit = false;
	newPort = Registry.REGISTRY_PORT;
	int count = 0;
	int attempts = 7;
	do {
	    count++;
	    try {
		log.debug("Registering to port: " + newPort);
		LocateRegistry.createRegistry(newPort);
		exit = true;
	    } catch (RemoteException e) {
		System.err.println("Port " + newPort + " is already in use");
		newPort--;
		exit = false;
		if (count >= attempts) {
		    throw e;
		}
	    }
	} while (!exit);
    }

    public String toString() {
	return this.composite.getName();
    }

    /**
     * The function registeres listener with the ROADtest
     * 
     * @param listener
     */
    public synchronized void addOrganiserListener(OrganiserListener listener) {
	listeners.add(listener);
    }

    /**
     * The function deregisteres listener from the ROADtest
     * 
     * @param listener
     */
    public synchronized void removeOrganiserListeners(OrganiserListener listener) {
	listeners.remove(listener);
    }

    public IOrganiserRole getOrganier() {
	return this.organiserRole;
    }

    /**
     * Method calls addNewRole of organiser
     * 
     * @param id
     * @param name
     * @param description
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult addNewRole(String id, String name,
	    String description) {

	OrganiserOperationResult result = organiserRole.addNewRole(id, name,
		description);

	// successfully added then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("ADDROLE", id);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    /**
     * Method calls removeRole of organiser
     * 
     * @param roleId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult removeRole(String roleId) {

	OrganiserOperationResult result = organiserRole.removeRole(roleId);

	// successfully removed then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("REMOVEROLE", roleId);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    /**
     * Method calls addNewContract of organiser
     * 
     * @param id
     * @param name
     * @param description
     * @param state
     * @param type
     * @param ruleFile
     * @param isAbstract
     * @param roleAId
     * @param roleBId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult addNewContract(String id, String name,
	    String description, String state, String type, String ruleFile,
	    boolean isAbstract, String roleAId, String roleBId) {

	OrganiserOperationResult result = organiserRole.addNewContract(id,
		name, description, state, type, ruleFile, isAbstract, roleAId,
		roleBId);

	// successfully added then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("ADDCONTRACT", id);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    /**
     * Method calls removeContract of organiser
     * 
     * @param contractId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult removeContract(String contractId) {

	OrganiserOperationResult result = organiserRole
		.removeContract(contractId);

	// successfully removed then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("REMOVECONTRACT", contractId);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    /**
     * Method calls addNewTerm of organiser
     * 
     * @param id
     * @param name
     * @param messageType
     * @param deonticType
     * @param description
     * @param direction
     * @param contractId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult addNewTerm(String id, String name,
	    String messageType, String deonticType, String description,
	    String direction, String contractId) {

	OrganiserOperationResult result = organiserRole.addNewTerm(id, name,
		messageType, deonticType, description, direction, contractId);

	// successfully added then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("ADDTERM", id);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    /**
     * Method calls removeTerm of organiser
     * 
     * @param termId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult removeTerm(String ctID, String termId) {
	OrganiserOperationResult result = organiserRole
		.removeTerm(ctID, termId);

	// successfully removed then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("REMOVETERM", termId);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    public OrganiserOperationResult addOperation(String operationName,
	    String operationReturnType, Parameter[] parameters, String termId,
	    String cId) {

	OrganiserOperationResult result = organiserRole.addNewOperation(
		operationName, operationReturnType, parameters, termId, cId);

	// successfully removed then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("ADDOPERATION", operationName);
	    fireOrganiserEvent(e);
	}
	return result;

    }

    /**
     * Method calls removeOperation of organiser
     * 
     * @param operationName
     * @param termId
     * @return OrganiserOperationResult
     */
    public OrganiserOperationResult removeOperation(String operationName,
	    String termId) {
	OrganiserOperationResult result = organiserRole.removeOperation(
		operationName, termId);

	// successfully removed then notify UI
	if (result.getResult()) {
	    OrganiserEvent e = new OrganiserEvent("REMOVEOPERATION",
		    operationName);
	    fireOrganiserEvent(e);
	}
	return result;
    }

    protected synchronized void fireOrganiserEvent(OrganiserEvent e) {
	String str = e.getSourceid();
	for (OrganiserListener li : listeners) {
	    if (str.equals("ADDROLE")) {
		li.addRoleEvent(e);
	    } else if (str.equals("ADDCONTRACT")) {
		li.addContractEvent(e);
	    } else if (str.equals("ADDTERM")) {
		li.addTermEvent(e);
	    } else if (str.equals("ADDOPERATION")) {
		li.addOperationEvent(e);
	    } else if (str.equals("REMOVEROLE")) {
		li.removeRoleEvent(e);
	    } else if (str.equals("REMOVECONTRACT")) {
		li.removeContractEvent(e);
	    } else if (str.equals("REMOVETERM")) {
		li.removeTermEvent(e);
	    } else if (str.equals("REMOVEOPERATION")) {
		li.removeOperationEvent(e);
	    }

	}
    }

}
