package au.edu.swin.ict.road.composite.rules.drools;

import org.apache.log4j.Logger;
import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.road.composite.contract.Contract;

public class DroolsRulesFirer implements Runnable {
    private static Logger log = Logger.getLogger(DroolsRulesFirer.class
	    .getName());
    private StatefulKnowledgeSession session;
    private boolean terminate = false;

    public DroolsRulesFirer(StatefulKnowledgeSession session) {
	this.session = session;

    }

    @Override
    public void run() {
	log.info("fireUntilHalt ");

	if (terminate) {
	    if (session != null) {
		session.dispose();
	    }
	    return;
	}

	session.fireUntilHalt();
    }

    public boolean isTerminate() {
	return terminate;
    }

    public void setTerminate(boolean terminate) {
	this.terminate = terminate;
    }

}
