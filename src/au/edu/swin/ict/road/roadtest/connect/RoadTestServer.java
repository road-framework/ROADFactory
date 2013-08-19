package au.edu.swin.ict.road.roadtest.connect;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.roadtest.ROADTest;
import au.edu.swin.ict.road.roadtest.exception.PlayerNotFoundException;

/**
 * RoadTestServer is the Remote object with which a client can interact. This
 * provides the functionality to return the roles for a given composite and send
 * messages as a role.
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 */
public class RoadTestServer extends UnicastRemoteObject implements IServerRMI {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    private Map<String, String> roles;
    private ROADTest rt;

    /**
     * Constructor of this class which needs the roles which can be exposed to
     * clients who wants to interact with this server
     * 
     * @param roles
     *            Roles with which a client can interact
     * @param rt
     *            ROADTest class
     * @throws RemoteException
     */
    public RoadTestServer(List<IRole> roles, ROADTest rt)
	    throws RemoteException {
	super();
	this.rt = rt;
	this.roles = new HashMap<String, String>();
	for (IRole r : roles) {
	    this.roles.put(r.getId(), r.getName());
	}
    }

    public Map<String, String> getRoles() throws RemoteException {
	return this.roles;
    }

    public void sendMessage(String role, String msgSig, String msgCont,
	    boolean response) throws RemoteException, PlayerNotFoundException {
	rt.sendMessage(role, msgSig, msgCont, response);
    }

}
