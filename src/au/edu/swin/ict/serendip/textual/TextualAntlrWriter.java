package au.edu.swin.ict.serendip.textual;

import java.util.List;

import au.edu.swin.ict.road.xml.bindings.BehaviorTermType;
import au.edu.swin.ict.road.xml.bindings.ConstraintType;
import au.edu.swin.ict.road.xml.bindings.ProcessDefinitionType;
import au.edu.swin.ict.road.xml.bindings.TaskRefType;
import au.edu.swin.ict.serendip.composition.Task;

/**
 * 
 * @author Malinda
 * 
 */
public class TextualAntlrWriter {
    public static String PDTypeToText(ProcessDefinitionType pdt) {
	StringBuffer buf = new StringBuffer();
	buf.append("ProcessDefinittion " + pdt.getId() + "{");
	buf.append("CoS " + pdt.getCoS());
	buf.append("CoT) " + pdt.getCoT());
	List<String> btIdList = pdt.getBehaviorTermRefs().getBehavirTermId();
	for (String s : btIdList) {
	    buf.append("BehaviorTermRef " + s + ";");
	}
	buf.append("};\n");
	return buf.toString();
    }

    public static String btTypeToText(BehaviorTermType btt) {
	// TODO: Us toString of BehaviorTerm in the grammar
	StringBuffer buf = new StringBuffer();
	String extendsPart = "";
	if (!((null == btt.getExtends()) || (btt.getExtends().equals("")))) {
	    extendsPart = " extends " + btt.getExtends();
	}

	// Show behavior terms
	buf.append("BehaviorTerm " + btt.getId() + extendsPart + "{\n");
	List<TaskRefType> trList = btt.getTaskRefs().getTaskRef();
	for (TaskRefType trt : trList) {
	    buf.append(taskRefTypeToText(trt) + "\n");
	}

	// Show constraints
	List<ConstraintType> cList = btt.getConstraints().getConstraint();
	for (ConstraintType c : cList) {
	    buf.append(constraintToText(c));
	}
	buf.append("};\n");

	return buf.toString();
    }

    public static String taskRefTypeToText(TaskRefType trt) {
	StringBuffer buf = new StringBuffer();
	buf.append("Task " + trt.getId() + "{\n");
	buf.append("EPpre " + trt.getPreEP() + "\n");
	buf.append("EPPost " + trt.getPostEP() + "\n");
	buf.append("PerfProb " + trt.getPerformanceVal() + "\n");

	buf.append("};" + "\n");

	return buf.toString();
    }

    public static String constraintToText(ConstraintType ct) {
	StringBuffer buf = new StringBuffer();

	return buf.toString();
    }
}
