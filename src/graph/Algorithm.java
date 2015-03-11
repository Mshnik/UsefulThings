package graph;

import graph.Graph.NotInGraphException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import common.Util;
import common.tuple.Tuple2;

/** Holder class for various algorithms for graphs **/
public class Algorithm {

	/** Returns the sum of the weights for the given path in the given graph.
	 * @throws an NotInGraphException if the path makes an illegal jump -
	 * there is no such edge to travel
	 * @return - 0 if path == null or path.size() < 2, the sum of the edge weights otherwise
	 */
	public static <V, E extends Weighted> int sumPathWeight(Graph<V, E> g, LinkedList<V> path)
			throws NotInGraphException{
		if(path == null || path.size() < 2) return 0;

		int s = 0;
		Iterator<V> one = path.iterator();
		Iterator<V> two = path.iterator();
		two.next(); //Advance two forward one.
		while(two.hasNext()){
			V v1 = one.next();
			V v2 = two.next();
			E e = g.getConnection(v1, v2);
			if(e == null)
				throw new NotInGraphException("Can't get cost of " + v1 + " to " + v2, e);
			s += e.getWeight();
		}

		return s;
	}

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

	/** Returns true if the given graph is a DAG, using a topological sort */
	public static <V,E> boolean isDAG(Graph<V,E> g){
		if(! g.isDirected()) return false;

		final Graph<V,E> g2 = g.clone(); //create modifiable copy

		Comparator<V> minDegreeComparator = new Comparator<V>(){
			public int compare(V o1, V o2) {
				return g2.inDegreeOf(o1) - g2.inDegreeOf(o2);
			}
		};

		ArrayList<V> arrLst = new ArrayList<V>();
		arrLst.addAll(g2.vertexSet());

		while(g2.vertexSize() > 0){
			V minV = Collections.min(arrLst, minDegreeComparator);

			//If minimum inDegree has in degree > 0, there is a cycle.
			if(g2.inDegreeOf(minV) > 0) return false;

			//minimum inDegree is 0 - remove from arrayList and from actual graph
			arrLst.remove(minV);
			g2.removeVertex(minV);
		}
		return true;
	}

	/** Returns true if the given graph is Bipartite */
	public static <V,E> boolean isBipartite(Graph<V,E> g){		
		HashSet<V> sideA = new HashSet<V>();
		HashSet<V> sideB = new HashSet<V>();

		HashSet<V> allVertices = new HashSet<V>(g.vertexSet());

		LinkedList<V> queue = new LinkedList<V>();

		while(! allVertices.isEmpty()){

			queue.clear();
			sideA.clear();
			sideB.clear();
			queue.add(Util.randomElement(allVertices));
			sideA.add(queue.getFirst());

			while(! queue.isEmpty()){
				V v = queue.poll();
				allVertices.remove(v);

				HashSet<V> side;
				HashSet<V> otherSide;
				if(sideA.contains(v)){
					side = sideA;
					otherSide = sideB;
				} else{
					side = sideB;
					otherSide = sideA;
				}

				for(V neighbor : g.neighborsOf(v)){
					if(side.contains(neighbor)){
						return false;
					}
					if(! otherSide.contains(neighbor)){
						queue.addFirst(neighbor);
						otherSide.add(neighbor);
					}
				}
			}
		}
		return true;
	}
	
	/** An instance represents a flow on a graph with edge type E
	 * The first value is the total flow, the second is a map of each
	 * edge in the graph to the amount of flow on that edge
	 * @author Mshnik
	 */
	public static class Flow<E> extends Tuple2<Integer, HashMap<E, Integer>>{
		public Flow(Integer first, HashMap<E, Integer> second) {
			super(first, second);
		}		
	}
	
	/** An instance is created to perform a maxflow calculation.
	 * This simplifies passing around the various hashMaps used in the calculation
	 */
	private static class MaxFlow<V, E extends Flowable>{
		private HashMap<V, Integer> label;
		private HashMap<E, Integer> flow;
		private HashMap<E, Integer> residualFlow;
		private HashMap<V, Integer> excess;
		private final Graph<V,E> g;
		private final V source;
		private final V sink;
		
		public MaxFlow(Graph<V,E> g, V source, V sink){
			this.g = g;
			this.source = source;
			this.sink = sink;
			
			//Initialize maps
		}
		
		/** Helper method for the maxFlow calculation. */
		private void push(V u, V v){
			E e = g.getConnection(u, v);
			assert(excess.get(u) > 0 && label.get(u) == label.get(v) + 1);
			int delta = Math.min(excess.get(u), e.getCapacity() - flow.get(e));
			flow.put(e, flow.get(e) + delta);
			residualFlow.put(e, residualFlow.get(e) - delta);
			excess.put(u, excess.get(u) - delta);
			excess.put(v, excess.get(v) + delta);
		}
		
		private void relabel(V u){
			assert(excess.get(u) > 0);
			int minVal = Integer.MAX_VALUE;
			for(V v : g.vertexSet()){
				E e = g.getConnection(u,v);
				if(e != null && flow.get(e) < e.getCapacity()){
					assert(label.get(u) <= label.get(v));
					minVal = Math.min(minVal, label.get(v));
				}
			}
			label.put(u, minVal + 1);
		}
		
		private Flow<E> computeMaxFlow(){
			return null;
		}
	}
	
	/** Returns the maximum flow possible on graph g.
	 * Uses the preflow push algorithm to compute the max flow
	 * @param g - the graph to find the flow on
	 * @param source - the vertex to treat as the source of all flow
	 * @param sink - the vertex to treat as the sink of all flow
	 * @return - a flow object, contianing the value of the max flow and the placement of all flow
	 */
	public static <V, E extends Flowable> Flow<E> maxFlow(Graph<V,E> g, V source, V sink){
		return new MaxFlow<V,E>(g, source, sink).computeMaxFlow();
	}
	

	
}
