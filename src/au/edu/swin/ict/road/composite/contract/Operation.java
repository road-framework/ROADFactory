package au.edu.swin.ict.road.composite.contract;

import java.util.ArrayList;
import java.util.List;

public class Operation {

    private String name;
    private List<Parameter> parameters;
    private String returnType;// Could this be a complex type?

    public Operation() {
	name = null;
	returnType = null;
	parameters = new ArrayList<Parameter>();
    }

    public Operation(String name, List<Parameter> parameters, String returnType) {
	super();
	this.name = name;
	this.parameters = parameters;
	this.returnType = returnType;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Parameter> getParameters() {
	return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
	this.parameters = parameters;
    }

    public String getReturnType() {
	return returnType;
    }

    public void setReturnType(String returnType) {
	this.returnType = returnType;
    }

    public String toString() {
	return "Operation: " + name;
    }
}
