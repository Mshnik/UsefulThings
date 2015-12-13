package common.dataStructures;

import functional.impl.Function1;

import java.util.*;

public class BloomFilter<T> {

  public static int DEFAULT_FLAGS_SIZE = 1024;
  private boolean flags[];
  private ArrayList<Function1<T, Integer>> hashFunctions;
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

  public boolean addHashFunction(Function1<T, Integer> f) {
    if(size != 0) {
      return false;
    }
    hashFunctions.add(f);
    return true;
  }

  public int getHashFunctionCount() {
    return hashFunctions.size();
  }

  public List<Function1<T, Integer>> getHashFunctions() {
    return new DeArrList<>(hashFunctions);
  }

  public void add(T t) {
    for(Function1<? super T, Integer> func : hashFunctions) {
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

  public int flagsSize() {
    return flags.length;
  }

  public boolean contains(Object o){
    try {
      for (Function1<? super T, Integer> func : hashFunctions) {
        if (!flags[func.apply((T)o) % flags.length]) {
          return false;
        }
      }
      return true;
    } catch (ClassCastException e){
      return false;
    }
  }

  public boolean containsAll(Collection<?> col) {
    for(Object o : col) {
      if(! contains(o)) {
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
