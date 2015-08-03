package common.dataStructures;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

abstract class AbsTrie<T, C> implements Set<T> {
	
	private TrieNode root;
	private int size;
	
	TrieNode getRoot(){ //exposed for testing
		return root;
	}
	
	protected AbsTrie() {
		root = new TrieNode(null, false, null);
		size = 0;
	}
	
	protected abstract Iterator<C> toSequence(T t);
	protected abstract T fromSequence(Iterator<C> iter);
	
	protected int compareC(C c1, C c2){
		throw new UnsupportedOperationException("Can't compare " + c1 + " and " + c2);
	}
	
	public boolean contains(Object o){
		try{
			@SuppressWarnings("unchecked")
			Iterator<C> iter = toSequence((T) o);
			TrieNode node = root;
			while(iter.hasNext()){
				C c = iter.next();
				if(! node.children.containsKey(c)) {
					return false;
				}
				node = node.children.get(c);
			}
			return node.isTerminatingNode;
		}catch(ClassCastException c){
			return false;
		}
	}
	
	@Override
	public boolean containsAll(Collection<?> c) {
		for(Object o : c) {
			if(! contains(o)) return false;
		}
		return true;
	}
	
	public boolean add(T t){
		Iterator<C> iter = toSequence(t);
		TrieNode node = root;
		while(iter.hasNext()) {
			C c = iter.next();
			if(! node.children.containsKey(c)) {
				node.children.put(c, new TrieNode(c, false, node));
			}
			node = node.children.get(c);
		}
		if(node.isTerminatingNode) return false;
		node.isTerminatingNode = true;
		size++;
		return true;
	}
	
	@Override
	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for(T t : c) {
			changed = add(t) | changed;
		}
		return changed;
	}
	
	public List<T> toList() {
		return root.buildList(new DeArrList<>(), new ConsList<>());
	}
	
	@Override
	public Iterator<T> iterator() {
		return toList().iterator();
	}

	@Override
	public Object[] toArray() {
		Object[] arr = new Object[size()];
		int i = 0;
		for(T t : this) {
			arr[i++] = t;
		}
		return arr;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <X> X[] toArray(X[] a) {
		X[] arr = a.length >= size() ? a : Arrays.copyOf(a, size());
		int i = 0;
		for(T t : this) {
			arr[i++] = (X)t;
		}
		return arr;
	}
	
	public boolean remove(Object o){
		try{
			@SuppressWarnings("unchecked")
			Iterator<C> iter = toSequence((T) o);
			TrieNode node = root;
			while(iter.hasNext()){
				C c = iter.next();
				if(! node.children.containsKey(c)) {
					return false;
				}
				node = node.children.get(c);
			}
			if(! node.isTerminatingNode) return false;
			node.isTerminatingNode = false;
			
			//Remove now unused nodes
			while(node.children.isEmpty()) {
				TrieNode parent = node.parent;
				parent.children.remove(node.c);
				node = parent;
			}
			size--;
			return true;
		}catch(ClassCastException c){
			return false;
		}
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Object o : c) {
			changed = remove(o) | changed;
		}
		return changed;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
	// TODO Auto-generated method stub
	return false;
	}

	@Override
	public void clear() {
		root.children.clear();
		size = 0;
	}
	
	public int size(){
		return size;
	}
	
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}
	
	class TrieNode{
		
		public final C c;
		boolean isTerminatingNode;
		HashMap<C, TrieNode> children;
		TrieNode parent;
		
		TrieNode(C c, boolean isTerminatingNode, TrieNode parent){
			this.c = c;
			this.isTerminatingNode = isTerminatingNode;
			children = new HashMap<>();
			this.parent = parent;
		}
		
		public String toString() {
			return c + "-(term=" + isTerminatingNode + ") " + children;
		}
		
		List<T> buildList(List<T> builderList, ConsList<C> prefix) {
			if(c != null) {
				prefix = prefix.cons(c);
			}
			if(isTerminatingNode) {
				builderList.add(fromSequence(prefix.reverse().iterator()));
			}
			
			List<Entry<C, TrieNode>> lst = new DeArrList<>(children.entrySet());
			try{
				Collections.sort(lst, (c1, c2) -> compareC(c1.getKey(), c2.getKey()));
			}catch(UnsupportedOperationException e){}
			
			for(Entry<C, TrieNode> entry : lst){
				builderList = entry.getValue().buildList(builderList, prefix);
			}
			
			return builderList;
		}
	}
}
