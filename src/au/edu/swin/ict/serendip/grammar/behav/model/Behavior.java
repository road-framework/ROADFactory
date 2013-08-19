package au.edu.swin.ict.serendip.grammar.behav.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Scripting specific Behavior model
 * 
 * @author Malinda
 * 
 */
public class Behavior {
    private String behaviorId = null;
    private String extendId = null;
    private List<Task> tasks = new ArrayList<Task>();

    public Behavior() {
	super();
    }

    public void addTask(Task t) {
	this.tasks.add(t);
    }

    public String getBehaviorId() {
	return behaviorId;
    }

    public void setBehaviorId(String behaviorId) {
	this.behaviorId = behaviorId;
    }

    public String getExtendId() {
	return extendId;
    }

    public void setExtendId(String extendId) {
	this.extendId = extendId;
    }

    public List<Task> getTasks() {
	return tasks;
    }

    public void setTasks(List<Task> tasks) {
	this.tasks = tasks;
    }

    public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append("Behavior " + this.behaviorId);
	if (null != this.extendId) {
	    buf.append(" extends " + this.extendId);
	}
	buf.append("{");
	for (int i = 0; i < this.tasks.size(); i++) {
	    buf.append("\n" + this.tasks.get(i).toString());
	}
	buf.append("\n};");
	return buf.toString();
    }
}
