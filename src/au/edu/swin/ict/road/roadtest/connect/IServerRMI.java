package au.edu.swin.ict.road.roadtest.connect;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;

/**
 * Interface for RMI implementation which exposes the functionality on the
 * ROADTest instance on which this will be initiated
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public interface IServerRMI extends Remote {
    /**
     * Sends a message
     * 
     * @param role
     *            The role from where a message is going to be sent
     * @param msgSig
     *            The signature of the message
     * @param msgCont
     *            THe message content
     * @param response
     *            If this message should be considered as a response
     * @throws RemoteException
     * @throws PlayerNotFoundException
     */
    public void sendMessage(String role, String msgSig, String msgCont,
	    boolean response) throws RemoteException, PlayerNotFoundException;

    /**
     * Gets the available roles on the server
     * 
     * @return The available roles on the server key-RoleId value-Role Name
     * @throws RemoteException
     */
    public Map<String, String> getRoles() throws RemoteException;
}
