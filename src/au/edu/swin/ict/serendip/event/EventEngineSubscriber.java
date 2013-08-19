package au.edu.swin.ict.serendip.event;

import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.SerendipException;

/**
 * Any entity that performs tasks based upon a pre-condition should implement
 * this interface.
 * 
 * @author Malinda
 * 
 */
public interface EventEngineSubscriber {
    public String getId();

    public void performTask(TaskPerformAction e) throws SerendipException;
}
