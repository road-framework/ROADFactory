package au.edu.swin.ict.road.classbuilder;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javassist.CtClass;
import javassist.Loader;

/**
 * The interface IClassBuilder that provides the functionality to load a class
 * dynamically in runtime.
 * 
 * @author Malinda Kapuruge
 */
public interface IClassBuilder {
    /**
     * Function to build a runtime class using the Javassist library. The class
     * is then put into the memory using a class loader. The classes that is in
     * the memory are frozen to prevent further changes.
     * 
     * @param pkgName
     *            the package name of the runtime class
     * @param className
     *            the class name of the runtime class
     * @param operations
     *            an array of operations of the class
     * @return the built runtime class
     */
    Class<?> build(String pkgName, String className,
	    List<ClassOperations> operations);

    /**
     * Function to search a class and retrieve it from the runtime memory. The
     * class must be built first before this function is called.
     * 
     * @param className
     * @return the runtime class if exists, otherwise would return null
     */
    Class<?> loadClass(String className);

    /**
     * Function to retrieve the compile time class from the class pool. The
     * class to be retrieved can be either a compile time class, i.e. dynamic
     * class, or a development time class.
     * 
     * @param className
     *            the class name to be retrieved
     * @return the compile time class object
     */
    CtClass getCompileTimeClass(String className);

    /**
     * Function to retrieve the class loader, which is a manager that loads a
     * class into a runtime memory.
     * 
     * @return the class loader
     */
    Loader getClassLoader();

    /**
     * Function that retrieves the preserved parameter names in an attribute
     * specified in the Java bytecode. It unfreezes the compile-time class and
     * retrieve the attributes, then freezes it back again. <br/>
     * This function would only and can only work with the methods created using
     * the ClassBuilder.
     * 
     * @param m
     *            the reflection Method which parameters would be retrieved
     * @return a map containing the parameter names and class types
     */
    Map<String, Class<?>> getMethodParameters(Method m);
}
