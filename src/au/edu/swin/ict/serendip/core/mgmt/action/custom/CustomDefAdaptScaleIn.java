package au.edu.swin.ict.serendip.core.mgmt.action.custom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.IRole;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.player.PlayerBinding;
import au.edu.swin.ict.serendip.core.mgmt.AdaptationException;
import au.edu.swin.ict.serendip.core.mgmt.action.CustomDefAdaptationAction;

public class CustomDefAdaptScaleIn extends CustomDefAdaptationAction {
    private String lblStratergy = null;
    private List<String> tobeRemoved = new ArrayList<String>();

    public CustomDefAdaptScaleIn(String commandName, Properties properties) {
	super(commandName, properties);

	this.lblStratergy = super.getProperties().getProperty("s").trim();
	String tobeRemovedStr = super.getProperties().getProperty("rls").trim();
	if (null != tobeRemovedStr) {
	    String[] arr = tobeRemovedStr.split(",");
	    this.tobeRemoved = Arrays.asList(arr);
	}
    }

    @Override
    public boolean adapt(Composite comp) throws AdaptationException {
	// Map<String, PlayerBinding> playerMap = comp.getPlayerBindingMap();
	Map<String, Role> roleMap = comp.getRoleMap();

	// First we make sure that all the specified roles exist
	List<Role> roleArrList = new ArrayList<Role>();
	for (String rId : tobeRemoved) {
	    // for(String key: roleMap.keySet()){
	    // System.out.println("Key:"+key+":");
	    // }
	    Role r = roleMap.get(rId.trim());
	    if (r == null) {
		throw new AdaptationException("Cannot find the role " + rId);
	    } else {
		roleArrList.add(r);
	    }
	}

	// Then we perform the adaptations
	if ((comp.getRoleMap().size() - 2) <= roleArrList.size()) {
	    // In this case we left with just one role. We need to remove the
	    // complete composite and allocate the remaining player to the
	    // parent composite.
	    System.out.println("TODO:Remove ExpOrg");
	} else {
	    // We need to keep the composite but remove a subset of roles
	    for (Role role : roleArrList) {
		// We remove adjoining contracts. Well.. in reality there is
		// only one for a scaled out composite
		Contract[] adjContracts = role.getAllContracts();
		for (Contract c : adjContracts) {
		    comp.getOrganiserRole().removeContract(c.getId());
		}
		// We then remove the role.
		comp.getOrganiserRole().removeRole(role.getId());
	    }
	}

	return true;
    }

}
