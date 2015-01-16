package matching;

import java.util.*;

public class Matching {

	private HashSet<MatchObject> objects;
	private HashMap<MatchObject, MatchObject> matching;

	public Matching(){
		objects = new HashSet<>();
		matching = new HashMap<>();
	}
	
	public boolean add(MatchObject o){
		if(objects.contains(o)) return false;
		objects.add(o);
		return true;
	}
	
	public boolean isMatched(MatchObject o){
		return matching.containsKey(o);
	}
	
	public MatchObject getMatch(MatchObject o){
		return matching.get(o);
	}
	
	public boolean match(MatchObject o, MatchObject p){
		if(o.type.equals(p.type)) return false;
		
		add(o);
		add(p);
		if(matching.containsKey(o))
			matching.remove(matching.get(o));
		if(matching.containsKey(p))
			matching.remove(matching.get(p));
		
		matching.put(o, p);
		matching.put(p, o);
		
		return true;
	}
	
	public HashSet<Object> getMatched(){
		HashSet<Object> obj = new HashSet<>();
		for(MatchObject o : objects){
			if(matching.containsKey(o))
				obj.add(o);
		}
		return obj;
	}
	
	public HashSet<Object> getUnmatched(){
		HashSet<Object> obj = new HashSet<>();
		for(MatchObject o : objects){
			if(! matching.containsKey(o))
				obj.add(o);
		}
		return obj;
	}
	
	
}
