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
 * Class which represents the SAX Parser for creating FactObject objects from an
 * XML String. The class contains a single static method which takes an XML
 * String and generates FactObject objects.
 * 
 * @author Aditya Chitre (chitreaditya@gmail.com)
 * 
 */
public class FactParser {

    /**
     * Function to parse the XML String and convert it into FactObject objects
     * 
     * @param xmlString
     *            The input XML String which has to be parsed
     * 
     * @return List<FactObject> which have been created using the SAX parser
     */
    public static List<FactObject> parse(String xmlString) {

	FactSaxHandler saxHandler = new FactSaxHandler();
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
