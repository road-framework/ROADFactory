package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefApplyPatchAction implements DefAdaptAction {
    private String patchFile = null;

    public DefApplyPatchAction(String patchFile) {
	super();
	this.patchFile = patchFile;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// OrganiserOperationResult res =
	// comp.getOrganiserRole().applyPatch(patchFile);
	OrganiserOperationResult res = comp.getSerendipEngine()
		.getSerendipOrgenizer().applyPatch(patchFile);
	return res.getResult();
    }

}
