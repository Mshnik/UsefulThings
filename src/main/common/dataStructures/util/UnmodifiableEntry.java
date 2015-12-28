package common.dataStructures.util;

import java.util.Map.Entry;

import common.types.Tuple2;

//TODO - TEST
/**
 * An UnmodifiableEntry is a map entry that is not modifiable.
 * Specifically, the setValue(..) method throws an UnsupportedOperationException
 * Otherwise, it behaves as a basic entry type, with key and value.
 *
 * @param <K> - the key type
 * @param <V> - the value type
 * @author Mshnik
 */
public class UnmodifiableEntry<K, V> extends Tuple2<K, V> implements Entry<K, V> {

  /**
   * Constructs an UnmodifiableEntry with the given key and value
   */
  public UnmodifiableEntry(K k, V v) {
    super(k, v);
  }

  /**
   * Returns the key of this UnmodifiableEntry
   */
  @Override
  public K getKey() {
    return _1;
  }

  /**
   * Returns the value of this UnmodifiableEntry
   */
  @Override
  public V getValue() {
    return _2;
  }

  /**
   * Not supported - throws an UnsupportedOperationException
   */
  @Override
  public V setValue(V value) {
    throw new UnsupportedOperationException("Can't set the value of an UnmodifiableEntry");
  }

}
