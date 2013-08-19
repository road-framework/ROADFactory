package au.edu.swin.ict.road.composite.rules.drools;

import java.io.Reader;
import java.io.StringReader;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.Resource;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.road.composite.IInternalOrganiserView;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.ICompositeRules;
import au.edu.swin.ict.road.composite.rules.events.composite.MessageRecievedAtContractEvent;
import au.edu.swin.ict.road.composite.rules.events.composite.MessageRecievedAtDestinationEvent;
import au.edu.swin.ict.road.composite.rules.events.composite.MessageRecievedAtSourceEvent;
import au.edu.swin.ict.road.composite.rules.events.composite.RoutingFailureEvent;
import au.edu.swin.ict.road.composite.rules.events.composite.RoutingSuccessEvent;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

/**
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class DroolsCompositeRulesImpl implements ICompositeRules {

    protected String ruleFile;
    protected KnowledgeBuilder kBuilder;
    protected KnowledgeBase kb;
    protected StatefulKnowledgeSession session;
    protected DroolsRulesFirer firer;
    protected Thread firerThread;
    protected IInternalOrganiserView organiser;

    public DroolsCompositeRulesImpl(String ruleFile,
	    IInternalOrganiserView organiser) throws RulesException {
	this.ruleFile = ruleFile;
	this.organiser = organiser;
	kb = readKB();
	createAndRunNewSession();
    }

    @Override
    public void insertMessageRecievedAtSourceEvent(MessageWrapper msg,
	    String roleId) {
	MessageRecievedAtSourceEvent event = new MessageRecievedAtSourceEvent(
		msg, roleId);
	// session.insert(event);
    }

    @Override
    public void insertRoutingFailureEvent(MessageWrapper msg, String roleId) {
	RoutingFailureEvent event = new RoutingFailureEvent(msg, roleId);
	// session.insert(event);
    }

    @Override
    public void insertRoutingSuccessEvent(MessageWrapper msg, String roleId,
	    String contractId) {
	RoutingSuccessEvent event = new RoutingSuccessEvent(msg, roleId,
		contractId);
	// session.insert(event);
    }

    @Override
    public void insertMessageRecievedAtDestinationEvent(MessageWrapper msg,
	    String roleId) {
	MessageRecievedAtDestinationEvent event = new MessageRecievedAtDestinationEvent(
		msg, roleId);
	// session.insert(event);
    }

    @Override
    public void insertMessageRecievedAtContractEvent(MessageWrapper msg,
	    String contractId) {
	MessageRecievedAtContractEvent event = new MessageRecievedAtContractEvent(
		msg, contractId);
	// session.insert(event);
    }

    @Override
    public boolean insertRule(String rule) {
	Resource ruleResource = ResourceFactory
		.newReaderResource((Reader) new StringReader(rule));
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

    private KnowledgeBase readKB() throws RulesException {

	KnowledgeBaseConfiguration config = KnowledgeBaseFactory
		.newKnowledgeBaseConfiguration();
	config.setOption(EventProcessingOption.STREAM);

	KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);

	// Creating KnowledgeBuilder reference
	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		.newKnowledgeBuilder();
	kBuilder = kbuilder;

	// Loading all rules from a rules file
	Resource resource = ResourceFactory.newFileResource(ruleFile);
	// Resource resource = ResourceFactory.newClassPathResource( ruleFile,
	// DroolsRulesImpl.class);
	kbuilder.add(resource, ResourceType.DRL);

	// check for syntax errors
	if (kbuilder.hasErrors())
	    throw new RulesException("Error found in rule file: " + ruleFile);

	kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	return kbase;

    }

    private void createAndRunNewSession() {
	if (session != null) {
	    session.dispose();
	}

	session = kb.newStatefulKnowledgeSession();
	session.setGlobal("organiser", organiser);

	firer = new DroolsRulesFirer(session);
	firerThread = new Thread(firer);
	// Testing
	firerThread.setName("DroolsSession" + session.getId());
	firerThread.start();
    }
}
