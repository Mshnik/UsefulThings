package common.dataStructures;

import functional.impl.Function1;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class BloomFilteredCollection<T> implements Collection<T> {

  private BloomFilter<T> filter;
  private Collection<T> underlyingCollection;

  private int filterResizeCount;

  public BloomFilteredCollection() {
    this(new HashSet<>());
  }

  public BloomFilteredCollection(Collection<T> col) {
    this.underlyingCollection = col;
    this.filter = new BloomFilter<>();
  }

  @Override
  public int size() {
    return underlyingCollection.size();
  }

  @Override
  public boolean isEmpty() {
    return underlyingCollection.isEmpty();
  }

  @Override
  public boolean contains(Object o) {

    if (! filter.contains(o)) {
      return false;
    } else {
      return underlyingCollection.contains(o);
    }

  }

  @Override
  public Iterator<T> iterator() {
    return underlyingCollection.iterator();
  }

  @Override
  public Object[] toArray() {
    return underlyingCollection.toArray();
  }

  @Override
  public <T1> T1[] toArray(T1[] a) {
    return underlyingCollection.toArray(a);
  }

  private void increaseFilterSize() {
    BloomFilter<T> newFilter = new BloomFilter<>(filter.flagsSize() * 2, false);
    for(Function1<T, Integer> f : filter.getHashFunctions()) {
      newFilter.addHashFunction(f);
    }
    filter = newFilter;
    filterResizeCount++;
  }

  @Override
  public boolean add(T t) {
    if (size() < filter.flagsSize() / filter.getHashFunctionCount()) {
      filter.add(t);
      return underlyingCollection.add(t);
    } else {
      boolean ok = underlyingCollection.add(t);
      increaseFilterSize();
      for(T t2 : this) {
        filter.add(t2);
      }
      return ok;
    }
  }

  @Override
  public boolean remove(Object o) {
    return underlyingCollection.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    for(Object o : c) {
      if (! contains(o)) return false;
    }
    return true;
  }

  @Override
  public boolean addAll(Collection<? extends T> c) {
    boolean changed = false;
    for(T t : c) {
      changed = add(t) || changed;
    }
    return changed;
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    boolean changed = false;
    for(Object o : c) {
      changed = remove(o) || changed;
    }
    return changed;
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    HashSet<T> toRemove = new HashSet<>(size());
    for(T t : this) {
      if (! c.contains(t)) {
        toRemove.add(t);
      }
    }
    return removeAll(toRemove);
  }

  @Override
  public void clear() {
    filter = new BloomFilter<>();
    underlyingCollection.clear();
  }
}
