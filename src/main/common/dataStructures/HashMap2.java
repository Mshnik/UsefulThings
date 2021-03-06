package common.dataStructures;

import java.util.*;
import java.util.stream.Collectors;

import common.types.Tuple;
import common.types.Tuple2;

//TODO - TEST
/**
 * A HashMap from the given two key values to a value type
 * Each key can be present more than once, as it takes a pair of
 * key values to map to a single value.
 * <br><br>
 * Uses Tuple2 internally to manage key pairs.
 *
 * @param <K1> - the first key type
 * @param <K2> - the second key type
 * @param <V>  - the value type
 * @author Mshnik
 */
public class HashMap2<K1, K2, V> extends HashMap<Tuple2<K1, K2>, V> {

  /***/
  private static final long serialVersionUID = 1L;

  /**
   * Constructs an empty map
   */
  public HashMap2() {
    super();
  }

  /**
   * Constructs a map that initially contains all of the elements in m.
   * This is a shallow copy - elements will be contained in both this and m
   */
  public HashMap2(Map<? extends Tuple2<K1, K2>, V> m) {
    super(m);
  }

  /**
   * Returns the v associated with the given keys, null if none
   */
  public V get(K1 k1, K2 k2) {
    return get(Tuple.of(k1, k2));
  }

  /**
   * Returns the list of v associated with the given key 1, null if none
   */
  public List<V> getAll1(K1 k1) {
    return keySet().stream().filter(t -> k1.equals(t._1)).map(this::get).collect(Collectors.toList());
  }

  /**
   * Returns the list of v associated with the given key 1, null if none
   */
  public List<V> getAll2(K2 k2) {
    return keySet().stream().filter(t -> k2.equals(t._2)).map(this::get).collect(Collectors.toList());
  }

  /**
   * Puts the given v to be associated with keys k1, k2.
   * Adds a new empty hashmap associated with k1 if k1 is new key
   */
  public V put(K1 k1, K2 k2, V v) {
    return put(Tuple.of(k1, k2), v);
  }

  /**
   * Puts the given v to be associated with keys k1, k2 if the pair isn't present
   * Adds a new empty hashmap associated with k1 if k1 is new key
   */
  public V putIfAbsent(K1 k1, K2 k2, V v) {
    return putIfAbsent(Tuple.of(k1, k2), v);
  }

  /**
   * Returns true iff this map contains the given combination of
   * keys for a single key
   */
  public boolean containsKeyPair(K1 k1, K2 k2) {
    return containsKey(Tuple.of(k1, k2));
  }

  /**
   * Returns true iff this map contains the given single key in any of
   * its key combinations. This operation is O(N) to loop over all key pairs.
   */
  public boolean containsKeyHalf(Object k) {
    for (Tuple2<K1, K2> t : keySet()) {
      if (Objects.equals(t._1, k) || Objects.equals(t._2, k)) return true;
    }
    return false;
  }

  /**
   * Removes the mapping for the specified keys
   */
  public V removePair(K1 k1, K2 k2) {
    return remove(Tuple.of(k1, k2));
  }

  /**
   * Removes all values associated with the given keyhalf.
   * Returns all of the values removed in this operation, empty set if none
   */
  public Set<V> removeHalf(Object half) {
    HashSet<V> s = new HashSet<V>();
    for (Tuple2<K1, K2> t : new HashSet<Tuple2<K1, K2>>(keySet())) {
      if (Objects.equals(t._1, half) || Objects.equals(t._2, half)) {
        s.add(get(t));
        remove(t);
      }
    }
    return s;
  }

}
