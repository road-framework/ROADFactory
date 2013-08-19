package au.edu.swin.ict.serendip.message;

import au.edu.swin.ict.serendip.composition.Role;
import au.edu.swin.ict.road.xml.bindings.MessageType;

public class Message {
    String id = null;
    String pid = null;

    MessageType msgType = null;
    Object contents = null;
    Role from = null;
    Role to = null;

    public Object getContents() {
	return contents;
    }

    public void setContents(Object contents) {
	this.contents = contents;
    }

    public Message(String id) {
	super();
	this.id = id;

    }

    public Message(String id, String pid) {
	this(id);
	this.pid = pid;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getPid() {
	return pid;
    }

    public void setPid(String pid) {
	this.pid = pid;
    }

}
