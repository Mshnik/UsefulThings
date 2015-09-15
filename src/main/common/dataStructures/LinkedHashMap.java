package common.dataStructures;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import common.dataStructures.util.ViewSet;

public class LinkedHashMap<K, V> extends AbstractMap<K, V> implements Map<K, V>, Iterable<Entry<K, V>> {

  private HashMap<K, LinkedHashEntry> map;

  private LinkedHashEntry head;
  private LinkedHashEntry tail;

  protected int modCount; //Number of times this LinkedHashMap has been structurally modified

  private KeySet keySet;
  private ValueCollection values; //not actually a set
  private EntrySet entrySet;

  private static final int DEFAULT_SIZE = 16;
  private static final float DEFAULT_LOAD_FACTOR = 0.75f;

  /**
   * A single entry in the hashmap. Maintains a key and value,
   * along with previous and next elements
   *
   * @author Mshnik
   */
  private class LinkedHashEntry implements Entry<K, V> {

    /**
     * The key stored in this LinkedHashEntry
     */
    private K key;

    /**
     * The value stored in this LinkedHashEntry
     */
    private V val;

    /**
     * The entry after this LinkedHashEntry. Null if this is the tail
     */
    private LinkedHashEntry next;

    /**
     * The entry before this LinkedHashEntry. Null if this is the head
     */
    private LinkedHashEntry prev;

    /**
     * Constructs a new LinkedHashEntry with the given key and value
     * The next and previous fields are left as null.
     *
     * @param k - the key to give this LinkedHashEntry
     * @param v - the value to give this LinkedHashEntry
     * @throws IllegalArgumentException - if k == null
     */
    private LinkedHashEntry(K k, V v) throws IllegalArgumentException {
      if (k == null)
        throw new IllegalArgumentException("Null keys not allowed");
      key = k;
      val = v;
    }

    /**
     * Returns the key stored in this LinkedHashEntry
     */
    @Override
    public K getKey() {
      return key;
    }


    /**
     * Returns the value stored in this LinkedHashEntry
     */
    @Override
    public V getValue() {
      return val;
    }

    /**
     * Sets the value stored in this LinkedHashEntry to {@code value}
     */
    @Override
    public V setValue(V value) {
      V oldVal = val;
      val = value;
      return oldVal;
    }

    /**
     * Two LinkedHashEntries are equivalent iff they have the same key and value
     */
    @Override
    public boolean equals(Object o) {
      try {
        @SuppressWarnings("unchecked")
        LinkedHashEntry e = (LinkedHashEntry) o;

        return key.equals(e.key) && Objects.equals(val, e.val);
      } catch (ClassCastException e) {
        return false;
      }
    }

    /**
     * Hashes a LinkedHashEntry based off of its key and value
     */
    @Override
    public int hashCode() {
      return Objects.hash(key, val);
    }

    /**
     * Returns {@code key + "=" + value}
     */
    @Override
    public String toString() {
      return key + "=" + val;
    }
  }

  public LinkedHashMap() {
    this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
  }

  public LinkedHashMap(int initalCapacity, float loadFactor) {
    map = new HashMap<>(initalCapacity, loadFactor);
    keySet = new KeySet();
    values = new ValueCollection();
    entrySet = new EntrySet();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean containsKey(Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(Object value) {
    for (V v : values()) {
      if (Objects.equals(v, value)) return true;
    }
    return false;
  }

  /**
   * Returns the linkedHashEntry that corresponds to {@code key}
   */
  private LinkedHashEntry getEntry(K key) {
    return map.get(key);
  }

  /**
   * Returns the entry at index {@code index} in the map.
   * Does this by starting at the end of the map and iterating in
   * until the desired index is reached.
   *
   * @throws IllegalArgumentException - if index &lt; 0 || index &gt;= size()
   */
  public Entry<K, V> get(int index) throws IllegalArgumentException {
    if (index < 0 || index >= size())
      throw new IllegalArgumentException(index + " is OOB for " + this);
    LinkedHashEntry current = null;

    if (index < size() / 2) {
      current = head;
      while (index > 0) {
        current = current.next;
        index--;
      }
    } else {
      current = tail;
      index = size() - 1 - index;
      while (index > 0) {
        current = current.prev;
        index--;
      }
    }

    return current;
  }


  @Override
  public V get(Object key) {
    if (!map.containsKey(key)) return null;
    return map.get(key).val;
  }

  /**
   * Returns the first entry in this LinkedHashMap
   */
  public Entry<K, V> getFirst() {
    return head;
  }

  /**
   * Returns the last entry in this LinkedHashMap
   */
  public Entry<K, V> getLast() {
    return tail;
  }

  /**
   * Returns the index {@code i} such that at index {@code i} in this
   * LinkedHashMap, either the entry equals o, or the key equals o, or the value equals o.
   * Thus finds the indexOf any matching object type. If a specific collection is desired,
   * use the collection getter (keySet(), values(), entrySet()) and call indexOf there
   *
   * @return the index of the equivalent object if found, -1 otherwise
   */
  public int indexOf(Object o) {
    LinkedHashEntry current = head;
    int i = 0;
    while (current != null) {
      if (Objects.equals(current, o) || Objects.equals(current.key, o) || Objects.equals(current.val, o))
        return i;
      current = current.next;
      i++;
    }
    return -1;
  }

  public int lastIndexOf(Object o) {
    LinkedHashEntry current = tail;
    int i = 0;
    while (current != null) {
      if (Objects.equals(current, o) || Objects.equals(current.key, o) || Objects.equals(current.val, o))
        return i;
      current = current.prev;
      i++;
    }
    return -1;
  }

  private V putHelper(K key, V value, int index) {
    if (index < 0 || index > size())
      throw new IllegalArgumentException("Can't put;" + index + " is OOB for " + this);

    if (containsKey(key)) {
      V oldVal = getEntry(key).val;
      getEntry(key).val = value;
      return oldVal;
    } else {
      LinkedHashEntry e = new LinkedHashEntry(key, value);
      if (size() == 0) {
        head = e;
        tail = e;
      } else if (index == size()) {
        tail.next = e;
        e.prev = tail;
        tail = e;
      } else if (index == 0) {
        head.prev = e;
        e.next = head;
        head = e;
      } else {
        LinkedHashEntry e2 = (LinkedHashEntry) get(index);
        LinkedHashEntry e2P = e2.prev;
        e2P.next = e;
        e2.prev = e;
        e.prev = e2P;
        e.next = e2;
      }

      map.put(key, e);
      modCount++;
      return null;
    }
  }

  @Override
  public V put(K key, V value) {
    return putHelper(key, value, size());
  }

  public V putLast(K key, V value) {
    return putAt(key, value, size());
  }

  public V putFirst(K key, V value) {
    return putAt(key, value, 0);
  }

  public V putAt(K key, V value, int index) {
    if (containsKey(key))
      throw new RuntimeException("Can't call putAt on an already existing key");
    return putHelper(key, value, index);
  }

  @Override
  public void putAll(Map<? extends K, ? extends V> m) {
    throw new UnsupportedOperationException("Can't put all of an unordered Map");
  }

  /**
   * Adds the given entry to the end of this LinkedHashMap
   * iff the key for the entry isn't already in the map
   *
   * @param e - the entry to add
   * @return - true iff the entry was added this way
   */
  public boolean add(Entry<K, V> e) {
    if (containsKey(e.getKey())) return false;
    putLast(e.getKey(), e.getValue());
    return true;
  }

  public boolean add(int index, Entry<K, V> element) {
    if (containsKey(element.getKey())) return false;
    putAt(element.getKey(), element.getValue(), index);
    return true;
  }

  public boolean addAll(Collection<? extends Entry<K, V>> c) {
    boolean altered = false;
    for (Entry<K, V> e : c) {
      altered = add(e) || altered;
    }
    return altered;
  }

  public boolean addAll(int index, Collection<? extends Entry<K, V>> c) {
    boolean altered = false;
    for (Entry<K, V> e : c) {
      altered = add(index, e) || altered;
      index++;
    }
    return altered;
  }

  public Entry<K, V> set(int index, Entry<K, V> element) {
    if (containsKey(element.getKey()))
      throw new IllegalArgumentException("Can't call set with already present key " + element.getKey());

    LinkedHashEntry old = (LinkedHashEntry) get(index);
    LinkedHashEntry oldCopy = new LinkedHashEntry(old.key, old.val);

    old.key = element.getKey();
    old.val = element.getValue();

    return oldCopy;
  }

  @Override
  public V remove(Object key) {

    LinkedHashEntry entry = map.get(key);
    if (entry == null) return null;

    LinkedHashEntry e0 = entry.prev;
    LinkedHashEntry e1 = entry.next;

    if (e0 != null) {
      e0.next = e1;
    }
    if (e1 != null) {
      e1.prev = e0;
    }
    if (entry == head) {
      head = e1;
    }
    if (entry == tail) {
      tail = e0;
    }

    map.remove(key);
    modCount++;

    return entry.val;
  }

  public Entry<K, V> remove(int index) {
    Entry<K, V> e = get(index);
    remove(e.getKey());
    return e;
  }

  public boolean removeAll(Collection<?> c) {
    boolean altered = false;
    for (Object o : c) {
      altered = remove(o) != null || altered;
    }
    return altered;
  }

  public boolean retainAll(Collection<?> c) {
    Iterator<K> iter = keySet.iterator();
    boolean altered = false;
    while (iter.hasNext()) {
      K k = iter.next();
      if (!c.contains(k)) {
        iter.remove();
        altered = true;
      }
    }
    return altered;
  }

  @Override
  public void clear() {
    map.clear();
    head = null;
    tail = null;
  }

  @Override
  public Set<K> keySet() {
    return keySet;
  }

  @Override
  public Collection<V> values() {
    return values;
  }

  @Override
  public Set<Entry<K, V>> entrySet() {
    return entrySet;
  }

  /**
   * Returns an iterator over the entries in this LinkedHashMap
   */
  public Iterator<Entry<K, V>> iterator() {
    return entrySet.iterator();
  }

  /**
   * Returns an array of Entries, in the iteration order of this LinkedHashMap
   */
  public Object[] toArray() {
    Object[] o = new Object[size()];

    int i = 0;
    for (Entry<K, V> e : this) {
      o[i] = e;
      i++;
    }

    return o;
  }

  @SuppressWarnings("unchecked")
  public <T> T[] toArray(T[] a) throws ClassCastException {
    T[] arr = a.length >= size() ? a : Arrays.copyOf(a, size());

    int i = 0;
    for (Entry<K, V> e : this) {
      arr[i] = (T) e;
      i++;
    }

    return arr;
  }

  private abstract class HashIterator<E> implements Iterator<E> {
    private int expectedModCount;
    private boolean removed;

    private LinkedHashEntry current;

    public HashIterator() {
      removed = false;
      expectedModCount = LinkedHashMap.this.modCount;
      current = head;
    }

    @Override
    public boolean hasNext() {
      return current != null;
    }

    protected abstract E valFromEntry(Entry<K, V> e);

    @Override
    public E next() {
      if (expectedModCount != modCount)
        throw new ConcurrentModificationException();

      Entry<K, V> e = current;
      removed = false;
      current = current.next;
      return valFromEntry(e);
    }

    @Override
    public void remove() {
      if (removed) return;

      LinkedHashMap.this.remove(current.key);
      expectedModCount++;
      removed = true;
    }
  }

  private class KeySet extends ViewSet<K> {
    public KeySet() {
      super(LinkedHashMap.this);
    }

    @Override
    public boolean remove(Object o) {
      return LinkedHashMap.this.remove(o) != null;
    }

    @Override
    public Iterator<K> iterator() {
      return new HashIterator<K>() {
        @Override
        protected K valFromEntry(Entry<K, V> e) {
          return e.getKey();
        }
      };
    }
  }

  private class ValueCollection extends ViewSet<V> {
    public ValueCollection() {
      super(LinkedHashMap.this);
    }

    @Override
    public boolean remove(Object o) {
      throw new UnsupportedOperationException("Can't remove from a values collection "
          + "- may have multiple mappings");
    }

    @Override
    public Iterator<V> iterator() {
      return new HashIterator<V>() {
        @Override
        protected V valFromEntry(Entry<K, V> e) {
          return e.getValue();
        }
      };
    }
  }

  private class EntrySet extends ViewSet<Entry<K, V>> {
    public EntrySet() {
      super(LinkedHashMap.this);
    }

    @Override
    public boolean remove(Object o) {
      try {
        return LinkedHashMap.this.remove(((Entry<?, ?>) o).getKey()) != null;
      } catch (ClassCastException e) {
        return false;
      }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
      return new HashIterator<Entry<K, V>>() {
        @Override
        protected Entry<K, V> valFromEntry(Entry<K, V> e) {
          return e;
        }
      };
    }
  }

  //	public ListIterator<Entry<K, V>> listIterator() {
  //		return null;
  //	}
  //
  //	public ListIterator<Entry<K, V>> listIterator(int index) {
  //		return null;
  //	}
  //
  //	public List<Entry<K, V>> subList(int fromIndex, int toIndex) {
  //		return null;
  //	}
}
