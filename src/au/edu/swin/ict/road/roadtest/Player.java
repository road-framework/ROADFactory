package au.edu.swin.ict.road.roadtest;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.roadtest.connect.ConnectionSettings;
import au.edu.swin.ict.road.roadtest.connect.IServerRMI;
import au.edu.swin.ict.road.roadtest.events.MessageArrivedEvent;
import au.edu.swin.ict.road.roadtest.exception.CompositeNotFoundException;
import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;
import au.edu.swin.ict.road.roadtest.exception.RoleNotFoundException;
import au.edu.swin.ict.road.roadtest.listeners.MessageArrivedEventListener;
import au.edu.swin.ict.road.roadtest.parameter.Content;
import au.edu.swin.ict.road.roadtest.parameter.ObjectParameterHelper;

/**
 * A player which is playing a role in a composite. It also has a in- and out
 * box, can send messages in intervals, and auto respond to incoming message
 * signatures. Furthermore the Player can connect to a server/composite which is
 * running on a other/same machine (need to provide server/IP, composite name
 * and role to which it is connected)
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class Player {
    private static Logger log = Logger.getLogger(Player.class.getName());
    private String playerId;
    private Role role;
    private Inbox inbox;
    private Outbox outbox;
    private Map<String, Map<String, List<Content>>> providedNew;
    private Map<String, Map<String, List<Content>>> requiredNew;
    private Map<UUID, AutoResponder> mapAutoResponder;
    private Map<UUID, IntervalMessageSender> mapIntervalMessageSender;
    private IServerRMI remoteServer;
    private ConnectionSettings cs;
    private Registry registry;
    private Map<String, String> remoteRoles;

    /**
     * Construct the Player and provide the Role which it is playing
     * 
     * @param role
     *            The Role which <code>this</code> Player is going to play
     */
    @SuppressWarnings("unchecked")
    public Player(IRole role) {
	this.playerId = role.getId();
	this.role = (Role) role;
	this.providedNew = new HashMap<String, Map<String, List<Content>>>();
	List<Operation> listOperation = this.role.getProvidedOperationsList();
	for (Operation o : listOperation) {
	    this.providedNew.put(o.getName(),
		    ObjectParameterHelper.getParameterMap(o.getParameters()));
	}

	this.requiredNew = new HashMap<String, Map<String, List<Content>>>();

	listOperation = this.role.getRequiredOperationsList();
	for (Operation o : listOperation) {
	    this.requiredNew.put(o.getName(),
		    ObjectParameterHelper.getParameterMap(o.getParameters()));
	}

	this.inbox = new Inbox(this.role);

	this.outbox = new Outbox();
	this.mapIntervalMessageSender = new HashMap<UUID, IntervalMessageSender>();
	this.mapAutoResponder = new HashMap<UUID, AutoResponder>();
	this.remoteServer = null;
	this.cs = null;
	this.registry = null;
	this.remoteRoles = null;

	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId() + " created.");
    }

    public void addProvidedOperation(Operation o) {
	this.providedNew.put(o.getName(),
		ObjectParameterHelper.getParameterMap(o.getParameters()));
    }

    public void removeOperation(Operation o) {
	this.providedNew.remove(o.getName());
	this.requiredNew.remove(o.getName());

    }

    public void addRequiredOperation(Operation o) {
	this.requiredNew.put(o.getName(),
		ObjectParameterHelper.getParameterMap(o.getParameters()));
    }

    /**
     * Returns all required signatures which a are provided to this player
     * 
     * @return List of <code>String</code> with all required method signatures.
     */
    public List<String> getRequiredSignatures() {
	return new ArrayList<String>(this.requiredNew.keySet());
    }

    /**
     * Returns all provided signatures which a are provided to this player
     * 
     * @return List of <code>String</code> with all provided method signatures.
     */
    public List<String> getProvidedSignatures() {
	// return this.provided;
	return new ArrayList<String>(this.providedNew.keySet());
    }

    public Map<String, List<Content>> getProvidedParameters(String signature) {
	return this.providedNew.get(signature);
    }

    public Map<String, List<Content>> getRequiredParameter(String signature) {
	return this.requiredNew.get(signature);
    }

    /**
     * Gets the player id which is the roleId of the role this player is playing
     * 
     * @return The role id.
     */
    public String getPlayerId() {
	return this.playerId;
    }

    /**
     * Gets the role object of this player.
     * 
     * @return The role object.
     */
    public Role getRole() {
	return this.role;
    }

    /**
     * Return the Inbox of this Player
     * 
     * @return the Inbox of this Player
     */
    public Inbox getInbox() {
	return this.inbox;
    }

    /**
     * Returns the Outbox of this player
     * 
     * @return The Outbox of this player
     */
    public Outbox getOutbox() {
	return this.outbox;
    }

    /**
     * Insert a message with the given content and signature as this player in
     * its role.
     * 
     * @param msgSignature
     *            The message signature as a <code>String</code>.
     * @param msgContent
     *            The content of the message as a <code>String</code>.
     */
    public void sendMessage(String msgSignature, String msgContent,
	    boolean response) {

	MessageWrapper mw = new MessageWrapper(msgContent, msgSignature,
		response);
	Message message = new Message(msgContent, msgSignature,
		this.role.getId(), mw.isResponse());
	this.outbox.addOutboxMessage(message);
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " sends message with following signature: "
			+ msgSignature);

	role.putMessage(mw);
	/*
	 * boolean found = false; for(Operation o :
	 * role.getProvidedOperationsList()){
	 * if(o.getName().equalsIgnoreCase(message.getOperationName()) &&
	 * !o.getReturnType().equalsIgnoreCase("void")){ found = true; } }
	 * if(!found) role.putMessage(mw); else role.putSyncMessage(mw);
	 */
    }

    /**
     * Sends a message with the message object usually from the interval sender
     * 
     * @param message
     *            The Message object which is going to be sent
     */
    public void sendMessage(Message message) {
	MessageWrapper mw = new MessageWrapper(message.getMessageContent(),
		message.getOperationName(), message.isResponse());
	if (message instanceof IntervalMessage) {
	    ((IntervalMessage) message).addTimeStamp();
	} else if (message instanceof AutoResponseMessage) {
	    ((AutoResponseMessage) message).addTimeStamp();
	    mw = new MessageWrapper(message.getMessageContent(),
		    ((AutoResponseMessage) message).getOutMsgSignature(), true);
	} else {
	    message.setNewTimeStamp();
	    ROADTest.logROADTest.log(Level.INFO,
		    "Player with id:" + this.getPlayerId()
			    + " sends message with following signature: "
			    + message.getOperationName());
	}
	this.outbox.addOutboxMessage(message);
	role.putMessage(mw);
	/*
	 * boolean found = false; for(Operation o :
	 * role.getProvidedOperationsList()){
	 * if(o.getName().equalsIgnoreCase(message.getOperationName()) &&
	 * !o.getReturnType().equalsIgnoreCase("void")){ found = true; } }
	 * if(!found) role.putMessage(mw); else role.putSyncMessage(mw);
	 */
    }

    /**
     * Sets up an interval message but dose not start it
     * 
     * @param msgSignature
     *            The signature of the message
     * @param msgContent
     *            The content of the message
     * @param interval
     *            the interval in which this message is going to be fired in
     *            <code>long</code>
     * @return The UUID of the IntervalMessageSender so this can be started
     */
    public UUID setUpIntervalMessage(String msgSignature, String msgContent,
	    long interval) {
	IntervalMessage im = new IntervalMessage(msgContent, msgSignature,
		playerId, interval);
	IntervalMessageSender ims = new IntervalMessageSender(im, this);
	this.mapIntervalMessageSender.put(ims.getUid(), ims);
	ROADTest.logROADTest.log(
		Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " sets up following interval message (UUID:"
			+ ims.getUid() + "): MsgSig:" + msgSignature
			+ "; Interval in millisec:" + interval);
	return ims.getUid();
    }

    /**
     * Set up auto response for this player
     * 
     * @param inMsgSignature
     *            The message signature to which it needs to auto respond
     * @param outMsgSignature
     *            The message signature with which this player has to auto
     *            respond
     * @param msgContent
     *            The content of the auto response message
     * @param delay
     *            The delay for the auto response >=0
     * @return The unique id of the auto responder
     */
    public UUID setUpAutoResponder(String inMsgSignature,
	    String outMsgSignature, String msgContent, long delay) {
	log.debug(inMsgSignature + ":::" + outMsgSignature);
	AutoResponseMessage arm = new AutoResponseMessage(inMsgSignature,
		outMsgSignature, msgContent, delay);
	AutoResponder ar = new AutoResponder(this, arm);
	this.mapAutoResponder.put(ar.getUid(), ar);
	ROADTest.logROADTest.log(
		Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " sets up following auto responder (UUID:"
			+ ar.getUid() + "): inMsgSig:" + inMsgSignature
			+ "; outMsgSig: " + outMsgSignature
			+ "; delay in millisec:" + delay);
	return ar.getUid();
    }

    /**
     * Starts all previously created IntervalMessages
     */
    @SuppressWarnings("unchecked")
    public void startAllIntervalMessages() {
	Iterator it = mapIntervalMessageSender.values().iterator();
	while (it.hasNext()) {
	    IntervalMessageSender ims = (IntervalMessageSender) it.next();
	    if (!(ims.isAlive())) {
		ims.start();
	    }
	}
	ROADTest.logROADTest
		.log(Level.INFO,
			"Player with id:"
				+ this.getPlayerId()
				+ " starts all Interval Messages (which haven't been stoped before)");
    }

    /**
     * Starts all Auto response which has been previously configured
     */
    @SuppressWarnings("unchecked")
    public void startAllAutoResponses() {
	Iterator it = this.mapAutoResponder.values().iterator();
	while (it.hasNext()) {
	    AutoResponder ar = (AutoResponder) it.next();
	    if (!(ar.isRunning())) {
		ar.start();
	    }
	}
	ROADTest.logROADTest
		.log(Level.INFO,
			"Player with id:"
				+ this.getPlayerId()
				+ " starts all Auto Respones (which haven't been stoped before)");
    }

    /**
     * Starts the interval message sender with the uid
     * 
     * @param uid
     *            the uid for which the interval message should be started as
     *            <code>UUID</code>
     * @throws NullPointerException
     *             Is going to be thrown if the uid is not available
     */
    public void startIntervalMessageById(UUID uid) throws NullPointerException {
	IntervalMessageSender ims = mapIntervalMessageSender.get(uid);
	ims.start();
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " started following interval message (UUID:" + uid
			+ ")");
    }

    /**
     * Starts the auto response with the given uid
     * 
     * @param uid
     *            the uid for which the auto response needs to be started
     * @throws NullPointerException
     *             Is going to be thrown if the uid is not available
     */
    public void startAutoResponseById(UUID uid) throws NullPointerException {
	AutoResponder ar = this.mapAutoResponder.get(uid);
	ar.start();
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " started following auto response message (UUID:"
			+ uid + ")");
    }

    /**
     * Terminates all interval message processes
     */
    @SuppressWarnings("unchecked")
    public void terminateAllIntervalMessages() {
	// Iterator it = mapIntervalMessageSender.entrySet().iterator();
	Iterator it = mapIntervalMessageSender.values().iterator();
	while (it.hasNext()) {
	    IntervalMessageSender ims = (IntervalMessageSender) it.next();
	    if (ims.isAlive()) {
		ims.setTerminate(true);
	    }
	}
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " stops all Interval Messages");
    }

    /**
     * Terminates all Auto response which has been previously started
     */
    @SuppressWarnings("unchecked")
    public void terminateAllAutoResponses() {
	Iterator it = this.mapAutoResponder.values().iterator();
	while (it.hasNext()) {
	    AutoResponder ar = (AutoResponder) it.next();
	    if (ar.isRunning()) {
		ar.stop();
	    }
	}
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " stops all Auto Respones");
    }

    /**
     * Terminates the interval message sender with the uid
     * 
     * @param uid
     *            the uid for which the interval message should be stopped as
     *            <code>UUID</code>
     * @throws NullPointerException
     *             Is going to be thrown if the uid is not available
     */
    public void terminateIntervalMessageById(UUID uid)
	    throws NullPointerException {
	mapIntervalMessageSender.get(uid).setTerminate(true);
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " stops following interval message (UUID:" + uid
			+ ")");
    }

    /**
     * Terminates the auto response with the uid
     * 
     * @param uid
     *            the uid for which the auto response message should be stopped
     *            as <code>UUID</code>
     * @throws NullPointerException
     *             Is going to be thrown if the uid is not available
     */
    public void terminateAutoResponseById(UUID uid) throws NullPointerException {
	this.mapAutoResponder.get(uid).stop();
	ROADTest.logROADTest.log(Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " stops following auto response message (UUID:" + uid
			+ ")");
    }

    /**
     * Sets the connection for this player with the connection setting cs
     * 
     * @param cs
     *            The connection settings as a <code>ConnectionSettings</code>
     *            object
     * @return String array with available composites on the given IP if alr
     * @throws RemoteException
     */
    public String[] setConnection(ConnectionSettings cs) throws RemoteException {
	this.cs = cs;
	try {
	    this.registry = LocateRegistry.getRegistry(cs.getServer());
	} catch (RemoteException e) {
	    ROADTest.logROADTest.log(
		    Level.FATAL,
		    "Player with id:" + this.getPlayerId()
			    + " tryed to set up connection with server: "
			    + cs.getServer()
			    + ". Following exception occured: "
			    + e.getClass().getName() + "; with the message: "
			    + e.getMessage());
	    throw e;
	}
	ROADTest.logROADTest.log(
		Level.INFO,
		"Player with id:" + this.getPlayerId()
			+ " sets up connection with following server: "
			+ cs.getServer());
	return this.registry.list();
    }

    /**
     * Sets the connection for this player with the connection setting cs
     * 
     * @param serverAddress
     *            The ip address of the server to which it needs to connect as
     *            <code>String</code>
     * @return String array with available composites on the given IP if alr
     * @throws RemoteException
     */
    public String[] setConnection(String serverAddress) throws RemoteException {
	return setConnection(new ConnectionSettings(serverAddress));
    }

    /**
     * Set up to use a composite with the given composite. It will return null
     * if the server has not been set up yet.
     * 
     * @param composite
     *            String to which composite on the server it should connect
     * @return Return a Map with the Roles to which it can connect. It is a map
     *         of String, String where the key is the role id and the value is
     *         the role name
     * @throws RemoteException
     * @throws CompositeNotFoundException
     *             Is thrown if the composite not found
     */
    public Map<String, String> useCompositeOnServer(String composite)
	    throws RemoteException, CompositeNotFoundException {
	this.cs.setCompositeName(composite);
	return useCompositeOnServer();
    }

    /**
     * Set up to use a composite which was set before with the connection
     * settings object. It will return null if the server has not been set up
     * yet.
     * 
     * @return Return a Map with the Roles to which it can connect. It is a map
     *         of String, String where the key is the role id and the value is
     *         the role name
     * @throws RemoteException
     * @throws CompositeNotFoundException
     *             Is thrown if the composite not found
     */
    public Map<String, String> useCompositeOnServer() throws RemoteException,
	    CompositeNotFoundException {
	// if (this.remoteServer == null) {
	// return null;
	// }
	try {
	    String c = "";
	    if (cs.getCompositeName().startsWith("server.ROADTest."))
		c = cs.getCompositeName();
	    else
		c = "server.ROADTest." + cs.getCompositeName();
	    log.debug("composite name is: " + c);
	    this.remoteServer = (IServerRMI) this.registry.lookup(c);
	    log.debug("Got the remote server");
	} catch (NotBoundException e) {
	    e.printStackTrace();
	    ROADTest.logROADTest
		    .log(Level.FATAL,
			    "Player with id:"
				    + this.getPlayerId()
				    + " exception: CompositeNotFoundException "
				    + "occured the required composite dose not exist on the server"
				    + cs.getServer());
	    throw new CompositeNotFoundException("The composite "
		    + cs.getCompositeName()
		    + " is not available on the server: " + cs.getServer());
	}
	log.debug("Getting the remote roles");
	try {
	    remoteRoles = this.remoteServer.getRoles();
	} catch (RemoteException e) {
	    ROADTest.logROADTest.log(Level.FATAL,
		    "Player with id:" + this.getPlayerId()
			    + " tryed to get roles on server " + cs.getServer()
			    + " and following exception occured "
			    + e.getClass().getName()
			    + " with the following message: " + e.getMessage());
	    throw e;
	}
	log.debug("got the remote roles");
	return remoteRoles;
    }

    /**
     * Bind this player to the role id of the server which has been set up
     * before in the ConnectionSettings object which has been passed while
     * setting up the connection.
     * 
     * @throws RoleNotFoundException
     *             Throws this exception if the role id is not available on the
     *             connected composite
     */
    public void bindRoleToPlay() throws RoleNotFoundException {
	if (remoteRoles.containsKey(cs.getRoleId())) {
	    this.inbox
		    .addMessageArrivedEventListener(new MessageArrivedEventListener() {
			public void messageArrived(
				MessageArrivedEvent msgArrivedEvent) {
			    try {

				remoteServer.sendMessage(
					cs.getRoleId(),
					msgArrivedEvent.getMessage()
						.getOperationName(),
					msgArrivedEvent.getMessage()
						.getMessageContent().toString(),
					msgArrivedEvent.getMessage()
						.isResponse());
			    } catch (RemoteException e) {
				ROADTest.logROADTest
					.log(Level.FATAL,
						"Player with id:"
							+ getPlayerId()
							+ " tryed to send a message according to the connection "
							+ "settings but server might not be available");
			    } catch (PlayerNotFoundException e) {
				ROADTest.logROADTest
					.log(Level.FATAL,
						"Player with id:"
							+ getPlayerId()
							+ " tryed to send a message according to the connection "
							+ "settings but server dose not have such a role at his"
							+ "composite");
			    }
			}
		    });
	} else {
	    ROADTest.logROADTest.log(Level.FATAL,
		    "Player with id:" + this.getPlayerId()
			    + " tryes to bind to: " + cs.getRoleId()
			    + " on server " + cs.getServer()
			    + " and failed with "
			    + "RoleNotFoundException and following message: "
			    + "This role id is not available");
	    throw new RoleNotFoundException("This role id is not available");
	}
    }

    /**
     * Bind this player to the role id which has to be passed with this method.
     * 
     * @param id
     *            The role id of the connected composite
     * @throws RoleNotFoundException
     *             Throws this exception if the role id is not available on the
     *             connected composite
     */
    public void bindRoleToPlay(String id) throws RoleNotFoundException {
	this.cs.setRoleId(id);
	bindRoleToPlay();
    }

    public String toString() {
	return "Role:" + this.getRole().getName();
    }
}
