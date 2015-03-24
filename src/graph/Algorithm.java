package graph;

import graph.matching.Agent;
import graph.matching.Matching;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import common.Util;
import common.types.Tuple2;
import common.dataStructures.ConsList;
import common.dataStructures.NotInCollectionException;
import common.dataStructures.UnionFind;

/** Holder class for various algorithms for graphs and matching **/
public class Algorithm {

	/** Prevent construction on class MAlgorithm */
	private Algorithm(){}
	
	/** Returns the sum of the weights for the given path in the given graph.
	 * @throws an NotInCollectionException if the path makes an illegal jump -
	 * there is no such edge to travel
	 * @return - 0 if path == null or path.size() < 2, the sum of the edge weights otherwise
	 */
	public static <V, E extends Weighted> int sumPathWeight(Graph<V, E> g, LinkedList<V> path)
			throws NotInCollectionException{
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
				throw new NotInCollectionException("Can't get cost of " + v1 + " to " + v2, e);
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
	public static <V, E extends Weighted> LinkedList<V> dijkstra(Graph<V, E> g, V start, V goal)
			throws RuntimeException, NotInCollectionException{
		return null;
	}

	/** Returns an arbitrary cycle in the graph. If none are present,
	 * returns null. This method is well slower than the isDAG method,
	 * so if cycle detection (instead of identity) is desired and the graph
	 * is directed, use that method.
	 */
	public static <V,E> List<E> getCycle(Graph<V,E> g){
		//Check for self loops
		for(E e : g.edgeSet()){
			if(g.isSelfEdge(e)){
				 List<E> lst = new ArrayList<E>();
				 lst.add(e);
				 return lst;
			}
		}
		
		//No self loops at this point
		
		if(g.isDirected()){
			//Use DFS method
		} else{
			//Use Union-Find method
			UnionFind<V> uf = new UnionFind<V>(g.vertexSet());
			HashMap<V,E> childToParentEdge = new HashMap<>();
			
			for(E e : g.edgeSet()){
				V v1 = g.sourceOf(e);
				V v2 = g.sinkOf(e);
				if(uf.find(v1).equals(uf.find(v2))){
					//Found a cycle. Package and return. Start of cycle is v1.
					ArrayList<E> cycle = new ArrayList<E>();
					E current = e;
					while(! g.isEndpointOf(current, v1) || g.isEndpointOf(current, v2)){
						cycle.add(current);
						current = childToParentEdge.get(current); //TODO
					}
					return cycle;
				}
				//We know that by calling union here v1 and v2 weren't connected before
				V parent = uf.union(v1, v2);
				V child = parent == v1 ? v2 : v1;
				childToParentEdge.put(child, e);
			}
			
			//No cycle found
			return null;
		}
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
		private HashMap<V, Integer> excess;
		private final V source;
		private final V sink;
		private final Graph<V,E> g;
		private final Flow<E> flowObj;

		public MaxFlow(Graph<V,E> g, V source, V sink){
			this.g = g;
			this.source = source;
			this.sink = sink;

			label = new HashMap<V, Integer>();
			excess = new HashMap<V, Integer>();
			flow = new HashMap<E, Integer>();

			for(V v : g.vertexSet()){
				label.put(v, 0);
				excess.put(v,0);
				for(V v2 : g.vertexSet()){
					E e = g.getConnection(v, v2);
					if(e != null){
						flow.put(e, 0);
					}
				}
			}

			//Label for start node is number of nodes
			label.put(source, g.vertexSize());

			//Assemble preflows for edges leaving source
			for(E e : g.edgeSetOfSource(source)){
				if(! g.isSelfEdge(e)){
					V edgeEnd = g.getOther(e, source);
					excess.put(source, excess.get(source) + e.getCapacity());
					excess.put(edgeEnd, excess.get(edgeEnd) + e.getCapacity());
					flow.put(e, e.getCapacity());
				}
			}

			//Main loop. Continue while an operation occurred
			boolean opOccured = false;
			do{
				opOccured = false;
				for(V v : g.vertexSet()){
					for(E e : g.edgeSetOfSource(v)){
						opOccured = push(v, e, true) | opOccured;
					}
					for(E e : g.edgeSetOfSink(v)){
						opOccured = push(v, e, false) | opOccured;
					}
					if(! opOccured) opOccured = relabel(v) | opOccured;
				}
			}while(opOccured);

			int maxFlow = 0;
			for(E e : g.edgeSetOfSink(sink)){
				maxFlow += flow.get(e);
			}
			flowObj = new Flow<E>(maxFlow , flow);

		}

		/** Helper method for the maxFlow calculation. */
		private boolean push(V u, E e, boolean forward){
			V v = g.getOther(e, u);
			if(excess.get(u) <= 0 || label.get(u) != label.get(v) + 1)
				return false;
			int mult = (forward ? 1 : -1);
			int delta = Math.min(excess.get(u), e.getCapacity() - flow.get(e));
			flow.put(e, flow.get(e) + mult * delta);
			excess.put(u, excess.get(u) - mult * delta);
			excess.put(v, excess.get(v) + mult * delta);
			return delta != 0;
		}

		/** Helper method for the maxFlow calculation. */
		private boolean relabel(V u){
			if (excess.get(u) <= 0 || u == source || u == sink)
				return false;
			int minVal = Integer.MAX_VALUE;
			for(V v : g.vertexSet()){
				E e = g.getConnection(u,v);
				if(e != null && flow.get(e) < e.getCapacity()){
					if(label.get(u) > label.get(v))
						return false;
					minVal = Math.min(minVal, label.get(v));
				}
			}
			if(minVal == Integer.MAX_VALUE) return false;
			int oldVal = label.get(u);
			label.put(u, minVal + 1);
			return oldVal != minVal + 1;
		}

		private Flow<E> computeMaxFlow(){
			return flowObj;
		}
	}

	/** Returns the maximum flow possible on graph g.
	 * Uses the preflow push algorithm to compute the max flow
	 * @param g - the graph to find the flow on
	 * @param source - the vertex to treat as the source of all flow
	 * @param sink - the vertex to treat as the sink of all flow
	 * @return - a flow object, contianing the value of the max flow and the placement of all flow
	 * @throws IllegalArgumentException if source or sink is null, or if they are the same node
	 */
	public static <V, E extends Flowable> Flow<E> maxFlow(Graph<V,E> g, V source, V sink)
			throws IllegalArgumentException{
		if(source == null || sink == null || source.equals(sink))
			throw new IllegalArgumentException("Source and Sink must be non-null and distinct");
		return new MaxFlow<V,E>(g, source, sink).computeMaxFlow();
	}

	/** Runs the SerialDictatorship algorithm on the given agents and items.
	 * Uses a random ordering over the agents for the priority order.
	 * @param agents - the set of agents. Each A must have a preference order over items.
	 * @param items - the items. No preferences for items are taken into account.
	 * @return - a Matching of agents and items.
	 */
	public static <A extends Agent<I>, I> Matching<A,I> serialDictator(Set<A> agents, Set<I> items){
		List<A> ordering = new ArrayList<A>(agents);
		Collections.shuffle(ordering);
		return serialDictator(ordering, items);
	}

	/** Runs the SerialDictatorship algorithm on the given agents and items.
	 * @param agents - the list of agents. Each A must have a preference order over items.
	 * 						The order that the agents appear in the list is the preference order over agents
	 * @param items - the items. No preferences for items are taken into account.
	 * @return - a Matching of agents and items.
	 * @throws IllegalArgumentException if any agent appears in the list twice.
	 */
	public static <A extends Agent<I>, I> Matching<A,I> serialDictator(List<A> agents, Set<I> items)
			throws IllegalArgumentException {
		//Make sure agent doesn't appear twice in ordering
		HashSet<A> agentsSet = new HashSet<A>(agents);
		if(agents.size() != agentsSet.size())
			throw new IllegalArgumentException("Can't use ordering " + agents + " over agents " + agentsSet);

		Matching<A,I> matching = new Matching<A,I>();
		matching.addAllA(agents);
		matching.addAllB(items);
		for(A agent : agents){
			for(I item : agent.getPreferences()){
				if(matching.isUnmatched(item)){
					matching.match(agent, item);
					break;
				}
			}
		}
		return matching;
	}	

}
