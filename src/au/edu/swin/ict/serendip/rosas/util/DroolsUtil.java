package au.edu.swin.ict.serendip.rosas.util;

import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.rules.events.contract.MessageRecievedEvent;

public class DroolsUtil {
    public static boolean evaluate(MessageRecievedEvent msg) {
	MessageWrapper mw = msg.getMessageWrapper();
	Object msgContent = mw.getMessage();
	// Evaluate message contents
	return false;
    }
}
