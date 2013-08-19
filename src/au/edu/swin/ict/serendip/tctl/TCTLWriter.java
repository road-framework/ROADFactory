package au.edu.swin.ict.serendip.tctl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.processmining.framework.models.petrinet.PetriNet;

import au.edu.swin.ict.serendip.constraint.parser.ConstraintParser;
import au.edu.swin.ict.serendip.constraint.parser.SimplePetriNetConstraintParser;
import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.core.ModelProviderFactory;
import au.edu.swin.ict.serendip.core.SerendipException;

public class TCTLWriter {
    static Logger logger = Logger.getLogger(TCTLWriter.class);
    static ConstraintParser parser = new SimplePetriNetConstraintParser();

    /**
     * 
     * @param constraintId
     *            Should be the constriant id
     * @param constraint
     *            in serendip format
     * @throws IOException
     * @throws SerendipException
     */
    public static void writeTCTL(String fileName, String constraint,
	    PetriNet basePetriNet) throws IOException, SerendipException {
	BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, false));

	//
	String tctlExpr = parser.parse(constraint, basePetriNet) + "\n";// Bug
									// in
									// remeo
									// command
									// line
									// tool.
									// We
									// need
									// to
									// add a
									// line
									// break.
	logger.debug("Writing expression " + tctlExpr + " to file " + fileName);
	if (null == tctlExpr) {
	    throw new SerendipException("Cannot parse the constrinat "
		    + constraint);
	}

	bw.write(tctlExpr);
	bw.close();
    }
}
