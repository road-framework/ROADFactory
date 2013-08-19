package au.edu.swin.ict.serendip.core.mgmt;

import java.util.List;

import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.road.xml.bindings.SMC;
import au.edu.swin.ict.serendip.composition.Composition;
import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.mgmt.action.DefAdaptAction;
import au.edu.swin.ict.serendip.core.mgmt.action.InstanceAdaptAction;

/**
 * 
 * @author Malinda
 * 
 */
public class DefAdaptationEngine {
    private SerendipEngine engine = null;

    public DefAdaptationEngine(SerendipEngine engine) {
	this.engine = engine;
    }

    public void executeAdaptation(List<DefAdaptAction> adaptationList)
	    throws AdaptationException {
	this.backup();
	for (DefAdaptAction daa : adaptationList) {
	    boolean res = daa
		    .adapt(this.engine.getComposition().getComposite());

	    if (!res) {
		this.restore();
		throw new AdaptationException(
			"Problem in adapting the definition of "
				+ engine.getCompositionName());
	    }
	}
    }

    private void backup() {
	// TODO Auto-generated method stub

    }

    private void restore() {
	// TODO Auto-generated method stub

    }
}
