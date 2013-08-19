package au.edu.swin.ict.road.classbuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javassist.Loader;

import au.edu.swin.ict.road.composite.contract.Operation;
import au.edu.swin.ict.road.composite.contract.Parameter;

/**
 * A class that provides the ability to create runtime objects from descriptions
 * such as class name, method names, etc for use in ROADfactory.
 * 
 * @author The ROAD team, Swinburne University of Technology
 */
public class ROADClassBuilder extends ClassBuilder {

    private Loader classLoader;

    public ROADClassBuilder() {
    }

    /**
     * Procedure that builds a compile time class and puts it into the loaded
     * memory. It iterates through a list of operations defined by the
     * ROADfactory and puts them into class operations. By default the
     * visibility of the class is public, the return type is void and the method
     * body is return null.
     * 
     * @param pkgName
     *            the name of the package of the runtime generated class.
     * @param className
     *            the name of the runtime generated class.
     * @param operations
     *            a list of operations defined by ROADfactory.
     */
    public void buildClass(String pkgName, String className,
	    List<Operation> operations) {
	List<ClassOperations> classOperations = new ArrayList<ClassOperations>();

	String visibility = "public";
	String returnType = "void";
	String parameters = "";
	String methodName = "methodName";
	String methodBody = "return null;";

	// Retrieve the operations.
	ClassOperations operation;
	for (Operation op : operations) {
	    parameters = "";
	    methodName = op.getName();
	    returnType = op.getReturnType();

	    // Iterating through a list of parameters
	    for (Parameter param : op.getParameters()) {
		// Adding the parameters up
		parameters += param.getType() + " " + param.getName();

		// If not the last element in the list of parameters add a comma
		if (!param.equals(op.getParameters().get(
			op.getParameters().size() - 1)))
		    parameters += ", ";
	    }

	    if (parameters.equalsIgnoreCase(""))
		methodBody = "return null;";
	    else
		methodBody = "System.out.println($1); return null;";
	    operation = new ClassOperations(visibility, returnType, methodName,
		    parameters, methodBody);
	    classOperations.add(operation);
	}

	// Check if the class exists
	try {
	    java.lang.reflect.Method m;
	    m = ClassLoader.class.getDeclaredMethod("findLoadedClass",
		    new Class[] { String.class });
	    m.setAccessible(true);
	    ClassLoader cl = ClassLoader.getSystemClassLoader();
	    Object testObject = m.invoke(cl, pkgName + "." + className);
	    if (testObject != null) {
		// Something can be done after checking if the class exists or
		// not
	    } else {
		// Something can be done after checking if the class exists or
		// not
	    }
	} catch (SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (NoSuchMethodException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalArgumentException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IllegalAccessException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (InvocationTargetException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	// Build the compile time class
	super.build(pkgName, className, classOperations);

	// Refreshing the class loader for retrieving the loaded memory
	classLoader = super.getClassLoader();
    }

    /**
     * Function to load a class from the working memory.
     * 
     * @param fullClassName
     *            the class name of the class to load.
     * @return the class object or null if not found.
     */
    public Class<?> loadClass(String fullClassName) {
	try {
	    return classLoader.loadClass(fullClassName);
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    /**
     * Returns the Javassist classLoader being used.
     */
    @Override
    public Loader getClassLoader() {
	return this.classLoader;
    }
}
