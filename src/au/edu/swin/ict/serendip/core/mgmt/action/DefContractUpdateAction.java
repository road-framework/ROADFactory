package au.edu.swin.ict.serendip.core.mgmt.action;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.message.OrganiserOperationResult;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;

public class DefContractUpdateAction implements DefAdaptAction {
    private String id = null;
    private String name = null;
    private String description = null;
    private String state = null;
    private String type = null;
    private String ruleFile = null;
    private boolean isAbstract = false;
    private String roleAId = null;
    private String roleBId = null;

    public DefContractUpdateAction(String id, String name, String description,
	    String state, String type, String ruleFile, boolean isAbstract,
	    String roleAId, String roleBId) {
	super();
	this.id = id;
	this.name = name;
	this.description = description;
	this.state = state;
	this.type = type;
	this.ruleFile = ruleFile;
	this.isAbstract = isAbstract;
	this.roleAId = roleAId;
	this.roleBId = roleBId;
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {

	OrganiserOperationResult res = comp.getOrganiserRole().addNewContract(
		id, name, description, state, type, ruleFile, isAbstract,
		roleAId, roleBId);
	return res.getResult();
    }

}
