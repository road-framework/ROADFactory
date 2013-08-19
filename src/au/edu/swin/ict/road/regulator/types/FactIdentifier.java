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
public class FactIdentifier {

    private String identifierKey;
    private String identifierValue;

    public String getIdentifierKey() {
	return identifierKey;
    }

    public void setIdentifierKey(String identifierKey) {
	this.identifierKey = identifierKey;
    }

    public String getIdentifierValue() {
	return identifierValue;
    }

    public void setIdentifierValue(String identifierValue) {
	this.identifierValue = identifierValue;
    }

}
