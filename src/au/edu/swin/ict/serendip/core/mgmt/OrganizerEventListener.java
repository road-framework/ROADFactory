package au.edu.swin.ict.serendip.core.mgmt;

import au.edu.swin.ict.road.composite.Composite.OrganiserRole;
import au.edu.swin.ict.road.composite.IOrganiserRole;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.SerendipEventListener;

/**
 * The event listener class for organizr The organizer can subscribe to
 * interested event patterns. For each and every event pattern, an instance of
 * this class need to be created.
 * 
 * @see SerendipOrganizerImpl
 * @author Malinda
 * 
 */
public class OrganizerEventListener extends SerendipEventListener {
    private String id = null;
    private IOrganiserRole orgRole = null;
    static int counter = 0;

    public OrganizerEventListener(String ep, String pId, IOrganiserRole orgRole) {
	this.id = "orgListener" + counter++;
	this.eventPattern = ep;
	this.pId = pId;
	this.orgRole = orgRole;
    }

    @Override
    public void eventPatternMatched(String ep, String pId)
	    throws SerendipException {
	MessageWrapper mw = new MessageWrapper();
	mw.setMessage("Event pattern " + ep + " matched for" + pId);
	((OrganiserRole) this.orgRole).sendToOrganiser(mw);
    }

    @Override
    public String getId() {
	return this.id;
    }

}
