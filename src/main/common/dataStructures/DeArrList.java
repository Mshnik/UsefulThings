package common.dataStructures;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;

import common.Util;
import common.dataStructures.util.SmartIterator;

/**
 * A DeArrList is a Double-Ended Array List. It behaves like a mashup of
 * a Deque and an ArrayList. <br>
 * By allowing the array to wrap around the underlying array in either direction,
 * it does not shift elements when adding to the beginning or the end of the array.
 * This allows it to achieve amortized O(1) append (add to end) and prepend (add to front)
 * behavior. Both operations are amortized O(1), as they do require
 * reArraying the whole DeArrList whenever it would become full. <br>
 * Any shifting that is done as the result of an insertion (insert in middle)
 * is optimized to be the smaller of the two shifts. This does not change the
 * O(N) time required to add or remove from the middle of the array, but
 * does speed up the functions in real time.
 * <br><br>
 * I came up with the idea while explaining to the section I teach about the
 * complexity differences between an ArrayList and a LinkedList.
 * One of the main points in the LinkedList's favor is O(1) prepend.
 * By contrast, the standard ArrayList implementation has to shift all elements
 * down 1 in order to insert an element at the front of the list. However,
 * both do have O(1) append. I realized that there's no reason I couldn't extend
 * the way ArrayList is able to do O(1) append to include O(1) prepend by
 * wrapping values around either side of the array.
 *
 * @author Mshnik
 */
public class DeArrList<E> extends AbstractList<E> implements Cloneable, Deque<E> {

  //start will never equal end. ReArray whenever this would become the case.
  private int start; //inclusive
  private int end; //exclusive
  private int size; //Number of elements in list
  private Object[] vals;

  /**
   * The default size DeArLists are initialized to if not provided a size
   */
  static final int DEFAULT_SIZE = 16;

  /**
   * Constructs a new DeArrList with size 16
   */
  public DeArrList() {
    this(DEFAULT_SIZE);
  }

  /**
   * Constructs a new DeArrList containing all of the elements in c
   *
   * @param c - the collection to initially add to this DeArrList.
   *          Elements are added in the order of iteration over c.
   */
  public DeArrList(Collection<? extends E> c) {
    this(Math.max(DEFAULT_SIZE, c.size()));
    addAll(c);
  }

  /**
   * Returns a new DeArrList with initial size {@code size}
   */
  public DeArrList(int size) {
    vals = new Object[size];
    start = size / 4;
    end = size / 4;
  }

  /**
   * Returns a new DeArrList with the same elements (in the same order) as this
   */
  public DeArrList<E> clone() {
    return new DeArrList<E>(this);
  }

  /**
   * Returns the number of elements in this DeArrList
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns the index in the array of values that represents the
   * start of this DeArrList. This is purely an internal operation and
   * doesn't affect the list this represents from the outside
   **/
  int getStart() {
    return start;
  }

  /**
   * Returns the index in the array of values that represents the end
   * of this DeArrList.  This is purely an internal operation and
   * doesn't affect the list this represents from the outside
   */
  int getEnd() {
    return end;
  }

  /**
   * Returns the length of the array of values that represents this DeArrList.
   * This is purely an internal operation and
   * doesn't affect the list this represents from the outside
   */
  int getArrLength() {
    return vals.length;
  }

  /**
   * Rotates the DeArrList such that the first element is at true position newStart.
   * This is purely an internal operation and doesn't affect the list this represents
   * from the outside
   */
  void rotateTo(int newStart) {
    if (newStart < 0 || newStart >= vals.length)
      throw new ArrayIndexOutOfBoundsException();

    if (newStart == start) return;

    Object[] arr = new Object[vals.length];

    //Move main portion
    int portion1 = Math.min(size, Math.min(vals.length - start, vals.length - newStart));
    System.arraycopy(vals, start, arr, newStart, portion1);
    //Move bridge portion
    int portion2 = Math.min(size - portion1, Math.abs(start - newStart));
    if (portion2 > 0) {
      System.arraycopy(vals, Util.mod(start + portion1, vals.length), arr,
          Util.mod(newStart + portion1, arr.length), portion2);
      //Move final portion
      int portion3 = size - portion1 - portion2;
      if (portion3 > 0) {
        System.arraycopy(vals, Util.mod(start + portion1 + portion2, vals.length), arr,
            Util.mod(newStart + portion1 + portion2, arr.length), portion3);
      }
    }

    start = newStart;
    end = Util.mod(newStart + size, arr.length);
    vals = arr;
  }

  /**
   * Returns a string representation of this DeArrList. Surrounded with "("
   * and with elements seperated by ",".
   */
  @Override
  public String toString() {
    if (size() == 0)
      return "()";
    String s = "(";
    for (E e : this) {
      s += e + ",";
    }
    return s.substring(0, s.length() - 1) + ")";
  }

  /**
   * Moves vals to a new array of double the length, starting at length / 4.
   * This is purely an internal operation and doesn't affect the list this represents
   * from the outside. It is used when the internal array must be increased in size.
   */
  private boolean reArray() {
    if (size() >= vals.length) {
      Object[] oArr = new Object[vals.length * 2];
      if (start < end) {
        System.arraycopy(vals, start, oArr, vals.length / 2, end - start);
      } else {
        System.arraycopy(vals, start, oArr, vals.length / 2, vals.length - start);
        System.arraycopy(vals, 0, oArr, vals.length / 2 + (vals.length - start), end);
      }
      start = vals.length / 2;
      end = vals.length * 3 / 2;
      vals = oArr;
      return true;
    }
    return false;
  }

  /**
   * Adds the given element at the given index to this DeArrList.
   *
   * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt; size().
   */
  @Override
  public void add(int index, E element) throws ArrayIndexOutOfBoundsException {
    if (index < 0 || index > size())
      throw new ArrayIndexOutOfBoundsException();
    reArray();

    int realIndex = Util.mod(start + index, vals.length);

    //Check for simple append, prepend operations
    if (index == size()) {
      end = Util.mod((end + 1), vals.length);
      vals[Util.mod(realIndex, vals.length)] = element;
    } else if (index == 0) {
      start = Util.mod((start - 1), vals.length);
      vals[Util.mod(realIndex - 1, vals.length)] = element;
    } else {
      //Shift left if there is no room to shift right (end == vals.length) and
      // (nondisjoint and in first half or disjoint and in second half
      if (end == vals.length || start != 0 &&
          (start < end && index < size() / 2 || start > end && start < realIndex)) {
        System.arraycopy(vals, start, vals, start - 1, index);
        start = Util.mod((start - 1), vals.length);
        vals[Util.mod(realIndex - 1, vals.length)] = element;
      }
      //Shift right otherwise
      else {
        System.arraycopy(vals, realIndex, vals, realIndex + 1, (size() - index));
        end = Util.mod((end + 1), vals.length);
        vals[Util.mod(realIndex, vals.length)] = element;
      }
    }
    size++;
    modCount++;
  }

  /**
   * Adds {@code e} to the front of this DeArrList
   */
  @Override
  public void push(E e) {
    add(0, e);
  }

  /**
   * Adds {@code e} to the back of this DeArrList.
   *
   * @return true
   */
  @Override
  public boolean offer(E e) {
    add(e);
    return true;
  }

  /**
   * Adds {@code e} to the front of this DeArrList
   */
  @Override
  public void addFirst(E e) {
    add(0, e);
  }

  /**
   * Adds {@code e} to the back of this DeArrList.
   */
  @Override
  public void addLast(E e) {
    add(e);
  }

  /**
   * Adds {@code e} to the front of this DeArrList.
   *
   * @return true
   */
  @Override
  public boolean offerFirst(E e) {
    add(0, e);
    return true;
  }

  /**
   * Adds {@code e} to the back of this DeArrList.
   *
   * @return true
   */
  @Override
  public boolean offerLast(E e) {
    add(e);
    return true;
  }

  /**
   * Returns the element at index {@code index} in this DeArrList.
   *
   * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt;= size().
   */
  @Override
  public E get(int index) throws ArrayIndexOutOfBoundsException {
    if (index < 0 || index >= size())
      throw new ArrayIndexOutOfBoundsException();

    @SuppressWarnings("unchecked")
    E e = (E) vals[Util.mod((index + start), vals.length)];
    return e;
  }

  /**
   * Returns the element at index 0 (the first element) in this DeArrList.
   *
   * @throws ArrayIndexOutOfBoundsException if the DeArrList is empty.
   */
  @Override
  public E element() throws ArrayIndexOutOfBoundsException {
    return get(0);
  }

  /**
   * Returns the element at index 0 (the first element) in this DeArrList.
   *
   * @throws ArrayIndexOutOfBoundsException if the DeArrList is empty.
   */
  @Override
  public E getFirst() throws ArrayIndexOutOfBoundsException {
    return get(0);
  }

  /**
   * Returns the element at index size()-1 (the last element) in this DeArrList.
   *
   * @throws ArrayIndexOutOfBoundsException if the DeArrList is empty.
   */
  @Override
  public E getLast() throws ArrayIndexOutOfBoundsException {
    return get(size() - 1);
  }

  /**
   * Returns the element at index 0 (the first element) in this DeArrList.
   * Returns null if the DeArrList is empty
   */
  @Override
  public E peek() {
    return peekFirst();
  }

  /**
   * Returns the element at index 0 (the first element) in this DeArrList
   * Returns null if the DeArrList is empty
   */
  @Override
  public E peekFirst() {
    try {
      return get(0);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Returns the element at index size()-1 (the last element) in this DeArrList
   * Returns null if the DeArrList is empty
   */
  @Override
  public E peekLast() {
    try {
      return get(size() - 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Sets the value of index {@code index} to be {@code element}.
   *
   * @return the old value at that index, that was just overwritten by the set operation.
   * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt;= size().
   */
  @Override
  public E set(int index, E element) throws ArrayIndexOutOfBoundsException {
    if (index < 0 || index >= size())
      throw new ArrayIndexOutOfBoundsException();

    E prev = get(index);
    vals[Util.mod((index + start), vals.length)] = element;
    return prev;
  }

  /**
   * Removes the element at index {@code index} from the DeArrList.
   * Shifts elements to close the gap.
   *
   * @return the element that was removed by this operation.
   * @throws ArrayIndexOutOfBoundsException if index &lt; 0 or index &gt;= size().
   */
  @Override
  public E remove(int index) throws ArrayIndexOutOfBoundsException {
    E e = get(index);
    if (index == size() - 1) {
      vals[Util.mod(end - 1, vals.length)] = null;
      end = Util.mod((end - 1), vals.length);
    } else if (index == 0) {
      vals[start] = null;
      start = Util.mod((start + 1), vals.length);
    } else {
      int realIndex = Util.mod(start + index, vals.length);
      //Shift right if
      // (nondisjoint and in first half or disjoint and in second half
      if ((start < end && index < size() / 2 || start >= end && start < realIndex)) {
        System.arraycopy(vals, start, vals, start + 1, realIndex - start);
        vals[start] = null;
        start = Util.mod((start + 1), vals.length);
      }
      //Shift left otherwise
      else {
        System.arraycopy(vals, realIndex + 1, vals, realIndex, end - realIndex - 1);
        vals[end - 1] = null;
        end = Util.mod((end - 1), vals.length);
      }
    }
    size--;
    modCount++;
    return e;
  }

  /**
   * Removes and returns the element at the front of this DeArrList
   * Returns null if this DeArrList is empty.
   */
  @Override
  public E poll() {
    try {
      return remove(0);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Removes and returns the element at the front of this DeArrList
   *
   * @throws ArrayIndexOutOfBoundsException if this DeArrList is empty
   */
  @Override
  public E pop() throws ArrayIndexOutOfBoundsException {
    return removeFirst();
  }

  /**
   * Removes and returns the element at the front of this DeArrList
   *
   * @throws ArrayIndexOutOfBoundsException if this DeArrList is empty
   */
  @Override
  public E remove() throws ArrayIndexOutOfBoundsException {
    return removeFirst();
  }

  /**
   * Clears the DeArrList of all elements
   */
  @Override
  public void clear() {
    vals = new Object[vals.length];
    size = 0;
    modCount++;
  }

  /**
   * Removes and returns the element at the front of this DeArrList
   *
   * @throws ArrayIndexOutOfBoundsException if this DeArrList is empty
   */
  @Override
  public E removeFirst() throws ArrayIndexOutOfBoundsException {
    return remove(0);
  }

  /**
   * Removes and returns the element at the back of this DeArrList
   *
   * @throws ArrayIndexOutOfBoundsException if this DeArrList is empty
   */
  @Override
  public E removeLast() throws ArrayIndexOutOfBoundsException {
    return remove(size() - 1);
  }

  /**
   * Removes and returns the element at the front of this DeArrList
   * Returns null if this DeArrList is empty.
   */
  @Override
  public E pollFirst() {
    try {
      return remove(0);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Removes and returns the element at the back of this DeArrList
   * Returns null if this DeArrList is empty.
   */
  @Override
  public E pollLast() {
    try {
      return remove(size() - 1);
    } catch (ArrayIndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * Removes the first occurrence of Object {@code o} in this DeArrList
   *
   * @return true if the DeArrList was modified as a result of this call
   * (If o was contained in this DeArrList).
   */
  @Override
  public boolean removeFirstOccurrence(Object o) {
    try {
      return remove(indexOf(o)) != null;
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }

  /**
   * Removes the last occurrence of Object o in this DeArrList
   *
   * @return true if the DeArrList was modified as a result of this call
   * (If o was contained in this DeArrList).
   */
  @Override
  public boolean removeLastOccurrence(Object o) {
    try {
      return remove(lastIndexOf(o)) != null;
    } catch (ArrayIndexOutOfBoundsException e) {
      return false;
    }
  }

  @Override
  public Iterator<E> iterator() {
    return new SmartIterator<E>(super.iterator(), () -> modCount);
  }

  @Override
  public Iterator<E> descendingIterator() {
    throw new UnsupportedOperationException("Not Yet Implemented"); //TODO
  }
}
