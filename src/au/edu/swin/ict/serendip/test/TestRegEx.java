package au.edu.swin.ict.serendip.test;

import java.io.File;

import javax.swing.JFrame;

import org.apache.log4j.Logger;

public class TestRegEx {
    private static Logger log = Logger.getLogger(TestRegEx.class.getName());

    public static void main(String[] args) {
	JFrame f = new JFrame();
	f.setBounds(0, 0, 200, 400);
	f.setVisible(true);

	// //
	String blockName = "PI:PDGold001";
	String[] blockNameSplit = blockName.split(":");
	if (blockNameSplit.length != 2) {
	    log.debug("ERROR");
	} else {
	    log.debug("OK");
	}
	for (String s : blockNameSplit) {
	    log.debug(s);
	}
	// testDir();
    }

    public static void testDir() {
	File file = new File("E:/temp/data.txt");
	log.debug(file.getParent());
	log.debug(file.getAbsoluteFile().getName());
	int index = file.getAbsoluteFile().getName().lastIndexOf('.');
	if (index > 0 && index <= file.getName().length() - 2) {
	    log.debug("Filename without Extension: "
		    + file.getName().substring(0, index));
	}
    }

    public static void testRegex() {
	String input = "[m1,m2]->{CO.doTask(24)}";

	String[] messages = null;
	String role = null, taskName = null, prop = null;

	String msgsStr = input.substring(input.indexOf("[") + 1,
		input.indexOf("]"));
	messages = msgsStr.split(",");

	for (int i = 0; i < messages.length; i++) {
	    log.debug("Msg " + i + " is " + messages[i]);
	}

	String taskStr = input.substring(input.indexOf("{") + 1,
		input.indexOf("}"));

	log.debug(">>" + taskStr);
	String[] taskSplit = taskStr.split("\\.");

	role = taskSplit[0];
	prop = taskSplit[1].substring(taskSplit[1].indexOf("(") + 1,
		taskSplit[1].indexOf(")"));

	taskName = taskSplit[1].substring(taskSplit[1].indexOf(".") + 1,
		taskSplit[1].indexOf("("));

	log.debug("Task Name : " + taskName);
	log.debug("role : " + role);
	log.debug("prop: " + prop);
    }
}
