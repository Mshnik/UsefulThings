package graph;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/** Holder class for various algorithms for graphs **/
public class Algorithm {

	/** Attempts to find the shortest path in g from start to goal
	 * @param start - a vertex in g, the start of the path
	 * @param goal - a vertex in g, the end of the path
	 * @return - the path as a list, where return[0] is start and return[last] is goal.
	 *           returns null if start or goal isn't in g, or there is no such path.
	 */
	public static <V, E extends Weighted> LinkedList<V> dijkstra(Graph<V, E> g, V start, V goal){
		for(E e : g.edgeSet()){
			if(e.getWeight() <= 0)
				throw new RuntimeException("Can't run dijkstra's algorithm on graph with non-positive weights");
		}
		
		final HashMap<V, Integer> distance = new HashMap<>();
		Comparator<V> distanceComparator = new Comparator<V>(){
			@Override
			public int compare(V o1, V o2) {
				return distance.get(o1) - distance.get(o2);
			}
		};
		
		final HashMap<V, V> previous = new HashMap<>();
		
		//Initalize step
		for(V v : g.vertexSet()){
			distance.put(v, Integer.MAX_VALUE);
			previous.put(v, null);
		}
		
		distance.put(start, 0);
		HashSet<V> frontier = new HashSet<V>();
		
		frontier.add(start);
		
		//Iteration
		do{
			//Find next node (closest) and remove from frontier
			V next = Collections.min(frontier, distanceComparator);
			frontier.remove(next);
			//Found the goal - break out of loop
			if(next.equals(goal))
				break;
			
			//Look at each edge leaving next
			for(E e : g.edgeSetOfSource(next)){
				V neighbor = g.getOther(e, next);
				//Relax edge - check if shortest path to neighbor is beaten by next -> neighbor.
				if(distance.get(neighbor) > distance.get(next) + e.getWeight()){
					distance.put(neighbor, distance.get(next) + e.getWeight());
					previous.put(neighbor, next);
					if(! frontier.contains(neighbor))
						frontier.add(neighbor);
				}
			}
			
		}while(! frontier.isEmpty());
		
		//If the frontier is empty, then the goal was never found.
		if(frontier.isEmpty()){
			return null;
		}
		
		//Assemble path
		LinkedList<V> path = new LinkedList<V>();
		V v = goal;
		do{
			path.push(v);
			v = previous.get(v);
		}while(v != null);
		
		return path;
	}
}
