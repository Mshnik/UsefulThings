package common.logging;

import com.sun.jdi.connect.IllegalConnectorArgumentsException;
import common.types.Tuple;
import common.types.Tuple2;
import javassist.*;
import javassist.util.HotSwapper;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.util.*;
import java.util.function.Predicate;

/**
 * Logger is the delegate for recording method calls.
 * Inserted code routes to Logger#log, which puts the method calls into a locally stored map.
 *
 * IMPORTANT FOR USE:
 * 0) augmentClass must be called on the class to output logging calls, or logging calls won't be generated
 *
 * 1) must run with VM arg:
 *
 *      -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000
 *
 *    This is required for HotSwapping.
 *    If address port has to change, also change SWAP_PORT static field below
 *
 *
 * Known issue: It's still putting in a new logging statement every time you run it
 *  rebuilding clears it out correctly though. This doesn't end up affecting anything
 *  because the logger is smart about duplicates, but it's weird and should be fixed
 *
 * //TODO - Test
 *
 * @author Mshnik
 */
public class Logger {

  /** The port the HotSwapping is done on */
  private static final int SWAP_PORT = 8000;

  /** The location of compiled .class files in the workspace */
  private static String CLASS_OUTPUT_FOLDER = null;

  /**
   * The method calls that have been made since the Logger was last reset.
   * The map is of the form:
   *    Key: method name in full form. Ex: add(Object), getNode(int), add(int, Object)
   *    Value: a tuple of (toString of object at time of call, args given to call in a list)
   */
  private final static Map<String, Set<Tuple2<String, List<Object>>>> methodCalls = new HashMap<>();

  /**
   * Sets the ClassOutputFolder to dir. This method must be called before
   * AugmentClass can be used.
   * @param dir - the class output folder directory
   */
  public static void setClassOutputFolder(String dir) {
    CLASS_OUTPUT_FOLDER = dir;
  }

  /** Logs the given method call
   * @param methodName - the full name of the method that was called
   * @param calledOn - the object the method was called on
   * @param args - the arguments given to the method call
   */
  public static void log(String methodName, Object calledOn, Object[] args) {
    if (! methodCalls.containsKey(methodName)) {
      methodCalls.put(methodName, new HashSet<>());
    }

    methodCalls.get(methodName).add(Tuple.of(calledOn.toString(), Arrays.asList(args)));
  }

  /** Writes the method calls to the given PrintStream */
  public static void writeMethodCalls(PrintStream out) {
    final String indent = "\t";

    for(String s : methodCalls.keySet()) {
      out.println(s);
      for(Tuple2<String, List<Object>> t : methodCalls.get(s)) {
        out.println(indent + t._2 + " on " + t._1);
      }
    }
  }

  /**
   * Checks if the map of method calls contains a valid call
   *
   * @param methodName - the method that should have been called
   * @param criteria - a predicate that tests whether a call matches
   * @return - true if a valid call is found, false otherwise
   */
  public static boolean hasMethodCall(String methodName, Predicate<Tuple2<String, List<Object>>> criteria) {
    if (! methodCalls.containsKey(methodName)) {
      return false;
    } else {
      for(Tuple2<String, List<Object>> t : methodCalls.get(methodName)) {
        if(criteria.test(t)) {
          return true;
        }
      }
      return false;
    }
  }

  /** Returns a view of the map of calls */
  public static Map<String, Set<Tuple2<String, List<Object>>>> getMethodCalls() {
    return Collections.unmodifiableMap(methodCalls);
  }

  /**
   * Re-Compiles and HotSwaps the given class to include logging statements.
   *
   * Specifically, loads the class and checks each method for the @Logged annotation.
   * When found, adds a logging statement to the top of the method call.
   * Re-Compiles the class, writes it to the .class file, and HotSwaps the new version
   * of the class into memory.
   *
   * @param eClass - the Class to augment with logging statements
   * @param <E> - the type of the Class to augment
   * @throws RuntimeException - if CLASS_OUTPUT_FOLDER is still null at the time of this call
   */
  public static <E> void augmentClass(Class<E> eClass) throws RuntimeException {
    if(CLASS_OUTPUT_FOLDER == null) {
      throw new RuntimeException("CLASS_OUTPUT_FOLDER not set");
    }
    final String targetClassName = eClass.getCanonicalName();
    try {
      final ClassPool pool = ClassPool.getDefault();

      //Find the already compiled class
      pool.appendClassPath(new LoaderClassPath(ClassLoader.getSystemClassLoader()));
      final CtClass compiledClass = pool.get(targetClassName);

      //Check each method to determine if it should be logged
      for (Method m : eClass.getDeclaredMethods()) {
        //Check if this method should get a logging statement
        if (!m.isAnnotationPresent(Logged.class)) {
          continue;
        }

        //Create array of parameters for this method
        CtClass[] params = new CtClass[m.getParameterTypes().length];
        for(int i = 0; i < params.length; i++) {
          params[i] = pool.get(m.getParameterTypes()[i].getCanonicalName());
        }

        //Look up the method
        final CtMethod method = compiledClass.getDeclaredMethod(m.getName(), params);

        //Create the full name with arg types (so overloaded methods aren't the same string)
        String methodNameAndTypes = m.getName() + "(";
        for(CtClass t : method.getParameterTypes()) {
          methodNameAndTypes += t.getSimpleName() + ", ";
        }
        methodNameAndTypes = methodNameAndTypes.substring(0, methodNameAndTypes.length() - 2) + ")";

        //Add line of code to the method
        method.insertBefore("Logger.log(\"" + methodNameAndTypes  + "\", $0, $args);");
      }

      //Class is done being modified; write to class file
      compiledClass.writeFile(CLASS_OUTPUT_FOLDER);

      //Reload (hotswap) the class
      HotSwapper swapper = new HotSwapper(SWAP_PORT);
      swapper.reload(targetClassName, compiledClass.toBytecode());

    } catch (ConnectException e) {
      System.err.println("Need VM arg : -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=8000");
      e.printStackTrace();
    } catch (NotFoundException e) {
      System.err.println("Couldn't find class for " + targetClassName);
      e.printStackTrace();
    } catch (CannotCompileException e) {
      System.err.println("Error compiling class " + targetClassName);
      e.printStackTrace();
    }  catch (IllegalConnectorArgumentsException e) {
      System.err.println("Error with constructing hotswapping agent");
      e.printStackTrace();
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /** Clears the method call map */
  public static void reset() {
    methodCalls.clear();
  }
}
