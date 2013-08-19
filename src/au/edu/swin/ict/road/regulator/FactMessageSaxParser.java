/**
 * 
 */
package au.edu.swin.ict.road.regulator;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Aditya
 * 
 */
public class FactMessageSaxParser {

    public static List<FactObject> parse(String xmlString) {

	FactMessageSaxHandler saxHandler = new FactMessageSaxHandler();
	if (xmlString.length() != 0) {
	    // Creating the input source for reading the XML string
	    InputSource source = new InputSource(new StringReader(
		    xmlString.toString()));

	    // Everytime the XML String is parsed, a new instance of
	    // FactSaxHandler
	    // is used so that it does not conflict with the earlier ones

	    XMLReader reader;
	    try {
		reader = XMLReaderFactory.createXMLReader();

		// Setting the content handler and parsing the XML String
		reader.setContentHandler(saxHandler);
		reader.setErrorHandler(saxHandler);
		reader.parse(source);
	    } catch (SAXException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	return saxHandler.getFactObjectList();
    }

}
