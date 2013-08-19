/**
 * 
 */
package au.edu.swin.ict.road.regulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Aditya
 * 
 */
public class FactMessageSaxHandler extends DefaultHandler {

    private List<FactObject> factObjectList = new ArrayList<FactObject>();
    private FactObject factObject = null;
    private int factSource;
    private String attributeKey = null;
    private String attributeValue = null;
    private String IdentifierKey = null;
    private String IdentifierValue = null;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private String currentElement = null;
    private String factNameValue = null;

    public void startElement(String uri, String localName, String qName,
	    Attributes attributes) throws SAXException {

	if (localName.equalsIgnoreCase("IdentifierKey")) {
	    currentElement = "IdentifierKey";
	}

	if (localName.equalsIgnoreCase("IdentifierValue")) {
	    currentElement = "IdentifierValue";
	}

	if (localName.equalsIgnoreCase("AttributeKey")) {
	    currentElement = "AttributeKey";
	}

	if (localName.equalsIgnoreCase("AttributeValue")) {
	    currentElement = "AttributeValue";
	}

	if (localName.equalsIgnoreCase("Name")) {
	    currentElement = "Name";
	}

	if (localName.equalsIgnoreCase("Source")) {
	    currentElement = "Source";
	}
    }

    public void characters(char ch[], int start, int length)
	    throws SAXException {
	String value = new String(ch, start, length);

	if (!value.trim().equals("")) {

	    if (currentElement.equalsIgnoreCase("Name")) {
		factNameValue = value;
	    }

	    else if (currentElement.equalsIgnoreCase("Source")) {
		if (value.equalsIgnoreCase("Internal")) {
		    factSource = FactObject.INTERNAL_SOURCE;
		} else if (value.equalsIgnoreCase("External")) {
		    factSource = FactObject.EXTERNAL_SOURCE;
		} else {
		    throw new SAXException(
			    "The source must be either internal or external");
		}
	    }

	    else if (currentElement.equalsIgnoreCase("AttributeKey")) {
		attributeKey = value;
	    }

	    else if (currentElement.equalsIgnoreCase("AttributeValue")) {
		attributeValue = value;
		properties.put(attributeKey, attributeValue);
	    }

	    else if (currentElement.equalsIgnoreCase("IdentifierKey")) {
		IdentifierKey = value;
	    }

	    else if (currentElement.equalsIgnoreCase("IdentifierValue")) {
		IdentifierValue = value;
	    }

	}

    }

    public void endElement(String uri, String localName, String qName)
	    throws SAXException {

	if (localName.equalsIgnoreCase("Fact")) {

	    if (factNameValue.length() == 0 || IdentifierKey.length() == 0
		    || IdentifierValue.length() == 0) {
		throw new SAXException(
			"The Fact Type, Identifier or Identifier Value cannot be null");
	    } else {
		factObject = new FactObject(factNameValue, IdentifierKey,
			IdentifierValue);
		factObject.setAttributes(properties);
		factObject.setFactSource(factSource);
		factObjectList.add(factObject);
		factNameValue = null;
		IdentifierKey = null;
		IdentifierValue = null;
		properties = new HashMap<String, Object>();
	    }
	}
    }

    public List<FactObject> getFactObjectList() {
	return factObjectList;
    }

}
