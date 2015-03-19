package common.dataStructures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UnionFind<E> {

	private class Node{
		private Node parent;
		private E val;
		private int size;
		
		private Node(E val){
			this.val = val;
			parent = this;
			size = 1;
		}
		
		public String toString(){
			return parent == this ? val.toString() : val + "->" + parent.toString();
		}
	}
	
	private HashMap<E, Node> elms;
	
	public UnionFind(){
		this.elms = new HashMap<E, Node>();
	}
	
	public UnionFind(Set<E> elms){
		this();
		for(E e : elms){
			add(e);
		}
	}
	
	public boolean add(E e){
		if(elms.containsKey(e))
			return false;
		elms.put(e, new Node(e));
		return true;
	}
	
	public Set<E> toSet(){
		return new HashSet<E>(elms.keySet());
	}
	
	public E find(E elm) throws NotInCollectionException{
		if(! elms.containsKey(elm)) throw new NotInCollectionException("Can't find ", elm);
		return find(elms.get(elm));
	}
	
	private E find(Node n){
		if(n.parent == n)
			return n.val;
		E val = find(n.parent);
		
		//Compress
		n.parent = elms.get(val);
		n.size = 1;
		
		return val;
	}
	
	public int size(E elm) throws NotInCollectionException{
		if(! elms.containsKey(elm)) 
			throw new NotInCollectionException("Can't get size of ", elm);
		return elms.get(elm).size;
	}
	
	public E union(E elm1, E elm2) throws NotInCollectionException{
		if(! elms.containsKey(elm1) || ! elms.containsKey(elm2)) 
			throw new NotInCollectionException("Can't union ", elm1, elm2);
		
		Node p1 = elms.get(find(elm1)); //parent of elm1
		Node p2 = elms.get(find(elm2)); //parent of elm2
		
		if(p1 == p2) return p1.val;
		
		if(p1.size < p2.size){
			p1.parent = p2;
			p2.size += p1.size;
			return p2.val;
		} else{
			p2.parent = p1;
			p1.size += p2.size;
			return p1.val;
		}
	}
	
}
