package common.dataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/**
 * An immutable collection representing a Cons List.
 * All Cons Lists are terminated with a Nil element (no tail, no value),
 * but this element is hidden from iteration and stringing when there
 * are non-nil elements in the list.
 * <br><br>
 * Because the List is immutable, different lists can be branched off of a shared
 * tail without altering the tail. This makes the ConsList useful in some algorithms
 * that require exploring different possibility chains without altering earlier
 * sub-chains.
 *
 * @param <E> - the generic type of elements stored within the lst.
 *            No methods are ever called on elements of type E, so no requirements
 *            are made of the type.
 * @author Mshnik
 */
public class ConsList<E> implements Iterable<E> {

  /**
   * A string used for toString implementations for the Nil element of
   * a Cons List
   */
  public static final String NIL_STRING = "_NIL_";

  /**
   * The tail of this List.
   * If this is Nil, tail is null. Otherwise, this is another ConsList
   * of one smaller size.
   */
  private final ConsList<E> tail;

  /**
   * The value stored in the head of this list.
   * If this is Nil, value is null. Otherwise the value may be null,
   * or the value stored in the head.
   */
  public final E val;

  /**
   * The size of this list. Because ConsLists are immutable, this size
   * value is immutable. If this is Nil, size is 0. Otherwise, size
   * is some positive, non-zero value
   */
  public final int size;

  /**
   * Initializes an empty (NIL) ConsList, with tail = null, val = null,
   * size = 0. <br>
   * A Nil element is required to be at the end of every ConsList,
   * so this constructor is used to start any ConsList. Elements are then
   * cons (prepended) on to this element to build a list.
   */
  public ConsList() {
    val = null;
    tail = null;
    size = 0;
  }

  /**
   * Creates a new head of a list
   *
   * @param val  - the value to store in the head of this list
   * @param tail - the tail of this list, an existing ConsList
   * @param size - the size of this list
   */
  private ConsList(E val, ConsList<E> tail, int size) {
    this.val = val;
    this.size = size;
    this.tail = tail;
    if (val == null || tail == null || size < 1)
      throw new RuntimeException("Illegal ConsList construction" + this);
  }

  /**
   * Returns the value stored in the head of this ConsList
   */
  public E value() {
    return val;
  }

  /**
   * Returns the tail of this ConsList. Returns null if this is NIL
   */
  public ConsList<E> tail() {
    return tail;
  }

  /**
   * Returns true if this is NIL - the trailing element in a consList.
   * A Nil element has no tail and no value
   */
  public boolean isNil() {
    return tail() == null && val == null;
  }

  /**
   * Returns true if this is the last element in a list.
   * Returns true for NIL elements, and the last real element before a NIL element
   * Used to stop iteration before it reaches the NIL terminator
   */
  public boolean isLast() {
    return isNil() || tail().tail() == null && tail().val == null;
  }

  /**
   * Returns a new ConsList that consists of {@code head} prepended
   * onto this ConsList
   */
  public ConsList<E> cons(E head) {
    return new ConsList<E>(head, this, size + 1);
  }

  /**
   * Returns a new ConsList that consists of this full list, reversed.
   */
  public ConsList<E> reverse() {
    ConsList<E> reversed = new ConsList<E>();
    ConsList<E> ptr = this;
    while (!ptr.isNil()) {
      reversed = reversed.cons(ptr.val);
      ptr = ptr.tail;
    }
    return reversed;
  }

  /**
   * Returns a string representation of a ConsList
   */
  @Override
  public String toString() {
    String s = "(";
    ConsList<E> current = this;
    while (current != null) {
      if (current.val == null && current.tail() == null) {
        if (current == this) s += NIL_STRING + ",";
      } else {
        s += current.val + ",";
      }
      current = current.tail();
    }
    return s.substring(0, s.length() - 1) + ")";
  }

  /**
   * Returns true if this equals {@code o}.
   * Two ConsLists are equal if they are the same length and every value they
   * at each index are equal.
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof ConsList<?>) || o == null) return false;

    ConsList<?> lst = (ConsList<?>) o;

    return size == lst.size && Objects.equals(val, lst.val) && Objects.equals(tail(), lst.tail());
  }

  /**
   * Returns the size of this list. @see ConsList<E>.size
   */
  public int size() {
    return size;
  }

  /**
   * Returns true iff the size of this list is 0. Equivalently, if this list is NIL
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns true iff this list contains the Object o.
   * NIL lists do not contain any elements, thus calling contains on a NIL
   * list for any input will always return false.
   */
  public boolean contains(Object o) {
    return !isNil() && (Objects.equals(val, o) || !isLast() && tail().contains(o));
  }

  /**
   * Returns true iff, for all {@code Object o : c}, this.contains(o)
   */
  public boolean containsAll(Collection<?> c) {
    for (Object o : c) {
      if (!contains(o)) return false;
    }
    return true;
  }

  /**
   * Returns an Iterator over the elements in this ConsList.
   * As ConsLists are immutable, the returned iterator does not support removal,
   * and cannot throw a ConcurrentModificationException.
   * The iterator returned will stop at the last real element in the list,
   * *before* the NIL element that terminates all lists. If this method
   * is called on the NIL list, the iterator will have no elements to return -
   * hasNext() will immediately return false. Thus the iterator behaves as if
   * the NIL element does not exist.
   */
  @Override
  public Iterator<E> iterator() {
    return new ConsIterator<E>(this);
  }

  /**
   * Converts this ConsList to an array of Objects.
   * The length of the returned array is equal to this.size.
   * Therefore for the NIL array, the returned array is of length 0.
   */
  public Object[] toArray() {
    Object[] arr = new Object[size];
    ConsList<E> current = this;
    for (int i = 0; i < size; i++, current = current.tail()) {
      arr[i] = current.val;
    }
    return arr;
  }

  /**
   * Converts this ConsList to an array of type T.
   * The length of the returned array is equal to Max(this.size, arr.length).
   * Therefore for the NIL array, the returned array is of length 0.
   * See the List interface for the weird type bullcrap about casting to T[]
   */
  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] arr) {
    if (arr.length < size) {
      arr = Arrays.copyOf(arr, size);
    }
    ConsList<E> current = this;
    for (int i = 0; i < size; i++, current = current.tail()) {
      arr[i] = (T) current.val;
    }
    return arr;
  }

  /**
   * Returns the {@code index}th element in this list.
   *
   * @throws IllegalArgumentException - if index < 0 or index >= size - thus throws
   * exception if this is called with any index value on the nil list
   */
  public E get(int index) {
    if (index < 0 || index >= size)
      throw new IllegalArgumentException("Can't get element at index " + index + " OOB");

    if (index == 0) return val;
    return tail().get(index - 1);
  }

  /**
   * Returns the first index of {@code o} in this List, or -1 if it does not occur
   */
  public int indexOf(Object o) {
    return indexOf(o, 0);
  }

  /**
   * Helper method for the indexOf function - keeps track of how many recursive
   * calls have been made thus far to this function to return the correct value
   * if {@code o} is found.
   */
  private int indexOf(Object o, int x) {
    if (Objects.equals(val, o)) return x;
    else if (isLast()) return -1;
    return tail().indexOf(o, x + 1);
  }

  /**
   * Helper class for iterating over ConsList
   * Keeps track of a current list element that will be returned by next() calls.
   *
   * @param <E>
   * @author Mshnik
   */
  public static class ConsIterator<E> implements Iterator<E> {

    private ConsList<E> current; //next element to return when next() is called

    /**
     * Creates a new ConsIterator, starting at first
     */
    public ConsIterator(ConsList<E> first) {
      current = first;
    }

    /**
     * Returns true iff there is another element to iterate over.
     * Specifically, returns {@code !current.isNil()}
     */
    @Override
    public boolean hasNext() {
      return !current.isNil();
    }

    /**
     * Returns the next value in this iteration, and moves current
     * forward one element.
     */
    @Override
    public E next() {
      E val = current.val;
      current = current.tail();
      return val;
    }

  }

}
