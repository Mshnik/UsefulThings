package game.decision;

/**
 * @author Mshnik
 */
public final class UnselectableException extends RuntimeException {
  public UnselectableException(Choice c) {
    super("Cannot select choice " + c + ", it is not currently selectable.");
  }
}
