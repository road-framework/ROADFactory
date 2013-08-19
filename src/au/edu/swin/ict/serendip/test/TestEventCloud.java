package au.edu.swin.ict.serendip.test;

import org.apache.log4j.Logger;

import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventCloud;
import au.edu.swin.ict.serendip.event.EventRecord;
import au.edu.swin.ict.serendip.event.SerendipEventListener;

public class TestEventCloud {
    private static Logger log = Logger
	    .getLogger(TestEventCloud.class.getName());

    public static void main(String[] args) throws SerendipException {
	EventCloud ec = new EventCloud(null);
	ec.subscribe(new TempSerRelSubscriber(
		"(([Event1]OR[Event2])AND[Breakdown])", "001"));
	// ec.subscribe(new TempSerRelSubscriber("[event1]AND[event2]", "002"));

	ec.addEvent(new EventRecord("Event1", "001"));
	ec.addEvent(new EventRecord("Breakdown", "001"));
	// ec.addEvent(new EventRecord("event3", "001"));
    }

}

class TempSerRelSubscriber extends SerendipEventListener {

    private static Logger log = Logger.getLogger(TempSerRelSubscriber.class
	    .getName());

    public TempSerRelSubscriber(String eventPattern, String pId) {
	super();
	// TODO Auto-generated constructor stub
	this.eventPattern = eventPattern;
	this.pId = pId;
    }

    @Override
    public void eventPatternMatched(String ep, String id) {
	// TODO Auto-generated method stub
	log.debug("EP " + ep + " matched for pid=" + id);
    }

    @Override
    public String getId() {
	// TODO Auto-generated method stub
	return "TempSerRelSubscriber";
    }

}
