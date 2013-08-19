package au.edu.swin.ict.road.composite.rules.drools;

import java.util.List;

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

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.IMessageExaminer;
import au.edu.swin.ict.road.composite.message.MessageTypeExaminer;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.SOAPExaminer;
import au.edu.swin.ict.road.composite.rules.IRoutingRules;
import au.edu.swin.ict.road.composite.rules.events.contract.MessageRecievedEvent;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

/**
 * Class for routing the message if there are multiple same signature on the
 * composite. It will fire the routing rules that has been defined by developer
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class DroolsRoutingRulesImpl implements IRoutingRules {

    protected String ruleFile;
    protected KnowledgeBuilder kBuilder;
    protected KnowledgeBase kb;
    protected StatefulKnowledgeSession session;

    public DroolsRoutingRulesImpl(String ruleFile) throws RulesException {
	this.ruleFile = ruleFile;
	kb = readKB();
	if (session != null)
	    session.dispose();
	session = kb.newStatefulKnowledgeSession();
    }

    /**
     * Function to execute the routing rule based on the message type and
     * signature
     * 
     * @return the destination contract
     * @throws RulesException
     */
    @Override
    public Contract executeRoutingRules(MessageWrapper messageWrapper,
	    List<Contract> contracts) throws RulesException {
	// Creating a message received event and a type checker for the message
	MessageRecievedEvent mre = new MessageRecievedEvent(messageWrapper);
	IMessageExaminer typeCheck = new MessageTypeExaminer(messageWrapper,
		contracts);

	// Retrieve the specific type of message checker
	IMessageExaminer check = typeCheck.getMessageExaminer();

	// Inserting fact/event to the rules engine and deciding the destination
	// role
	session.insert(check);
	session.insert(mre);

	session.fireAllRules();

	// Setting the destination contract from role id
	check.setDestinationContract(check.getDestinationRole());
	return check.getDestinationContract();
    }

    /**
     * Function to execute the routing rule based on the message type and
     * signature
     * 
     * @return the destination contract
     * @throws RulesException
     */
    private KnowledgeBase readKB() throws RulesException {
	try {
	    KnowledgeBaseConfiguration config = KnowledgeBaseFactory
		    .newKnowledgeBaseConfiguration();
	    config.setOption(EventProcessingOption.STREAM);

	    KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase(config);

	    // Creating KnowledgeBuilder reference
	    KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		    .newKnowledgeBuilder();

	    // Loading all rules from a rules file
	    Resource resource = ResourceFactory.newFileResource(ruleFile);
	    kbuilder.add(resource, ResourceType.DRL);

	    // check for syntax errors
	    if (kbuilder.hasErrors())
		throw new RulesException("Error found in rule file: "
			+ ruleFile);

	    kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());

	    return kbase;
	} catch (Exception e) {
	    throw new RulesException("Cannot read rules from " + ruleFile);
	}
    }

}
