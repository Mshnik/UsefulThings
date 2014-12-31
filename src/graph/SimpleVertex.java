package graph;

import java.awt.Point;
import java.util.Set;

/** A simple implementation of the Vertex/Graph interface system.
 * Relies on on SimpleGraph for network storage.
 * SimpleVertex only stores information specifically about itself.
 * @author MPatashnik
 *
 */
public class SimpleVertex implements Vertex{

	private final Graph graph; //The graph this vertex belongs to
	private int label;  //An extra label field for graph algs
	private Point coordinates;
	
	private Vertex previous; //A previous vertex - useful for pathfinding algorithms.
	
	
	public SimpleVertex(Graph g){
		graph = g;
	}
	
	public Graph getGraph(){
		return graph;
	}
	
	@Override
	public Set<Edge> getOutEdges() {
		return graph.outSet(this);
	}

	@Override
	public Set<Edge> getInEdges() {
		return graph.inSet(this);
	}

	@Override
	public int getLabel() {
		return label;
	}

	@Override
	public void setLabel(int i) {
		label = i;
	}

	@Override
	public Point getPosition() {
		return coordinates;
	}

	@Override
	public void setPosition(Point p) {
		coordinates = p;
	}
	
	@Override
	public Vertex getPrevious(){
		return previous;
	}
	
	@Override
	public void setPrevious(Vertex v) throws IllegalArgumentException{
		if(v != null && v.getGraph() != graph)
			throw new IllegalArgumentException(v + " can't be the previous of " + this + ", they belong to different graphs");
		previous = v;
	}

}
