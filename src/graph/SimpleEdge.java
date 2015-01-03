package graph;

import graph.interf.Edge;
import graph.interf.Vertex;

public class SimpleEdge implements Edge {

	private int capacity;
	private int flow;
	private Vertex source;
	private Vertex sink;
	
	public SimpleEdge(int c, Vertex source, Vertex sink){
		capacity = c;
		this.source = source;
		this.sink = sink;
	}
	
	@Override
	public int getCapacity() {
		return capacity;
	}

	@Override
	public int getFlow() {
		return flow;
	}

	@Override
	public void setFlow(int newFlow) throws IllegalArgumentException {
        if(Math.abs(newFlow) > capacity)
        	throw new IllegalArgumentException("Can't set flow to " + newFlow + " because cap is " + capacity);
        flow = newFlow;
	}

	@Override
	public Vertex getSource() {
		return source;
	}

	@Override
	public Vertex getSink() {
		return sink;
	}

}
