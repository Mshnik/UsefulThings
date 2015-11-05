package common.dataStructures;

import functional.Function;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

public class BloomFilter<T> {

  private static int DEFAULT_FLAGS_SIZE = 1024;
  private boolean flags[];
  private ArrayList<Function<T, Integer>> hashFunctions;
  private int size;

  public BloomFilter() {
    this(DEFAULT_FLAGS_SIZE, true);
  }

  public BloomFilter(int flagsLength, boolean useDefaultHashFunctions) {
    this.flags = new boolean[flagsLength];
    this.hashFunctions = new ArrayList<>();
    this.size = 0;

    if(useDefaultHashFunctions) {
      addHashFunction((e) -> e.hashCode());
      addHashFunction((e) -> e.hashCode() + 1);
      addHashFunction((e) -> e.hashCode() * 2);
      addHashFunction((e) -> e.hashCode() * 3);
    }
  }

  public boolean addHashFunction(Function<T, Integer> f) {
    if(size != 0) {
      return false;
    }
    hashFunctions.add(f);
    return true;
  }

  public void add(T t) {
    for(Function<? super T, Integer> func : hashFunctions) {
      flags[func.apply(t) % flags.length] = true;
    }
    size++;
  }

  public void addAll(Set<? extends T> t) {
    for(T elm : t) {
      add(elm);
    }
  }

  public int size() {
    return size;
  }

  public boolean contains(T t){
    for(Function<? super T, Integer> func : hashFunctions) {
      if( ! flags[func.apply(t) % flags.length]) {
        return false;
      }
    }
    return true;
  }

  public boolean containsAll(Collection<? extends T> col) {
    for(T t : col) {
      if(! contains(t)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean equals(Object o) {
    try {
      BloomFilter<?> b = (BloomFilter<?>) o;
      return size == b.size && Arrays.equals(flags, b.flags);
    }catch(ClassCastException e) {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(flags) + size;
  }

  @Override
  public String toString() {
    return "Size = " + size + ", " + Arrays.toString(flags);
  }
}
