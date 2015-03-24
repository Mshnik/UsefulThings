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
		matching = new BiMap<>();
	}
	
	public boolean addA(A a){
		if(aObjects.contains(a)) return false;
		aObjects.add(a);
		return true;
	}
	
	public boolean addB(B b){
		if(bObjects.contains(b)) return false;
		bObjects.add(b);
		return true;
	}
	
	public boolean isMatched(Object o){
		return matching.containsKey(o) || matching.containsValue(o);
	}
	
	public A getMatchedA(B b){
		return matching.getKey(b);
	}
	
	public B getMatchedB(A a){
		return matching.getValue(a);
	}
	
	public boolean match(A a, B b){		
		addA(a);
		addB(b);
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
