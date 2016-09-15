package common.dataStructures;

import functional.impl.Predicate1;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
  public final ConsList<E> tail;

  /**
   * The value stored in the head of this list.
   * If this is Nil, value is null. Otherwise the value may be null,
   * or the value stored in the head.
   */
  public final E head;

  /**
   * The size of this list. Because ConsLists are immutable, this size
   * value is immutable. If this is Nil, size is 0. Otherwise, size
   * is some positive, non-zero value
   */
  public final int size;

  /**
   * Initializes an empty (NIL) ConsList, with tail = null, head = null,
   * size = 0. <br>
   * A Nil element is required to be at the end of every ConsList,
   * so this constructor is used to start any ConsList. Elements are then
   * cons (prepended) on to this element to build a list.
   */
  public ConsList() {
    head = null;
    tail = null;
    size = 0;
  }

  /**
   * Creates a new head of a list
   *
   * @param head  - the value to store in the head of this list
   * @param tail - the tail of this list, an existing ConsList
   * @param size - the size of this list
   */
  private ConsList(E head, ConsList<E> tail, int size) {
    this.head = head;
    this.size = size;
    this.tail = tail;
    if (tail == null || size < 1)
      throw new RuntimeException("Illegal ConsList construction" + this);
  }

  /**
   * Returns the value stored in the head of this ConsList
   */
  public E head() {
    return head;
  }

  /**
   * Returns the value stored in the head of this ConsList
   */
  public E value() {
    return head;
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
    return tail == null && head == null;
  }

  /**
   * Returns true if this is the last element in a list.
   * Returns true for NIL elements, and the last real element before a NIL element
   * Used to stop iteration before it reaches the NIL terminator
   */
  public boolean isLast() {
    return isNil() || tail.tail == null && tail.head == null;
  }

  /**
   * Returns a new ConsList that consists of {@code head} prepended
   * onto this ConsList
   */
  public ConsList<E> cons(E head) {
    return new ConsList<>(head, this, size + 1);
  }

  /**
   * Returns a new ConsList that consists of this full list, reversed.
   */
  public ConsList<E> reverse() {
    ConsList<E> reversed = new ConsList<E>();
    ConsList<E> ptr = this;
    while (!ptr.isNil()) {
      reversed = reversed.cons(ptr.head);
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
      if (current.head == null && current.tail == null) {
        if (current == this) s += NIL_STRING + ",";
      } else {
        s += current.head + ",";
      }
      current = current.tail;
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
    if (o == null || !(o instanceof ConsList<?>)) return false;

    ConsList<?> lst = (ConsList<?>) o;

    return size == lst.size && Objects.equals(head, lst.head) && Objects.equals(tail, lst.tail);
  }

  /** Hashes the ConsList by the combined hash of its elements.
   * Hash definition copied from Arrays.hashCode and implemented
   * with iterator so a new array isn't implemented. */
  @Override
  public int hashCode() {
    int result = 1;

    for (E element : this) {
      result = 31 * result + (element == null ? 0 : element.hashCode());
    }

    return result;
  }

  /**
   * Returns the size of this list. See {@link common.dataStructures.ConsList#size}
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
  public boolean contains(E o) {
    return !isNil() && (Objects.equals(head, o) || !isLast() && tail.contains(o));
  }

  /**
   * Returns true iff, for all {@code Object o : c}, this.contains(o)
   */
  public boolean containsAll(Collection<? extends E> c) {
    for (E o : c) {
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
    return new ConsIterator<>(this);
  }

  /**
   * Returns a Spliterator over the elements in this ConsList.
   * The returned Spliterator is sized, immutable, and concurrent
   */
  @Override
  public Spliterator<E> spliterator() {
    return new ConsSpliterator<>(this, null);
  }

  /** Returns a stream over the elements in this ConsList. */
  public Stream<E> stream() {
    return StreamSupport.stream(spliterator(), true);
  }

  /**
   * Converts this ConsList to an array of Objects.
   * The length of the returned array is equal to this.size.
   * Therefore for the NIL array, the returned array is of length 0.
   */
  public Object[] toArray() {
    Object[] arr = new Object[size];
    ConsList<E> current = this;
    for (int i = 0; i < size; i++, current = current.tail) {
      arr[i] = current.head;
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
    for (int i = 0; i < size; i++, current = current.tail) {
      arr[i] = (T) current.head;
    }
    return arr;
  }

  /** Return a new ConsList, containing only the elements that pass the predicate */
  public ConsList<E> filter(Predicate1<? super E> predicate) {
    ConsList<E> lst = new ConsList<>();
    for(E e : this) {
      if(predicate.apply(e)){
        lst = lst.cons(e);
      }
    }
    return lst.reverse();
  }

  /**
   * Returns the {@code index}th element in this list.
   *
   * @throws IllegalArgumentException - if index &lt; 0 or index &gt;= size - thus throws
   * exception if this is called with any index value on the nil list
   */
  public E get(int index) {
    if (index < 0 || index >= size)
      throw new IllegalArgumentException("Can't get element at index " + index + " OOB");

    if (index == 0) return head;
    return tail.get(index - 1);
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
    if (Objects.equals(head, o)) return x;
    else if (isLast()) return -1;
    return tail.indexOf(o, x + 1);
  }

  /**
   * Helper class for iterating over ConsList
   * Keeps track of a current list element that will be returned by next() calls.
   *
   * @param <E>
   * @author Mshnik
   */
  static class ConsIterator<E> implements Iterator<E>{

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
      E val = current.head;
      current = current.tail;
      return val;
    }
  }

  /** Helper class for spliterating over ConsList
   *
   * @param <E>
   * @author Mshnik
   */
  static class ConsSpliterator<E> implements Spliterator<E> {

    private ConsList<E> next;
    private final ConsList<E> end; //When next==end, this Spliterator is done

    public ConsSpliterator(ConsList<E> first, ConsList<E> last) {
      this.next = first;
      this.end = last;
    }

    @Override
    public boolean tryAdvance(Consumer<? super E> action) {
      if(next.isNil() || next == end) {
        return false;
      } else {
        action.accept(next.head);
        next = next.tail;
        return true;
      }
    }

    @Override
    public Spliterator<E> trySplit() {
      if(estimateSize() <= 1) {
        return null;
      } else {
        ConsList<E> first = next;
        long size = estimateSize();
        for(int i = 0; i < size / 2; i++) {
          next = next.tail;
        }
        return new ConsSpliterator<>(first, next);
      }
    }

    @Override
    public long estimateSize() {
      return next.size - (end != null ? end.size : 0);
    }

    @Override
    public int characteristics() {
      return SIZED | IMMUTABLE | CONCURRENT;
    }
  }
}
