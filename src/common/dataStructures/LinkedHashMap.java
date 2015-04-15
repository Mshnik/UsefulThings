package common.dataStructures;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.AbstractMap;
import java.util.Objects;
import java.util.Set;

public class LinkedHashMap<K, V> implements Map<K, V> {

	private int size;
	private Object[] vals; //Each element is a bucket, a linked list of LinkedHashEntries

	private static final int DEFAULT_SIZE = 16;

	private class LinkedHashEntry implements Entry<K,V>{

		private final K key;
		private V val;

		LinkedHashEntry(K k, V v) throws IllegalArgumentException{
			if(k == null)
				throw new IllegalArgumentException("Null keys not allowed");
			key = k;
			val = v;
		}

		@Override
		public K getKey() {
			return key;
		}

		@Override
		public V getValue() {
			return val;
		}

		@Override
		public V setValue(V value) {
			V oldVal = val;
			val = value;
			return oldVal;
		}

	}

	public LinkedHashMap(){
		vals = new Object[DEFAULT_SIZE];
		size = 0;
		clear(); //Inits buckets
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public boolean isEmpty() {
		return size() != 0;
	}
	
	@SuppressWarnings("unchecked")
	/** Returns the bucket associated with the given key, for the 
	 * current length of vals. Hashes the key, mods by length, and finds that indexed bucket.
	 * @param key - the key to hash
	 * @return - the associated bucket. Will never be null.
	 */
	private LinkedList<LinkedHashEntry> getBucketFor(Object key){
		return (LinkedList<LinkedHashEntry>) vals[key.hashCode() % vals.length];
	}

	@Override
	public boolean containsKey(Object key) {
		LinkedList<LinkedHashEntry> bucket = getBucketFor(key);
		for(LinkedHashEntry e : bucket){
			if(e.getKey().equals(key)){
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		for(V v : values()){
			if(Objects.equals(v, value)) return true;
		}
		return false;
	}

	@Override
	public V get(Object key) {
		LinkedList<LinkedHashEntry> bucket = getBucketFor(key);
		for(LinkedHashEntry e : bucket){
			if(e.getKey().equals(key)){
				return e.getValue();
			}
		}
		return null;
	}

	@Override
	public V put(K key, V value) {
		LinkedList<LinkedHashEntry> bucket = getBucketFor(key);

		for(LinkedHashEntry e : bucket){
			if(e.getKey().equals(key)){
				V oldVal = e.getValue();
				e.setValue(value);
				return oldVal;
			}
		}
		
		//Check for rehash
		//TODO
		
		bucket.add(new LinkedHashEntry(key, value));
		size++;
		return null; //Return null is correct here - no previous mapping
	}

	@Override
	public V remove(Object key) {
		LinkedList<LinkedHashEntry> bucket = getBucketFor(key);

		for(LinkedHashEntry e : bucket){
			if(e.getKey().equals(key)){
				V oldVal = e.getValue();
				bucket.remove(e);
				return oldVal;
			}
		}
		return null;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("Can't put all of an unordered Map");
	}

	@Override
	public void clear() {
		size = 0;
		for(int i = 0; i < vals.length; i++){
			if(vals[i] != null) 
				((LinkedList<?>)vals[i]).clear();
			else 
				vals[i] = new LinkedList<LinkedHashEntry>();
		}
	}

	@Override
	public Set<K> keySet() {
		
		
		
		return null;
	}

	@Override
	public Collection<V> values() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}

}
