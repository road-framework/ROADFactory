package au.edu.swin.ict.road.composite.player;

import java.util.ArrayList;
import java.util.List;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.xml.bindings.PlayerBindingType;

public class PlayerBinding {

    private String id;
    private String name;
    private String description;
    private String endpoint;
    private String implementation;
    private boolean endpointBinding;
    private boolean implementationBinding;
    private List<String> roleIdList;
    private Composite playerComposite = null;// Not null, when player is a
					     // subcomposite

    public PlayerBinding() {
	id = null;
	name = null;
	description = null;
	endpoint = null;
	implementation = null;
	endpointBinding = false;
	implementationBinding = false;
	roleIdList = new ArrayList<String>();
    }

    public PlayerBinding(PlayerBindingType playerBinding) {
	id = null;
	name = null;
	description = null;
	endpoint = null;
	implementation = null;
	endpointBinding = false;
	implementationBinding = false;
	roleIdList = new ArrayList<String>();

	this.extractData(playerBinding);
    }

    public boolean isBoundToRole(String roleId) {
	return roleIdList.contains(roleId);
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public String getEndpoint() {
	return endpoint;
    }

    public void setEndpoint(String endpoint) {
	this.endpoint = endpoint;
    }

    public String getImplementation() {
	return implementation;
    }

    public void setImplementation(String implementation) {
	this.implementation = implementation;
    }

    public List<String> getRoleIdList() {
	return roleIdList;
    }

    public void setRoleIdList(List<String> roleIdList) {
	this.roleIdList = roleIdList;
    }

    public boolean isEndpointBinding() {
	return endpointBinding;
    }

    public void setEndpointBinding(boolean endpointBinding) {
	this.endpointBinding = endpointBinding;
    }

    public boolean isImplementationBinding() {
	return implementationBinding;
    }

    public void setImplementationBinding(boolean implementationBinding) {
	this.implementationBinding = implementationBinding;
    }

    private void extractData(PlayerBindingType playerBinding) {
	if (playerBinding.getId() != null) {
	    id = playerBinding.getId();
	}
	if (playerBinding.getName() != null) {
	    name = playerBinding.getName();
	}
	if (playerBinding.getDescription() != null) {
	    description = playerBinding.getDescription();
	}

	if (playerBinding.getImplementation() != null) {
	    implementation = playerBinding.getImplementation();
	    implementationBinding = true;
	} else {
	    endpoint = playerBinding.getEndpoint();
	    endpointBinding = true;
	}

	List<String> bindingRoleIdList = playerBinding.getRoles().getRoleID();

	for (String rid : bindingRoleIdList) {
	    roleIdList.add(rid);
	}
    }

    /**
     * Creates and returns the player binding object from the composite's player
     * binding object to use in the JAXB transformation of the XML
     * representation of the composite.
     * 
     * @return the player binding object used in JAXB transformation
     */
    public PlayerBindingType createPlayerBinding() {

	/*
	 * Creating a new player binding type - JAXB binding object and setting
	 * the instance variables using the player binding object's instance
	 * values
	 */
	PlayerBindingType playerBindingType = new PlayerBindingType();

	playerBindingType.setId(this.getId());
	playerBindingType.setName(this.getName());
	playerBindingType.setEndpoint(this.getEndpoint());

	/* Creating the role type object */
	PlayerBindingType.Roles roles = new PlayerBindingType.Roles();
	List<String> roleIDs = new ArrayList<String>();

	for (String id : this.getRoleIdList()) {

	    roleIDs.add(id);
	    roles.getRoleID().add(id);
	}

	/* Adding role object to the player binding object */
	// roles.setRoleIDList(roleIDs);
	playerBindingType.setRoles(roles);
	playerBindingType.setDescription(this.getDescription());

	return playerBindingType;
    }

}
