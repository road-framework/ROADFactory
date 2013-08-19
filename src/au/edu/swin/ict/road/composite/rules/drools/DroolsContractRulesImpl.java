package au.edu.swin.ict.road.composite.rules.drools;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.logging.Logger;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.PackageBuilder;
import org.drools.conf.EventProcessingOption;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.rule.Package;
import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.road.composite.MessageDeliverer;
import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.IMessageExaminer;
import au.edu.swin.ict.road.composite.message.MessageTypeExaminer;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.IContractRules;
import au.edu.swin.ict.road.composite.rules.MessageProcessResult;
import au.edu.swin.ict.road.composite.rules.events.contract.MessageRecievedEvent;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

public class DroolsContractRulesImpl implements IContractRules {
    private static Logger log = Logger.getLogger(DroolsContractRulesImpl.class
	    .getName());

    protected String ruleFile;
    protected KnowledgeBuilder kBuilder;
    protected KnowledgeBase kb;
    protected StatefulKnowledgeSession session;
    protected DroolsRulesFirer firer;
    protected Contract contract;
    protected Thread firerThread;
    private static int firerThreadCount = 0;

    public DroolsContractRulesImpl(String ruleFile, Contract contract)
	    throws RulesException {
	this.ruleFile = ruleFile;
	kb = readKB();
	createAndRunNewSession();
	this.contract = contract;

    }

    public DroolsContractRulesImpl(String ruleFile) throws RulesException {
	this.ruleFile = ruleFile;
	kb = readKB();
	createAndRunNewSession();
	contract = null;
    }

    private boolean fileExist(String fileName) {
	boolean exists = (new File(fileName)).exists();
	// TODO: Possibly look for other palces?
	return exists;
    }

    public KnowledgeBase readKB() throws RulesException {
	log.info("Reading KB from " + this.ruleFile);
	KnowledgeBaseConfiguration config = KnowledgeBaseFactory
		.newKnowledgeBaseConfiguration();
	config.setOption(EventProcessingOption.STREAM);

	KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);

	// Creating KnowledgeBuilder reference
	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		.newKnowledgeBuilder();
	kBuilder = kbuilder;

	// Loading all rules from a rules file
	// if(fileExist(ruleFile)){//ADDed
	Resource resource = ResourceFactory.newFileResource(ruleFile);
	// Resource resource = ResourceFactory.newClassPathResource( ruleFile,
	// DroolsRulesImpl.class);
	kbuilder.add(resource, ResourceType.DRL);
	// }else{
	// log.info("ERROR: File does no exist" + ruleFile);
	// }
	// check for syntax errors
	if (kbuilder.hasErrors()) {
	    throw new RulesException("Error found in rule file: " + ruleFile
		    + " Errors found: " + kbuilder.getErrors().toString());
	}
	kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	return kbase;

    }

    private void createAndRunNewSession() {

	if (session != null) {
	    session.dispose();
	}

	session = kb.newStatefulKnowledgeSession();
	if (contract != null) {
	    session.setGlobal("contract", contract);
	}
	// This is where we start firing rules (or lettig it fire???) May be not
	// a good way to do.:-(
	// e.g., how to know the end of firing rules in a session?
	// is this reason for hack in insertMessageRecievedAtContractEvent??
	// But live with the current implementation. -Malinda
	firer = new DroolsRulesFirer(session);
	firerThread = new Thread(firer);
	firerThreadCount++;
	log.info("Thread " + firerThreadCount
		+ " started for a new stateful knowledge session for "
		+ this.ruleFile);
	firerThread.start();
    }

    @Override
    public MessageProcessResult insertMessageRecievedAtContractEvent(
	    MessageWrapper messageWrapper) throws RulesException {

	MessageRecievedEvent mre = new MessageRecievedEvent(messageWrapper);
	IMessageExaminer typeCheck = new MessageTypeExaminer(messageWrapper,
		null);

	// Retrieve the specific type of message checker
	IMessageExaminer check = typeCheck.getMessageExaminer();

	// Inserting fact/event to the rules engine
	if (null != contract) {
	    contract.injectFact();
	}

	session.insert(check);
	session.insert(mre);
	// major hack!!!!!!!!!, needs work (Q. why we do this???? To sync with
	// rule firing in KB? -Malinda)
	// Note: I think this way of using i.e. letting drools to fire rules can
	// cause some major performance issues. - Malinda
	// Note: Also the ROADfactory code cannot continue until the rules has
	// finished firing. Hence I assume the sleep() below. -Malinda
	try {
	    Thread.sleep(30);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// Here we create a new object that stores the message processing
	// results
	MessageProcessResult mpr = new MessageProcessResult();
	mpr.setBlocked(mre.isBlocked());
	mpr.addInterpretedEvents(mre.getAllTriggeredEvents());
	mpr.setCorrelationId(messageWrapper.getCorrelationId());
	return mpr;
    }

    public String getRuleFile() {
	return ruleFile;
    }

    public void setRuleFile(String ruleFile) {
	this.ruleFile = ruleFile;
    }

    @Override
    public boolean addRule(String newRule) {
	Resource ruleResource = ResourceFactory
		.newReaderResource((Reader) new StringReader(newRule));
	kBuilder.add(ruleResource, ResourceType.DRL);
	if (kBuilder.hasErrors())
	    return false;
	this.kb.addKnowledgePackages(kBuilder.getKnowledgePackages());
	return true;
    }

    @Override
    public boolean removeRule(String ruleName) {
	try {
	    kb.removeRule("defaultpkg", ruleName);
	    return true;
	} catch (Exception e) {
	    return false;
	}
    }

    private RuleBase readRules() throws RulesException {
	try {
	    Reader source = new InputStreamReader(new FileInputStream(
		    this.ruleFile));
	    PackageBuilder builder = new PackageBuilder();
	    builder.addPackageFromDrl(source);

	    Package pkg = builder.getPackage();

	    // add the package to a rulebase (deploy the rule package).
	    RuleBase ruleB = RuleBaseFactory.newRuleBase();
	    ruleB.addPackage(pkg);
	    return ruleB;
	} catch (Exception e) {
	    throw new RulesException("Cannot read rules from " + this.ruleFile);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see au.edu.swin.ict.road.composite.rules.IContractRules#getSession()
     */
    @Override
    public StatefulKnowledgeSession getSession() {
	// TODO Auto-generated method stub
	return this.session;
    }

    @Override
    public void terminateSession() {
	// TODO Auto-generated method stub
	if (contract != null) {
	    log.info("Disposing contractual rules session aka Knowledge Base for Contract "
		    + this.contract.getId());
	}

	if (session != null) {
	    session.dispose();
	}
	this.firer.setTerminate(true);
    }

}
