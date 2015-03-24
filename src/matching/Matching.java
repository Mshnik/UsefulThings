package matching;

import java.util.*;

import common.dataStructures.BiMap;
import common.types.*;

public class Matching<A,B> {

	private HashSet<A> aObjects;
	private HashSet<B> bObjects;
	private BiMap<A,B> matching;

	public Matching(){
		aObjects = new HashSet<>();
		bObjects = new HashSet<>();
		matching = new BiMap<>(); //Can only contain pairs from aObjects and bObjects
	}
	
	public boolean addA(A a){
		if(aObjects.contains(a)) return false;
		aObjects.add(a);
		return true;
	}
	
	public boolean addAllA(Collection<? extends A> colA){
		boolean changed = false;
		for(A a : colA){
			changed = addA(a) | changed;
		}
		return changed;
	}
	
	public boolean addB(B b){
		if(bObjects.contains(b)) return false;
		bObjects.add(b);
		return true;
	}
	
	public boolean addAllB(Collection<? extends B> colB){
		boolean changed = false;
		for(B b : colB){
			changed = addB(b) | changed;
		}
		return changed;
	}
	
	/** Returns true iff o is currently matched in this matching */
	public boolean isMatched(Object o){
		return matching.containsKey(o) || matching.containsValue(o);
	}
	
	/** Returns true iff o is currently unmatched in this matching, but is
	 * a valid object in this matching.
	 */
	public boolean isUnmatched(Object o){
		return (aObjects.contains(o) || bObjects.contains(o)) && ! isMatched(o);
	}
	
	public A getMatchedA(B b){
		return matching.getKey(b);
	}
	
	public B getMatchedB(A a){
		return matching.getValue(a);
	}
	
	public boolean match(A a, B b){		
		if(! aObjects.contains(a) || ! bObjects.contains(b))
			return false;
		matching.put(a, b);
		return true;
	}
	
	public Map<A,B> getMatching(){
		return matching.toMap();
	}
	
	public Map<B,A> getFlippedMatching(){
		return matching.toFlippedMap();
	}
	
	public HashSet<Either<A,B>> getMatched(){
		HashSet<Either<A,B>> obj = new HashSet<>();
		for(A a : aObjects){
			if(matching.containsKey(a)){
				obj.add(new Left<A,B>(a));
			}
		}
		for(B b : bObjects){
			if(matching.containsKey(b)){
				obj.add(new Right<A,B>(b));
			}
		}
		return obj;
	}
	
	public HashSet<Either<A,B>> getUnmatched(){
		HashSet<Either<A,B>> obj = new HashSet<>();
		for(A a : aObjects){
			if(! matching.containsKey(a)){
				obj.add(new Left<A,B>(a));
			}
		}
		for(B b : bObjects){
			if(! matching.containsKey(b)){
				obj.add(new Right<A,B>(b));
			}
		}
		return obj;
	}
}
