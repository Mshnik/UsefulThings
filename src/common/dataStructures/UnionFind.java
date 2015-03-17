package common.dataStructures;

import java.util.HashMap;
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
	}
	
	private HashMap<E, Node> elms;
	
	public UnionFind(Set<E> elms){
		this.elms = new HashMap<E, Node>();
		for(E e : elms){
			this.elms.put(e, new Node(e));
		}
	}
	
	public E find(E elm) throws NotInCollectionException{
		if(! elms.containsKey(elm)) throw new NotInCollectionException("Can't find ", elm);
		return find(elms.get(elm));
	}
	
	private E find(Node n){
		if(n.parent == n)
			return n.val;
		return find(n.parent);
	}
	
	public void union(E elm1, E elm2){
		if(! elms.containsKey(elm1) || ! elms.containsKey(elm2)) 
			throw new NotInCollectionException("Can't union ", elm1, elm2);
		
		Node p1 = elms.get(find(elm1)); //parent of elm1
		Node p2 = elms.get(find(elm2)); //parent of elm2
		
		if(p1 == p2) return;
		
		if(p1.size < p2.size){
			p1.parent = p2;
			p2.size += p1.size;
		} else{
			p2.parent = p1;
			p1.size += p2.size;
		}
	}
	
}
