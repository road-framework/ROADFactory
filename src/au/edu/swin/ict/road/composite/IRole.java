package au.edu.swin.ict.road.composite;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.exceptions.RoleDescriptionGenerationException;
import au.edu.swin.ict.road.composite.listeners.RolePushMessageListener;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.player.PlayerBinding;

/**
 * <code>IRole</code> is an interface to be used by players to interact with
 * functional roles of a <code>Composite</code>. Any functional role classes
 * must implement this interface.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public interface IRole {

    public static final String GET_NEXT_MSG_OP = "getNextMessage";
    public static final String BIND_OP = "bind";
    public static final String UNBIND_OP = "unbind";

    public void registerNewPushListener(RolePushMessageListener l);

    /**
     * Allows a player to send a message (in a <code>MessageWrapper</code>) to
     * the <code>Composite</code> via this functional <code>Role</code> for
     * contract processing and eventual delivery to its destination role.
     * 
     * @param message
     *            the <code>MessageWrapper</code> to pass to the Composite.
     */
    public void putMessage(MessageWrapper message);

    public MessageWrapper putSyncMessage(MessageWrapper message);

    /**
     * Can be used to place a message directly on the Pending Out Queue. A
     * useful scenario is when a message delivery is failed at the lower level
     * message delivery mechanism, e.g., ROAD4WS when endpoint is unavailable.
     * In that case the message can be placed back to be delivered later.
     * 
     * @param message
     */
    public void putPendingOutBufMessage(MessageWrapper message);

    public MessageWrapper peekNextMessage();

    /**
     * Allows a player to get incoming messages (in message wrappers) from this
     * functional roles message queue.
     * 
     * @return the new MessageWrapper to receive.
     */
    public MessageWrapper getNextMessage();

    /**
     * 
     * @param timeout
     * @param unit
     * @return
     */
    public MessageWrapper getNextMessage(long timeout, TimeUnit unit);

    public MessageWrapper peekNextPushMessage();

    /**
     * 
     * @return
     */
    public MessageWrapper getNextPushMessage();

    /**
     * 
     * @param timeout
     * @param unit
     * @return
     */
    public MessageWrapper getNextPushMessage(long timeout, TimeUnit unit);

    /**
     * Returns all the rules associated with this role in a map data structure.
     * Rules inserted or deleted at runtime are also included in the map.
     * 
     * @return Map which contains all the rules grouped according to the
     *         contract IDs.
     */
    public Map<String, List<String>> getRules();

    /**
     * Allows a player to send a management related message (in a
     * <code>MessageWrapper</code>) to the <code>Composite</code> organiser via
     * this functional <code>Role</code>. This functional <code>Role</code> will
     * be marked as the source of the management message.
     * 
     * @param message
     *            the <code>MessageWrapper</code> to pass to the organiser.
     */
    public void putManagementMessage(MessageWrapper message);

    /**
     * Allows a player to get incoming management related messages sent from the
     * organiser(in message wrappers) from this functional roles message queue.
     * 
     * @return the new MessageWrapper to receive.
     */
    public MessageWrapper getNextManagementMessage();

    MessageWrapper getNextManagementMessage(long timeout, TimeUnit unit);

    /**
     * Get the roles description.
     * 
     * @return the description.
     */
    public String getDescription();

    /**
     * Gets the <code>Composite</code> that this role is a part of.
     * 
     * @return the roles <code>Composite</code>
     */
    public Composite getComposite();

    /**
     * Get the roles name.
     * 
     * @return the name.
     */
    public String getName();

    /**
     * Gets this roles unique ID within the composite.
     * 
     * @return the roles unique id.
     */
    public String getId();

    /**
     * Gets a list of strings the contains the provided operations this role
     * contains, which depends on linked contracts (operations a player can
     * invoke on the role, or message types a player may send).
     * 
     * @return the list of provided operations.
     */
    public List<Operation> getProvidedOperationsList();

    /**
     * Gets a list of strings the contains the required operations this role
     * contains, which depends on linked contracts (operations a player can have
     * invoked on themselves by other parties, or message types a player may
     * receive).
     * 
     * @return the list of required operations.
     */
    public List<Operation> getRequiredOperationsList();

    public Object getRequiredOperationObject()
	    throws RoleDescriptionGenerationException;

    public Object getProvidedOperationObject()
	    throws RoleDescriptionGenerationException;

    /**
     * 
     * @param playerBinding
     */
    public void bind(String playerBinding);

    /**
	 * 
	 */
    public void unBind();

    /**
     * 
     * @return
     */
    public String getPlayerBinding();

    /**
     * 
     * @return player binding object if bound. Otherwise returns null.
     */
    public PlayerBinding getPlayerBindingObject();

    public void terminate();

    public Contract[] getAllContracts();
}
