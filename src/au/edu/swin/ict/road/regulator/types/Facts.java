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
public class Facts {
    private Fact[] fact;

    public void setFact(Fact[] facts) {
	this.fact = facts;
    }

    public Fact[] getFact() {
	return fact;
    }
}
