package common.dataStructures.util;

import java.util.Iterator;

//TODO - SPEC
//TODO - TEST
public abstract class ViewIterator<E> implements Iterator<E> {

  protected final Iterator<E> iterator;
  protected boolean removed;
  protected E current;

  public ViewIterator(Iterator<E> i) {
    iterator = i;
    removed = false;
    current = null;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public E next() {
    current = iterator.next();
    removed = false;
    return current;
  }

  protected boolean isRemoved() {
    return removed;
  }

  @Override
  public abstract void remove();

}
