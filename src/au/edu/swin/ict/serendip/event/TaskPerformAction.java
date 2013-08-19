package au.edu.swin.ict.serendip.event;

import au.edu.swin.ict.serendip.composition.Task;
import au.edu.swin.ict.serendip.core.SerendipEngine;

/**
 * The bean class to carry information from the engine to the Task performing
 * entity, i.e. Role in the composite.
 * 
 * @author Malinda
 * 
 */
public class TaskPerformAction {

    private Task task;

    /**
     * The constructor
     * 
     * @param taskId
     *            is the id of the task
     * @param processInstanceId
     *            is the id of the process instance. Should be used to correlate
     *            the messages with a process instance.
     */
    public TaskPerformAction(Task task) {
	super();
	this.task = task;
    }

    public Task getTask() {
	return task;
    }

    public void setTask(Task task) {
	this.task = task;
    }

}
