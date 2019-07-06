package asserts;

/** @author Mshnik */
public final class Asserts {
  private Asserts() {}

  /**
   * Asserts that the given runnable throws an exception with the given class. Fails if a different
   * exception is thrown or if nothing is thrown.
   */
  public static <T extends Throwable> T assertThrows(Class<T> exceptionClass, Runnable runnable) {
    try {
      runnable.run();
      throw new AssertionError(
          "Expected exception of type " + exceptionClass + ", but nothing was thrown.");
    } catch (Throwable t) {
      if (exceptionClass.isInstance(t)) {
        return exceptionClass.cast(t);
      } else {
        throw new AssertionError("Expected exception of type " + exceptionClass + ", but got " + t);
      }
    }
  }
}
