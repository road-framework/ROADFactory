package au.edu.swin.ict.serendip.romeo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import au.edu.swin.ict.serendip.core.Constants;
import au.edu.swin.ict.serendip.verficiation.PNVerification;
import au.edu.swin.ict.serendip.verficiation.VerificationResult;

public class RomeoPNVerify {
    static Logger log = Logger.getLogger(RomeoPNVerify.class);
    private static final String modelExtension = Constants.SERENDIP_PETRINET_FILE_EXT;
    private static final String propertyExtension = Constants.SERENDIP_TCTL_FILE_EXT;
    String mercutioTctl = null;

    // String propertyDir = null;
    // String modelDir = null;

    /**
     * This class will verify a set of TCTL properties against a petriNet
     * 
     * @param exeName
     * @param propertyDir
     * @param modelDir
     * @throws Exception
     */
    public RomeoPNVerify(String exeName) throws Exception {
	if (null == exeName) {
	    throw new Exception("Romeo exe is not set");
	}
	this.mercutioTctl = exeName;
    }

    /**
     * 
     * @param id
     *            the name of the behavior/process that need to be verified
     * @param propertyNames
     *            the name of the properties.
     * @return a verification result that contains the result=valid/invalid +
     *         {metadata}
     * @throws Exception
     *             if something goes wrong. e.g IOException
     */
    public VerificationResult verifyAll(String fileLocation, String id,
	    String[] propertyNames) throws Exception {
	VerificationResult result = null;
	boolean answer = false;
	for (int i = 0; i < propertyNames.length; i++) {
	    // [C:/models/]R1-R2_B1[.xml]
	    File tptnFile = new File(fileLocation + id + modelExtension);
	    File tctlFile = new File(fileLocation + propertyNames[i]
		    + propertyExtension);
	    // ///////////////////////////////////
	    // VERIFICATION///////////////////
	    answer = this.verifyTCTL(tptnFile, tctlFile);
	    // If a single property failed, return false
	    if (!answer) {
		result = new VerificationResult(
			false,
			"Failed to verify against property " + propertyNames[i],
			id, propertyNames[i]);
		return result;
	    }
	}

	result = new VerificationResult(true, "", null, null);
	return result;
    }

    /**
     * Use to verify only one TCTL expression against a petri net model
     * 
     * @param mercutioTctl
     * @param tctlFile
     * @param fileXml
     * @return
     * @throws Exception
     */
    public boolean verifyTCTL(File fileXml, File tctlFile) throws Exception {

	/**
	 * mercutioTctl=path to the exe
	 * 
	 */
	log.info("Executing " + mercutioTctl + ", with "
		+ tctlFile.getAbsolutePath() + " against "
		+ fileXml.getAbsolutePath());
	if (!fileXml.exists()) {
	    throw new Exception("Model(XML) file not found "
		    + fileXml.getAbsolutePath());
	}
	if (!tctlFile.exists()) {
	    throw new Exception("Constraint(TCTL) file not found "
		    + tctlFile.getAbsolutePath());
	}

	/**
	 * C:\romeo\gui\bin>mercutio-tctl.exe -t -f
	 * E:\ROAD\svn\serendip\trunk\sample
	 * \composites\RoSAS3\temp\CO-GR_B1_C1.tctl
	 * E:\ROAD\svn\serendip\trunk\sample\composites\RoSAS3\temp\p12.xml
	 */
	String[] cmd = { mercutioTctl, "-t", "-f", tctlFile.getAbsolutePath(),
		fileXml.getAbsolutePath() };
	boolean answer = false;

	Runtime r = Runtime.getRuntime();
	try {
	    Process p = r.exec(cmd);

	    // The following code is needed to retrieve the result and then
	    // write down the trace (or counter-example) that is given by
	    // mercutio-tctl
	    // It is far from being optimum, but it does the trick!
	    // "mercutioAnsFile" is the path of the result file written by Romeo
	    // (usually in something like C:/Documents and
	    // Settings/userName/Preferences/.romeo/temp/mercutio.ans,
	    // so you should write something like
	    // System.getProperty("user.home") +
	    // "\\Preferences\\.romeo\\temp\\mercutio.ans";)
	    java.io.BufferedReader br = new java.io.BufferedReader(
		    new InputStreamReader(p.getInputStream()));

	    String s = null;
	    try {
		// String mercutioAnsFile=
		// fileXml.getAbsolutePath()+".ans";//e.g. src.xml.ans

		String mercutioAnsFile = System.getProperty("user.home")
			+ "/Preferences/.romeo/temp/mercutio.ans";
		log.info("ANS file :" + mercutioAnsFile);
		PrintWriter printWriterResult = new PrintWriter(new FileWriter(
			mercutioAnsFile, false));
		while ((s = br.readLine()) != null) {
		    if (s.equals("true")) {// This is how we read it :-(
			answer = true;
			log.info("Yey... Verified: "
				+ tctlFile.getAbsolutePath() + " against "
				+ fileXml.getAbsolutePath());
		    } else {
			// Whatever else is given by the program is the
			// counter-example, we should save it
			log.info("Opps ... verification failed for : "
				+ tctlFile.getAbsolutePath() + " against "
				+ fileXml.getAbsolutePath());
			printWriterResult.println(s);
		    }

		}
		printWriterResult.close();
	    } catch (IOException e) {
		throw new Exception(e.getMessage());
	    } finally {
		IOUtils.closeQuietly(br);
	    }
	} catch (IOException e) {
	    // There was a problem with mercutio-tctl, throw some exception
	    e.printStackTrace();
	}
	return answer;
    }

    public static void main(String[] args) {

	try {
	    RomeoPNVerify pnV = new RomeoPNVerify(
		    "C:/romeo/gui/bin/mercutio-tctl.exe");
	    boolean res = pnV
		    .verifyTCTL(
			    new File(
				    "E://ROAD/svn/serendip/trunk/sample/composites/RoSAS3/temp/p12.xml"),
			    new File(
				    "E://ROAD/svn/serendip/trunk/sample/composites/RoSAS3/temp/CO-GR_B1_C1.tctl"));
	    // boolean res = pnV.verifyTCTL(new
	    // File("C:/romeo/examples/toCharlott/model.xml"), new
	    // File("C:/romeo/examples/toCharlott/occurence.tctl"));
	    log.debug(res);
	} catch (Exception e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

    }
}
