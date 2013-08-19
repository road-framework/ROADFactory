package au.edu.swin.ict.serendip.grammar.behav.model;

import au.edu.swin.ict.serendip.util.StrUtil;

/**
 * Scripting specific Task model
 * 
 * @author Malinda
 * 
 */
public class Task {
    private String taskId = null;

    private String preEP = null;// Without quotes
    private String postEP = null;// Without quotes
    private String pp = null;
    private String roblig = null;

    public Task() {
	super();
    }

    public String getTaskId() {
	return taskId;
    }

    public void setTaskId(String taskId) {
	this.taskId = taskId;
    }

    public String getPreEP() {
	return preEP;
    }

    public void setPreEP(String preEP) {
	preEP = StrUtil.removeQuotes(preEP);// Remove double quotes
	this.preEP = preEP;
    }

    public String getPostEP() {
	return postEP;
    }

    public void setPostEP(String postEP) {
	postEP = StrUtil.removeQuotes(postEP);// Remove double quotes
	this.postEP = postEP;
    }

    public String getPp() {
	return pp;
    }

    public void setPp(String pp) {
	this.pp = pp;
    }

    public String getRoblig() {
	return roblig;
    }

    public void setRoblig(String roblig) {
	this.roblig = roblig;
    }

    public String toString() {
	return "\tTask " + this.taskId + "{ " + "\n\t\t pre \"" + this.preEP
		+ "\";"
		+ // Add double quotes
		"\n\t\t post \"" + this.postEP + "\";"
		+ // Add double quotes
		"\n\t\t pp " + this.pp + ";" + "\n\t\t role " + this.roblig
		+ ";" + "\n\t};";
    }
}
