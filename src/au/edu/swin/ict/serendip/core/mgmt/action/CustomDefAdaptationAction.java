package au.edu.swin.ict.serendip.core.mgmt.action;

import java.util.Properties;

/**
 * This class must be extended by any custom class written to support adaptation
 * action.
 * 
 * @author Malinda
 */
public abstract class CustomDefAdaptationAction implements DefAdaptAction {
    protected Properties properties = null;
    protected String commandName = null;

    /**
     * @param commandName
     *            is the commandName used in the script
     * @param properties
     *            is the key value pairs supplied to the command.
     */
    public CustomDefAdaptationAction(String commandName, Properties properties) {
	this.commandName = commandName;
	this.properties = properties;
    }

    public Properties getProperties() {
	return properties;
    }

    public void setProperties(Properties properties) {
	this.properties = properties;
    }

    public String getCommandName() {
	return commandName;
    }

    public void setCommandName(String commandName) {
	this.commandName = commandName;
    }

}
