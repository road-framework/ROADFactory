package au.edu.swin.ict.serendip.epc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.processmining.analysis.epcmerge.EPCMergeMethod;
import org.processmining.framework.models.epcpack.ConfigurableEPC;
import org.processmining.framework.models.epcpack.EPCEvent;
import org.processmining.framework.models.epcpack.EPCFunction;
import org.processmining.framework.models.epcpack.EPCObject;
import org.processmining.mining.epcmining.EPCResult;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;

public class EPMLBehavior {
    static Logger logger = Logger.getLogger(EPMLBehavior.class);
    private String epmllocation = "";
    private String[] btIds = null;
    private static final String EXT = ".epml";
    private Vector<EPCResult> resultsVec = new Vector<EPCResult>();

    public EPMLBehavior(String epmllocation, String[] btIds) {
	this.epmllocation = epmllocation;
	this.btIds = btIds;
    }

    public static void createEmptyBehaviors(String epmllocation, String[] btIds)
	    throws IOException {
	// TODO: Create empty epml files for the given btIds array. Later users
	// can load them using the editor and define the interactions
	for (int i = 0; i < btIds.length; i++) {
	    createEmptyBehavior(epmllocation, btIds[i]);

	}
    }

    public static void createEmptyBehavior(String epmllocation, String btId)
	    throws IOException {

	// Create a file for each behavior id if they do not exist
	File file = new File(epmllocation + "/" + btId + EXT);
	if (file.exists()) {
	    logger.debug("We do not create " + file.getPath()
		    + " as it already exists");
	    return;
	}
	FileWriter fstream = new FileWriter(file.getAbsolutePath());

	BufferedWriter out = new BufferedWriter(fstream);
	out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<epml:epml xmlns:epml=\"http://www.epml.de\" "
		+ "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
		+ "xsi:schemaLocation=\"" + "epml_1_draft.xsd\">\n"
		+ "<epc EpcId=\"1\" Name=\"EPC\">\n" + "</epc>\n"
		+ "</epml:epml>\n");
	out.close();
    }

    // Translate the epml files to equivalent petrinets.
    public void translateAll() throws FileNotFoundException, IOException {
	File file = null;
	EPCResult result = null;

	for (int i = 0; i < btIds.length; i++) {
	    file = new File(epmllocation + btIds[i] + EXT);
	    result = EPMLReader.readFile(file);
	    resultsVec.add(result);
	}
    }

    public Vector<EPCResult> getEPCResults() {
	return this.resultsVec;
    }

}
