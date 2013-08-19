package au.edu.swin.ict.serendip.grammar.behav;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.antlr.runtime.tree.TreeAdaptor;
import org.apache.log4j.Logger;

import antlr.Token;
import au.edu.swin.ict.serendip.grammar.behav.behavParser.script_return;
import au.edu.swin.ict.serendip.grammar.behav.model.Behavior;
import au.edu.swin.ict.serendip.grammar.behav.model.Script;
import au.edu.swin.ict.serendip.grammar.behav.model.Task;

public class ScriptConstructor {
    private static Logger log = Logger.getLogger(ScriptConstructor.class
	    .getName());

    public static void main(String[] args) throws IOException, Exception {
	String contents = readFileAsString("./src/au/edu/swin/ict/serendip/grammar/behav/syntax.txt");
	Script script = constructScript(contents);
	log.debug(script.toString());
    }

    public static Script constructScript(String contents) throws Exception {
	try {
	    CharStream input = new ANTLRStringStream(contents);
	    behavLexer lexer = new behavLexer(input);
	    CommonTokenStream tokens = new CommonTokenStream(lexer);
	    behavParser parser = new behavParser(tokens);

	    // //Inner TreeAdaptor class
	    final TreeAdaptor adaptor = new CommonTreeAdaptor() {
		public Object create(Token payload) {
		    return new CommonTree();
		}
	    };

	    parser.setTreeAdaptor(adaptor);
	    script_return ret = parser.script();

	    if (null == ret) {
		throw new Exception("Cannot parse the script.");
	    }
	    CommonTree tree = (CommonTree) ret.getTree();
	    if (null == tree) {
		throw new Exception(
			"Cannot construct the abstract syntax tree.");
	    }
	    // showTree(tree);
	    // printTree(tree, 2);
	    // /
	    return getScript(tree);
	} catch (Throwable t) {
	    t.printStackTrace();
	    throw new Exception(t.getMessage());

	}

    }

    public static void printTree(CommonTree t, int indent) {
	if (t != null) {
	    StringBuffer sb = new StringBuffer(indent);
	    for (int i = 0; i < indent; i++)
		sb = sb.append("  -  ");
	    for (int i = 0; i < t.getChildCount(); i++) {
		log.debug(sb.toString() + t.getChild(i).toString());
		printTree((CommonTree) t.getChild(i), indent + 1);
	    }
	}
    }

    public static Script getScript(CommonTree t) {
	Script script = new Script();
	if (null != t) {
	    for (int i = 0; i < t.getChildCount(); i++) {
		CommonTree child = (CommonTree) t.getChild(i);
		switch (child.getType()) {
		case behavLexer.BEHAVIOR:
		    Behavior b = getBehavior(child);
		    script.addBehaviour(b);
		    break;
		default:
		    // We shouldn't reach here
		    System.err.println("ERROR " + child.getText());
		}

	    }
	}

	return script;
    }

    public static Behavior getBehavior(CommonTree t) {
	if (null == t) {
	    return null;
	}
	// Create behavior
	Behavior behav = new Behavior();

	for (int i = 0; i < t.getChildCount(); i++) {
	    CommonTree child = (CommonTree) t.getChild(i);

	    switch (child.getType()) {
	    case behavLexer.BTNAME:
		String btName = child.getFirstChildWithType(behavLexer.WORD)
			.getText();
		behav.setBehaviorId(btName);
		break;
	    case behavLexer.EXTENDS:
		String extendId = child.getFirstChildWithType(behavLexer.WORD)
			.getText();
		behav.setExtendId(extendId);
		break;
	    case behavLexer.TASK:
		Task task = getTask(child);
		behav.addTask(task);
		break;
	    }// switch
	}// for

	return behav;
    }

    public static Task getTask(CommonTree t) {

	if (null == t) {
	    return null;
	}
	// Create Task
	Task task = new Task();

	for (int i = 0; i < t.getChildCount(); i++) {
	    CommonTree child = (CommonTree) t.getChild(i);
	    switch (child.getType()) {
	    case behavLexer.TASKNAME:
		String taskId = child.getFirstChildWithType(behavLexer.WORD)
			.getText();
		task.setTaskId(taskId);
		break;
	    case behavLexer.PRE:
		String pre = child.getFirstChildWithType(behavLexer.STRING)
			.getText();
		task.setPreEP(pre);
		break;
	    case behavLexer.POST:
		String post = child.getFirstChildWithType(behavLexer.STRING)
			.getText();
		task.setPostEP(post);
		break;
	    case behavLexer.PP:
		String pp = child.getFirstChildWithType(behavLexer.WORD)
			.getText();
		task.setPp(pp);
		break;
	    case behavLexer.ROLE:
		String role = child.getFirstChildWithType(behavLexer.WORD)
			.getText();
		task.setRoblig(role);
		break;
	    }// switch
	}// for

	return task;
    }

    private static String readFileAsString(String filePath)
	    throws java.io.IOException {
	StringBuffer fileData = new StringBuffer(1000);
	BufferedReader reader = new BufferedReader(new FileReader(filePath));
	char[] buf = new char[1024];
	int numRead = 0;
	while ((numRead = reader.read(buf)) != -1) {
	    fileData.append(buf, 0, numRead);
	}
	reader.close();
	return fileData.toString();
    }
}
