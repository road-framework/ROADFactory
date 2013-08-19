package au.edu.swin.ict.serendip.message;

import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventRecord;

/**
 * @deprecated
 * @author Malinda
 * 
 */
public abstract class MessageInterpreter {
    protected SerendipEngine engine = null;

    public MessageInterpreter(SerendipEngine engine) {
	this.engine = engine;
    }

    public abstract EventRecord[] interpretMessage(Message msg)
	    throws SerendipException;

    protected boolean isAmbiguous(String eventPattern) {
	// Ambiguous
	if (eventPattern.contains("OR")) {
	    return true;
	}

	return false;
    }

}
