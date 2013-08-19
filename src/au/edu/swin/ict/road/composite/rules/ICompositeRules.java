package au.edu.swin.ict.road.composite.rules;

import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

public interface ICompositeRules {

    public void insertMessageRecievedAtSourceEvent(MessageWrapper msg,
	    String roleId);

    public void insertRoutingFailureEvent(MessageWrapper msg, String roleId);

    public void insertRoutingSuccessEvent(MessageWrapper msg, String roleId,
	    String contractId);

    public void insertMessageRecievedAtDestinationEvent(MessageWrapper msg,
	    String roleId);

    public void insertMessageRecievedAtContractEvent(MessageWrapper msg,
	    String contractId);

    public boolean insertRule(String rule);

    public boolean removeRule(String ruleName);
}
