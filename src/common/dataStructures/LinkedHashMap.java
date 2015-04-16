package common.dataStructures;

import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import common.dataStructures.util.ViewSet;

public class LinkedHashMap<K, V> implements Map<K, V> {

	private int size;
	private final float loadFactor;
	private Object[] vals; //Each element is a bucket, a linked list of LinkedHashEntries

	protected int modCount; //Number of times this LinkedHashMap has been structurally modified

	private KeySet keySet;
	private ValueCollection values; //not actually a set
	private EntrySet entrySet;
	
	private static final int DEFAULT_SIZE = 16;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

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

		@Override
		public boolean equals(Object o){
			try{
				@SuppressWarnings("unchecked")
				LinkedHashEntry e = (LinkedHashEntry)o;

				return key.equals(e.key) && Objects.equals(val, e.val);
			}catch(ClassCastException e){
				return false;
			}
		}

		@Override
		public int hashCode(){
			return Objects.hash(key, val);
		}

		@Override
		public String toString(){
			return key + "=" + val;
		}
	}

	public LinkedHashMap(){
		this(DEFAULT_SIZE, DEFAULT_LOAD_FACTOR);
	}

	public LinkedHashMap(int initalCapacity, float loadFactor){
		vals = new Object[initalCapacity];
		size = 0;
		this.loadFactor = loadFactor;
		keySet = new KeySet();
		values = new ValueCollection();
		entrySet = new EntrySet();
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

	@Override
	public String toString(){
		String s = "{";
		for(Object o : vals){
			s += o.toString();
		}
		return s + "}";
	}

	@SuppressWarnings("unchecked")
	/** Returns the bucket at the given index
	 * @param index - the index to get a bucket at. Shouldn't be out of vals bounds
	 * @return - the bucket at the given index. Will never be null.
	 */
	private LinkedList<LinkedHashEntry> getBucketAt(int index){
		return (LinkedList<LinkedHashEntry>) vals[index];
	}

	/** Returns the bucket associated with the given key, for the 
	 * current length of vals. Hashes the key, mods by length, and finds that indexed bucket.
	 * @param key - the key to hash
	 * @return - the associated bucket. Will never be null.
	 */
	private LinkedList<LinkedHashEntry> getBucketFor(Object key){
		return getBucketAt(key.hashCode() % vals.length);
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
		boolean rehashed = false;
		if(size() >= loadFactor * vals.length){
			LinkedList<LinkedHashEntry> temp = new LinkedList<>();
			for(Object b : vals){
				@SuppressWarnings("unchecked")
				LinkedList<LinkedHashEntry> l = (LinkedList<LinkedHashEntry>)b;
				temp.addAll(l);
			}
			vals = new Object[vals.length * 2 + 1]; //Keep odd
			clear();

			for(LinkedHashEntry e : temp){
				LinkedList<LinkedHashEntry> bucket2 = getBucketFor(e.key);
				bucket2.add(e);
				size++;
			}
			rehashed = true;
		}

		if(rehashed) bucket = getBucketFor(key);

		LinkedHashEntry e = new LinkedHashEntry(key, value);
		bucket.add(e);
		size++;
		modCount++;
		return null; //Return null is correct here - no previous mapping
	}

	@Override
	public V remove(Object key) {
		LinkedList<LinkedHashEntry> bucket = getBucketFor(key);

		for(LinkedHashEntry e : bucket){
			if(e.getKey().equals(key)){
				V oldVal = e.getValue();
				bucket.remove(e);
				size--;
				modCount++;
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
		modCount++;
		for(int i = 0; i < vals.length; i++){
			if(vals[i] != null) 
				((LinkedList<?>)vals[i]).clear();
			else 
				vals[i] = new LinkedList<LinkedHashEntry>();
		}
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

	private abstract class HashIterator<E> implements Iterator<E>{
		private int index; //bucket this is on
		private int expectedModCount;
		private boolean removed;
		private boolean hasNext;
		private Iterator<LinkedHashEntry> bucket;

		public HashIterator(){
			index = 0;
			removed = false;
			expectedModCount = LinkedHashMap.this.modCount;
			moveToNextBucket();
		}
		
		private void moveToNextBucket(){
			while(vals[index] == null || ((List<?>)vals[index]).size() == 0) index++;
			if(index < vals.length){
				bucket = getBucketAt(index).iterator();
				hasNext = true;
			} else{
				hasNext = false;
			}
		}

		@Override
		public boolean hasNext() {
			return hasNext;
		}

		protected abstract E valFromEntry(Entry<K,V> e);
		
		@Override
		public E next() {
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			Entry<K,V> e = bucket.next();

			removed = false;
			
			//Figure out next bucket
			if(! bucket.hasNext()){
				index++;
				moveToNextBucket();
			}
			return valFromEntry(e);
		}
		
		@Override
		public void remove(){
			if(removed) return;
			
			bucket.remove();
			expectedModCount++;
			removed = true;
		}
	}

	private class KeySet extends ViewSet<K>{
		public KeySet() {
			super(LinkedHashMap.this);
		}

		@Override
		public boolean remove(Object o) {
			return LinkedHashMap.this.remove(o) != null;
		}

		@Override
		public Iterator<K> iterator() {
			return new HashIterator<K>(){
				@Override
				protected K valFromEntry(Entry<K, V> e) {
					return e.getKey();
				}
			};
		}
	}
	
	private class ValueCollection extends ViewSet<V>{
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
			return new HashIterator<V>(){
				@Override
				protected V valFromEntry(Entry<K, V> e) {
					return e.getValue();
				}
			};
		}
	}
	
	private class EntrySet extends ViewSet<Entry<K,V>>{
		public EntrySet() {
			super(LinkedHashMap.this);
		}

		@Override
		public boolean remove(Object o) {
			try{
				return LinkedHashMap.this.remove( ((Entry<?,?>)o).getKey()) != null;
			}catch(ClassCastException e){
				return false;
			}
		}

		@Override
		public Iterator<Entry<K,V>> iterator() {
			return new HashIterator<Entry<K,V>>(){
				@Override
				protected Entry<K,V> valFromEntry(Entry<K, V> e) {
					return e;
				}
			};
		}
	}

	

}
