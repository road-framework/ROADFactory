package au.edu.swin.ict.road.roadtest.parameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Content for a parameterised method signature
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class Content implements Cloneable {
    private boolean primitive;
    private boolean array;
    private String value;
    private String type;
    private String name;
    private Map<String, List<Content>> mapValues;

    public Content() {
	this.mapValues = new HashMap<String, List<Content>>();
    }

    /**
     * Set primitive type of this content
     * 
     * @param primitive
     *            the primitive to set
     */
    public void setPrimitive(boolean primitive) {
	this.primitive = primitive;
    }

    /**
     * Is this content primitive
     * 
     * @return the primitive
     */
    public boolean isPrimitive() {
	return primitive;
    }

    /**
     * @param array
     *            the array to set
     */
    public void setArray(boolean array) {
	this.array = array;
    }

    /**
     * @return the array
     */
    public boolean isArray() {
	return array;
    }

    /**
     * @param value
     *            the value to set
     */
    public void setValue(String value) {
	this.value = value;
    }

    /**
     * @return the value
     */
    public String getValue() {
	return value;
    }

    /**
     * @param type
     *            the type to set
     */
    public void setType(String type) {
	this.type = type;
    }

    /**
     * @return the type
     */
    public String getType() {
	return type;
    }

    /**
     * @param mapValues
     *            the mapValues to set
     */
    public void setMapValues(Map<String, List<Content>> mapValues) {
	this.mapValues = mapValues;
    }

    /**
     * @return the mapValues
     */
    public Map<String, List<Content>> getMapValues() {
	return mapValues;
    }

    /**
     * @param value
     *            the name to set
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    public String toString() {
	if (this.isArray())
	    return this.getName() + ":" + this.getType() + "[]";
	else
	    return this.getName() + ":" + this.getType();
    }

    public Object clone() throws CloneNotSupportedException {
	return super.clone();
    }

}
