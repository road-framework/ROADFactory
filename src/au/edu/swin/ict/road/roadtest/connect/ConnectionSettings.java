package au.edu.swin.ict.road.roadtest.connect;

/**
 * ConnectionSettings class can be used to save the settings for a connection.
 * This is needed for a player who wants to connect to another composite. It
 * stores all the needed information
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class ConnectionSettings {
    private String server = null;
    private String roleId = null;
    private String compositeName = null;

    /**
     * Construct the ConnectionSettings object by providing at least the server
     * address
     * 
     * @param server
     *            The address of the server to which it needs to be connected
     */
    public ConnectionSettings(String server) {
	this.server = server;
    }

    /**
     * Gets the server of this ConnectionSettings
     * 
     * @return the server
     */
    public String getServer() {
	return server;
    }

    /**
     * Sets the name of the composite with which it needs to interact
     * 
     * @param compositeName
     *            the compositeName to set which it needs to interact with
     */
    public void setCompositeName(String compositeName) {
	this.compositeName = compositeName;
    }

    /**
     * Gets the composite name of the server which has been set, if none was set
     * null will be returned
     * 
     * @return the compositeName The name of the composite which has been set,
     *         null if none was set before
     */
    public String getCompositeName() {
	return compositeName;
    }

    /**
     * Sets the role id with which it needs to interact with
     * 
     * @param roleId
     *            the roleId to set which it needs to interact with
     */
    public void setRoleId(String roleId) {
	this.roleId = roleId;
    }

    /**
     * Gets the role id of the server which has been set, if non was set null
     * will be returned
     * 
     * @return the roleId The role id of which has been set, null if none was
     *         set before
     */
    public String getRoleId() {
	return roleId;
    }
}
