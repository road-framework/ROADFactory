package au.edu.swin.ict.serendip.core.mgmt.scripting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Command {

    private String name;
    private Properties props = new Properties(); // keyed properties
    private Set values = new HashSet(); // unkeyed properties

    public Command(String command) {
	this.name = command;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return this.name;
    }

    public void addProperty(String key, String value) {
	this.props.setProperty(key, value);
    }

    public void addProperty(String value) {
	this.values.add(value);
    }

    public Properties getProperties() {
	return this.props;
    }

    public String getProperty(String key) {

	return this.props.getProperty(key);
    }

    public Iterator getValueIterator() {
	return this.values.iterator();
    }

    public String toString() {
	StringBuffer buffer = new StringBuffer(quoteString(name));

	Iterator valueIter = values.iterator();
	while (valueIter.hasNext()) {
	    buffer.append(" ");
	    buffer.append(quoteString((String) valueIter.next()));
	}

	Iterator entryIter = props.entrySet().iterator();
	while (entryIter.hasNext()) {
	    Map.Entry entry = (Map.Entry) entryIter.next();
	    buffer.append(" ");
	    buffer.append(quoteString((String) entry.getKey()));
	    buffer.append("=");
	    buffer.append(quoteString((String) entry.getValue()));
	}

	buffer.append(";");
	return buffer.toString();
    }

    public static String quoteString(String value) {
	if (value != null) {
	    for (int i = 0; i < value.length(); i++) {
		char c = value.charAt(i);
		if (!Character.isLetterOrDigit(c) && c != '_') {
		    return "\"" + value + "\"";
		}
	    }
	}
	return value;
    }
}
