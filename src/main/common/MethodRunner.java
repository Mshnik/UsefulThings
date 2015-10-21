package common;

import common.types.Either;
import functional.*;

/** MethodRunner allows a method call to be made along with a timeout time.
 * The method call is made in a newly created thread while the calling thread waits for it to
 * finish. If the method call terminates before the timeout time, the value is simply returned.
 * If, however, the timeout time arrives without the method call finished, the calling thread is
 * simply terminated in an unsafe state. This was done in an effort to make MethodRunner simplistic
 * and easy to use, but has concequences if methods with side-effects.
 * <br><br>
 * MethodRunners are not meant to be shared within threads. Multi-threaded behavior is unsafe and
 * completely unspecified.
 *
 * @param <T> - the return type of the method call
 * @author Mshnik
 */
public class MethodRunner<T> {

  private final Supplier<T> methodCall;
  private T result;
  private Throwable throwable;
  private boolean halted;
  private boolean done;

  private final long millisToWait;
  private long completionMillis;

  /**
   * The default max amount of milliseconds to wait for the operation to finish, if
   * millisToWait is not provided in construction.
   */
  public static final long DEFAULT_WAIT_TIME = 1000;
  private static final long MILLIS_SLEEP_BETWEEN_UPDATES = 100;

  /** Constructs a new MethodRunner that takes at most DEFAULT_WAIT_TIME ms.
   * @param methodCall - the method call to make
   * @throws IllegalArgumentException if methodCall == null
   */
  public MethodRunner(Supplier<T> methodCall) throws IllegalArgumentException {
    this(methodCall, DEFAULT_WAIT_TIME);
  }

  /** Construct a new MethodRunner
   * @param methodCall - the method call to make
   * @param millisToWait - the number of milliseconds to wait for methodCall to return
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public MethodRunner(Supplier<T> methodCall, long millisToWait) throws IllegalArgumentException {
    if(methodCall == null) {
      throw new IllegalArgumentException("Can't create timeout worker on null method call");
    }
    if(millisToWait <= 0) {
      throw new IllegalArgumentException("Can't wait a non-positive number of milliseconds");
    }

    this.methodCall = methodCall;
    this.millisToWait = millisToWait;
    this.completionMillis = -1;
  }

  /** Construct a new MethodRunner that takes at most DEFAULT_WAIT_TIME ms.
   * @param methodCall - the method call to make
   * @param arg - the argument to give to the method call
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A> MethodRunner(Function<A, T> methodCall, A arg) throws IllegalArgumentException {
    this(methodCall.partialApply(arg));
  }

  /** Construct a new MethodRunner
   * @param methodCall - the method call to make
   * @param arg - the argument to give to the method call
   * @param millisToWait - the number of milliseconds to wait for methodCall to return
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A> MethodRunner(Function<A, T> methodCall, A arg, long millisToWait) throws IllegalArgumentException {
    this(methodCall.partialApply(arg), millisToWait);
  }

  /** Construct a new MethodRunner that takes at most DEFAULT_WAIT_TIME ms.
   * @param methodCall - the method call to make
   * @param arg - the first argument to give to the method call
   * @param arg2 - the second argument to give to the method call
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A,B> MethodRunner(BiFunction<A, B, T> methodCall, A arg, B arg2) throws IllegalArgumentException {
    this(methodCall.partialApply(arg, arg2));
  }

  /** Construct a new MethodRunner
   * @param methodCall - the method call to make
   * @param arg - the first argument to give to the method call
   * @param arg2 - the second argument to give to the method call
   * @param millisToWait - the number of milliseconds to wait for methodCall to return
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A,B> MethodRunner(BiFunction<A, B, T> methodCall, A arg, B arg2, long millisToWait) throws IllegalArgumentException {
    this(methodCall.partialApply(arg, arg2), millisToWait);
  }

  /** Construct a new MethodRunner that takes at most DEFAULT_WAIT_TIME ms.
   * @param methodCall - the method call to make
   * @param arg - the first argument to give to the method call
   * @param arg2 - the second argument to give to the method call
   * @param arg3 - the third argument to give to the method call
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A,B,C> MethodRunner(TriFunction<A, B, C, T> methodCall, A arg, B arg2, C arg3) throws IllegalArgumentException {
    this(methodCall.partialApply(arg, arg2, arg3));
  }

  /** Construct a new MethodRunner
   * @param methodCall - the method call to make
   * @param arg - the first argument to give to the method call
   * @param arg2 - the second argument to give to the method call
   * @param arg3 - the third argument to give to the method call
   * @param millisToWait - the number of milliseconds to wait for methodCall to return
   * @throws IllegalArgumentException - if methodCall == null, or millisToWait <= 0
   */
  public <A,B,C> MethodRunner(TriFunction<A, B, C, T> methodCall, A arg, B arg2, C arg3, long millisToWait) throws IllegalArgumentException {
    this(methodCall.partialApply(arg, arg2, arg3), millisToWait);
  }

  /** Runs the method call for up to {@code millisToWait} milliseconds,
   * then returns the result. If the call was interrupted, null will be returned.
   * This method can be invoked multiple times to make the same method call multiple times,
   * but the result of the first call will be forgotten upon making the second call.
   *
   * @return the result if it was computed, the throwable if an exception or error occurred, or null if
   *    the call was terminated due to timeout or if the worker thread was somehow interrupted.
   */
  private Either<Throwable,T> runAndGet() {
    done = false;
    halted = false;
    result = null;
    throwable = null;
    completionMillis = -1;

    TimeoutWorker r = new TimeoutWorker();
    long startTime = System.currentTimeMillis();
    r.start();
    while (System.currentTimeMillis() < startTime + millisToWait && !done) {
      try {
        Thread.sleep(MILLIS_SLEEP_BETWEEN_UPDATES);
      } catch (InterruptedException e) {
        throw new RuntimeException("Worker thread was interrupted, somehow. Bad.");
      }
    }

    completionMillis = System.currentTimeMillis() - startTime;

    if(!done) {
      r.stop();
      halted = true;
      done = true;
      return null;
    }

    return Either.selectNonNull(throwable, result);
  }

  /** If his method call has not been run, calls runAndGet() to compute and return it.
   * If it has, returns the result that was computed.
   * Thus multiple calls to get() will compute the result once and then return it for each call.
   *
   * @return the result if it was computed, the throwable if an exception or error occurred, or null if
   *    the call was terminated due to timeout
   */
  public Either<Throwable,T> get() {
    if (!done) {
      return runAndGet();
    } else if (halted) {
      return null;
    } else {
      return Either.selectNonNull(throwable, result);
    }
  }

  /** If his method call has not been run, calls runAndGet() to compute and return it.
   * If it has, returns the result that was computed.
   * Thus multiple calls to get() will compute the result once and then return it for each call.
   * If the method call resulted in an exception or error, re-throws that exception or error.
   * Otherwise, returns the result that the method call gave.
   *
   * @throws Throwable, whatever Throwable the method call threw.
   * @return the result if computed, or null if the method call was terminated due to timeout
   */
  public T getOrThrow() throws Throwable {
    if (!done) {
      Either<Throwable, T> e = runAndGet();
      if (e == null) return null;
      else if (e.isLeft()) throw e.asLeft();
      else return e.asRight();
    } else if (halted) {
      return null;
    } else {
      if (throwable != null) throw throwable;
      else return result;
    }
  }

  /** Return the amount of time it took this method runner to run.
   *  If this methodRunner hasn't been run yet or is in the process of running, return -1.
   *  If this wasn't terminated due to infinite loop, the returned value will be less than
   *  the allotted time. If it was terminated due to infinite loop, the returned value
   *  will be >= the allotted time
   */
  public long getCompletionMillis() {
    return completionMillis;
  }

  /** A helper class for MethodRunner - simply executes the method call and sets done to
   * true upon termination.
   * @author Mshnik
   */
  private class TimeoutWorker extends Thread {

    /**
     * Executes the method call for this MethodRunner.
     * If the method call runs to completion without exception or error,
     * stores the result in result. If an exception occurs, stores the throwable in throwable.
     * Either way, if this terminates, sets done to true.
     */
    @Override
    public void run() {
      try {
        result = methodCall.apply();
      } catch(Throwable t) {
        throwable = t;
      } finally {
        done = true;
      }
    }

    @Override
    public String toString() {
      return "Timeout worker for " + TimeoutWorker.this;
    }
  }

}
