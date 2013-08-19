package au.edu.swin.ict.serendip.core.mgmt.scripting;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Block {

    private String name;
    private List commands = new ArrayList();

    public Block(String name) {
	this.name = name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getName() {
	return this.name;
    }

    public void addCommand(Command command) {
	this.commands.add(command);
    }

    public List getCommands() {
	return this.commands;
    }

    public String toString() {
	StringBuffer buffer = new StringBuffer(Command.quoteString(this.name));
	buffer.append(" {\n");

	Iterator iter = this.commands.iterator();
	while (iter.hasNext()) {
	    buffer.append("  ");
	    buffer.append(iter.next());
	    buffer.append("\n");
	}

	buffer.append("}\n\n");
	return buffer.toString();
    }
}
