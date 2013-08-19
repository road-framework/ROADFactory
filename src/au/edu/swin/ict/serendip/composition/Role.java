package au.edu.swin.ict.serendip.composition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

import org.apache.log4j.Logger;

import au.edu.swin.ict.serendip.core.SerendipEngine;
import au.edu.swin.ict.serendip.core.ProcessInstance;
import au.edu.swin.ict.serendip.core.SerendipException;
import au.edu.swin.ict.serendip.event.EventEngineSubscriber;
import au.edu.swin.ict.serendip.event.TaskPerformAction;
import au.edu.swin.ict.serendip.message.Message;
import au.edu.swin.ict.serendip.tool.gui.RoleView;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;

/**
 * @deprecated Use the ROAD Role class
 * @author Malinda
 * 
 */
public abstract class Role implements EventEngineSubscriber {

    public static boolean interrupted = true;
    static Logger logger = Logger.getLogger(Role.class);
    protected RoleView roleView = null;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    private String id = null;
    private Vector<Message> inQueue = new Vector<Message>();
    private Vector<Message> outQueue = new Vector<Message>();
    private SerendipEngine engine = null;

    public Role(String id) {
	super();
	this.id = id;

    }

    public Role(String id, SerendipEngine engine) {
	this(id);
	this.engine = engine;
    }

    public SerendipEngine getEngine() {
	return engine;
    }

    public void setEngine(SerendipEngine engine) {
	this.engine = engine;
    }

    public void dropToInQueue(Message m) {
	this.inQueue.add(m);
    }

    public void placeInOutQueue(Message m) {
	this.outQueue.add(m);
    }

    public abstract void perform(SerendipEngine engineInstance, Task task);

    /**
     * The ROAD implementation should use this method
     */
    // public void emitMessage(Task task) throws SerendipException {
    // // TODO: Include the destination of the message
    //
    // // Comment this line if a non-interrupted run is required
    // if (interrupted) {
    // this.waitForResponse(task);
    // }
    //
    // this.engine.dropMessage(new Message(task.getOutMessageId(), task
    // .getPId()));
    // }

    public Vector<Task> getAllMyTasksForProcessInstance(String pId) {

	Vector<Task> tasksOfRoleVec = new Vector<Task>();
	ProcessInstance pi = this.engine.getProcessInstance(pId);
	Vector<BehaviorTerm> btVec = pi.getBtVec();
	for (int i = 0; i < btVec.size(); i++) {
	    BehaviorTerm bt = btVec.get(i);
	    Vector<Task> tempVec = bt.getTasksOfRole(this.id);
	    tasksOfRoleVec.addAll(tempVec);
	}
	return tasksOfRoleVec;
    }

    private void waitForResponse(Task task) {
	// logger.debug("[Role="+this.id+"] Press any key when task "+task.getTaskDescr()+" is done >");
	//
	// try {
	// String str = new BufferedReader(new
	// InputStreamReader(System.in)).readLine();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
    }

    @Override
    public void performTask(TaskPerformAction tpa) {
	// TODO Auto-generated method stub
	this.perform(this.engine, tpa.getTask());

    }
}
