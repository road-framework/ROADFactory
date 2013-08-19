package au.edu.swin.ict.road.roadtest.parameter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.contract.Parameter;

/**
 * This class is there to help to build paramter lists and map for ui
 * 
 * @author Ufuk Altin (altin.ufuk@gmail.com)
 * 
 */
public class ObjectParameterHelper {
    private static Logger log = Logger.getLogger(ObjectParameterHelper.class
	    .getName());
    private static final HashSet<Class<?>> WRAPPER_TYPES = getWrapperTypes();
    private static final HashSet<String> PRIMITIVE_TYPES = getPrimitiveTypes();

    /**
     * Cecks if a given class c is of primitive type. String in this case is a
     * primitive type as well
     * 
     * @param c
     *            The class to check
     * @return True if the Class c is a primitive type otherwise false
     */
    public static boolean isPrimitive(Class<?> c) {
	if (ObjectParameterHelper.isWrapperType(c) || c.isPrimitive()) {
	    return true;
	} else {
	    return false;
	}
    }

    public static Map<String, Content> getParameterMap(
	    Map<String, Class<?>> strClasMap) {
	Map<String, Content> ret = new HashMap<String, Content>();
	Iterator it = strClasMap.keySet().iterator();
	while (it.hasNext()) {
	    // Content cont = new Content();
	    String key = (String) it.next();
	    ret.put(key, ObjectParameterHelper.getContent(strClasMap.get(key)));
	}
	return ret;
    }

    /**
     * Gets the parameter map for the list of parameters provided
     * 
     * @param parameters
     *            The list of parameters which needs to be transformed to a Map
     * @return The map which contains the parameters so the ui can work wit it
     */
    public static Map<String, List<Content>> getParameterMap(
	    List<Parameter> parameters) {
	Map<String, List<Content>> ret = new HashMap<String, List<Content>>();
	for (Parameter p : parameters) {
	    List<Content> cList = new ArrayList<Content>();
	    Content c = new Content();
	    if (p.getType().endsWith("[]"))
		c.setArray(true);
	    else
		c.setArray(false);
	    c.setMapValues(null);
	    if (ObjectParameterHelper.isPrimitiveType((p.getType()))) {
		c.setPrimitive(true);
	    } else {
		c.setPrimitive(false);
	    }
	    c.setType(p.getType());
	    cList.add(c);
	    ret.put(p.getName(), cList);
	}

	return ret;
    }

    private static Content getContent(Class<?> clazz) {
	Content content = new Content();
	if (clazz.isArray()) {
	    content.setArray(true);
	    clazz = clazz.getComponentType();
	} else {
	    content.setArray(false);
	}
	if (ObjectParameterHelper.isWrapperType(clazz) || clazz.isPrimitive()) {
	    content.setPrimitive(true);
	    content.setMapValues(null);
	    log.debug("is primitive->" + clazz.getSimpleName());
	} else {
	    content.setPrimitive(false);
	    content.setMapValues(ObjectParameterHelper
		    .getComplexObjectMap(clazz));
	    log.debug("is not primitive " + clazz.getSimpleName());
	}
	content.setType(clazz.getSimpleName());
	return content;
    }

    private static Map<String, List<Content>> getComplexObjectMap(Class<?> c) {
	Map<String, List<Content>> ret = new HashMap<String, List<Content>>();

	for (Field f : c.getFields()) {
	    List<Content> contentList = new ArrayList<Content>();
	    Content content = ObjectParameterHelper.getContent(f.getType());
	    contentList.add(content);
	    ret.put(f.getName(), contentList);
	}
	return ret;
    }

    /**
     * 
     * @param clazz
     * @return True if it is a wrapper type
     */
    public static boolean isWrapperType(Class<?> clazz) {
	return WRAPPER_TYPES.contains(clazz);
    }

    /**
     * 
     * @param str
     * @return True if it is a primitive type otherwise false
     */
    public static boolean isPrimitiveType(String str) {
	return PRIMITIVE_TYPES.contains(str);
    }

    private static HashSet<Class<?>> getWrapperTypes() {
	HashSet<Class<?>> ret = new HashSet<Class<?>>();
	ret.add(Boolean.class);
	ret.add(Character.class);
	ret.add(Byte.class);
	ret.add(Short.class);
	ret.add(Integer.class);
	ret.add(Long.class);
	ret.add(Float.class);
	ret.add(Double.class);
	ret.add(Void.class);
	ret.add(String.class);
	return ret;
    }

    private static HashSet<String> getPrimitiveTypes() {
	HashSet<String> ret = new HashSet<String>();
	ret.add("boolean");
	ret.add("char");
	ret.add("byte");
	ret.add("short");
	ret.add("int");
	ret.add("long");
	ret.add("float");
	ret.add("double");
	ret.add("String");
	ret.add("string");
	return ret;
    }

    private static void writeNonePrimitiveType(StringBuilder sb,
	    Map<String, List<Content>> listNonPrimitive) {
	for (String s : listNonPrimitive.keySet()) {
	    for (Content c : listNonPrimitive.get(s)) {
		writeOpenTag(s, sb);
		if (c.isPrimitive())
		    sb.append(c.getValue());
		else
		    writeNonePrimitiveType(sb, c.getMapValues());
		writeCloseTag(s, sb);
	    }
	}
    }

    /**
     * Creates an StringBuilder from a Map of Parameters which then can be send
     * through ROADfactory
     * 
     * @param toConvertMap
     *            The Map which contains the values of the parameters
     * @return A StringBuilder with the content of the XML document
     */
    public static StringBuilder getXmlString(
	    Map<String, List<Content>> toConvertMap) {
	StringBuilder sb = new StringBuilder();
	sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
	for (String s : toConvertMap.keySet()) {
	    for (Content c : toConvertMap.get(s)) {
		writeOpenTag(s, sb);
		if (c.isPrimitive())
		    sb.append(c.getValue());
		else
		    writeNonePrimitiveType(sb, c.getMapValues());
		writeCloseTag(s, sb);
	    }
	}

	return sb;
    }

    private static void writeOpenTag(String tagName, StringBuilder sb) {
	sb.append(System.getProperty("line.separator") + "<");
	sb.append(tagName);
	sb.append(">");
    }

    private static void writeCloseTag(String tagName, StringBuilder sb) {
	sb.append("</");
	sb.append(tagName);
	sb.append(">");
    }
}
