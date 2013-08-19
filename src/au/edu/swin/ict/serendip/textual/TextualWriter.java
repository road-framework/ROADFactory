package au.edu.swin.ict.serendip.textual;

import java.util.Vector;

import au.edu.swin.ict.serendip.composition.BehaviorTerm;
import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.grammar.behav.model.Behavior;

public class TextualWriter {

    public static String processToText(ProcessInstance pi) {
	if (null == pi) {
	    return "Process is not available";
	}
	String s = "";
	Vector<BehaviorTerm> btVec = pi.getBtVec();
	for (int i = 0; i < btVec.size(); i++) {
	    if (i != 0) {
		s += "\n";
	    }
	    s += btToText(btVec.get(i));
	}

	return s;
    }

    /**
     * {[TowReqd]:ReportMsg } -> CO.SendTowReq(P=2)-> {[TowReqSent AND
     * PickupLocationKnown]:TowRequest}, {[PickupLocKnown AND DestinationKnown]:
     * TowReqMsg}-> TC.Tow(P=4)->{[CarTowed]:TowCompleteMsg}, {[CarTowed AND
     * TowAcked]: TowedMsg }-> CO.PayTow(P=2)->{[TCPaid]:TCPayMsg};
     * 
     * @param bt
     */
    public static String btToText(BehaviorTerm bt) {
	Behavior behav = new Behavior();

	behav.setBehaviorId(bt.getId());
	behav.setExtendId(bt.getExtendsFrom());
	for (Task t : bt.getAllTasks()) {
	    au.edu.swin.ict.serendip.grammar.behav.model.Task task = new au.edu.swin.ict.serendip.grammar.behav.model.Task();
	    task.setTaskId(t.getId());
	    task.setPreEP(t.getEventPattern());
	    task.setPostEP(t.getPostEventPattern());
	    task.setPp(t.getProperty().getValue());
	    task.setRoblig(t.getObligatedRoleId());
	    behav.addTask(task);
	}

	return behav.toString();
	//

	// String s = bt.getId() + ":\n";
	// Vector<Task> taskVec = bt.getTasksVec();
	// for (int i = 0; i < taskVec.size(); i++) {
	//
	// Task t = taskVec.get(i);
	// String preEP = t.getEventPattern();
	// String Min = t.getInputMsgs();
	// String role = t.getObligatedRoleId();
	// String taskid = t.getId();
	// String pVal = t.getProperty().getValue();
	// String postEP = t.getPostEventPattern();
	// String mOut = t.getOutMessageId();
	//
	// // Some modifications
	// if (null != preEP) {
	// preEP = preEP.replace('[', ' ');
	// preEP = preEP.replace(']', ' ');
	// }
	// if (null != postEP) {
	// postEP = postEP.replace('[', ' ');
	// postEP = postEP.replace(']', ' ');
	// }
	// if ("" == Min) {
	// Min = "0";
	// }
	//
	// // {( preEP ): Min } > role . taskid (t= 2 )> {( postEP ): mOut }
	// //s += "{(" + preEP + "):" + Min + "}>" + role + "." + taskid +
	// "(t="+ pVal + ")>{(" + postEP + "):" + mOut + "}";
	//
	// //{( preEP ) } > role . taskid (t= 2 )> {( postEP ) }
	// s += "{" + preEP + "}>" + role + "." + taskid + "(t="+ pVal + ")>{" +
	// postEP + "}";
	// if (i != taskVec.size() - 1) {// skip the last comma
	// s += ",\n";
	// }
	//
	// }
	// return s;
    }

    public static String getSyntax() {
	return " {(preEP):Min} >  role.taskid(t=2) > {(postEP):mOut}";

    }

}
