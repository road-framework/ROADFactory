package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermsType;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefBehaviorUpdateAction implements DefAdaptAction {
    private String id = null;
    private String prop = null;
    private String value = null;

    public DefBehaviorUpdateAction(String id, String prop, String value) {
	super();
	this.id = id;
	this.prop = prop;
	this.value = value;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {

	BehaviorTermsType behaviorTerms = comp.getSmcBinding()
		.getBehaviorTerms();
	if (null == behaviorTerms) {
	    throw new AdaptationException("No behaviors");
	}
	for (BehaviorTermType btt : behaviorTerms.getBehaviorTerm()) {
	    if (btt.getId().equals(id)) {
		if (prop.equals(BehaviorTerm.propertyAttribute.extendsFrom
			.name())) {
		    // TODO: Check whether the parent bt (value) exist or not
		    btt.setExtends(value);
		} else if (prop
			.equals(BehaviorTerm.propertyAttribute.isAbstract
				.name())) {
		    btt.setIsAbstract(value.equals(Constants.VAL_TRUE));
		}

		return true;
	    }
	}

	throw new AdaptationException("Cannot find Behavior " + id);
    }

}
