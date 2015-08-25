package common.dataStructures.util;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * A ViewSet is a Set that is an un-addable view of another source collection or map.
 * The remove operation is optional - extending classes can have it throw an
 * UnsupportedOperationException if the ViewSet is intended to be fully unmodifiable.
 * Otherwise, calling remove on a ViewSet will also cause the removed element to
 * be removed from the underlying collection or map.
 * However, if that underlying collection or map is altered, the associated ViewSet will
 * reflect the change. A ViewSet is returned by many element getters
 * in other data structures.
 *
 * @param <E> - the type of elements in the underlying set.
 * @author Mshnik
 */
public abstract class ViewSet<E> extends AbstractSet<E> {

  private boolean isCollection; //true if the source is a collection, false for map
  private Object source; //Either a collection or a map

  /**
   * Constructs a ViewSet that is a view of the given collection
   */
  public ViewSet(Collection<?> c) {
    isCollection = true;
    source = c;
  }

  /**
   * Constructs a ViewSet that is a view of the given map
   */
  public ViewSet(Map<?, ?> m) {
    isCollection = false;
    source = m;
  }

  /**
   * Not supported - throws an UnsupportedOperationException
   */
  @Override
  public final boolean add(E e) {
    throw new UnsupportedOperationException("Can't add an element to a ViewSet");
  }

  /**
   * Causes the given element to be removed from the underlying collection or map,
   * if it is present.
   *
   * @return true if the object was removed, false otherwise
   */
  @Override
  public abstract boolean remove(Object o);

  /**
   * Returns an iterator over the elements in this ViewSet
   */
  @Override
  public abstract Iterator<E> iterator();

  /**
   * Removes all elements from the underlying collection or map
   */
  @Override
  public void clear() {
    if (isCollection)
      ((Collection<?>) source).clear();
    else
      ((Map<?, ?>) source).clear();
  }

  /**
   * Returns the size of the underlying collection or map
   */
  @Override
  public int size() {
    if (isCollection)
      return ((Collection<?>) source).size();
    else
      return ((Map<?, ?>) source).size();
  }
}