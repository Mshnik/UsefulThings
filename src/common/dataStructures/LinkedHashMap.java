package common.dataStructures;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import common.dataStructures.util.ViewSet;

public class LinkedHashMap<K, V> extends AbstractMap<K,V> implements Map<K, V>, Iterable<Entry<K,V>>{

	private int size;
	private final float loadFactor;
	private Object[] vals; //Each element is a bucket, a linked list of LinkedHashEntries

	private LinkedHashEntry head;
	private LinkedHashEntry tail;
	
	protected int modCount; //Number of times this LinkedHashMap has been structurally modified

	private KeySet keySet;
	private ValueCollection values; //not actually a set
	private EntrySet entrySet;
	
	private static final int DEFAULT_SIZE = 16;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private class LinkedHashEntry implements Entry<K,V>{

		private final K key;
		private V val;
		
		LinkedHashEntry next; // The next hashEntry in the chain

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

		//Check for updating currently present key
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
		
		//Fix pointers
		if(size == 0){
			head = e;
			tail = e;
		} else {
			tail.next = e;
			tail = e;
		}
		
		size++;
		modCount++;
		return null; //Return null is correct here - no previous mapping
	}
	
	/** Adds the given entry to the end of this LinkedHashMap
	 * iff the key for the entry isn't already in the map
	 * @param e - the entry to add
	 * @return - true iff the entry was added this way
	 */
	public boolean add(Entry<K, V> e) {
		if(containsKey(e.getKey())) return false;
		put(e.getKey(), e.getValue());
		return true;
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
	
	private V safeRemove(K key){
		V v = remove(key);
		if(v != null) modCount--;
		return v;
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
	
	/** Returns an iterator over the entries in this LinkedHashMap */
	public Iterator<Entry<K, V>> iterator() {
		return entrySet.iterator();
	}
	
	/** Returns an array of Entries, in the iteration order of this LinkedHashMap */
	public Object[] toArray() {
		Object[] o = new Object[size()];
		
		int i = 0;
		for(Entry<K,V> e : this){
			o[i] = e;
			i++;
		}
		
		return o;
	}

	@SuppressWarnings("unchecked")
	public <T> T[] toArray(T[] a) throws ClassCastException {
		T[] arr = a.length >= size() ? a : Arrays.copyOf(a, size());
		
		int i = 0;
		for(Entry<K,V> e : this){
			arr[i] = (T)e;
			i++;
		}
		
		return arr;
	}

	private abstract class HashIterator<E> implements Iterator<E>{
		private int expectedModCount;
		private boolean removed;
		
		private LinkedHashEntry current;

		public HashIterator(){
			removed = false;
			expectedModCount = LinkedHashMap.this.modCount;
			current = head;
		}

		@Override
		public boolean hasNext() {
			return current != null;
		}

		protected abstract E valFromEntry(Entry<K,V> e);
		
		@Override
		public E next() {
			if(expectedModCount != modCount)
				throw new ConcurrentModificationException();
			
			Entry<K,V> e = current;
			removed = false;
			current = current.next;
			return valFromEntry(e);
		}
		
		@Override
		public void remove(){
			if(removed) return;
			
			safeRemove(current.key);
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

	public boolean addAll(Collection<? extends Entry<K, V>> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean addAll(int index, Collection<? extends Entry<K, V>> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean retainAll(Collection<?> c) {
		// TODO Auto-generated method stub
		return false;
	}

	public Entry<K, V> get(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public Entry<K, V> set(int index, Entry<K, V> element) {
		// TODO Auto-generated method stub
		return null;
	}

	public void add(int index, Entry<K, V> element) {
		// TODO Auto-generated method stub
		
	}

	public Entry<K, V> remove(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public int indexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public int lastIndexOf(Object o) {
		// TODO Auto-generated method stub
		return 0;
	}

	public ListIterator<Entry<K, V>> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	public ListIterator<Entry<K, V>> listIterator(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Entry<K, V>> subList(int fromIndex, int toIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
