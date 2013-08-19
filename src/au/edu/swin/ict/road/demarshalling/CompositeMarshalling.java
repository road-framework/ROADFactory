package au.edu.swin.ict.road.demarshalling;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Composite;
import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.RuleChangeTracker;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.player.PlayerBinding;
import au.edu.swin.ict.road.xml.bindings.ContractType;
import au.edu.swin.ict.road.xml.bindings.PlayerBindingType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.road.xml.bindings.RoleType;
import au.edu.swin.ict.road.xml.bindings.SMC;

/**
 * Composite Marshalling is responsible for generating a complete updated
 * snapshot of the runtime composite with the updated SMC and rule files. It
 * takes a runtime Java Composite object and uses it to get its current state
 * in-order to create an XML representation and store it in a file. It creates
 * all the binding objects which were earlier created during XML to JAVA
 * instantiation. JAXB is being used as the library for Java to XML
 * transformation. The corresponding rule files are also updated according to
 * the rule changes done and saved along with the SMC. This snapshot is saved in
 * the snapshots directory and is arranged according to the current time-stamp
 * in-order to create a history of snapshots for the composite.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class CompositeMarshalling {

    /* Instance variables for JAXB Binding classes */
    private List<RoleType> roleList = new ArrayList<RoleType>();
    private List<ContractType> contractList = new ArrayList<ContractType>();
    private List<PlayerBindingType> playerBindingList = new ArrayList<PlayerBindingType>();
    private static Logger log = Logger.getLogger(CompositeDemarshaller.class
	    .getName());
    private String dirPath = null;;
    private String foldername;

    /**
     * Returns the dirPath of the CompositeMarshalling.
     * 
     * @return the dirPath of the CompositeMarshalling.
     */
    public String getDirPath() {
	return dirPath;
    }

    /**
     * Sets the dirPath.
     * 
     * @param dirPath
     *            the dirPath to be set
     */
    public void setDirPath(String dirPath) {
	this.dirPath = dirPath;
    }

    /**
     * Returns the foldername of the CompositeMarshalling.
     * 
     * @return the foldername of the CompositeMarshalling.
     */
    public String getFoldername() {
	return foldername;
    }

    /**
     * Sets the foldername.
     * 
     * @param foldername
     *            the foldername to be set
     */
    public void setFoldername(String foldername) {
	this.foldername = foldername;
    }

    /**
     * Receives the composite and hands it over to the compositeConverter for
     * generating the XML representation of the Composite object
     * 
     * @param c
     *            the composite object which has to be converter to an XML
     *            representation
     */
    public boolean marshalSMC(Composite c) {

	this.dirPath = "";
	generateDirPath();
	if (convertComposite(c)) {
	    return true;
	} else {
	    return false;
	}

    }

    /**
     * Receives the composite and the directory path for storing the XML and
     * hands it over to the compositeConverter for generating the XML
     * representation of the Composite object
     * 
     * @param c
     *            the composite object which has to be converter to an XML
     *            representation
     * @param dirPath
     *            the String which represents the directory path for storing the
     *            XML representation
     */
    public boolean marshalSMC(Composite c, String dirPath) {
	if (dirPath != null) {
	    this.dirPath = dirPath;
	} else {
	    this.dirPath = ".";
	}
	generateDirPath();
	if (convertComposite(c)) {
	    return true;
	} else {
	    return false;
	}
    }

    /**
     * Converts the run time composite object to an XML representation and
     * stores it in an xml file. The method creates binding objects according to
     * the classes created during un-marshalling. It then uses JAXB to marshal
     * the SMC object which contains the updated state of the composite into a
     * xml representation.
     * 
     * @param c
     *            the composite object which has to be converter to an XML
     *            representation
     */
    private boolean convertComposite(Composite c) {

	log.info("Starting marshalling of the composite");

	/* Creating the Roles binding JAXB object */
	SMC.Roles roles = new SMC.Roles();

	/*
	 * Iterating through the composite object's role map to get the current
	 * roles
	 */
	Iterator i = c.getRoleMap().values().iterator();
	while (i.hasNext()) {

	    Role r = (Role) i.next();
	    /* Creating the actual RoleType binding object */
	    RoleType rt = r.createRoleBinding();
	    roleList.add(rt);
	    roles.getRole().add(rt);

	}
	// roles.setRole(this.roleList);

	/* Creating the Contract binding JAXB object */
	SMC.Contracts contracts = new SMC.Contracts();

	/*
	 * Iterating through the composite object's role map to get the current
	 * contracts
	 */
	Iterator i2 = c.getContractMap().values().iterator();
	while (i2.hasNext()) {

	    Contract ct = (Contract) i2.next();
	    /* Creating the actual ContractType binding object */
	    ContractType ctype = ct.createContractBinding();
	    contractList.add(ctype);
	    contracts.getContract().add(ctype);
	}
	// contracts.setContract(contractList);

	/* Creating the PlayerBindings binding JAXB object */
	SMC.PlayerBindings playerBindings = new SMC.PlayerBindings();

	/*
	 * Iterating through the composite object's role map to get the current
	 * roles
	 */
	Iterator i3 = c.getPlayerBindingMap().values().iterator();
	while (i3.hasNext()) {

	    PlayerBinding pb = (PlayerBinding) i3.next();
	    /* Creating the actual PlayerBindingType binding object */
	    PlayerBindingType pbType = pb.createPlayerBinding();
	    playerBindingList.add(pbType);
	    playerBindings.getPlayerBinding().add(pbType);

	}
	// playerBindings.setPlayerBindingList(playerBindingList);

	/* Creating the SMC object for JAXB transformation */
	SMC smcBinding = new SMC();
	smcBinding.setName(c.getName());
	smcBinding.setRoutingRuleFile(c.getRoutingRulesFileName());
	smcBinding.setCompositeRuleFile(c.getCompositeRulesFileName());
	smcBinding.setDataDir(c.getRulesDir());
	// Adding Roles
	smcBinding.setRoles(roles);

	// Adding Contracts
	smcBinding.setContracts(contracts);
	// Adding PlayerBindings
	smcBinding.setPlayerBindings(playerBindings);

	// Adding Process Definitions and Behavior Units - Note: Here we use the
	// up-to-date SMC binding. Process layer always update the binding.
	smcBinding.setProcessDefinitions(c.getSmcBinding()
		.getProcessDefinitions());
	smcBinding.setBehaviorTerms(c.getSmcBinding().getBehaviorTerms());
	// JAXB marshalling code
	JAXBContext context;
	FileWriter compositeDataFile;
	try {
	    String filePath = dirPath + "/" + c.getName() + ".xml";
	    compositeDataFile = new FileWriter(filePath);
	    context = JAXBContext.newInstance(SMC.class);
	    Marshaller marshaller = context.createMarshaller();
	    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
	    log.info("Saving the snapshot to  " + filePath);
	    marshaller.marshal(smcBinding, compositeDataFile);
	    compositeDataFile.close();

	    log.info("Marshalling Process Completed");

	} catch (JAXBException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	if (marshalDRL(c)) {
	    return true;
	} else {
	    log.fatal("Snapshot Generation Failed. Contact System Administrator.");
	    return false;
	}

    }

    /**
     * Sets the directory path for storing the XML representation of the
     * Composite object
     * 
     * 
     */
    private void generateDirPath() {

	/* Creating the time-stamp which represents the folder name */
	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat timestampFile = new SimpleDateFormat("yyyyMMddHHmmss");
	foldername = timestampFile.format(calendar.getTime());

	/* Checking if the directory path has been set */
	if ((null == dirPath) || dirPath.length() > 0) {
	    // Custom directory path
	    dirPath = "ROAD_Snapshots/" + foldername;
	} else {
	    // Default Directory path
	    // dirPath = "C:/dev/newProjects/ROADFactory_JAXB/data/snapshots/"+
	    // foldername;
	    try {
		dirPath = new File(".").getCanonicalPath() + "/data/snapshots/"
			+ foldername;

	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}

	/*
	 * Creating the directory structure if the directory structure does not
	 * exist
	 */
	File completePath = new File(dirPath);
	if (!completePath.exists()) {
	    log.info("Creating directory structure");
	    completePath.mkdirs();
	}

    }

    /**
     * Create the drool (drl) files from the composite definition and the
     * original drl files. The method receives the composite and determines the
     * relevant drl files required to construct the exact same composite. It
     * also accesses the rule change tracking list to find all the inserts and
     * removes done on the original rules and generates the updated drl files.
     * The original files are copied to a temporary location and modified
     * according to the rule change tracking list. After all the changes are
     * applied, the temporary files are copied to the SMC location.
     * 
     * @param c
     *            the composite object which has to be converter to an XML
     *            representation
     * @return boolean which indicates whether the marshaling was successful or
     *         not
     */
    private boolean marshalDRL(Composite c) {

	// boolean to indicate whether the marshaling was successful or not
	boolean isDRLMarshalled = true;

	// Temporary file location for applying the rule changes
	File tempFolder;

	// Getting all the current contracts from the composite to determine the
	// relevant drl files
	Iterator contractIterator = c.getContractMap().values().iterator();

	while (contractIterator.hasNext()) {

	    Contract tempContract = (Contract) contractIterator.next();

	    // Getting the file path from the contract. It would
	    // data/Rules/<filename>.drl
	    String droolFileName = tempContract.getContractRules()
		    .getRuleFile();

	    try {
		// Creating the temporary folder to store the files for
		// modification
		tempFolder = new File(new File(".").getCanonicalFile()
			+ "/data/temp");
		if (!tempFolder.exists()) {
		    tempFolder.mkdirs();
		}

		// Since the file-path from the contract is in the format
		// "data/Rules/<filename>.drl", tokenizing it to get the
		// filename
		StringTokenizer originalFilename_tokenizer = new StringTokenizer(
			droolFileName, "/");
		String dataFolder = originalFilename_tokenizer.nextToken();
		String rulesFolder = originalFilename_tokenizer.nextToken();
		String tempDestFilename = originalFilename_tokenizer
			.nextToken();

		String originalDrlFilePath = new File(".").getCanonicalPath()
			.toString() + "/" + droolFileName;

		// Copying the original file to the temporary folder
		copyFile(originalDrlFilePath, tempFolder + "/"
			+ tempDestFilename);

	    } catch (IOException e1) {
		isDRLMarshalled = false;
		e1.printStackTrace();
	    }

	}

	// Getting the rule change tracking list from the contract and iterating
	// through each rule change
	// List<String> tempRuleTracker = c.getRuleChangeTracker();
	List<RuleChangeTracker> tempRuleTracker = c.getRuleChangeTrackerList();

	for (RuleChangeTracker ruleInfo : tempRuleTracker) {

	    String action = ruleInfo.getAction();
	    String drlFile = ruleInfo.getDrlFile();
	    String ruleName = ruleInfo.getRuleInformation();

	    // Checking if the action is remove or insert
	    if (action.equalsIgnoreCase("remove")) {

		File folder;
		try {
		    // Locating the correct file from the temporary folder which
		    // has to be modified
		    folder = new File(new File(".").getCanonicalFile()
			    + "/data/temp");
		    File[] fileList = folder.listFiles();
		    for (int i = 0; i < fileList.length; i++) {
			if (drlFile.contains(fileList[i].getName())) {

			    // Copying the temporary file to the SMC location
			    copyFile(
				    new File(".").getCanonicalPath().toString()
					    + "/data/temp/"
					    + fileList[i].getName(), dirPath
					    + "/" + fileList[i].getName());

			    // Reading the from the temporary file and writing
			    // to the file in the SMC location
			    FileReader droolsFileReader = new FileReader(
				    new File(".").getCanonicalPath().toString()
					    + "/data/temp/"
					    + fileList[i].getName());
			    BufferedReader droolsFile = new BufferedReader(
				    droolsFileReader);

			    PrintWriter pw = new PrintWriter(dirPath + "/"
				    + fileList[i].getName());
			    String tempLine = "";

			    // Writing to the file in the Snapshot location but
			    // skipping over the rule which has to be deleted
			    boolean startWritingFlag = true;
			    while ((tempLine = droolsFile.readLine()) != null) {

				if (tempLine.contains(ruleName)) {

				    startWritingFlag = false;
				    String endString[] = tempLine.trim().split(
					    "\\s+");
				    if (endString[endString.length - 1]
					    .equalsIgnoreCase("end")) {
					startWritingFlag = true;
					// tempLine = droolsFile.readLine();
					continue;
				    }

				} else if (tempLine.contains("end")
					&& startWritingFlag == false) {
				    startWritingFlag = true;
				    continue;
				}

				if (startWritingFlag) {

				    pw.println(tempLine);

				}

			    }

			    // Closing the files
			    droolsFile.close();
			    pw.close();

			    // Copying the file in the SMC location to the
			    // temporary location so that if there is another
			    // rule removal change then the updated temporary
			    // file will be read from
			    copyFile(
				    dirPath + "/" + fileList[i].getName(),
				    new File(".").getCanonicalPath().toString()
					    + "/data/temp/"
					    + fileList[i].getName());

			    break;
			}

		    }
		} catch (FileNotFoundException e) {
		    isDRLMarshalled = false;
		    e.printStackTrace();
		} catch (IOException e1) {
		    isDRLMarshalled = false;
		    e1.printStackTrace();
		}

	    } else if (action.equalsIgnoreCase("insert")) {

		File folder;
		try {
		    // Locating the correct file from the temporary folder which
		    // has to be modified
		    folder = new File(new File(".").getCanonicalFile()
			    + "/data/temp");
		    File[] fileList = folder.listFiles();
		    for (int i = 0; i < fileList.length; i++) {
			if (drlFile.contains(fileList[i].getName())) {

			    // Opening the temporary file for writing
			    PrintWriter pw = new PrintWriter(new FileWriter(
				    new File(".").getCanonicalPath().toString()
					    + "/data/temp/"
					    + fileList[i].getName(), true));

			    // Appending the new rule to the temporary file
			    pw.println(ruleName);

			    // Closing the file
			    pw.close();

			    // Copying the modified temporary file to the SMC
			    // location
			    copyFile(
				    new File(".").getCanonicalPath().toString()
					    + "/data/temp/"
					    + fileList[i].getName(), dirPath
					    + "/" + fileList[i].getName());
			}
		    }

		} catch (FileNotFoundException e) {
		    isDRLMarshalled = false;
		    e.printStackTrace();
		} catch (IOException e1) {
		    isDRLMarshalled = false;
		    e1.printStackTrace();
		}

	    }
	}

	// Calling this method to copy the drl files which do not have any rule
	// changes
	transferDroolFiles(c);

	// Deleting the temporary folders
	try {
	    File tempDir = new File(new File(".").getCanonicalFile().toString()
		    + "/data/temp");
	    deleteTempDir(tempDir);
	} catch (IOException e) {
	    isDRLMarshalled = false;
	    e.printStackTrace();
	}

	log.info("Generation of drl files completed");
	return isDRLMarshalled;

    }

    /**
     * Copies the file at the specified source path to the given destination
     * path
     * 
     * @param sourceFile
     *            The path where the source file is located
     * @param destinationFile
     *            The path where the copied source file is to be stored
     * 
     */
    private void copyFile(String sourceFile, String destinationFile) {

	try {
	    File f1 = new File(sourceFile);
	    File f2 = new File(destinationFile);
	    InputStream in = new FileInputStream(f1);

	    OutputStream out = new FileOutputStream(f2);

	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
		out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();

	} catch (FileNotFoundException ex) {
	    System.out
		    .println(ex.getMessage() + " in the specified directory.");

	} catch (IOException e) {
	    log.debug(e.getMessage());
	}
    }

    /**
     * Copies all the drl files relevant to the composite contracts from the
     * temporary location to the SMC location. The drl files that are not
     * related to any rule changes would be indicated by the rule change
     * tracking list stored in the composite. If this list does not contain an
     * entry for the drl file, then it would not be related to a rule change.
     * Such files are simply copied from the temporary location to the SMC
     * location.
     * 
     * @param c
     *            the composite object which has to be converter to an XML
     *            representation
     */
    private void transferDroolFiles(Composite c) {

	Iterator i = c.getContractMap().values().iterator();

	while (i.hasNext()) {

	    Contract tempContract = (Contract) i.next();

	    String droolFileName = tempContract.getContractRules()
		    .getRuleFile();

	    try {

		File folder = new File(new File(".").getCanonicalFile()
			.toString() + "/data/temp");
		File[] fileList = folder.listFiles();
		for (int count = 0; count < fileList.length; count++) {
		    if (droolFileName.contains(fileList[count].getName())) {
			File checkFile = new File(dirPath + "/"
				+ fileList[count].getName());
			if (!checkFile.exists()) {
			    copyFile(new File(".").getCanonicalFile()
				    .toString()
				    + "/data/temp/"
				    + fileList[count].getName(), dirPath + "/"
				    + fileList[count].getName());
			}
		    }

		}
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }

	}
    }

    /**
     * Deletes the temporary folder after the snapshot is successfully saved to
     * the SMC location
     */
    private void deleteTempDir(File tempDir) {

	if (tempDir.exists() && tempDir.isDirectory()) {

	    File[] fileList = tempDir.listFiles();
	    for (int fileCount = 0; fileCount < fileList.length; fileCount++) {

		if (fileList[fileCount].isFile()) {

		    fileList[fileCount].delete();
		    // + fileList[fileCount].getName() + " file deleted");
		}
	    }

	    if (tempDir.delete()) {

		log.debug("Temp Directory: " + tempDir.toString() + " Deleted");
	    }

	}

    }

}
