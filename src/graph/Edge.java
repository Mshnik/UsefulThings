package graph;

/** Designates that an instance of the implementing Class represents an Edge in a Graph */
public interface Edge {
	
	/** Returns the capacity of this edge. Should not change during graph computation,
	 * should be positive.
	 * Can also be used as a cost for shortest path algorithms
	 */
	public int getCapacity();

	/** Returns the graph on this edge. Implementing class should not change 
	 * this outside of setFlow()*/
	public int getFlow();
	
	/** Sets the graph to int f. Throws an illegal argument exception if 
	 * abs(this) is greater than the capacity. Negative flow can be used to denote residual flow, but 
	 * again capacity should be respected */
	public void setFlow(int newFlow) throws IllegalArgumentException;
	
	/** Returns the Vertex that this edge comes from */
	public Vertex getSource();
	
	/** Returns the Vertex that this edge goes into */
	public Vertex getSink();
	
}
