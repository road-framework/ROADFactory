package au.edu.swin.ict.road.testing;

import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.MessageProcessResult;
import au.edu.swin.ict.road.composite.rules.drools.DroolsContractRulesImpl;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;
import au.edu.swin.ict.serendip.event.EventRecord;

public class DroolsTest {
    private static Logger log = Logger.getLogger(DroolsTest.class.getName());

    public static void main(String[] args) {

	test2();
	System.exit(0);
    }

    public static void test2() {
	DroolsContractRulesImpl dcri;
	try {
	    dcri = new DroolsContractRulesImpl(
		    "E://ROAD/workspace/SerendipMerge/sample/Scenario1/data/rules/co-tc.drl");

	    MessageWrapper mw = new MessageWrapper();
	    mw.setCorrelationId("p002");
	    mw.setMessage("Some  Message Object");
	    mw.setOperationName("orderTow");
	    MessageProcessResult mpr;

	    mpr = dcri.insertMessageRecievedAtContractEvent(mw);

	    log.debug("Done");
	} catch (RulesException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

    public static void test1() {
	DroolsContractRulesImpl dcri;
	try {
	    dcri = new DroolsContractRulesImpl(
		    "E://ROAD/workspace/SerendipMerge/sample/Scenario1/data/rules/co-tc.drl");

	    MessageWrapper mw = new MessageWrapper();
	    mw.setCorrelationId("p002");
	    mw.setMessage("Some  Message Object");
	    mw.setOperationName("orderTow");
	    MessageProcessResult mpr;

	    mpr = dcri.insertMessageRecievedAtContractEvent(mw);
	    List<EventRecord> events = mpr.getAllInterprettedEvents();

	    for (EventRecord e : events) {
		log.debug("Event found " + e.getEventId());
	    }
	    log.debug("Done");
	} catch (RulesException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}
    }

}
