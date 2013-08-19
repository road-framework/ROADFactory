package au.edu.swin.ict.road.regulator;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * Class that acts as a default implementation of the
 * <code>IContextProvider</code> interface. This class is only used when the
 * regulator is set to <em>passive</em> mode, requiring the external entities to
 * invoke a method that is implemented within this class.
 * 
 * @author Jovino Margathe (jmargathe@gmail.com)
 */
public class ContextProvider implements IContextProvider {
    private static Logger log = Logger.getLogger(ContextProvider.class
	    .getName());
    private List<FactObject> factList;
    private FactSynchroniser fSync;

    /**
     * Default constructor for the <code>ContextProvider</code> class. This
     * constructor should <strong>NOT</strong> be used since the
     * <code>ContextProvider</code> requires a reference to the
     * <code>FactSynchroniser</code>.
     */
    public ContextProvider() {
	this.factList = new ArrayList<FactObject>();
    }

    /**
     * Constructor for the <code>ContextProvider</code> that sets the reference
     * for the <code>FactSynchroniser</code> associated with this
     * <code>ContextProvider</code>. It is automatically set by the
     * <code>FactSynchroniser</code> during the composite boot time.
     * 
     * @param fSync
     *            the <code>FactSynchroniser</code> associated with this
     *            <code>ContextProvider</code>.
     */
    public ContextProvider(FactSynchroniser fSync) {
	this.factList = new ArrayList<FactObject>();
	this.fSync = fSync;
    }

    @Override
    public String pollFacts() {
	String facts = "";

	try {
	    URL url = new URL("http://127.0.0.1:8080/" + this.fSync.getId()
		    + "?parameter=getFacts");
	    // make connection
	    URLConnection urlc = url.openConnection();

	    // use post mode
	    urlc.setDoOutput(true);
	    urlc.setAllowUserInteraction(false);

	    // send query
	    PrintStream ps = new PrintStream(urlc.getOutputStream());
	    ps.print("");
	    // ps.print(query);
	    ps.close();

	    // get result
	    BufferedReader br = new BufferedReader(new InputStreamReader(
		    urlc.getInputStream()));
	    String l = null;
	    while ((l = br.readLine()) != null) {
		facts = l;
	    }
	    br.close();
	} catch (MalformedURLException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	log.debug("After invoking service: " + facts);
	return facts;
    }

    @Override
    public void notifyFactSource(String factXML) {
	try {
	    PrintWriter notifyExtEnt = new PrintWriter(new FileWriter(
		    "C:/dev/newProjects/Regulator-1.0/NotificationFile.txt",
		    true));
	    notifyExtEnt.println(factXML);
	    notifyExtEnt.close();
	    log.debug("NOTIFICATION DONE");
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    public void putFacts(String factXML) {
	this.factList = FactParser.parse(factXML);
	fSync.loadFacts();
    }

    @Override
    public String retrieveFactUpdates() {
	List<FactObject> fObjList = new ArrayList<FactObject>();
	if (fSync.getFactSource() == FactObject.EXTERNAL_SOURCE)
	    fObjList = fSync.getExternalFacts();
	else
	    fObjList = fSync.getInternalFacts();

	String facts = "<Facts>";
	for (FactObject fObj : fObjList)
	    facts += fObj.toXMLString();
	facts += "</Facts>";
	return facts;
    }

    @Override
    public List<FactObject> getExternalFacts() {
	return this.factList;
    }
}