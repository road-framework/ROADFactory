package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.serendip.composition.ProcessDefinition;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefPDUpdateAction implements DefAdaptAction {
    private String id = null;
    private String prop = null;
    private String value = null;

    public DefPDUpdateAction(String id, String prop, String value) {
	super();
	this.id = id;
	this.prop = prop;
	this.value = value;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// TODO Auto-generated method stub
	ProcessDefinitionsType processDefinitions = comp.getSmcBinding()
		.getProcessDefinitions();
	if (null == processDefinitions) {
	    throw new AdaptationException("No process definitions");
	}
	for (ProcessDefinitionType pd : processDefinitions
		.getProcessDefinition()) {
	    if (pd.getId().equals(this.id)) {
		// found pd
		if (prop.equals(ProcessDefinition.propertyAttribute.cos)) {
		    pd.setCoS(value);
		} else if (prop.equals(ProcessDefinition.propertyAttribute.cot)) {
		    pd.setCoT(value);
		} else {
		    throw new AdaptationException("Unknown property " + prop);
		}

		return true;
	    }
	}
	throw new AdaptationException("Cannot find process def " + id);

    }

}
