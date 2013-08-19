package au.edu.swin.ict.road.composite.rules;

import org.drools.runtime.StatefulKnowledgeSession;

import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

public interface IContractRules {

    public MessageProcessResult insertMessageRecievedAtContractEvent(
	    MessageWrapper msg) throws RulesException;

    public boolean addRule(String newRule);

    public boolean removeRule(String newRule);

    public String getRuleFile();

    /**
     * @return
     */
    public StatefulKnowledgeSession getSession();

    public void terminateSession();
}
