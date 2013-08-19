package au.edu.swin.ict.serendip.core.mgmt;

import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.core.mgmt.scripting.Script;
import au.edu.swin.ict.serendip.event.SerendipEventListener;

/**
 * A single instance of schedualing a scripting.
 * 
 * @author Malinda
 * 
 */
public class AdaptationScriptSchedular extends SerendipEventListener {
    // static int counter = 0;
    private String id = null;
    private String script = null;
    private SerendipOrganizer org = null;

    AdaptationScriptSchedular(String script, String onEventPattern, String pid,
	    SerendipOrganizer org) {
	this.id = pid + "_" + onEventPattern;
	this.script = script;
	this.eventPattern = onEventPattern;
	this.org = org;
	this.pId = pid;// TODO: Check! can be a problem
    }

    @Override
    public String getId() {
	// TODO Auto-generated method stub
	return this.id;
    }

    @Override
    public void eventPatternMatched(String ep, String id)
	    throws SerendipException {
	// TODO Execute the script
	OrganiserOperationResult result = this.org.executeScript(this.script);
	if (false == result.getResult()) {
	    // TODO: Drop a management message to the organizer

	}

    }

}
