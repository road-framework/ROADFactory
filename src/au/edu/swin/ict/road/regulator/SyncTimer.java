package au.edu.swin.ict.road.regulator;

import java.util.TimerTask;

import au.edu.swin.ict.road.composite.message.MessageWrapper;

/**
 * Class that acts as a timer that continuously synchronises the
 * <code>FactSynchroniser</code> to load facts. The timer is only used when the
 * regulator is set to <em>active</em> mode.
 * <p/>
 * This class extends the <code>TimerTask</code> class.
 * 
 * @author Jovino Margathe (jmargathe@gmail.com)
 * @author mtalib
 */
public class SyncTimer extends TimerTask {

    private FactSynchroniser fSync;

    /**
     * Default constructor for <code>SyncTimer</code> class. It retrieves a
     * reference for the <code>FactSynchroniser</code> that it is associated to.
     * 
     * @param fSync
     *            the <code>FactSynchroniser</code> associated with this timer.
     */
    public SyncTimer(FactSynchroniser fSync) {
	this.fSync = fSync;
    }

    @Override
    public void run() {

	MessageWrapper factMessage = new MessageWrapper("Requesting Facts",
		"request" + fSync.getFactType() + "FactsFromPlayer", false);
	fSync.getAssociatedRole().delivererPutOutgoingMessage(factMessage);
	fSync.synchroniseFacts();
    }
}
