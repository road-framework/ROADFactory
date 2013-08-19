package au.edu.swin.ict.road.regulator.types;

/**
 * This class type is created to act either as a parameter type or return type
 * for the fact methods in the required interface and provided interfaces of the
 * role. These are used instead of the bindings generated from the SMC as the
 * bindings use lists which do not have an equivalent type in WSDL.
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */
public class Fact {
    private String name;
    private String source;
    private FactIdentifier identifier;
    private FactAttributes attributes;

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

    public void setIdentifier(FactIdentifier identifier) {
	this.identifier = identifier;
    }

    public FactIdentifier getIdentifier() {
	return identifier;
    }

    public void setAttributes(FactAttributes attributes) {
	this.attributes = attributes;
    }

    public FactAttributes getAttributes() {
	return attributes;
    }

}
