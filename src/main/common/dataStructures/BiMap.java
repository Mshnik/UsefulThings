package common.dataStructures;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import common.dataStructures.util.SmartIterator;
import common.dataStructures.util.UnmodifiableEntry;
import common.dataStructures.util.ViewIterator;
import common.dataStructures.util.ViewSet;

/** A BiMap is a Bidirectional Map between the two given generic types.
 * Thus both keys and values exist in a set, so there cannot be duplicate values.
 * Lookup operations use the correct map, so all have O(1) behavior.
 * @author Mshnik
 *
 * @param <K> - the Key type
 * @param <V> - the Value type
 */
public class BiMap<K, V> extends AbstractMap<K, V> implements Cloneable, Map<K,V>{

	private HashMap<K,V> forwardMap = new HashMap<K,V>();
	private HashMap<V,K> backMap = new HashMap<V,K>();

	private Set<K> keySet = new KeySet();
	private Set<V> valueSet = new ValueSet();
	private Set<Entry<K,V>> entrySet = new EntrySet();
	private Set<Entry<V,K>> rEntrySet = new REntrySet();

	protected int modCount;
	
	/** Constructs a new empty BiMap */
	public BiMap(){
		modCount = 0;
	}

	/** Constructs a new BiMap that is a copy of this BiMap */
	public BiMap<K,V> clone(){
		BiMap<K,V> b = new BiMap<K,V>();
		b.putAll(this);
		return b;
	}

	/** Returns true if O is a BiMap and they have the same entryset */
	@Override
	public boolean equals(Object o){
		if(o == this) return true;
		if(o == null) return false;
		
		try{
			@SuppressWarnings("unchecked")
			BiMap<K,V> b = (BiMap<K,V>) o;
			return entrySet().equals(b.entrySet());
		}catch(ClassCastException e){
			return false;
		}
	}

	/** Returns a new BiMap with the keys and values flipped. */
	public BiMap<V, K> flip(){
		BiMap<V,K> b = new BiMap<V,K>();
		b.backMap.putAll(forwardMap);
		b.forwardMap.putAll(backMap);
		return b;
	}

	/** Returns a single-sided map facing <K,V>.
	 * The returned map is independent of this BiMap. It can be modified,
	 * but the modifications will not be reflected in this map
	 */
	public Map<K,V> toMap(){
		return new HashMap<>(forwardMap);
	}

	/** Returns a single-sided map facing <V,K>.
	 * The returned map is independent of this BiMap. It can be modified,
	 * but the modifications will not be reflected in this map
	 */
	public Map<V,K> toFlippedMap(){
		return new HashMap<>(backMap);
	}

	/** Returns the size ofthis BiMap. */
	@Override
	public int size(){
		//Overridden for speed - no need to create entry set
		return forwardMap.size(); 
	}

	/** Puts the key, value pair in this BiMap. Because this is a bymap,
	 * also updates the value to point to the given key.
	 * Doesn't permit null keys or null values.
	 * @return the old value associated with the given key, or null if none.
	 */
	@Override
	public V put(K k, V v){
		if(k == null || v == null)
			throw new IllegalArgumentException("Null keys or values aren't valid in a BiMap");

		V old = forwardMap.put(k, v);
		K old2 = backMap.put(v, k);
		if(old2 != null && ! old2.equals(k)){
			forwardMap.remove(old2);
		}
		if(old != null && ! old.equals(v)){
			backMap.remove(old);
		}
		modCount++;
		return old;
	}

	/** Puts all of the key,value pairs in this BiMap. Overwrites old pairs
	 * encountered during the putAll operation.
	 * @throws IllegalArgumentException if m contains the same value twice
	 * among its key-value pairs, as this BiMap cannot contain the same value twice,
	 * and the order of put would determine which value persists. This would
	 * result in unspecified behavior.  */
	@Override
	public void putAll(Map<? extends K, ? extends V> m){
		//If it's a biMap, then no trouble, can putAll.
		if(m instanceof BiMap<?,?>){
			super.putAll(m);
			return;
		} 
		//Only a standard map - check that the values are a set
		HashSet<V> vals = new HashSet<>(m.values());
		if(vals.size() != m.values().size()){
			throw new IllegalArgumentException("Can't add " + m + " to " + this + ":"
					+"Values contain duplicates, would have unspecified performance");
		}
		super.putAll(m);
	}

	/** Returns true iff this BiMap contains the given value */
	@Override
	public boolean containsValue(Object v){
		return backMap.containsKey(v);
	}

	/** Returns true iff this BiMap contains the given key */
	@Override
	public boolean containsKey(Object k){
		return forwardMap.containsKey(k);
	}

	/** Returns the value associated with the given key, or null if none */
	@Override
	public V get(Object key){
		return forwardMap.get(key);
	}

	/** Returns the value associated with the given key, or the default if none */
	@Override
	public V getOrDefault(Object key, V defaultV){
		V v = get(key);
		return v != null ? v : defaultV;
	}

	/** @see get(Object key) */
	public V getValue(K key){
		return get(key);
	}

	/** @see getOrDefault(Object key) */
	public V getValueOrDefault(K key, V defaultV){
		return getOrDefault(key, defaultV);
	}

	/** Returns the key associated with the given value, or null if none */
	public K getKey(V value){
		return backMap.get(value);
	}

	/** Returns the key associated with the given value, or the default if none */
	public K getKeyOrDefault(V value, K defaultK){
		K k = getKey(value);
		return k != null ? k : defaultK;
	}

	/** Removes the given key from this map, if present. Returns the value associated
	 * with the given key, or null if not present
	 */
	public V remove(Object key){
		V v = forwardMap.remove(key);
		if(v != null) backMap.remove(v);
		modCount++;
		return v;
	}

	/** @see remove(Object key) */
	public V removeKey(K key){
		return remove(key);
	}

	/** Removes the given value from this map, if present. Returns the associated
	 * key, or null if value was not present.
	 */
	public K removeValue(V value){
		K k = backMap.remove(value);
		if(k != null) forwardMap.remove(k);
		modCount++;
		return k;
	}

	/** Removes all keys and values from this BiMap */
	@Override
	public void clear(){
		forwardMap.clear();
		backMap.clear();
		modCount++;
	}

	/** Returns the set of keys in this BiMap. This is a view of the map,
	 * and only one set is ever returned. Alterations in the set will
	 * cause similar alterations on the map */
	@Override
	public Set<K> keySet(){
		return keySet;
	}

	/** Returns the set of values in this BiMap. This is a view of the map,
	 * and only one set is ever returned. Alterations in the set will
	 * cause similar alterations on the map  */
	@Override
	public Set<V> values(){
		return valueSet;
	}

	/** Returns the set of entries<Key, Value> in this BiMap.
	 * This is a read-only view of the map. Changes in the returned
	 * set will not be reflected by the BiMap */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return entrySet;
	}

	/** Returns the set of entries<Value, Key> in this BiMap
	 * This is a read-only view of the map. Changes in the returned
	 * set will not be reflected by the BiMap */
	public Set<Entry<V,K>> entrySetFlipped(){
		return rEntrySet;
	}

	/** An iterator over the keys or all the values in this map.
	 * Behaves as a viewIterator - thus alterations made
	 * to this iterator will alter the BiMap 
	 * @author Mshnik
	 */
	private class SingleIterator<E,O> extends ViewIterator<E>{

		private Map<E,O> forwardMap;
		private Map<O,E> backMap;

		public SingleIterator(Map<E,O> f, Map<O,E> b) {
			super(f.keySet().iterator());
			forwardMap = f;
			backMap = b;
		}

		@Override
		public void remove(){
			if(removed) return;
			O o = forwardMap.get(current);
			iterator.remove();
			if(o != null) backMap.remove(o);
			removed = true;
		}
	}

	/** An iterator over the entry set of keys and values in this map.
	 * Behaves as a viewIterator - thus alterations made
	 * to this iterator will alter the BiMap.
	 * Notably, the entries returned by this EntryIterator do not support
	 * the setValue() method, as that could cause the BiMap invariant to be broken 
	 * @author Mshnik
	 */
	private class EntryIterator<E, O> extends ViewIterator<Entry<E,O>>{

		private Map<O,E> backMap;

		public EntryIterator(Map<E,O> f, Map<O,E> b) {
			super(f.entrySet().iterator());
			backMap = b;
		}
		
		@Override
		public Entry<E,O> next(){
			Entry<E,O> e = super.next();
			return new UnmodifiableEntry<E,O>(e.getKey(), e.getValue());
		}

		@Override
		public void remove(){
			if(removed) return;
			iterator.remove();
			backMap.remove(current.getValue());
			removed = true;
		}
	}

	/** A view of the keys in this BiMap as a set.
	 * Behaves as a viewSet - thus alterations made to this set
	 * will alter the BiMap
	 * @author Mshnik
	 */
	private class KeySet extends ViewSet<K>{

		public KeySet() {
			super(BiMap.this);
		}

		@Override
		public boolean remove(Object o) {
			return BiMap.this.remove(o) != null;
		}

		@Override
		public Iterator<K> iterator(){
			return new SmartIterator<K>(new SingleIterator<K,V>(forwardMap, backMap), () -> modCount);
		}
	}

	/** A view of the keys in this BiMap as a set.
	 * Behaves as a viewSet - thus alterations made to this set
	 * will alter the BiMap
	 * @author Mshnik
	 */
	private class ValueSet extends ViewSet<V>{

		public ValueSet() {
			super(BiMap.this);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(Object o) {
			try{
				return BiMap.this.removeValue((V)o) != null;
			}catch(ClassCastException e){
				return false;
			}
		}

		@Override
		public Iterator<V> iterator(){
			return new SmartIterator<V>(new SingleIterator<V,K>(backMap, forwardMap), () -> modCount);
		}
	}
	
	/** A view of the K,V entries in this BiMap as a set.
	 * Behaves as a viewSet - thus alterations made to this set
	 * will alter the BiMap
	 * @author Mshnik
	 */
	private class EntrySet extends ViewSet<Entry<K,V>>{

		public EntrySet() {
			super(BiMap.this);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(Object o) {
			try{
				return BiMap.this.remove( ((Entry<K,V>) o).getKey()) != null;
			}catch(ClassCastException e){
				return false;
			}
		}

		@Override
		public Iterator<Entry<K,V>> iterator(){
			return new SmartIterator<Entry<K,V>>(new EntryIterator<K,V>(forwardMap, backMap), () -> modCount);
		}
	}
	
	/** A view of the V,K entries in this BiMap as a set.
	 * Behaves as a viewSet - thus alterations made to this set
	 * will alter the BiMap
	 * @author Mshnik
	 */
	private class REntrySet extends ViewSet<Entry<V,K>>{

		public REntrySet() {
			super(BiMap.this);
		}

		@SuppressWarnings("unchecked")
		@Override
		public boolean remove(Object o) {
			try{
				return BiMap.this.remove( ((Entry<V,K>) o).getValue()) != null;
			}catch(ClassCastException e){
				return false;
			}
		}

		@Override
		public Iterator<Entry<V,K>> iterator(){
			return new SmartIterator<Entry<V,K>>(new EntryIterator<V,K>(backMap, forwardMap), () -> modCount);
		}
	}
}
