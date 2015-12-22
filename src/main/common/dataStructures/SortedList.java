package common.dataStructures;

import java.util.Collection;

/** A simple List implementation that is always sorted.
 * The only modified functionality is the addition of new elements.
 * Addition is overridden such that new elements will always be placed in sorted order into the list.
 * Null elements are placed at the front of the list, and the sorting is not time-stable
 * (later additions of comparatively equal elements may be placed before or after their equivalent elements
 * already in the list)
 *
 * @param <E> - the element type in this list. Must be comparable to be sortable.
 */
public class SortedList<E extends Comparable<E>> extends DeArrList<E> {

  /** Constructs a new intially empty Sorted List */
  public SortedList() {
    super();
  }

  /** Constructs a new Sorted List initially containing the elements in col */
  public SortedList(Collection<? extends E> col) {
    super(col);
  }

  /** Constructs a new Sorted List initially empty, with underlying capacity for size elements */
  public SortedList(int size) {
    super(size);
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the front.
   */
  @Override
  public void push(E e) {
    add(e);
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the back
   * @return true
   */
  @Override
  public boolean offer(E e) {
    add(e);
    return true;
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the front.
   */
  @Override
  public void addFirst(E e) {
    add(e);
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the back.
   */
  @Override
  public void addLast(E e) {
    add(e);
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the index requested.
   */
  @Override
  public void add(int index, E elm) {
    add(elm);
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the front.
   * @return true
   */
  @Override
  public boolean offerFirst(E e) {
    add(e);
    return true;
  }

  /**
   * Adds {@code e} to this DeArrList. Adds in sorted order, not necessarily to the back.
   * @return true
   */
  @Override
  public boolean offerLast(E e) {
    add(e);
    return true;
  }

  /** Adds the given element to the SortedList.
   * Null values are placed at the front of the list, the rest in sorted order.
   * @param elm - the element to add to the list.
   */
  @Override
  public boolean add(E elm) {
    if(elm == null || size() == 0) {
      super.add(0, elm);
      return true;
    }

    int high = size();
    int low = -1;

    while(high > low + 1) {
      int mid = (high + low) / 2;

      E e = get(mid);
      int comp = e != null ? elm.compareTo(e) : 1;
      if(comp == 0) {
        high = mid;
        low = mid;
      } else if(comp < 0) {
        high = mid;
      } else {
        low = mid;
      }
    }
    super.add(low+1, elm);
    return true;
  }

}
