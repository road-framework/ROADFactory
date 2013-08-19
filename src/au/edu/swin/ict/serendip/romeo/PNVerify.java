package au.edu.swin.ict.serendip.romeo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.apache.commons.io.IOUtils;

import au.edu.swin.ict.serendip.verficiation.VerificationResult;

public class PNVerify {
    private static final String modelExtension = ".xml";
    private static final String propertyExtension = ".tctl";
    String mercutioTctl = null;
    String propertyDir = null;
    String modelDir = null;

    /**
     * This class will verify a set of TCTL properties against a petriNet
     * 
     * @param exeName
     * @param propertyDir
     * @param modelDir
     */
    public PNVerify(String exeName, String propertyDir, String modelDir) {
	this.mercutioTctl = exeName;
	this.propertyDir = propertyDir;
	this.modelDir = modelDir;
    }

    /**
     * 
     * @param behaviorName
     *            the name of the behavior that need to be verified
     * @param propertyNames
     *            the name of the properties.
     * @return a verification result that contains the result=valid/invalid +
     *         {metadata}
     * @throws Exception
     *             if something goes wrong. e.g IOException
     */
    public VerificationResult verifyAll(String behaviorName,
	    String[] propertyNames) throws Exception {
	VerificationResult result = null;
	boolean answer = false;
	for (int i = 0; i < propertyNames.length; i++) {
	    // [C:/models/]R1-R2_B1[.xml]
	    answer = this.verifyTCTL(new File(this.modelDir + behaviorName
		    + modelExtension), new File(this.propertyDir
		    + propertyNames[i] + propertyExtension));
	    // If a single property failed, return false
	    if (!answer) {
		result = new VerificationResult(false, "", behaviorName,
			propertyNames[i]);
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
    private boolean verifyTCTL(File fileXml, File tctlFile) throws Exception {
	// Calling mercutio-tctl.exe
	// "mercutioTctl" is the path of the mercutio-tctl.exe program
	// "tctlFile" is the (text) file containing the TCTL property (should
	// have the *.tctl extension)
	// "fileXml" is the XML containing the TPN to be checked
	// the "-t" and "-f" flags are explained in the mercutio-tctl
	// documentation (type mercutio-tctl -h for more information)
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
		String mercutioAnsFile = fileXml.getAbsolutePath() + ".ans";// e.g.
									    // src.xml.ans
		PrintWriter printWriterResult = new PrintWriter(new FileWriter(
			mercutioAnsFile, false));
		while ((s = br.readLine()) != null) {
		    if (s.equals("true")) {
			answer = true;
		    } else {
			// Whatever else is given by the program is the
			// counter-example, we should save it

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
	}
	return answer;
    }
}
