package au.edu.swin.ict.serendip.core;

import java.util.Iterator;
import java.util.Vector;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseConfiguration;
import org.drools.KnowledgeBaseFactory;

import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.conf.EventProcessingOption;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.KnowledgeSessionConfiguration;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.conf.ClockTypeOption;
import org.drools.runtime.rule.WorkingMemoryEntryPoint;

import au.edu.swin.ict.serendip.event.EventQuery;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;

/**
 * 
 * @author Malinda Kapuruge
 * @deprecated
 */
public class EventKnowledgeBase {

    private KnowledgeBase kbase = null;
    private StatefulKnowledgeSession ksession = null;
    private WorkingMemoryEntryPoint globalStream = null;

    public EventKnowledgeBase(String compositionId, String[] ruleFileNames)
	    throws Exception {
	KnowledgeSessionConfiguration kbSessionConfig = KnowledgeBaseFactory
		.newKnowledgeSessionConfiguration();
	kbase = readKnowledgeBase(compositionId, ruleFileNames);
	ksession = kbase.newStatefulKnowledgeSession();
	// KnowledgeRuntimeLogger logger =
	// KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
	KnowledgeBaseConfiguration config = KnowledgeBaseFactory
		.newKnowledgeBaseConfiguration();
	config.setOption(EventProcessingOption.STREAM);// Important to have the
						       // STREAM mode as we
						       // need maintain concept
						       // "NOW"

	kbSessionConfig.setOption(ClockTypeOption.get("realtime"));
	globalStream = ksession.getWorkingMemoryEntryPoint("serendib_global");

    }

    public void addEvent(EventRecord e) {
	ksession.insert(e);
	ksession.fireAllRules();
    }

    public void addEventPattern(EventQuery eq) {
	// Insert to KB as a fact. Or is this a rule that get fired as events
	// are inserted
	ksession.insert(eq);
    }

    public KnowledgeBase getKbase() {
	return kbase;
    }

    private KnowledgeBase readKnowledgeBase(String compositionId,
	    String[] ruleFileNames) throws Exception {
	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		.newKnowledgeBuilder();
	// Add all behavior terms to the memory
	for (int i = 0; i < ruleFileNames.length; i++) {

	    kbuilder.add(
		    ResourceFactory.newFileResource("sample/rules/"
			    + compositionId + "/" + ruleFileNames[i] + ".drl"),
		    ResourceType.DRL);
	}

	KnowledgeBuilderErrors errors = kbuilder.getErrors();
	if (errors.size() > 0) {
	    Iterator<KnowledgeBuilderError> iter2 = errors.iterator();
	    while (iter2.hasNext()) {
		System.err.println(iter2.next());
	    }
	    throw new IllegalArgumentException("Could not parse knowledge.");
	}

	KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	return kbase;
    }

    // @deprecated TODO Should not pass BTs
    private KnowledgeBase readKnowledgeBase(BehaviorTermType[] bTerms)
	    throws Exception {
	KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
		.newKnowledgeBuilder();
	// Add all behavior terms to the memory
	for (int i = 0; i < bTerms.length; i++) {
	    BehaviorTermType bt = bTerms[i];
	    kbuilder.add(
		    ResourceFactory.newFileResource("sample/rules/"
			    + bt.getId() + ".drl"), ResourceType.DRL);
	}

	KnowledgeBuilderErrors errors = kbuilder.getErrors();
	if (errors.size() > 0) {
	    Iterator<KnowledgeBuilderError> iter2 = errors.iterator();
	    while (iter2.hasNext()) {
		System.err.println(iter2.next());
	    }
	    throw new IllegalArgumentException("Could not parse knowledge.");
	}

	KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
	kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
	return kbase;
    }
}
