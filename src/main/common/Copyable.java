package common;

import java.util.Objects;

/**
 * A Copyable is a wrapper that supports copying around an arbitrary type.
 * Specifically, a.equals(a.copy()) should return false.
 * This allows for the creation of effectively equivalent objects that
 * are distinct objects for hashsets and map storage.
 * <br><br>
 * @param <E> - The type the Copyable wraps.
 * @author Mshnik
 */
public class Copyable<E> extends IDObject {

  private final E data;

  /**
   * Constructs a new Copyable wrapping the given data.
   * this is assigned a unique id via IDObject.
   * @param data the data to wrap. Can be null.
   */
  private Copyable(E data) {
    super();
    this.data = data;
  }

  /**
   * Constructs a new Copyable wrapping the given data.
   * this is assigned a unique id via IDObject.
   * @param data the data to wrap. Can be null.
   */
  public static <E> Copyable<E> of(E data) {
    return new Copyable<>(data);
  }

  /** Returns the data wrapped by this Copyable */
  public E get() {
    return data;
  }

  /**
   * Makes a Copy of this Copyable. The returned Copyable wraps the same instance
   * of data (this.data == returned.data), but has a different ID, thus
   * this.equals(returned) == false.
   */
  public Copyable<E> copy() {
    return new Copyable<>(data);
  }

  /** Two copyables are equal iff:
   * They have the same ID (super.equals(..))
   * They wrap the same instance of data, using .equals.
   * @param o the object to comare this to
   * @return true iff this is equivalent to o.
   */
  public boolean equals(Object o) {
    return (o instanceof Copyable<?>) && super.equals(o) && Objects.equals(data,((Copyable<?>)o).data);
  }

  /** Returns the string of the data, and the id for the toString of this Copyable */
  @Override
  public String toString() {
    return Objects.toString(data) + " (Copy ID=" + getID() +")";
  }
}
