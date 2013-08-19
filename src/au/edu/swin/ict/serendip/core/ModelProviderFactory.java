package au.edu.swin.ict.serendip.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionsType;
import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.view.BehaviorView;
import au.edu.swin.ict.serendip.composition.view.ProcessView;
import au.edu.swin.ict.serendip.drools.RuleHandler;
import au.edu.swin.ict.serendip.epc.EPMLBehavior;
import au.edu.swin.ict.serendip.parser.XMLCompositionParser;
import au.edu.swin.ict.serendip.util.CompositionUtil;
import au.edu.swin.ict.serendip.verficiation.PNVerification;
import au.edu.swin.ict.serendip.verficiation.SerendipVerification;
import au.edu.swin.ict.serendip.verficiation.SerendipVerificationException;
import au.edu.swin.ict.serendip.verficiation.VerificationResult;

/**
 * Hides the complexity of model loading from the engine.
 * 
 * @author Malinda Kapuruge
 * 
 */
public class ModelProviderFactory {

    static Logger logger = Logger.getLogger(ModelProviderFactory.class);
    private XMLCompositionParser parser = null;
    private SerendipEngine engine = null;
    private String dirName = null;
    private String compositioName = null;
    /* Important: add only those who are working properly */
    private SerendipVerification[] verifs = { new PNVerification() };

    // private File descriptorFile = null;

    /**
     * Constructor for model provider factory
     * 
     * @param engine
     */
    public ModelProviderFactory(SerendipEngine engine) {
	logger.info("Instantiating the MPF");
	this.engine = engine;

    }

    /**
     * Iterate thru the available process defnitions and see, if the condition
     * of start is matching to the one given. Things to ponder 1. A message id
     * shouldn't be used as a CoS. Because, the same message might require to
     * create two different process instances. e.g., A request from a motorist
     * might create a process instacne, based on its subscription. 2. Probably a
     * rule is best to decide which process instance to start. 2.
     * 
     * @param cos
     * @return
     */
    public String getMatchingDefForCoS(String cos) {
	ProcessDefinitionsType pDefType = this.engine.getComposition()
		.getComposite().getSmcBinding().getProcessDefinitions();
	if (null == pDefType) {
	    return null;
	}
	// If there are process definitions, iterate thru them to find if there
	// is a matching request
	List<ProcessDefinitionType> processDefinitions = pDefType
		.getProcessDefinition();
	for (ProcessDefinitionType pd : processDefinitions) {
	    if (pd.getCoS().equals(cos)) {
		return pd.getId();
	    }
	}
	return null;
    }

    /**
     * Validates a composition based on SerendipVerification objects
     * 
     * @return
     * @throws SerendipVerificationException
     */
    public boolean verifyModels() throws SerendipVerificationException {

	for (int i = 0; i < this.verifs.length; i++) {
	    VerificationResult result = verifs[i].verify(this);
	    if (!result.isValid()) {
		return false;
	    }
	}
	return true;
    }

    // Verify a particular process definition
    public boolean verifyPD(String defId) throws SerendipVerificationException {

	for (int i = 0; i < this.verifs.length; i++) {
	    VerificationResult result = verifs[i].verifyProcessDef(this, defId);
	    if (!result.isValid()) {
		return false;
	    }
	}
	return true;
    }

    public boolean verifyProcessInstance(ProcessInstance pi)
	    throws SerendipVerificationException {
	for (int i = 0; i < this.verifs.length; i++) {
	    VerificationResult result = verifs[i].verifyProcessInstance(pi,
		    this);
	    if (!result.isValid()) {
		throw new SerendipVerificationException(
			"Failed to verify instance " + pi.getId()
				+ "\n Reason: " + result.getMessage());
	    }
	}
	return true;
    }

    // Return a new process instance for the given def id
    public ProcessInstance getNewProcessInstance(String defId)
	    throws SerendipException {
	ProcessInstance pi = new ProcessInstance(this.engine, defId);

	// Create behavior terms
	List<String> btIdList = this.engine.getComposition()
		.getAllBehaviorTermIdsForPD(defId);

	// We need to make things more sophisticated by providing unaltered
	// behavior terms to make the app memory efficient.
	// But here we go with fresh copies unitl all the issues are identified.

	for (String btId : btIdList) {
	    // Get the type
	    BehaviorTermType btType = this.engine.getComposition()
		    .getBehaviorTermTypeById(btId);
	    if (null == btType) {
		logger.error("Behavior term " + btId + "not found for process "
			+ defId);
		throw new SerendipException("Behavior term " + btId
			+ "not found for process " + defId);
	    }

	    // Create a new behavior term

	    BehaviorTerm bt = new BehaviorTerm(btType, pi, this.engine);
	    // Subscribe all the tasks in BT to the event cloud
	    bt.subscribeTasksTo(this.engine.getEventCloud());
	    // Add it to the process instance
	    pi.addBehaviorTerm(bt);
	}

	return pi;

    }

    public BehaviorView getBehaviorView(String btId) throws SerendipException {

	// Find the behavior term and create a behavior view

	BehaviorTermType btType = this.engine.getComposition()
		.getBehaviorTermTypeById(btId);
	BehaviorTerm bt = new BehaviorTerm(btType, this.engine.getComposition()
		.getComposite());

	BehaviorView bv = new BehaviorView(bt.getId(), bt);
	return bv;
    }

    /**
     * Generates the view of an existing process instance. If the pid=null then
     * create a fresh instance from the pDefId
     * 
     * @param pDefId
     * @param pid
     *            (Optional if u need a specific instance
     * @return
     * @throws SerendipException
     */
    public ProcessView getProcessView(String pDefId, String pid)
	    throws SerendipException {
	ProcessView pv = null;
	ProcessInstance pi = null;
	if (null == pid) {
	    // A short cut way of doing this. NOTE: Do not call initialize
	    pi = this.getNewProcessInstance(pDefId);
	} else {
	    pi = this.engine.getProcessInstance(pid);
	}
	pv = pi.getCurrentProcessView();

	return pv;
    }

    public String getFileLoadingDirectory() {
	// return this.dirName + "/" + this.compositioName;
	return System.getProperty("java.io.tmpdir");
    }

    public String getTempFileDirectory() {

	String tempDirPath = getFileLoadingDirectory() + "/temp/";

	// If does not exist create one
	File tempDir = new File(tempDirPath);
	if (!tempDir.exists()) {
	    if (!tempDir.mkdir()) {
		System.err.println("Cannot create temp directory");
	    }
	}
	return tempDirPath;
    }

    public static String getProperty(String key) {
	Properties props = new Properties();
	try {
	    props.load(new FileInputStream("serendip.properties"));
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    // e.printStackTrace();
	    logger.error("serendip.properties file is missing. "
		    + "It should be in the same directory as the executable. "
		    + "e.g., if you are using TOMCAT+AXIS2 the file should be in TOMCAT_HOME/bin.");
	}
	return props.getProperty(key);
    }

    public SerendipEngine getEngine() {
	return engine;
    }

    public String getCompositioName() {
	return this.engine.getCompositionName();
    }

    /* These methods are for tools-------------------------- */
    public void createBehaviorDirectory() {
	String strDirectoy = this.dirName + this.compositioName;

	File theDir = new File(strDirectoy);

	if (!theDir.exists()) {
	    boolean success = theDir.mkdir();
	    logger.info("Created EPML directory for " + this.compositioName);
	    if (!success) {
		logger.error("Cannot create EPML directory for "
			+ this.compositioName);
	    }
	} else {
	    logger.debug("Directory exist" + strDirectoy);
	}
    }

    public boolean createAllBehaviorFiles() {
	String epmllocation = this.dirName + "/" + this.compositioName;
	File directory = new File(this.dirName);
	if (!directory.exists()) {
	    boolean success = directory.mkdir();
	    if (!success) {
		logger.info("Cannot create directory");
	    }
	}

	String[] btIds = CompositionUtil.getAllBTIDOfComposition(this.engine
		.getComposition());

	try {
	    EPMLBehavior.createEmptyBehaviors(epmllocation, btIds);
	    RuleHandler.createEmptyRules(epmllocation, btIds);
	} catch (IOException e) {

	    e.printStackTrace();
	    return false;
	}
	return true;
    }

    public void updateAndSaveDescriptorFile() throws IOException {
	// TODO
	// this.compType.save(this.descriptorFile);
    }

    public void saveDescriptorFileAs(File file) throws IOException {
	// TODO
	// this.compType.save(file);
    }

    // PI Adaptation methods////////////////////////////////////////////////

    // PD Adaptation methods////////////////////////////////////////////////

    // public ProcessDefinitionType addNewProcessDefinition(String pDefId) {
    // ProcessDefinitionType pdType = new ProcessDefinitionType();
    // pdType.setId(pDefId);
    // this.engine.getComposition().getComposite().getSmcBinding()
    // .getProcessDefinitions().getProcessDefinition().add(pdType);
    // return pdType;
    // }
    //
    // public void addNewConstraint(ProcessDefinitionType pDef,
    // String constraintExpression, String constraintId) {
    // ConstraintType constType = new ConstraintType();
    // constType.setExpression(constraintExpression);
    // constType.setId(constraintId);
    // pDef.getConstraints().getConstraint().add(constType);
    // }
    //
    // public String addNewBehaviorTermToProcessDefinition(
    // ProcessDefinitionType pDef, String btId) {
    // if (pDef.getBehaviorTermRefs().getBehavirTermId().add(btId)) {
    // return btId;
    // } else {
    // return null;
    // }
    // }

}
