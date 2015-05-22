package graph;

import common.IDObject;

/** A simple edge implementation for use in SimpleGraph.
 * Implements the Flowable and Weighted interfaces to that the SimpleGraph
 * is appropriate for use in shortest path or max flow algorithms.
 * @author Mshnik
 */
public class SimpleEdge extends IDObject implements Flowable, Weighted, Cloneable{

	private int weight;
	private int capacity;
	
	/** The name of this SimpleEdge. Not used for any calculation purposes
	 * (only for display and toString), but cannot be null.
	 */
	private String name;
	
	/** Constructs a new SimpleVertex instance. As instances are only constructed
	 * within the a SimpleGraph instance, this constructor is package protected.
	 * The name of this SimpleVertex is set to the empty string, and the weight
	 * and capacity are set to 0
	 */
	SimpleEdge(){
		weight = 0;
		capacity = 0;
		name = "";
	}
	
	/** Constructs a new SimpleEdge instance with the given id, weight, capacity,
	 * and name. Only used in cloning an existing SimpleEdge instance.
	 */
	private SimpleEdge(int id, int w, int c, String n){
		super(id);
		weight = w;
		capacity = c;
		name = n;
	}
	
	/** Returns a new SimpleEdge with the same name, ID, weight, and capacity as 
	 * this edge. */
	public SimpleEdge clone(){
		SimpleEdge e = new SimpleEdge(getID(), weight, capacity, name);
		return e;
	}

	/** Sets the weight of this edge to w */
	public void setWeight(int w){
		weight = w;
	}
	
	/** Sets the capacity of this edge to c. Must be a non-negative number
	 * @throws IllegalArgumentException if c < 0 */
	public void setCapacity(int c) throws IllegalArgumentException {
		if(c < 0) throw new IllegalArgumentException("Can't set capacity to negative int");
		capacity = c;
	}
	
	/** Returns the weight of this SimpleEdge */
	@Override
	public int getWeight() {
		return weight;
	}

	/** Returns the non-negative capacity of this SimpleEdge */
	@Override
	public int getCapacity() {
		return capacity;
	}
	
	/** Returns the name of this SimpleEdge. Not used for any calculation purposes
	 * (only for display and toString), but cannot be null.
	 */
	public String getName(){
		return name;
	}
	
	/** Sets the name of this SimpleEdge. Not used for any calculation purposes
	 * (only for display and toString), but cannot be null.
	 */
	public void setName(String n){
		if(n == null) throw new IllegalArgumentException("Can't set name to null");
		name = null;
	}
	
	/** Returns a string representation of this SimpleVertex, consisting of 
	 * name and ID */
	public String toString(){
		return name + "(" + getID() + ")";
	}
}
