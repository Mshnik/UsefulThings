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

public class LinkedHashMap<K, V> extends AbstractMap<K,V> implements Map<K, V>, Iterable<Entry<K,V>>{

	private HashMap<K,LinkedHashEntry> map;

	private LinkedHashEntry head;
	private LinkedHashEntry tail;
	
	protected int modCount; //Number of times this LinkedHashMap has been structurally modified

	private KeySet keySet;
	private ValueCollection values; //not actually a set
	private EntrySet entrySet;
	
	private static final int DEFAULT_SIZE = 16;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;

	private class LinkedHashEntry implements Entry<K,V>{

		private K key;
		private V val;
		
		LinkedHashEntry next; // The next hashEntry in the chain
		LinkedHashEntry prev;

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
	public boolean isEmpty() {
		return size() != 0;
	}
	
	@Override
	public String toString(){
		if(size() == 0) return "{}";
		String s = "{";
		for(Entry<K,V> e : this){
			s += e.getKey() + "=" + e.getValue() + ", ";
		}
		return s.substring(0, s.length() - 2) + "}";
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		for(V v : values()){
			if(Objects.equals(v,value)) return true;
		}
		return false;
	}

	private LinkedHashEntry getEntry(K key){
		return map.get(key);
	}
	
	@Override
	public V get(Object key) {
		if(! map.containsKey(key)) return null;
		return map.get(key).val;
	}
	
	public Entry<K, V> get(int index) {
		if(index < 0 || index >= size())
			throw new IllegalArgumentException(index + " is OOB for " + this);
		
		LinkedHashEntry current = head;
		while(index > 0){
			current = current.next;
			index--;
		}
		
		return current;
	}

	public Entry<K,V> getFirst(){
		return get(0);
	}
	
	public Entry<K,V> getLast(){
		return get(size() - 1);
	}
	
	public int indexOf(Object o) {
		LinkedHashEntry current = head;
		int i = 0;
		while(current != null){
			if(Objects.equals(current, o) || Objects.equals(current.key, o) || Objects.equals(current.val, o))
				return i;
			current = current.next;
			i++;
		}
		return -1;
	}

	public int lastIndexOf(Object o) {
		LinkedHashEntry current = tail;
		int i = 0;
		while(current != null){
			if(Objects.equals(current, o) || Objects.equals(current.key, o) || Objects.equals(current.val, o))
				return i;
			current = current.prev;
			i++;
		}
		return -1;
	}
	
	private V putHelper(K key, V value, int index){
		if(index < 0 || index > size())
			throw new IllegalArgumentException("Can't put;" + index + " is OOB for " + this);
				
		if(containsKey(key)){
			V oldVal = getEntry(key).val;
			getEntry(key).val = value;
			return oldVal;
		} else {
			LinkedHashEntry e = new LinkedHashEntry(key, value);			
			if(size() == 0){
				head = e;
				tail = e;
			} else if(index == size()){
				tail.next = e;
				e.prev = tail;
				tail = e;
			} else if(index == 0){
				head.prev = e;
				e.next = head;
				head = e;
			} else {
				LinkedHashEntry e2 = (LinkedHashEntry)get(index);
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
	
	public V putLast(K key, V value){
		return putAt(key, value, size());
	}
	
	public V putFirst(K key, V value){
		return putAt(key, value, 0);
	}
	
	public V putAt(K key, V value, int index){
		if(containsKey(key))
			throw new RuntimeException("Can't call putAt on an already existing key");
		return putHelper(key, value, index);
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		throw new UnsupportedOperationException("Can't put all of an unordered Map");
	}
	
	/** Adds the given entry to the end of this LinkedHashMap
	 * iff the key for the entry isn't already in the map
	 * @param e - the entry to add
	 * @return - true iff the entry was added this way
	 */
	public boolean add(Entry<K, V> e) {
		if(containsKey(e.getKey())) return false;
		putLast(e.getKey(), e.getValue());
		return true;
	}
	
	public boolean add(int index, Entry<K, V> element) {
		if(containsKey(element.getKey())) return false;
		putAt(element.getKey(), element.getValue(), index);
		return true;
	}

	public boolean addAll(Collection<? extends Entry<K, V>> c) {
		boolean altered = false;
		for(Entry<K,V> e : c){
			altered = add(e) || altered;
		}
		return altered;
	}

	public boolean addAll(int index, Collection<? extends Entry<K, V>> c) {
		boolean altered = false;
		for(Entry<K,V> e : c){
			altered = add(index, e) || altered;
			index++;
		}
		return altered;
	}
	
	public Entry<K, V> set(int index, Entry<K, V> element) {
		if(containsKey(element.getKey()))
			throw new IllegalArgumentException("Can't call set with already present key " + element.getKey());
		
		LinkedHashEntry old = (LinkedHashEntry)get(index);
		LinkedHashEntry oldCopy = new LinkedHashEntry(old.key, old.val);
		
		old.key = element.getKey();
		old.val = element.getValue();
		
		return oldCopy;
	}

	@Override
	public V remove(Object key) {

		LinkedHashEntry entry = map.get(key);
		if(entry == null) return null;
		
		LinkedHashEntry e0 = entry.prev;
		LinkedHashEntry e1 = entry.next;
		
		if(e0 != null){
			e0.next = e1;
		}
		if(e1 != null){
			e1.prev = e0;
		}
		if(entry == head){
			head = e1;
		}
		if(entry == tail){
			tail = e0;
		}
		
		map.remove(key);
		modCount++;
		
		return entry.val;
	}
	
	public Entry<K, V> remove(int index) {
		Entry<K,V> e = get(index);
		remove(e.getKey());
		return e;
	}

	public boolean removeAll(Collection<?> c) {
		boolean altered = false;
		for(Object o : c){
			altered = remove(o) != null || altered;
		}
		return altered;
	}

	public boolean retainAll(Collection<?> c) {
		Iterator<K> iter = keySet.iterator();
		boolean altered = false;
		while(iter.hasNext()){
			K k = iter.next();
			if(! c.contains(k)){
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
			
			LinkedHashMap.this.remove(current.key);
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
