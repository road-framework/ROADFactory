package au.edu.swin.ict.road.composite.rules;

import java.util.List;

import au.edu.swin.ict.road.composite.contract.Contract;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.exceptions.RulesException;

public interface IRoutingRules {

    public Contract executeRoutingRules(MessageWrapper messageWrapper,
	    List<Contract> contracts) throws RulesException;
}
