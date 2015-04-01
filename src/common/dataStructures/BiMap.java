package common.dataStructures;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

	/** Constructs a new empty BiMap */
	public BiMap(){

	}
	
	/** Constructs a new BiMap that is a copy of this BiMap */
	public BiMap<K,V> clone(){
		BiMap<K,V> b = new BiMap<K,V>();
		b.putAll(this);
		return b;
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
		//Overwridden for speed - no need to create entry set
		return forwardMap.size(); 
	}

	/** Puts the key, value pair in this BiMap. Because this is a bymap,
	 * also updates the value to point to the given key.
	 * @return the old value associated with the given key, or null if none.
	 */
	@Override
	public V put(K k, V v){
		V old = forwardMap.put(k, v);
		K old2 = backMap.put(v, k);
		if(old2 != null && ! old2.equals(k)){
			forwardMap.remove(old2);
		}
		if(old != null && ! old.equals(v)){
			backMap.remove(old);
		}
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
		return k;
	}

	/** Removes all keys and values from this BiMap */
	@Override
	public void clear(){
		forwardMap.clear();
		backMap.clear();
	}

	/** Returns the set of keys in this BiMap */
	@Override
	public Set<K> keySet(){
		return forwardMap.keySet();
	}

	/** Returns the set of values in this BiMap */
	@Override
	public Set<V> values(){
		return backMap.keySet();
	}

	/** Returns the set of entries<Key, Value> in this BiMap */
	@Override
	public Set<Entry<K, V>> entrySet() {
		return forwardMap.entrySet();
	}
	
	/** Returns the set of entries<Value, Key> in this BiMap */
	public Set<Entry<V,K>> entrySetFlipped(){
		return backMap.entrySet();
	}

	/** Returns true if O is a BiMap and they have the same entryset */
	@Override
	public boolean equals(Object o){
		try{
			@SuppressWarnings("unchecked")
			BiMap<K,V> b = (BiMap<K,V>) o;
			return entrySet().equals(b.entrySet());
		}catch(ClassCastException e){
			return false;
		}
	}
}
