package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

/**
 * All the atomic commands (adaptation actions) for the schema definition need
 * to support this.
 * 
 * @author Malinda
 */
public interface DefAdaptAction extends AdaptationAction {

    public boolean adapt(Composite comp) throws AdaptationException;
}
