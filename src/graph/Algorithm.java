package graph;

import graph.interf.Edge;
import graph.interf.Graph;
import graph.interf.Vertex;
import graph.interf.Weighted;

import java.util.Collections;
import java.util.Comparator;
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
	public static <E extends Edge> LinkedList<Vertex> dijkstra(Graph<? extends Vertex, E> g, Vertex start, Vertex goal){
		Graph g = start.getGraph();
		//Check preconditions
		if(start.getGraph() != g || goal.getGraph() != g)
			return null;
		
		for(Edge e : g.edgeSet()){
			if(e.getCapacity() < 0)
				return null;
		}
		
		//Initalize step
		for(Vertex v : g.vertexSet()){
			v.setLabel(Integer.MAX_VALUE);
			v.setPrevious(null);
		}
		
		start.setLabel(0);
		
		HashSet<Vertex> frontier = new HashSet<Vertex>();
		frontier.add(start);
		
		//Iteration
		do{
			//Find next node (closest) and remove from frontier
			Vertex next = Collections.min(frontier, new MinLabelComparator());
			frontier.remove(next);
			//Found the goal - break out of loop
			if(next == goal)
				break;
			
			//Look at each edge leaving next
			for(Edge e : next.getOutEdges()){
				Vertex neighbor = e.getSink();
				//Relax edge - check if shortest path to neighbor is beaten by next -> neighbor.
				if(neighbor.getLabel() > next.getLabel() + e.getCapacity()){
					neighbor.setLabel(next.getLabel() + e.getCapacity());
					neighbor.setPrevious(next);
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
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex v = goal;
		do{
			path.push(v);
			v = v.getPrevious();
		}while(v != null);
		
		return path;
	}
	
	/** Allows comparison of vertices by their label - smaller label first */
	private static class MinLabelComparator implements Comparator<Vertex>{
		@Override
		public int compare(Vertex v0, Vertex v1) {
			return v0.getLabel() - v1.getLabel();
		}
		
	}
}
