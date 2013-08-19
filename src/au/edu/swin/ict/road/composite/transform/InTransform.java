package au.edu.swin.ict.road.composite.transform;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.composite.Role;
import au.edu.swin.ict.road.composite.message.MessageWrapper;
import au.edu.swin.ict.road.composite.message.analyzer.MessageAnalyzer;
import au.edu.swin.ict.road.composite.message.analyzer.TransformLogic;
import au.edu.swin.ict.road.composite.message.analyzer.XSLTAnalyzer;
import au.edu.swin.ict.road.composite.message.containers.QueueListener;
import au.edu.swin.ict.road.xml.bindings.InMsgType;
import au.edu.swin.ict.road.xml.bindings.ResultMsgType;
import au.edu.swin.ict.road.xml.bindings.TaskType;
import au.edu.swin.ict.road.xml.bindings.TasksType;

/**
 * This class listens to any message inputs to the inQueue of a role by
 * implementing the QueueListener.
 * 
 * @author Saichander Kukunoor (saichanderreddy@gmail.com)
 * 
 */
public class InTransform implements QueueListener {

    private static Logger log = Logger.getLogger(InTransform.class.getName());
    private Role role;

    public InTransform(Role role) {
	this.role = role;
    }

    /**
     * This message is invoked whenever a message is placed in the inQueue of
     * the associated role
     * 
     * @see au.edu.swin.ict.road.composite.message.containers.QueueListener#messageReceived()
     */
    @Override
    public void messageReceived(MessageWrapper message) {
	// get the task id
	String mwTaskId = message.getTaskId();
	// get all the tasks in this role
	TasksType tasks = role.getRoleType().getTasks();
	// if the task id is null or there are no tasks, then place the message
	// directly in the routerQueue and return
	if (mwTaskId == null || tasks == null) {
	    role.putRouterQueueMessage(message);
	    return;
	}
	// iterate over the list of tasks
	for (TaskType task : tasks.getTask()) {
	    // get the In transform details of the task
	    InMsgType inMsgType = task.getIn();
	    // if there is no In transform for thid task, continue to the next
	    // task
	    if (inMsgType == null)
		continue;

	    // get the task id of the current task
	    String taskId = task.getId();
	    // if it is same as the task id of the message, create
	    // MessageAnalyzer
	    // and TransformLogic and invoke the disjunct of the analyzer
	    if (taskId.equalsIgnoreCase(mwTaskId)) {
		MessageAnalyzer analyzer;
		try {
		    // get the MessageAnalyzer declared in this task
		    String msgAnalyzerClass = task.getMsgAnalyser();
		    if (msgAnalyzerClass != null)
			analyzer = (MessageAnalyzer) Class.forName(
				msgAnalyzerClass).newInstance();
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} finally {
		    // when MessageAnalyzer is not declared the default
		    // MessageAnalyzer is used
		    log.info("Default MessageAnalyzer is used");
		    analyzer = new XSLTAnalyzer();
		}
		// create TransformLogic object and set all the details required
		// for transformation
		TransformLogic tLogic = new TransformLogic();
		tLogic.setTaskId(task.getId());
		// get result messages list from the task In
		List<ResultMsgType> resultMsgs = task.getResultMsgs()
			.getResultMsg();
		Iterator<ResultMsgType> iter = resultMsgs.iterator();
		// iterate over the result messages and add each xslt to the
		// tLogic
		while (iter.hasNext()) {
		    ResultMsgType resultMsg = iter.next();

		    tLogic.addXsltFilePath(resultMsg.getTransformation());

		}
		tLogic.setRole(role);
		// invoke the disjunct method in the analyzer
		List<MessageWrapper> disjunctMsgs;
		try {
		    role.getComposite()
			    .getBenchUtil()
			    .addBenchRecord("INTRANS BEGIN",
				    message.getMessageId());
		    disjunctMsgs = analyzer.disjunct(message, tLogic);
		    role.putAllRouterQueueMessages(disjunctMsgs);
		    role.getComposite()
			    .getBenchUtil()
			    .addBenchRecord("INTRANS END",
				    message.getMessageId());
		} catch (Exception e) {
		    // TODO Handle exception
		    e.printStackTrace();
		}
		// we will create an observer for all the messages of the same
		// disjunct operation
		// TODO: Check event observer
		// try {
		// EventObserver eventObserver = new
		// EventObserver(this.role.getComposite(),
		// message.getCorrelationId(), task.getId(), disjunctMsgs);
		// } catch (SerendipException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// place all the result messages in the router queue

		return;
	    }
	}
	// when there are tasks in the role but none of the In tasks
	// match the operation name in the message, the message is
	// placed directly in the router queue
	role.putRouterQueueMessage(message);
    }

}
