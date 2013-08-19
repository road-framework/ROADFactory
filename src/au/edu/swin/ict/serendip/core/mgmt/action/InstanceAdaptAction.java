package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * Any adaptation implementation for process instances should implement this
 * interface
 * 
 * @author Malinda
 */
public interface InstanceAdaptAction extends AdaptationAction {
    public boolean adapt(ProcessInstance pi) throws AdaptationException;
}
