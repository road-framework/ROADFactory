package au.edu.swin.ict.road.classbuilder;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.bytecode.MethodInfo;

/**
 * The default implementation of the the interface ClassBuilder. This class only
 * provides basic functionalities of loading and building a class, as well as
 * retrieving the class loader.
 * 
 * @author Malinda Kapuruge
 * @author Jovino Margathe (jmargathe@gmail.com)
 */
public class ClassBuilder implements IClassBuilder {
    private Loader classLoader;

    public ClassBuilder() {
	classLoader = null;
    }

    @Override
    public Class<?> build(String pkgName, String className,
	    List<ClassOperations> operations) {

	// Parsing the qualified class name, in case no package is specified, it
	// just uses the class name
	String classQlfdName;
	if (!pkgName.isEmpty() || !pkgName.equals(""))
	    classQlfdName = pkgName + "." + className;
	else
	    classQlfdName = className;

	// Retrieving the default class pool and load the pool into the class
	// loader
	CtClass cc = null;
	ClassPool pool = ClassPool.getDefault();
	classLoader = new Loader(pool);

	// Building the class
	try {
	    // Build the superclass java.lang.Object
	    CtClass sc = pool.get("java.lang.Object");
	    cc = pool.makeClass(classQlfdName, sc);

	    // Iterate through the operations
	    for (ClassOperations op : operations) {
		// Dunno what to do with these
		// CtClass returnType = pool.get("java.lang.String");
		// CtClass paramType = pool.get("java.lang.Integer");
		// CtClass[] paramArray = new CtClass[1];
		// paramArray[0] = paramType;

		// Creating methods for the class
		CtMethod aMethod = null;
		try {
		    // Building the method string
		    String methodStr = op.getVisibility() + " "
			    + op.getOutParam() + " ";
		    methodStr += op.getOpName() + " (";
		    if (!op.getInParams().equals(""))
			methodStr += op.getInParams();
		    methodStr += ") { " + op.getMethodBody() + " }";
		    // Create a method of the class and append it
		    aMethod = CtNewMethod.make(methodStr, cc);
		    aMethod.setAttribute("paramNames", op.getInParams()
			    .getBytes());
		    cc.addMethod(aMethod);
		} catch (CannotCompileException e) {
		    e.printStackTrace();
		}
	    }
	} catch (NotFoundException e) {
	    e.printStackTrace();
	}

	try {
	    cc.writeFile();
	} catch (NotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (CannotCompileException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	return cc.getClass();
    }

    @Override
    public Loader getClassLoader() {
	return classLoader;
    }

    @Override
    public Class<?> loadClass(String className) {
	try {
	    return this.classLoader.loadClass(className);
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public CtClass getCompileTimeClass(String className) {
	try {
	    return ClassPool.getDefault().getCtClass(className);
	} catch (NotFoundException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    @Override
    public Map<String, Class<?>> getMethodParameters(Method m) {
	// Create an empty map
	Map<String, Class<?>> paramList = new HashMap<String, Class<?>>();

	// Retrieve the compile-time class and defrost it
	CtClass ct = getCompileTimeClass(m.getDeclaringClass().getName());
	ct.defrost();

	try {
	    // Retrieve the method info and the param names attribute
	    MethodInfo mi = ct.getDeclaredMethod(m.getName()).getMethodInfo();
	    String paramNames = new String(mi.getAttribute("paramNames").get());

	    // If param names is not empty or is not a whitespace/blank
	    if (!paramNames.isEmpty() && !paramNames.equals(" ")) {
		// Split the param names and retrieve the param types
		String[] params = paramNames.split(",");
		Class<?>[] paramTypes = m.getParameterTypes();

		// Iterate through the params
		for (int i = 0; i < params.length; i++) {
		    // Trimming and substring the param name
		    String param = params[i].trim();
		    param = param.substring(param.indexOf(" ") + 1);

		    // Put param and param names in the map
		    paramList.put(param, paramTypes[i]);
		}
	    }
	} catch (NotFoundException e) {
	    e.printStackTrace();
	}

	ct.freeze();
	return paramList;
    }
}
