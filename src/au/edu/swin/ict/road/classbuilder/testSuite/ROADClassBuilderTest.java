package au.edu.swin.ict.road.classbuilder.testSuite;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import au.edu.swin.ict.road.classbuilder.ROADClassBuilder;
import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Parameter;

import com.thoughtworks.paranamer.BytecodeReadingParanamer;
import com.thoughtworks.paranamer.Paranamer;

public class ROADClassBuilderTest {
    private static Logger log = Logger.getLogger(ROADClassBuilderTest.class
	    .getName());

    public static void main(String[] args) throws ClassNotFoundException {
	// Create an operation
	Operation op = new Operation();

	// Create some parameters
	List<Parameter> params = new ArrayList<Parameter>();
	params.add(new Parameter("String", "name"));
	params.add(new Parameter("String", "age"));

	op.setName("setNameAge");
	op.setParameters(params);
	op.setReturnType("void");

	// Create another operation
	Operation op2 = new Operation();
	List<Parameter> params2 = new ArrayList<Parameter>();
	params2.add(new Parameter(
		"au.edu.swin.ict.road.classbuilder.testSuite.Person", "person"));
	params2.add(new Parameter("String", "age"));

	op2.setName("setPerson");
	op2.setParameters(params2);
	op2.setReturnType("void");

	// List of operations
	List<Operation> ops = new ArrayList<Operation>();
	ops.add(op);
	ops.add(op2);

	ROADClassBuilder rcb = new ROADClassBuilder();
	rcb.buildClass("ROAD.Test", "Suite", ops);
	Class<?> clz = rcb.loadClass("ROAD.Test.Suite");
	System.out.println(clz.toString());
	Method[] methods = clz.getDeclaredMethods();

	Person person = new Person();
	Paranamer paranamer = new BytecodeReadingParanamer();

	for (Method m : methods) {
	    try {
		log.debug(m.getName());

		String[] arguments = paranamer.lookupParameterNames(m);
		log.debug(arguments.length);
		for (String arg : arguments)
		    log.debug("This " + arg);
		// m.invoke(clz.newInstance(), new Object[]{ person });
	    } catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
    }
}
